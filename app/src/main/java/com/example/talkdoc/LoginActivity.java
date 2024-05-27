package com.example.talkdoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity
{
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // JDBC를 사용하여 데이터베이스에서 이메일과 비밀번호 확인
                boolean isAuthenticated = DatabaseHandler.authenticate(email, password);
                if (isAuthenticated) {
                    // 로그인 성공 시 splash 화면으로 이동
                    Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                    startActivity(intent);
                }
                else {
                    // 로그인 실패 시 회원 정보 없음을 띄워줌
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("회원 정보가 없습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // 다른 처리할 것이 없으면 빈채로 둘 수 있음
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });


        buttonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }
}
