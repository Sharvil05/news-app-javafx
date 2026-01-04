package com.newsproject.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class SignUpController {

    public Result signUp(String app_email, String app_password) {

        if (app_email == null || app_email.isEmpty() || app_password == null || app_password.isEmpty()) {
            return new Result(false, "Email and password cannot be empty.");
        }

        try {
            String app_apiKey = "AIzaSyD7cnDBxWJ1I2XwUE5yy0l-255bv7jB2as";

            URL app_url = new URL(
                    "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + app_apiKey
            );

            HttpURLConnection app_conn = (HttpURLConnection) app_url.openConnection();
            app_conn.setRequestMethod("POST");
            app_conn.setRequestProperty("Content-Type", "application/json");
            app_conn.setDoOutput(true);

            JSONObject app_body = new JSONObject();
            app_body.put("email", app_email);
            app_body.put("password", app_password);
            app_body.put("returnSecureToken", true);

            try (OutputStream app_os = app_conn.getOutputStream()) {
                app_os.write(app_body.toString().getBytes("UTF-8"));
            }

            int responseCode = app_conn.getResponseCode();
            InputStream responseStream = responseCode == 200 ? app_conn.getInputStream() : app_conn.getErrorStream();

            String response;
            try (Scanner scanner = new Scanner(responseStream)) {
                response = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }

            if (responseCode == 200) {
                return new Result(true, "User created successfully.");
            } else {
                JSONObject error = new JSONObject(response);
                String msg = error.has("error")
                        ? error.getJSONObject("error").getString("message")
                        : "Unknown error";
                return new Result(false, "Failed to create user: " + msg);
            }

        } catch (Exception e) {
            return new Result(false, "Exception occurred: " + e.getMessage());
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
