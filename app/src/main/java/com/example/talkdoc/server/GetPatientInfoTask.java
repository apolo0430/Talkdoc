package com.example.talkdoc.server;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPatientInfoTask extends AsyncTask<String, Void, String>
{
    @Override
    protected String doInBackground(String... urls)
    {
        String url = urls[0];
        try {
            HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

            // Optional default is GET
            httpClient.setRequestMethod("GET");

            int responseCode = httpClient.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        httpClient.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                return response.toString();
            } else {
                return "GET request not worked";
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
        }
        else {
            System.out.println("Error during GET request");
        }
    }
}
