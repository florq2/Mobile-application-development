package ru.mirea.venikovea.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.TreeMap;

import ru.mirea.venikovea.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Thread mainThread = Thread.currentThread();
        binding.textView.setText("Имя текущего потока: " + mainThread.getName());

        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-04-22, НОМЕР ПО СПИСКУ: 6, МОЙ ЛЮБИМЫЙ ФИЛЬМ: не смотрю фильмы");
        binding.textView.append("\nНовое имя потока: " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int numberThread = counter++;
                        Log.d("ThreadProject", String.format("Запущен поток No %d студентом группы No %s номер по списку No %d ",
                                numberThread, "БСБО-04-22", 6));
                        long endTime = System.currentTimeMillis() + 20 * 1000;
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try {
                                    wait(endTime - System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            Log.d("ThreadProject", "Выполнен поток No " + numberThread);
                        }

                        final float result = calculate(binding.editTextLessons.getText().toString(), binding.editTextDays.getText().toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.textViewResult.setText("Среднее кол-во пар: " + result);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private float calculate(String lessons, String days) {

        int lessonsCount = Integer.parseInt(lessons);
        int daysCount = Integer.parseInt(days);

        return (float) lessonsCount / daysCount;
    }
}