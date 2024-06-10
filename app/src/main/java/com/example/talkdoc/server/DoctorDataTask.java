package com.example.talkdoc.server;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DoctorDataTask extends AsyncTask<String, Void, String[]> {
    private static final String TAG = "DoctorDataTask";
    private DoctorDataTaskListener listener;

    public interface DoctorDataTaskListener {
        void onDoctorDataResult(String[] result);
    }

    public DoctorDataTask(DoctorDataTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected String[] doInBackground(String... params) {
        String doctorId = params[0];
        String serverUrl = params[1];

        try {
            URL url = new URL(serverUrl + "/doctor/" + doctorId);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(5000); // 5초 타임아웃 설정
            httpConn.setReadTimeout(5000);    // 5초 타임아웃 설정

            int responseCode = httpConn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                inputStream.close();

                // JSON 데이터 파싱
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray lines = jsonResponse.getJSONArray("lines");
                String[] doctorData = new String[lines.length()];
                for (int i = 0; i < lines.length(); i++) {
                    doctorData[i] = lines.getString(i);
                }
                return doctorData;
            } else {
                Log.e(TAG, "Server returned response code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred during doctor data request", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (listener != null) {
            listener.onDoctorDataResult(result);
        }
    }
}
