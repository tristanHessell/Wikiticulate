package tristan.hessell.pizza.wikiticulate.app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jsj on 8/08/15.
 */
public class ArticleSource {

    static String API_URL = "https://en.wikipedia.org/w/api.php?";

    /** @brief Get a list of random Wikipedia Articles
     @param count The number of articles titles to get
     @return JSON formatted string list of article titles.

     @see https://www.mediawiki.org/wiki/API:Random
     */
    private static String getJsonArticles(int count) {

        Log.v("ArticleSource", "getJsonArticles()");

        StringBuilder result    = new StringBuilder();
        String        urlToRead = API_URL
                + "action=query"
                + "&list=random"
                + "&rnlimit=" + count
                + "&format=json"
                + "&rnnamespace=0";
        try {
            URL               url  = new URL(urlToRead);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader    br   = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                result.append(line);
            }

            br.close();

        } catch (Exception e) {
            Log.e("ArticleSource", e.getMessage());
            e.printStackTrace();
        }

        return result.toString();
    }

    private static List<String> parseJSON(String json)
    {
        List<String> result = new LinkedList<String>();

        Log.v("ArticleSource", json);

        Log.v("ArticleSource", "parsing JSON");


        try {
            JSONObject jObject = new JSONObject(json).getJSONObject("query");

            JSONArray  jArray = jObject.getJSONArray("random");

            for (int i=0; i < jArray.length(); i++)
            {
                JSONObject oneObject = jArray.getJSONObject(i);
                // Pulling items from the array
                String title = oneObject.getString("title");
                Log.d("ArticleSource", "Parsed a new title: " + title);
                result.add(title);
            }

        } catch(JSONException ex) {
            Log.e("ArticleSource", ex.getMessage());
        }

        return result;
    }

    public static List<String> getArticles(int count) {
        Log.v("ArticleSource", "getArticles()");

        List<String> result = new LinkedList<String>();

        for(int i = 0; i < count / 10; ++i) {
            String json = getJsonArticles(10);
            result.addAll(parseJSON(json));
        }

        if(count % 10 > 0) {
            String json = getJsonArticles(count % 10);
            result.addAll(parseJSON(json));
        }


        return result;
    }

}
