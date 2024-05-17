package ru.mirea.venikovea.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FileOperations extends Fragment {

    public FileOperations() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_file_operations, container, false);

        Button buttonCreateFile = root.findViewById(R.id.button_create_file);
        Button buttonOpenFile = root.findViewById(R.id.button_open_file);

        buttonCreateFile.setOnClickListener(v -> showCreateFileDialog());
        buttonOpenFile.setOnClickListener(v -> showOpenFileDialog());

        return root;
    }

    private void showCreateFileDialog() {
        CreateFileDialogFragment dialog = new CreateFileDialogFragment();
        dialog.show(getFragmentManager(), "CreateFileDialog");
    }

    private void showOpenFileDialog() {
        OpenFileDialogFragment dialog = new OpenFileDialogFragment();
        dialog.show(getFragmentManager(), "OpenFileDialog");
    }
}