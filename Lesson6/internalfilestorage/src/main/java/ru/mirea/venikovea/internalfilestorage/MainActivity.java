package ru.mirea.venikovea.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.venikovea.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String fileName = "significantDates.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.saveButton.setOnClickListener(v -> {
            String text = binding.editText.getText().toString();
            saveData(text);
        });
    }

    private void saveData(String text) {
        try {
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}