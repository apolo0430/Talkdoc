package com.example.talkdoc.server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.os.AsyncTask;

import com.example.talkdoc.PatientInfo;

import org.json.JSONObject;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class GetPatientInfoTask extends AsyncTask<String, Void, String[]>
{
    private ArrayList<PatientInfo> patientList;
    private OnPatientInfoReceived callback;

    public GetPatientInfoTask(OnPatientInfoReceived callback)
    {
        this.callback = callback;
        this.patientList = new ArrayList<>();
    }

    @Override
    protected String[] doInBackground(String... urls) {
        String url = urls[0];
        try {
            HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
            httpClient.setRequestMethod("GET");

            int responseCode = httpClient.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String[] results = new String[jsonObject.length()];
                Iterator<String> keys = jsonObject.keys();

                int index = 0;
                while (keys.hasNext()) {
                    String key = keys.next();
                    results[index++] = jsonObject.getJSONObject(key).toString();
                }

                return results;
            } else {
                return new String[]{"GET request not worked"};
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"Exception occurred"};
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null && result.length > 0 && !result[0].equals("Exception occurred")) {
            System.out.println("SUCCEED");

            setPatientList(result);
            if (callback != null) callback.onReceived(patientList);
        }
        else {
            System.out.println("Error during GET request");
        }
    }

    private void setPatientList(String[] patientInfo)
    {
        for (Integer i = 1; i <= patientInfo.length; i++) {
            String info = patientInfo[i - 1];

            try {
                // JSON 문자열을 JSONObject로 파싱
                JSONObject jsonObject = new JSONObject(info);

                // 필드에서 값을 추출
                String name = decodeUnicodeEscape(jsonObject.getString("name"));
                String address = decodeUnicodeEscape(jsonObject.getString("address"));
                String email = jsonObject.getString("email");
                String birth = jsonObject.getString("birthdate");
                String phone = jsonObject.getString("phone_number");
                String imageBase64 = jsonObject.getString("image");

                // Base64 문자열을 Bitmap 이미지로 변환
                Bitmap image = base64ToBitmap(imageBase64, 1);
                //Bitmap image = Bitmap.createScaledBitmap(base64ToBitmap(imageBase64, 1), 20, 20, true);

                patientList.add(new PatientInfo(name, address, email, birth, phone, image, i.toString()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<PatientInfo> getPatientList()
    {
        return patientList;
    }

    public static String decodeUnicodeEscape(String input)
    {
        StringBuilder sb = new StringBuilder(input.length());
        char[] arr = input.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            char ch = arr[i];

            if (ch == '\\' && i + 1 < arr.length && arr[i + 1] == 'u') {
                String unicode = input.substring(i + 2, i + 6);
                sb.append((char) Integer.parseInt(unicode, 16));
                i += 5;
            }
            else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    public interface OnPatientInfoReceived
    {
        void onReceived(ArrayList<PatientInfo> patientList);
    }

    public static Bitmap base64ToBitmap(String base64String, int inSampleSize)
    {
        byte[] byteImage = Base64.decode(base64String, Base64.DEFAULT);

        // BitmapFactory.Options 객체 생성 및 inSampleSize 설정
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // 옵션을 사용하여 이미지 디코딩
        return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length, options);
    }

}
