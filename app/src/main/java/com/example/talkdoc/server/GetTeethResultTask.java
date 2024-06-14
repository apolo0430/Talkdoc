package com.example.talkdoc.server;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetTeethResultTask extends AsyncTask<Object, Void, String>
{
    private TextView resultTextView;

    public GetTeethResultTask(TextView resultTextView)
    {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(Object... params)
    {
        Bitmap imageBitmap = (Bitmap) params[0];
        String serverUrl = (String) params[1];

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image", encodedImage);

            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";
                scanner.close();

                return response;
            }
            else {
                return null;
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
            try {
                JSONObject jsonObject = new JSONObject(result);
                double prediction = jsonObject.getDouble("prediction");
                String predictionResult = jsonObject.getString("result");
                double prob = 1.0 - Math.round(prediction * 1000) / 1000.0;

                String message = "충치 확률: " + prob + "%\n결과: " + predictionResult;

                resultTextView.setText(message);
            }
            catch (Exception e) {
                e.printStackTrace();
                resultTextView.setText("Error parsing response");
            }
        }
        else {
            resultTextView.setText("Error connecting to server");
        }
    }
}
