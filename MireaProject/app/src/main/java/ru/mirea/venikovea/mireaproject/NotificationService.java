package ru.mirea.venikovea.mireaproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "NotificationServiceChannel";
    private static final int NOTIFICATION_ID = 123;

    private Timer timer;
    private String currentMessage = "Default message";
    private int currentInterval = 5000; // Интервал по умолчанию (в миллисекундах)

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notification Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendNotification(currentMessage);
            }
        }, 0, currentInterval); // Запускаем таймер с учетом текущего интервала
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            currentMessage = intent.getStringExtra("message");
            currentInterval = intent.getIntExtra("interval", 5000); // Получаем выбранный пользователем интервал
            restartTimer(); // Перезапускаем таймер с учетом нового интервала
        }
        return START_STICKY;
    }

    private void restartTimer() {
        timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendNotification(currentMessage);
            }
        }, 0, currentInterval);
    }

    private void sendNotification(String message) {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, createNotification(message));
    }

    private Notification createNotification(String message) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Notification Service")
                .setContentText(message)
                .setSmallIcon(R.drawable.bell)
                .build();
    }
}
