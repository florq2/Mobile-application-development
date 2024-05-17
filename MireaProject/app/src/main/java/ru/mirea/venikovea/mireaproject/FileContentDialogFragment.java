package ru.mirea.venikovea.mireaproject;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FileContentDialogFragment extends DialogFragment {

    private static final String ARG_FILE_CONTENT = "file_content";

    public static FileContentDialogFragment newInstance(String fileContent) {
        FileContentDialogFragment fragment = new FileContentDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_CONTENT, fileContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_file_content, container, false);

        TextView textViewFileContent = view.findViewById(R.id.text_view_file_content);
        if (getArguments() != null) {
            String fileContent = getArguments().getString(ARG_FILE_CONTENT);
            textViewFileContent.setText(fileContent);
        }

        return view;
    }
}
