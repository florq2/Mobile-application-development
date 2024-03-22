package ru.mirea.venikovea.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextText);
    }

    public void showToastOnClick(View view) {
        int count = editText.getText().toString().length();

        Toast toast = Toast.makeText(getApplicationContext(),
                "Студент №6 Группа БСБО-04-22 Количество символов - " + count,
                Toast.LENGTH_LONG);

        toast.show();
    }
}