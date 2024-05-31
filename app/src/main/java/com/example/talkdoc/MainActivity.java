package com.example.talkdoc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.talkdoc.server.GetPatientInfoTask;
import com.example.talkdoc.server.SignUpTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.talkdoc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private static final String[] REQUIRED_PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 권한 확인
        if (!checkPermissions()) {
            // 권한이 없는 경우 권한 요청
            requestPermissions();
        }
        else {
            // 권한이 있는 경우에는 일반적인 앱 초기화 작업을 진행
            //initializeApp();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_list, R.id.navigation_checkup, R.id.navigation_mypage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    private boolean checkPermissions()
    {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 앱 초기화 작업을 진행
                //initializeApp();
            } else {
                // 권한이 거부된 경우 사용자에게 앱의 기능에 대한 설명이나 추가적인 권한 요청을 할 수 있습니다.
            }
        }
    }

    // 앱 초기화 작업
    /*private void initializeApp()
    {
        // 권한이 허용된 경우에만 실행되는 초기화 작업을 진행
        setContentView(R.layout.activity_main);
        // 여기에 필요한 초기화 작업을 추가할 수 있습니다.
    }*/
}