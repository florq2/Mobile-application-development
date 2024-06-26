package ru.mirea.venikovea.yandexdriver;

import androidx.annotation.NonNull;

import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.user_location.UserLocationView;

public interface UserLocationObjectListener {
    public void onObjectAdded(@NonNull UserLocationView userLocationView);
    public void onObjectRemoved(@NonNull UserLocationView userLocationView);
    public void onObjectUpdate(@NonNull UserLocationView userLocationView,
                               @NonNull ObjectEvent objectEvent);
}