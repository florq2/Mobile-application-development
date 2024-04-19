package ru.mirea.venikovea.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    private String encryptedPhrase;
    private byte[] keyBytes;

    public static final String ARG_ENCRYPTED_PHRASE = "encrypted_phrase";
    public static final String ARG_SECRET_KEY = "secret_key";

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            encryptedPhrase = args.getString(ARG_ENCRYPTED_PHRASE);
            keyBytes = args.getByteArray(ARG_SECRET_KEY);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        String decryptedPhrase = decryptPhrase(encryptedPhrase, secretKey);

        return decryptedPhrase;
    }

    private String decryptPhrase(String encryptedPhrase, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedBytes = Base64.decode(encryptedPhrase, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
