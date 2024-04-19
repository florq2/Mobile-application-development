package ru.mirea.venikovea.lesson4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ru.mirea.venikovea.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editTextMirea.setText("Мой номер по списку №6");
        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.class.getSimpleName(), "onClickListener");
            }
        });
    }
}