package com.newsproject.controller;

import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginController {

    public Result login(String app_email, String app_password) {
        try {
            String app_apiKey = "AIzaSyD7cnDBxWJ1I2XwUE5yy0l-255bv7jB2as";

            URL app_url = new URL(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + app_apiKey
            );

            HttpURLConnection app_conn = (HttpURLConnection) app_url.openConnection();
            app_conn.setRequestMethod("POST");
            app_conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            app_conn.setDoOutput(true);

            JSONObject app_request = new JSONObject();
            app_request.put("email", app_email);
            app_request.put("password", app_password);
            app_request.put("returnSecureToken", true);

            try (OutputStream app_os = app_conn.getOutputStream()) {
                byte[] app_input = app_request.toString().getBytes(StandardCharsets.UTF_8);
                app_os.write(app_input, 0, app_input.length);
            }

            if (app_conn.getResponseCode() == 200) {
                return new Result(true, "Login successful.");
            } else {
                return new Result(false, "Failed to log in user.");
            }

        } catch (Exception e) {
            return new Result(false, "Failed to log in user: " + e.getMessage());
        }
    }

    public static class Result {
        public final boolean success;
        public final String message;

        public Result(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
