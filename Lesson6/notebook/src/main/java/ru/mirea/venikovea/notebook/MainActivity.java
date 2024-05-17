package ru.mirea.venikovea.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.venikovea.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveButton.setOnClickListener(v -> {
            String fileName = binding.editTextFileName.getText().toString();
            String text = binding.editTextQuote.getText().toString();
            saveData(fileName, text);
        });

        binding.loadButton.setOnClickListener(v -> {
            String fileName = binding.editTextFileName.getText().toString();
            String text = loadData(fileName);
            binding.editTextQuote.setText(text);
        });
    }

    private void saveData(String fileName, String text) {
        if (fileName.isEmpty() || text.isEmpty()) {
            Toast.makeText(this, "Название файла и цитата не должны быть пустыми", Toast.LENGTH_SHORT).show();
            return;
        }

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, fileName + ".txt");

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(text.getBytes());
            outputStream.close();

            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при сохранении данных", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }

    private String loadData(String fileName) {
        if (fileName.isEmpty()) {
            Toast.makeText(this, "Название файла не должно быть пустым", Toast.LENGTH_SHORT).show();

            return "";
        }

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, fileName + ".txt");

        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            inputStream.read(data);
            inputStream.close();

            String text = new String(data);
            Toast.makeText(this, "Данные загружены", Toast.LENGTH_SHORT).show();

            return text;
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при загрузке данных", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return "";
    }
}