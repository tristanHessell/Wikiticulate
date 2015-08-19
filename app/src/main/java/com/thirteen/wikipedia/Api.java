package com.thirteen.wikipedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by manko on 19/08/15.
 */
public class Api {

    protected static final Logger log = Logger.getLogger(Api.class.getName());

    protected static final String API_URL = "https://en.wikipedia.org/w/api.php?";


    protected static String request(String queryString) {

        StringBuilder result    = new StringBuilder();
        String        urlToRead = API_URL + "format=json&" + queryString;
        log.log(Level.CONFIG, "Wikipedia API request: " + urlToRead);
        try {
            URL url  = new URL(urlToRead);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br   = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                result.append(line);
            }

            br.close();

        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

        return result.toString();

    }

    protected static String query(String queryString) {
        return request("action=query&" + queryString);
    }

    /** @brief Get a list of random Wikipedia Articles
     @param count The number of articles titles to get
     @return List of Article titles

     @see https://www.mediawiki.org/wiki/API:Random
     */
    public static List<String> getRandomTitles(int count) {
        List<String> result = new LinkedList<String>();

        String json = query("list=random&rnlimit=" + count + "&rnnamespace=0");

        try {
            JSONObject jObject = new JSONObject(json).getJSONObject("query");

            JSONArray jArray = jObject.getJSONArray("random");

            for (int i=0; i < jArray.length(); i++)
            {
                JSONObject oneObject = jArray.getJSONObject(i);
                // Pulling items from the array
                String title = oneObject.getString("title");
                result.add(title);
            }

        } catch(JSONException ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }

        return result;
    }

}
