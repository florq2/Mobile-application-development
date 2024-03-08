package ru.mirea.venikovea.buttonclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textViewStudent;
    private Button btnWhoAmI;
    private Button btnItIsNotMe;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStudent = (TextView) findViewById(R.id.tvOut);
        btnWhoAmI = (Button) findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = (Button) findViewById(R.id.btnItIsNotMe);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewStudent.setText("Мой номер по списку - №6");
                checkBox.setChecked(true);
            }
        };

        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);
    }

    public void ocBtnItIsNotMe(View view) {
        textViewStudent.setText("Это тоже я делал!");
        checkBox.setChecked(false);
    }
}