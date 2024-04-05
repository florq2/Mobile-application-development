package ru.mirea.venikovea.simplefragmentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Fragment fragment1, fragment2;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment1 = new FirstFragment();
        fragment2 = new SecondFragment();
    }

    // R.id.btnFirstFragment и R.id.btnSecondFragment не распознаются как константы на этапе компиляции
    public void onClick(View view) {
        fragmentManager = getSupportFragmentManager();
        int viewId = view.getId();
        if (viewId == R.id.btnFirstFragment) { // Не нашел другого выхода кроме как if-else
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment1).commit();
        } else if (viewId == R.id.btnSecondFragment) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment2).commit();
        }
    }
}