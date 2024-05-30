package ru.mirea.venikovea.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FilenameFilter;

import ru.mirea.venikovea.mireaproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements OnProfilePhotoSavedListener, OnProfileNameSavedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Uri profilePhotoUri;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.dataFragment, R.id.webViewFragment, R.id.notificationFragment,
                R.id.compassFragment, R.id.profileFragment, R.id.microphoneFragment,
                R.id.fileOperations, R.id.weatherFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

        loadProfilePhoto();

        loadUserName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void loadProfilePhoto() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = storageDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("JPEG_");
            }
        });

        if (imageFiles != null && imageFiles.length > 0) {
            File latestImage = imageFiles[imageFiles.length - 1];
            Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", latestImage);

            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);

            ImageView profileImageView = headerView.findViewById(R.id.iv_ProfileImage);
            profileImageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onProfilePhotoSaved() {
        loadProfilePhoto();
    }

    @Override
    public void onProfileNameSaved() {
        loadUserName();
    }

    private void loadUserName() {
        String name = sharedPreferences.getString("userName", "");

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView userName = headerView.findViewById(R.id.userNameNavTextView);
        userName.setText(name);
    }
}

