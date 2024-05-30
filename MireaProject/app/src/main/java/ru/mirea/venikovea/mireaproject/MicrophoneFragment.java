package ru.mirea.venikovea.mireaproject;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MicrophoneFragment extends Fragment {

    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private Button startButton;
    private Button stopButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_microphone, container, false);

        startButton = rootView.findViewById(R.id.startRecordButton);
        stopButton = rootView.findViewById(R.id.stopRecordButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });

        stopButton.setEnabled(false);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRecording();
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        File audioFile = getOutputMediaFile();
        if (audioFile == null) {
            Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
            return;
        }
        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Toast.makeText(getActivity(), "Начало записи", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            Toast.makeText(getActivity(), "Конец записи", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT).show();
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(null), "Audio");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "AUDIO_" + timeStamp + ".3gp");
    }
}
