package com.example.talkdoc.server;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateTask extends AsyncTask<String, Void, String>
{
    private static final String TAG = "TranslateTask";
    private TranslateTaskListener listener;

    public interface TranslateTaskListener
    {
        void onTranslationResult(String result);
    }

    public TranslateTask(TranslateTaskListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String audioFilePath = params[0];
        String serverUrl = params[1];
        String province = params[2];
        String number = params[3];

        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            Log.e(TAG, "Audio file does not exist: " + audioFilePath);
            return "Audio file does not exist: " + audioFilePath;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // 파일 전송을 위한 RequestBody 생성
        RequestBody fileBody = RequestBody.create(MediaType.parse("audio/wav"), audioFile);

        // MultipartBody를 생성하고 파일을 추가
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", audioFile.getName(), fileBody)
                .build();

        // 서버 URL과 province를 이용하여 전체 URL 생성
        String url = serverUrl + "/translate_file/" + province + "/" + number;

        // POST 요청 생성
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            // 요청 실행
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 성공적으로 응답 받았을 때 처리
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                String code = jsonObject.getString("Code");

                if (code.equals("200")) {
                    String translation = jsonObject.getString("data");

                    return translation;
                }
                else {
                    Log.e(TAG, "Server returned error code: " + code);

                    return null;
                }
            }
            else {
                // 서버 응답이 실패인 경우
                Log.e(TAG, "Server returned response code: " + response.code());

                return null;
            }
        }
        catch (IOException | JSONException e) {
            // 예외 발생 시 처리
            Log.e(TAG, "Exception occurred during translation", e);

            return null;
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (listener != null) {
            listener.onTranslationResult(result);
        }
    }
}
