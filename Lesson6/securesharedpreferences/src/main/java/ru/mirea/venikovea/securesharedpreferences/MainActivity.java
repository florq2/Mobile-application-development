package ru.mirea.venikovea.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;

import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.venikovea.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String mainKeyAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        try {
            mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        SharedPreferences secureSharedPreferences = null;
        try {
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        secureSharedPreferences.edit().putString("secure", "Бенедикт Камбербэтч").apply();

        binding.bestActorTextView.setText(secureSharedPreferences.getString("secure", "Актер"));
    }
}