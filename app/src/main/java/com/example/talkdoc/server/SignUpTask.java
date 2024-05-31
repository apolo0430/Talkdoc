package com.example.talkdoc.server;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpTask extends AsyncTask<String, Void, String>
{
    private String name;
    private String id;
    private String password;
    private String authority;
    private String patientId;

    public SignUpTask(String name, String id, String password, String authority)
    {
        this.name = name;
        this.id = id;
        this.password = password;
        this.authority = authority;
    }

    @Override
    protected String doInBackground(String... urls)
    {
        String url = urls[0];
        try {
            HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

            // Set request method to POST
            httpClient.setRequestMethod("POST");
            httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpClient.setRequestProperty("Content-Type", "application/json");
            httpClient.setDoOutput(true);

            // Create JSON input string
            String jsonInputString = "{\"name\": \"" + name +
                    "\", \"ID\": \"" + id +
                    "\", \"password\": \"" + password +
                    "\", \"auth\": \"" + authority +
                    "\", \"patient_id\": \"-1\"}";

            System.out.println(jsonInputString);

            try (OutputStream os = httpClient.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = httpClient.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream(), "utf-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine.trim());
                }
                in.close();

                // print result
                return response.toString();
            }
            else {
                return "POST request not worked: " + responseCode;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (result != null) {
            System.out.println("RESULT: " + result);
        } else {
            System.out.println("Error during POST request");
        }
    }
}
