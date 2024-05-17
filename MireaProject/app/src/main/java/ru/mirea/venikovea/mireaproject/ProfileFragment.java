package ru.mirea.venikovea.mireaproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.venikovea.mireaproject.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int CAMERA_REQUEST = 0;
    private boolean isWork = false;
    private Uri imageUri;
    private OnProfilePhotoSavedListener photoListener;
    private OnProfileNameSavedListener nameListener;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        sharedPreferences = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);

        loadUserData();

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        binding.changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (isWork) {
                    try {
                        File photoFile = createImageFile();
                        String authorities = requireContext().getPackageName() + ".fileprovider";
                        imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        binding.saveUserDataButton.setOnClickListener(v -> saveUserData());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = storageDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("JPEG_");
            }
        });

        if (imageFiles != null && imageFiles.length > 0) {
            File latestImage = imageFiles[imageFiles.length - 1];
            imageUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", latestImage);

            binding.profileImageView.setImageURI(imageUri);
        }

        loadUserData();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isWork = true;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            binding.profileImageView.setImageURI(imageUri);
            photoListener.onProfilePhotoSaved();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File[] imageFiles = storageDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("JPEG_");
            }
        });

        if (imageFiles != null) {
            for (File file : imageFiles) {
                file.delete();
            }
        }

        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", imageFile);

        return imageFile;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnProfilePhotoSavedListener && context instanceof OnProfileNameSavedListener) {
            photoListener = (OnProfilePhotoSavedListener) context;
            nameListener = (OnProfileNameSavedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfilePhotoSavedListener");
        }
    }

    private void loadUserData() {
        String name = sharedPreferences.getString("userName", "");
        String about = sharedPreferences.getString("aboutUser", "");

        binding.userNameEditText.setText(name);
        binding.aboutMeEditText.setText(about);
    }

    private void saveUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userName", binding.userNameEditText.getText().toString());
        editor.putString("aboutUser", binding.aboutMeEditText.getText().toString());

        editor.apply();
    }
}
