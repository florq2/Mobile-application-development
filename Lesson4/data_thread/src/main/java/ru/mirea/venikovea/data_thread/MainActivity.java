package ru.mirea.venikovea.data_thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import ru.mirea.venikovea.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Runnable runn1 = new Runnable() {
            public void run() {
                binding.textView.setText("runOnUiThread. \n" +
                        "Позволяет выполнять код синхронно на главном потоке.Блокирует фоновый поток до завершения.");
            }
        };
        final Runnable runn2 = new Runnable() {
            public void run() {
                binding.textView.setText("post. \n" +
                        "Добавляет переданный код в очередь выполнения на главном потоке и продолжает выполнение кода на фоновом потоке.");
            }
        };
        final Runnable runn3 = new Runnable() {
            public void run() {
                binding.textView.setText("post.Delayed. \n" +
                        "Позволяет отложить выолнение кода на определенное кол-во времени.");
            }
        };

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    runOnUiThread(runn1);
                    TimeUnit.SECONDS.sleep(4);
                    binding.textView.postDelayed(runn3, 4000);
                    binding.textView.post(runn2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}