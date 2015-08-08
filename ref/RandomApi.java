import java.io.*;
import java.net.*;

public class RandomApi {

    static String API_URL = "https://en.wikipedia.org/w/api.php?";

    /** @brief Get a list of random Wikipedia Articles
    @param count The number of articles titles to get
    @return JSON formatted string list of article titles.

    @see https://www.mediawiki.org/wiki/API:Random
    */
    private static String getRandomArticles(int count) {

        StringBuilder result    = new StringBuilder();
        String        urlToRead = API_URL + "action=query" 
                                          + "&list=random" 
                                          + "&rnlimit=" + count 
                                          + "&format=json";
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
            e.printStackTrace();
        }

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandomArticles(3));
    }

}