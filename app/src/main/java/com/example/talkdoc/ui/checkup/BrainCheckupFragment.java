package com.example.talkdoc.ui.checkup;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.talkdoc.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BrainCheckupFragment extends Fragment
{
    private int resultScore = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_brain_checkup, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        Button submitButton = new Button(getContext());
        submitButton.setText("제출");
        submitButton.setTextSize(20);
        submitButton.setTextColor(Color.WHITE);
        submitButton.setBackground(ContextCompat.getDrawable(getContext(), R.color.mainColor));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 20, 0, 0); // 위에 16dp의 마진 추가
        submitButton.setLayoutParams(params);

        ArrayList<String> questionList = readAssetFile();
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String[]> scores = new ArrayList<>();

        for (String s : questionList) {
            String[] parts = s.split(",\\s*");
            questions.add(parts[1]);
            scores.add(new String[]{parts[2], parts[3], parts[4]});
        }

        for (int i = 0; i < questions.size(); i++) {
            addQuestion(linearLayout, i + 1, questions.get(i), scores.get(i));
        }

        linearLayout.addView(submitButton);

        submitButton.setOnClickListener(v -> {
            calculateScore(linearLayout);
            // 결과를 처리하는 코드를 여기에 작성하세요.
        });

        return view;
    }

    private void addQuestion(LinearLayout parent, int questionNumber, String questionText, String[] score)
    {
        TextView questionTextView = new TextView(getContext());
        questionTextView.setText("질문 " + questionNumber + ": " + questionText);
        questionTextView.setTextSize(20);
        questionTextView.setPadding(0, 16, 0, 8);
        parent.addView(questionTextView);

        RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);

        RadioButton option1 = new RadioButton(getContext());
        option1.setText(score[0] + "점");
        option1.setTextSize(20);
        option1.setId(View.generateViewId());
        radioGroup.addView(option1);

        RadioButton option2 = new RadioButton(getContext());
        option2.setText(score[1] + "점");
        option2.setTextSize(20);
        option2.setId(View.generateViewId());
        radioGroup.addView(option2);

        RadioButton option3 = new RadioButton(getContext());
        option3.setText(score[2] + "점");
        option3.setTextSize(20);
        option3.setId(View.generateViewId());
        radioGroup.addView(option3);

        parent.addView(radioGroup);
    }

    private void calculateScore(LinearLayout parent)
    {
        resultScore = 0;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof RadioGroup) {
                RadioGroup radioGroup = (RadioGroup) view;
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId != -1) {
                    RadioButton selectedButton = radioGroup.findViewById(selectedId);
                    String text = selectedButton.getText().toString();
                    resultScore += getScoreForOption(text);
                }
            }
        }

        String resultText = "";
        if (resultScore >= 7)
            resultText = "정상";
        else if (resultScore > -7 && resultScore < 7)
            resultText = "위험";
        else
            resultText = "심각";

        // 최종 점수 출력
        Toast.makeText(getContext(), "총점: " + resultScore + "점\n단계: " + resultText, Toast.LENGTH_SHORT).show();
    }

    private int getScoreForOption(String text)
    {
        switch (text) {
            case "0점":
                return 0;
            case "-1점":
                return -1;
            case "-2점":
                return -2;
            case "1점":
                return 1;
            case "2점":
                return 2;
            default:
                return 0;
        }
    }

    private ArrayList<String> readAssetFile()
    {
        Context context = getActivity();
        ArrayList<String> questionList = new ArrayList<>();

        if (context != null) {
            AssetManager assetManager = context.getAssets();

            try {
                InputStream inputStream = assetManager.open("aqs.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    questionList.add(line);
                    System.out.println(line);
                }
                inputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return questionList;
    }
}