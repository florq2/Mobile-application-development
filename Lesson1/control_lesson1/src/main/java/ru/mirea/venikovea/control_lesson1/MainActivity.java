package ru.mirea.venikovea.control_lesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            TextView tv = (TextView) findViewById(R.id.editTextText);
            tv.setText("Ok, it's Tony Stark");

            Button btn = findViewById(R.id.button);
            btn.setText("No, it's not!");
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            TextView tv = (TextView) findViewById(R.id.editTextText4);
            tv.setText("Ok, it's Tony Stark");

            Button btn = findViewById(R.id.button8);
            btn.setText("No, it's not!");
        }
    }
}