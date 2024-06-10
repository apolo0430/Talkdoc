package com.example.talkdoc;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.talkdoc.databinding.ActivityTranslationBinding;
import com.example.talkdoc.server.DoctorDataTask;
import com.example.talkdoc.server.TranslateTask;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TranslationActivity extends AppCompatActivity implements TranslateTask.TranslateTaskListener, DoctorDataTask.DoctorDataTaskListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTranslationBinding binding;
    private TextView originalText;
    private TextView resultText;
    private TextView doctorText;
    private PatientInfo selectedPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedPatient = getIntent().getParcelableExtra("selectedPatient");

        binding = ActivityTranslationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarTranslation.toolbar);
        ImageButton recordBtn = findViewById(R.id.recordButton);
        originalText = findViewById(R.id.original_text);
        resultText = findViewById(R.id.result_text);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Setting the header view
        View headerView = navigationView.getHeaderView(0);
        setInfoText(selectedPatient, headerView);

        doctorText = headerView.findViewById(R.id.doctor);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_translation).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_translation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            private boolean isRecording = false; // 녹음 중인지 여부를 나타내는 변수

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH-mm-ss");
            String audioFileName = "/" + dateFormat.format(date) + selectedPatient.getName() + ".3gp";
            String audioFile = getExternalCacheDir().getAbsolutePath() + audioFileName;

            private RecordVoice recordVoice = new RecordVoice(audioFile);
            private PlayVoice playVoice = new PlayVoice(audioFile);

            @Override
            public void onClick(View view) {
                if (!isRecording) { // 녹음 중이 아닌 경우
                    initializeTranslationText();

                    Snackbar.make(view, "녹음 시작", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setAnchorView(recordBtn).show();

                    recordVoice.startRecording();
                    isRecording = true; // 녹음 중으로 설정
                    recordBtn.setImageResource(R.drawable.ic_mic_black);
                } else { // 녹음 중인 경우
                    Snackbar.make(view, "녹음 종료", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setAnchorView(recordBtn).show();

                    recordVoice.stopRecording();
                    isRecording = false; // 녹음 중지로 설정
                    recordBtn.setImageResource(R.drawable.ic_mic_none_black);

                    playVoice.startPlaying();

                    // 서버로 음성 파일 전송 및 번역 요청
                    new TranslateTask(TranslationActivity.this).execute(audioFile, "http://14.63.125.208:7000/translate", "1"); // province 코드 수정 필요
                }
            }
        });

        // Doctor 데이터 요청
        new DoctorDataTask(this).execute("1", "http://14.63.125.208:7000");
    }

    @Override
    public void onTranslationResult(String result) {
        if (result != null) {
            originalText.setText("녹음된 내용");
            resultText.setText(result);
        } else {
            resultText.setText("번역 실패");
        }
    }

    @Override
    public void onDoctorDataResult(String[] result) {
        if (result != null) {
            doctorText.setText(String.join("\n", result));
        } else {
            doctorText.setText("Doctor data not found");
        }
    }

    private void initializeTranslationText() {
        originalText.setText("번역할 내용");
        resultText.setText("번역 결과");
    }

    private void setInfoText(PatientInfo selectedPatient, View headerView) {
        TextView nameText = headerView.findViewById(R.id.patient_name);
        TextView numText = headerView.findViewById(R.id.patient_number);
        TextView addressText = headerView.findViewById(R.id.patient_address);
        TextView emailText = headerView.findViewById(R.id.patient_email);
        TextView birthText = headerView.findViewById(R.id.patient_birth);
        TextView phoneText = headerView.findViewById(R.id.patient_phone);

        // Basic
        nameText.setText(selectedPatient.getName());
        numText.setText("#" + selectedPatient.getNumber());
        addressText.setText("- 주소:\n" + selectedPatient.getAddress());
        emailText.setText("- 이메일:\n" + selectedPatient.getEmail());
        birthText.setText("- 생년월일:\n" + selectedPatient.getBirth());
        phoneText.setText("- 전화번호:\n" + selectedPatient.getPhone());

        // others
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.translation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_translation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}
