package ru.mirea.venikovea.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import ru.mirea.venikovea.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        loadPreferences();

        binding.saveButton.setOnClickListener(v -> savePreferences());
    }

    private void loadPreferences() {
        String groupNumber = sharedPreferences.getString("groupNumber", "");
        String listNumber = sharedPreferences.getString("listNumber", "");
        String favoriteMovie = sharedPreferences.getString("favoriteMovie", "");

        binding.groupNumberEditText.setText(groupNumber);
        binding.listNumberEditText.setText(listNumber);
        binding.favoriteMovieEditText.setText(favoriteMovie);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("groupNumber", binding.groupNumberEditText.getText().toString());
        editor.putString("listNumber", binding.listNumberEditText.getText().toString());
        editor.putString("favoriteMovie", binding.favoriteMovieEditText.getText().toString());

        editor.apply();
    }
}