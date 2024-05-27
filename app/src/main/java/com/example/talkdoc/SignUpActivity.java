package com.example.talkdoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity
{
    private EditText editTextName, editTextEmail, editTextPassword;
    private RadioGroup radioGroup;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroup = findViewById(R.id.radioGroup);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                String userType = radioButton.getText().toString();

                // 모든 필드가 입력되었는지 확인
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedId == -1) {
                    // 입력되지 않은 칸이 있을 때 알림을 띄워줌
                    Toast.makeText(getApplicationContext(), "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // JDBC를 사용하여 회원가입 정보 저장
                    DatabaseHandler.signUp(name, email, password, userType);
                    // 회원가입 완료 후 로그인 화면으로 이동
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // 현재 액티비티 종료
                }
            }
        });
    }
}
