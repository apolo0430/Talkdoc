package com.example.talkdoc;

import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Sampler;
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

public class TranslationActivity extends AppCompatActivity implements TranslateTask.TranslateTaskListener, DoctorDataTask.DoctorDataTaskListener
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTranslationBinding binding;
    private TextView resultText;
    private TextView doctorText;
    private PatientInfo selectedPatient;
    private ExtAudioRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        selectedPatient = getIntent().getParcelableExtra("selectedPatient");

        binding = ActivityTranslationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarTranslation.toolbar);
        ImageButton recordBtn = findViewById(R.id.recordButton);
        resultText = findViewById(R.id.result_text);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Setting the header view
        View headerView = navigationView.getHeaderView(0);
        setInfoText(headerView);

        doctorText = headerView.findViewById(R.id.doctor);

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_translation).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_translation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        recordBtn.setOnClickListener(new View.OnClickListener()
        {
            boolean isRecording = false; // 녹음 중인지 여부를 나타내는 변수
            String audioFileName = "/audio.wav";
            String audioFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + audioFileName;

            @Override
            public void onClick(View view) {
                if (!isRecording) { // 녹음 중이 아닌 경우
                    initializeTranslationText();

                    Snackbar.make(view, "녹음 시작", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setAnchorView(recordBtn).show();

                    start(audioFilePath);
                    isRecording = true; // 녹음 중으로 설정
                    recordBtn.setImageResource(R.drawable.ic_mic_black);
                }
                else { // 녹음 중인 경우
                    Snackbar.make(view, "녹음 종료", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setAnchorView(recordBtn).show();

                    stop();
                    isRecording = false; // 녹음 중지로 설정
                    recordBtn.setImageResource(R.drawable.ic_mic_none_black);

                    String province = "0";
                    if (selectedPatient.getAddress().contains("경상남도") || selectedPatient.getAddress().contains("경상북도"))
                        province = "1";
                    else if (selectedPatient.getAddress().contains("강원도"))
                        province = "2";

                    // 서버로 음성 파일 전송 및 번역 요청
                    new TranslateTask(TranslationActivity.this).execute(audioFilePath, "http://192.168.221.44:5000", province, selectedPatient.getNumber());
                }
            }
        });

        // Doctor 데이터 요청
        new DoctorDataTask(this).execute(selectedPatient.getNumber(), "http://192.168.221.44:5000");
    }

    @Override
    public void onTranslationResult(String result)
    {
        if (result != null) {
            resultText.setText(result);
        }
        else {
            resultText.setText("번역 실패");
        }
    }

    @Override
    public void onDoctorDataResult(String[] result)
    {
        if (result != null) {
            doctorText.setText(String.join("\n", result));
        }
        else {
            doctorText.setText("의사 소견 없음");
        }
    }

    private void initializeTranslationText()
    {
        resultText.setText("번역 결과");
    }

    public void setInfoText(View headerView)
    {
        TextView nameText = headerView.findViewById(R.id.patient_name);
        TextView numText = headerView.findViewById(R.id.patient_number);
        TextView addressText = headerView.findViewById(R.id.patient_address);
        TextView emailText = headerView.findViewById(R.id.patient_email);
        TextView birthText = headerView.findViewById(R.id.patient_birth);
        TextView phoneText = headerView.findViewById(R.id.patient_phone);
        TextView scoreText = headerView.findViewById(R.id.brain_score);

        float score = Float.valueOf(selectedPatient.getScore());

        // Basic
        nameText.setText(selectedPatient.getName());
        numText.setText("#" + selectedPatient.getNumber());
        addressText.setText("- 주소:\n" + selectedPatient.getAddress());
        emailText.setText("- 이메일:\n" + selectedPatient.getEmail());
        birthText.setText("- 생년월일:\n" + selectedPatient.getBirth());
        phoneText.setText("- 전화번호:\n" + selectedPatient.getPhone());
        scoreText.setText("- 뇌질환 점수:\n" + String.format("%.1f", score) + "점");
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


    public void start(String filePath)
    {
        recorder = ExtAudioRecorder.getInstanse(false);

        recorder.setOutputFile(filePath);
        recorder.prepare();
        recorder.start();
    }

    public void stop()
    {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}
