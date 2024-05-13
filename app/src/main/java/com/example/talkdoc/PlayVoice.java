package com.example.talkdoc;

import android.media.MediaPlayer;

import java.io.IOException;

public class PlayVoice
{
    private MediaPlayer mediaPlayer;
    private String audioFile;

    public PlayVoice(String audioFile)
    {
        this.audioFile = audioFile;
    }

    public void startPlaying()
    {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying()
    {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
