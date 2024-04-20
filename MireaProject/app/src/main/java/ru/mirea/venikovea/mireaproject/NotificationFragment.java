package ru.mirea.venikovea.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotificationFragment extends Fragment {

    private Button startButton;
    private Button stopButton;
    private EditText editTextMessage;
    private EditText editTextInterval;
    private boolean isServiceRunning = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        startButton = view.findViewById(R.id.buttonStart);
        stopButton = view.findViewById(R.id.buttonStop);
        editTextMessage = view.findViewById(R.id.editTextText);
        editTextInterval = view.findViewById(R.id.editTextText2);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                String intervalString = editTextInterval.getText().toString().trim();
                if (!intervalString.isEmpty()) {
                    int interval = Integer.parseInt(intervalString);
                    if (interval > 0) {
                        startNotificationService(message, interval);
                        return;
                    }
                }
                Toast.makeText(getActivity(), "Invalid interval", Toast.LENGTH_SHORT).show();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopNotificationService();
            }
        });

        return view;
    }

    private void startNotificationService(String message, int interval) {
        if (!isServiceRunning) {
            Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
            serviceIntent.putExtra("message", message);
            serviceIntent.putExtra("interval", interval);
            getActivity().startService(serviceIntent);
            isServiceRunning = true;
        } else {
            Toast.makeText(getActivity(), "Service already started", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopNotificationService() {
        if (isServiceRunning) {
            Intent serviceIntent = new Intent(getActivity(), NotificationService.class);
            getActivity().stopService(serviceIntent);
            isServiceRunning = false;
        } else {
            Toast.makeText(getActivity(), "Service not running", Toast.LENGTH_SHORT).show();
        }
    }
}
