package ru.mirea.venikovea.mireaproject;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassFragment extends Fragment implements SensorEventListener {
    private ImageView ivDynamic;
    private TextView tvDegree;
    private float currentDegree = 0f;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private Sensor gyroscope;
    private float[] gravity;
    private float[] geomagnetic;
    private float[] gyroscopeValues = new float[3];

    private final Handler handler = new Handler();
    private final Runnable updateCompassTask = new Runnable() {
        @Override
        public void run() {
            updateCompass();
            handler.postDelayed(this, 200);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        ivDynamic = view.findViewById(R.id.iv_compass_dynamic);
        tvDegree = view.findViewById(R.id.textViewDegrees);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
        handler.postDelayed(updateCompassTask, 1000); // Start compass update
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        handler.removeCallbacks(updateCompassTask); // Stop compass update when fragment is paused
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gravity = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscopeValues = event.values;
        }
    }

    private void updateCompass() {
        if (gravity != null && geomagnetic != null) {
            float[] rotationMatrix = new float[9];
            if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                float azimuthInRadians = orientation[0];
                float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);
                tvDegree.setText("Degree from North: " + Float.toString(azimuthInDegrees) + " degrees");

                RotateAnimation ra = new RotateAnimation(currentDegree, -azimuthInDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(210);
                ra.setFillAfter(true);

                ivDynamic.startAnimation(ra);
                currentDegree = -azimuthInDegrees;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing for now
    }
}
