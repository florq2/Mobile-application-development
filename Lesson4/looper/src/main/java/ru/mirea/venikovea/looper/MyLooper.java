package ru.mirea.venikovea.looper;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.os.Handler;


public class MyLooper extends Thread{
    public Handler mHandler;
    private Handler mainHandler;
    public MyLooper(Handler mainThreadHandler) {
        mainHandler =mainThreadHandler;
    }
    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String ageString = bundle.getString("AGE");
                int age = Integer.parseInt(ageString);
                String jobInfo = bundle.getString("JOB");

                String messageText = String.format("Age: %d, Job: %s", age, jobInfo);
                Log.d("MyLooper", messageText);

                try {
                    Thread.sleep(age * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                Bundle resultBundle = new Bundle();
                resultBundle.putString("result", "Delay is over!");
                message.setData(resultBundle);
                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}
