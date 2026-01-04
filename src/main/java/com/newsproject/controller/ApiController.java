package com.newsproject.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiController {

    public JSONArray getNews(String app_category) {
        try {
            String app_apiKey = "608e4328f64f44599461efc82ce5d077";            // News API KEY

            String app_urlStr =
                    "https://newsapi.org/v2/top-headlines/sources?category="
                            + app_category + "&apiKey=" + app_apiKey;

            URL app_url = new URL(app_urlStr);
            HttpURLConnection app_conn = (HttpURLConnection) app_url.openConnection();
            app_conn.setRequestMethod("GET");

            BufferedReader app_reader = new BufferedReader(
                    new InputStreamReader(app_conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = app_reader.readLine()) != null) {
                response.append(line);
            }

            app_reader.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getJSONArray("sources");

        } catch (Exception e) {
            return null;
        }
    }
}
