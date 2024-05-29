package com.example.talkdoc;

import android.media.MediaRecorder;

import java.io.IOException;
public class RecordVoice
{
    private MediaRecorder mediaRecorder;
    private String outputFile;

    public RecordVoice(String outputFile)
    {
        this.outputFile = outputFile;
    }

    public void startRecording()
    {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaRecorder.start();
        }
    }

    public void stopRecording()
    {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void sendVoice()
    {

    }
}
