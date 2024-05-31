package ru.mirea.venikovea.mireaproject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapFragment extends Fragment implements AddMarkerFragment.OnMarkerAddedListener{
    private MapView mapView;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private Boolean isWork = false;
    private MyLocationNewOverlay locationNewOverlay;
    private Button addNewMarkerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getActivity(),
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()));

        checkPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        addNewMarkerButton = view.findViewById(R.id.addNewMarkerButton);
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(12.0);
        GeoPoint startPoint = new GeoPoint(55.752717, 37.622972);
        mapController.setCenter(startPoint);

        locationNewOverlay = new MyLocationNewOverlay(new
                GpsMyLocationProvider(getActivity()), mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(this.locationNewOverlay);

        CompassOverlay compassOverlay = new CompassOverlay(getActivity(),
                new InternalCompassOrientationProvider(getActivity()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        final Context context = getActivity();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);
        
        addNewMarkerButton.setOnClickListener(v -> {
            showAddMarkerFragment();
        });

        addNewMarker(new GeoPoint(55.794229, 37.700772),
                "РТУ МИРЭА, Стромынка 20", "Российское высшее учебное заведение в Москве," +
                        " которое образовано в 2015 году в результате объединения МИРЭА, " +
                        "МГУПИ, МИТХТ имени М. В. Ломоносова и ряда образовательных, " +
                        "научных, конструкторских и производственных организаций.");
        addNewMarker(new GeoPoint(55.752717, 37.622972),
                "Храм Василия Блаженного", "православный храм на Красной площади в Москве," +
                        " памятник русской архитектуры. Построен в 1555—1561 годах.");
        addNewMarker(new GeoPoint(55.731714, 37.574556),
                "РТУ МИРЭА, Малая Пироговская 1", "Российское высшее учебное заведение в Москве," +
                        " которое образовано в 2015 году в результате объединения МИРЭА, " +
                        "МГУПИ, МИТХТ имени М. В. Ломоносова и ряда образовательных, " +
                        "научных, конструкторских и производственных организаций.");
        addNewMarker(new GeoPoint(55.669986, 37.480409),
                "РТУ МИРЭА, Вернадского 78", "Российское высшее учебное заведение в Москве," +
                        " которое образовано в 2015 году в результате объединения МИРЭА, " +
                        "МГУПИ, МИТХТ имени М. В. Ломоносова и ряда образовательных, " +
                        "научных, конструкторских и производственных организаций.");
        addNewMarker(new GeoPoint(55.759467, 37.618976),
                "Большой театр России", "один из крупнейших и старейших в России и один " +
                        "из самых значительных в мире театров оперы и балета.");

        return view;
    }

    private void showAddMarkerFragment() {
        AddMarkerFragment addMarkerFragment = new AddMarkerFragment();
        addMarkerFragment.setOnMarkerAddedListener(this);
        addMarkerFragment.show(getFragmentManager(), "add_marker");
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getActivity(),
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getActivity(),
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()));
        if (mapView != null) {
            mapView.onPause();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    isWork = true;
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSION);
                }
            } else {
                isWork = true;
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                boolean allPermissionsGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
                isWork = allPermissionsGranted;
            }
        }
    }

    private void addNewMarker(GeoPoint geoPoint, String title, String desc) {
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                showMarkerInfoFragment(marker.getTitle(), desc);
                return true;
            }
        });

        Drawable originalDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.marker, null);
        if (originalDrawable != null) {
            int width = 75;
            int height = 75;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            originalDrawable.setBounds(0, 0, width, height);
            originalDrawable.draw(canvas);
            Drawable resizedDrawable = new BitmapDrawable(getResources(), bitmap);
            marker.setIcon(resizedDrawable);
        }

        mapView.getOverlays().add(marker);
        marker.setTitle(title);
    }

    private void showMarkerInfoFragment(String title, String description) {
        MarkerInfoFragment markerInfoFragment = MarkerInfoFragment.newInstance(title, description);
        markerInfoFragment.show(getFragmentManager(), "marker_info");
    }

    @Override
    public void onMarkerAdded(double latitude, double longitude, String title, String description) {
        GeoPoint geoPoint = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(mapView);
        marker.setPosition(geoPoint);
        marker.setTitle(title);
        marker.setSnippet(description);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                showMarkerInfoFragment(title, description);
                return true;
            }
        });

        Drawable originalDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.marker, null);
        if (originalDrawable != null) {
            int width = 75;
            int height = 75;
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            originalDrawable.setBounds(0, 0, width, height);
            originalDrawable.draw(canvas);
            Drawable resizedDrawable = new BitmapDrawable(getResources(), bitmap);
            marker.setIcon(resizedDrawable);
        }

        mapView.getOverlays().add(marker);
    }
}