package ru.mirea.venikovea.mireaproject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;

public class OpenFileDialogFragment extends DialogFragment {

    public OpenFileDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_open_file, container, false);

        EditText editTextFileName = view.findViewById(R.id.edit_text_file_name_open);
        Button buttonOpen = view.findViewById(R.id.button_open_file);

        buttonOpen.setOnClickListener(v -> {
            String fileName = editTextFileName.getText().toString();
            openFile(fileName);
            dismiss();
        });

        return view;
    }

    private void openFile(String fileName) {
        String fileContent = readFile(fileName);

        showFileContentDialog(fileContent);
    }

    private String readFile(String fileName) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, fileName + ".txt");

        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            inputStream.read(data);
            inputStream.close();

            String text = new String(data);

            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private void showFileContentDialog(String fileContent) {
        FileContentDialogFragment dialog = FileContentDialogFragment.newInstance(fileContent);
        dialog.show(getFragmentManager(), "FileContentDialog");
    }
}