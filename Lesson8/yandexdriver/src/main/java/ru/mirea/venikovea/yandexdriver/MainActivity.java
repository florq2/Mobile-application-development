package ru.mirea.venikovea.yandexdriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.venikovea.yandexdriver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements DrivingSession.DrivingRouteListener,
        UserLocationObjectListener {
    private ActivityMainBinding binding;
    private Point ROUTE_END_LOCATION = new Point(55.794229, 37.700772);
    private Point userLocation;
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private int[] colors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};
    private static final int REQUEST_CODE_PERMISSION = 100;
    private Boolean isWork;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkPermissions();
        Log.d(MainActivity.class.getSimpleName(), isWork.toString());

        mapView = binding.mapview;
        mapView.getMap().setRotateGesturesEnabled(false);

        mapObjects = mapView.getMap().getMapObjects().addCollection();

        if (isWork) {
            initializeRouterAndRequestLocation();
        }
    }

    private void initializeRouterAndRequestLocation() {
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        getUserLocationAndSubmitRequest();
    }

    private void getUserLocationAndSubmitRequest() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                userLocation = new Point(location.getLatitude(), location.getLongitude());
                                mapView.getMap().move(new CameraPosition(
                                        new Point((userLocation.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
                                                (userLocation.getLongitude() + ROUTE_END_LOCATION.getLongitude()) / 2), 10, 0, 0));
                                submitRequest();
                            } else {
                                Toast.makeText(MainActivity.this, "Не удалось получить местоположение", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void submitRequest() {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();

        drivingOptions.setRoutesCount(4);
        ArrayList<RequestPoint> requestPoints = new ArrayList<>();

        if (userLocation != null) {
            requestPoints.add(new RequestPoint(userLocation,
                    RequestPointType.WAYPOINT,
                    null));
        }
        requestPoints.add(new RequestPoint(ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null));

        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions,
                vehicleOptions, this);

        // Добавление маркера в конечной точке
        mapObjects.addPlacemark(ROUTE_END_LOCATION).addTapListener((mapObject, point) -> {
            Toast.makeText(MainActivity.this, "РТУ МИРЭА. Корпус Стромынка, 20\n" + ROUTE_END_LOCATION.getLatitude() + ", " + ROUTE_END_LOCATION.getLongitude(), Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    isWork = true;
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSION);
                }
            } else {
                isWork = true;
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
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
                if (isWork) {
                    initializeRouterAndRequestLocation();
                }
            }
            Log.d(MainActivity.class.getSimpleName(), "Permissions granted: " + isWork.toString());
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        int color;
        for (int i = 0; i < list.size(); i++) {
            color = colors[i];
            mapObjects.addPolyline(list.get(i).getGeometry()).setStrokeColor(color);
        }
    }
    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "An error occurred";
        if (error instanceof RemoteError) {
            errorMessage = "Remote error";
        } else if (error instanceof NetworkError) {
            errorMessage = "Network error";
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

    }

    @Override
    public void onObjectUpdate(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

    }
}
