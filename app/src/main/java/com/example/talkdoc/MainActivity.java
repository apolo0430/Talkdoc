package com.example.talkdoc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

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
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA // 카메라 권한 추가
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
        AppBarConfiguration appBarConfiguration;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if (UserInfo.getInstance().getAuthority().compareTo("의료인") == 0) {
            navView.getMenu().clear();
            navView.inflateMenu(R.menu.bottom_nav_menu_doctor);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_list, R.id.navigation_mypage)
                    .build();
            System.out.println("의료인");
        }
        else if (UserInfo.getInstance().getAuthority() == "근로자") {
            navView.getMenu().clear();
            navView.inflateMenu(R.menu.bottom_nav_menu_worker);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_list, R.id.navigation_brain_checkup, R.id.navigation_teeth_checkup, R.id.navigation_mypage)
                    .build();
            System.out.println("근로자");
        }
        else {
            navView.getMenu().clear();
            navView.inflateMenu(R.menu.bottom_nav_menu_fam);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_list, R.id.navigation_mypage)
                    .build();
            System.out.println("보호자");
        }

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private boolean checkPermissions()
    {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
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
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // 모든 권한이 허용된 경우 앱 초기화 작업을 진행
            }
            else {
                // 권한이 거부된 경우 사용자에게 앱의 기능에 대한 설명이나 추가적인 권한 요청을 할 수 있습니다.
            }
        }
    }
}