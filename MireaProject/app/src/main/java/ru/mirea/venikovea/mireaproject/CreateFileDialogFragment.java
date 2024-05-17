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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;

public class CreateFileDialogFragment extends DialogFragment {

    public CreateFileDialogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_file, container, false);

        EditText editTextFileName = view.findViewById(R.id.edit_text_file_name);
        EditText editTextFileContent = view.findViewById(R.id.edit_text_file_content);
        Button buttonSave = view.findViewById(R.id.button_save_file);

        buttonSave.setOnClickListener(v -> {
            String fileName = editTextFileName.getText().toString();
            String fileContent = editTextFileContent.getText().toString();
            saveFile(fileName, fileContent);
            dismiss();
        });

        return view;
    }

    private void saveFile(String fileName, String fileContent) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, fileName + ".txt");

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileContent.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
