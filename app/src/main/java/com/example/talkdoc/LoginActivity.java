package com.example.talkdoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talkdoc.server.LoginTask;
import com.example.talkdoc.server.SignUpTask;

public class LoginActivity extends AppCompatActivity
{

    private EditText editTextID, editTextPassword;
    private Button buttonLogin, buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextID = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String id = editTextID.getText().toString();
                String password = editTextPassword.getText().toString();

                new LoginTask(id, password).execute("http://14.63.125.208:7000/login");

                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                startActivity(intent);
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
