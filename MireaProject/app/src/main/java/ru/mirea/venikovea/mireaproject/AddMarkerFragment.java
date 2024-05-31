package ru.mirea.venikovea.mireaproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddMarkerFragment extends DialogFragment {
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private OnMarkerAddedListener listener;

    public interface OnMarkerAddedListener {
        void onMarkerAdded(double latitude, double longitude, String title, String description);
    }

    public void setOnMarkerAddedListener(OnMarkerAddedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_marker, container, false);

        latitudeEditText = view.findViewById(R.id.latitude);
        longitudeEditText = view.findViewById(R.id.longitude);
        titleEditText = view.findViewById(R.id.title);
        descriptionEditText = view.findViewById(R.id.description);
        Button addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitudeStr = latitudeEditText.getText().toString();
                String longitudeStr = longitudeEditText.getText().toString();
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                if (TextUtils.isEmpty(latitudeStr) || TextUtils.isEmpty(longitudeStr) ||
                        TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
                    Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    double latitude = Double.parseDouble(latitudeStr);
                    double longitude = Double.parseDouble(longitudeStr);
                    if (listener != null) {
                        listener.onMarkerAdded(latitude, longitude, title, description);
                    }
                    dismiss();
                }
            }
        });

        return view;
    }
}