package ru.mirea.venikovea.intentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textView);
        String text = "Квадрат значения моего номера по списку в группе 36, а текущее время: " +
                (String) getIntent().getSerializableExtra("date");

        textView.setText(text);
    }
}