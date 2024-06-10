package com.example.talkdoc.server;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetQuestionFileTask extends AsyncTask<Void, Void, ArrayList<String>> {
    private TaskListener listener;

    public interface TaskListener {
        void onFinished(ArrayList<String> questionList);
    }

    public GetQuestionFileTask(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> questionList = new ArrayList<>();
        try {
            URL url = new URL("http://yourserver.com/brain"); // 서버 URL로 변경
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray jsonData = jsonResponse.getJSONArray("data");

                for (int i = 0; i < jsonData.length(); i++) {
                    questionList.add(jsonData.getString(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questionList;
    }

    @Override
    protected void onPostExecute(ArrayList<String> questionList) {
        if (listener != null) {
            listener.onFinished(questionList);
        }
    }
}