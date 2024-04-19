package ru.mirea.venikovea.cryptoloader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import ru.mirea.venikovea.cryptoloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private ActivityMainBinding binding;
    public final String TAG = this.getClass().getSimpleName();
    private final int LoaderID = 1234;
    private String encryptedPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onClickButton(View view) {
        String phrase = binding.editTextText.getText().toString();

        SecretKey secretKey = generateKey();
        byte[] keyBytes = secretKey.getEncoded();

        encryptedPhrase = encryptPhrase(phrase, secretKey);

        Bundle bundle = new Bundle();
        bundle.putString(MyLoader.ARG_ENCRYPTED_PHRASE, encryptedPhrase);
        bundle.putByteArray(MyLoader.ARG_SECRET_KEY, keyBytes);

        LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LoaderID) {
            Toast.makeText(this, "onCreateLoader: " + encryptedPhrase, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, args);
        }
        throw new InvalidParameterException("Invalid loader id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (loader.getId() == LoaderID) {
            Log.d(TAG, "onLoadFinished: " + data);
            Toast.makeText(this, "onLoadFinished: " + data, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    private SecretKey generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String encryptPhrase(String phrase, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(phrase.getBytes());
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }
}