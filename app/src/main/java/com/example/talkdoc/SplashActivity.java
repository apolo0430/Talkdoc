package com.example.talkdoc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{
    private static final long SPLASH_DELAY = 3000; // 스플래시 화면이 표시될 시간(밀리초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 지연된 작업을 예약하여 메인 화면으로 전환
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 메인 화면으로 전환하는 인텐트 생성
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // 스플래시 화면 종료
                finish();
            }
        }, SPLASH_DELAY);
    }
}
