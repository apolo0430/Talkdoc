package com.example.talkdoc;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.talkdoc.databinding.ActivityTranslationBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class TranslationActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTranslationBinding binding;
    //private RecordVoice recordVoice;
    //private PlayVoice playVoice;
    private boolean record = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PatientInfo selectedPatient = getIntent().getParcelableExtra("selectedPatient");

        binding = ActivityTranslationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String audioFile = getExternalCacheDir().getAbsolutePath() + "/recorded_audio.3gp";

        /*recordVoice = new RecordVoice(audioFile);
        playVoice = new PlayVoice(audioFile);*/

        setSupportActionBar(binding.appBarTranslation.toolbar);
        /*binding.appBarTranslation.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Snackbar.make(view, "녹음 시작", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();

                recordVoice.startRecording();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        recordVoice.stopRecording();

                        Snackbar.make(view, "녹음 종료", Snackbar.LENGTH_LONG)
                                .setAction("Action", null)
                                .setAnchorView(R.id.fab).show();

                        playVoice.startPlaying();
                    }
                }, 5000); // 5초 후에 녹음 중지 (5000 밀리초)
            }
        });*/

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