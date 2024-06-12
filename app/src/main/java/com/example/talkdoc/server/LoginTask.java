package com.example.talkdoc.server;

import android.os.AsyncTask;

import com.example.talkdoc.UserInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginTask  extends AsyncTask<String, Void, String>
{
    private String id;
    private String password;
    private UserInfo userInfo;

    public LoginTask(String id, String password)
    {
        this.id = id;
        this.password = password;

        userInfo = UserInfo.getInstance();
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
            String jsonInputString = "{\"ID\": \"" + id +
                    "\", \"password\": \"" + password + "\"}";

            try (OutputStream os = httpClient.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = httpClient.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
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
                return "POST request not worked";
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

            setUserInfo(result);
        }
        else {
            System.out.println("Error during POST request");
        }
    }

    private void setUserInfo(String result)
    {
        try {
            // JSON 문자열을 JSONObject로 파싱
            JSONObject jsonObject = new JSONObject(result);

            // 필드에서 값을 추출
            String auth = jsonObject.getString("auth");
            String name = jsonObject.getString("name");

            if (auth.compareTo("2") == 0)
                auth = "보호자";
            else if (auth.compareTo("3") == 0)
                auth = "근로자";
            else if (auth.compareTo("4") == 0)
                auth = "의료인";

            userInfo.setName(name);
            userInfo.setAuthority(auth);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
