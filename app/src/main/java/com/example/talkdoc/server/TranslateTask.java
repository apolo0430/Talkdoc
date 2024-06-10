package com.example.talkdoc.server;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslateTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "TranslateTask";
    private TranslateTaskListener listener;

    public interface TranslateTaskListener {
        void onTranslationResult(String result);
    }

    public TranslateTask(TranslateTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String audioFilePath = params[0];
        String serverUrl = params[1];
        String province = params[2];

        try {
            // Read audio file and encode it to base64
            File audioFile = new File(audioFilePath);
            FileInputStream fileInputStream = new FileInputStream(audioFile);
            byte[] audioBytes = new byte[(int) audioFile.length()];
            fileInputStream.read(audioBytes);
            String encodedAudio = Base64.encodeToString(audioBytes, Base64.NO_WRAP); // NO_WRAP to avoid new lines
            fileInputStream.close();

            // URL encode the audio data
            String encodedAudioUrl = URLEncoder.encode(encodedAudio, "UTF-8");

            // Build the URL with province and encoded audio
            URL url = new URL(serverUrl + "/" + province + "/" + encodedAudioUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } else {
                Log.e(TAG, "Server returned response code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred during translation", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (listener != null) {
            listener.onTranslationResult(result);
        }
    }
}
