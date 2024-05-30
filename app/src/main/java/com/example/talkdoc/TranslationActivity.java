package com.example.talkdoc;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.talkdoc.databinding.ActivityTranslationBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class TranslationActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTranslationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        PatientInfo selectedPatient = getIntent().getParcelableExtra("selectedPatient");

        binding = ActivityTranslationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarTranslation.toolbar);
        ImageButton recordBtn = findViewById(R.id.recordButton);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);

        ImageView imageView = headerView.findViewById(R.id.patient_image);
        TextView nameText = headerView.findViewById(R.id.patient_name);
        TextView numText = headerView.findViewById(R.id.patient_number);

        imageView.setImageResource(R.drawable.ic_android_black);
        nameText.setText(selectedPatient.getName());
        numText.setText(selectedPatient.getNumber());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_translation).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_translation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            private boolean isRecording = false; // 녹음 중인지 여부를 나타내는 변수

            Date date = new Date();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH-mm-ss");

            String audioFileName = dateFormat.format(date) + selectedPatient.getName();

            String audioFile = getExternalCacheDir().getAbsolutePath() + "/recorded_audio.3gp";

            private RecordVoice recordVoice = new RecordVoice(audioFile);
            private PlayVoice playVoice = new PlayVoice(audioFile);

            @Override
            public void onClick(View view)
            {
                if (!isRecording) { // 녹음 중이 아닌 경우
                    Snackbar.make(view, "녹음 시작", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setAnchorView(recordBtn).show();

                    recordVoice.startRecording();
                    isRecording = true; // 녹음 중으로 설정
                    recordBtn.setImageResource(R.drawable.ic_mic_black);
                }
                else { // 녹음 중인 경우
                    Snackbar.make(view, "녹음 종료", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setAnchorView(recordBtn).show();

                    recordVoice.stopRecording();
                    isRecording = false; // 녹음 중지로 설정
                    recordBtn.setImageResource(R.drawable.ic_mic_none_black);

                    playVoice.startPlaying();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.translation, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_translation);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}