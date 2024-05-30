package com.example.talkdoc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talkdoc.server.GetPatientInfoTask;
import com.example.talkdoc.server.SignUpTask;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity
{

    private EditText editTextName, editTextID, editTextPassword;
    private RadioGroup radioGroup;
    private Button buttonSignUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextID = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroup = findViewById(R.id.radioGroup);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String id = editTextID.getText().toString();
                String password = editTextPassword.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String userType = radioButton.getText().toString();
                int userType_num = 0;

                if (userType.compareTo("환자") == 0)
                    userType_num = 1;
                else if (userType.compareTo("보호자") == 0)
                    userType_num = 2;
                else if (userType.compareTo("근로자") == 0)
                    userType_num = 3;
                else if (userType.compareTo("의료인") == 0)
                    userType_num = 4;

                // 모든 필드가 입력되었는지 확인
                if (name.isEmpty() || id.isEmpty() || password.isEmpty() || selectedId == -1) {
                    // 입력되지 않은 칸이 있을 때 알림을 띄워줌
                    Toast.makeText(getApplicationContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    new SignUpTask(name, id, password, userType_num).execute("http://192.168.9.249:5000/signup");

                    // 회원가입 완료 후 로그인 화면으로 이동
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // 현재 액티비티 종료
                }
            }
        });
    }
}
