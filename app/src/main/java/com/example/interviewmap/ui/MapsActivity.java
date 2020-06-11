package com.example.interviewmap.ui;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.interviewmap.R;
import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.base.local.AppDatabase;
import com.example.interviewmap.data.repository.base.LocalDataSource;
import com.example.interviewmap.data.repository.base.RemoteDataSource;
import com.example.interviewmap.data.repository.base.remote.ApiManager;
import com.example.interviewmap.data.repository.vehicle.VehiclesRepository;
import com.example.interviewmap.data.repository.vehicle.local.VehiclesLocalDataSource;
import com.example.interviewmap.data.repository.vehicle.remote.VehiclesRemoteDataSource;
import com.example.interviewmap.util.BitmapMarkerCreator;
import com.example.interviewmap.util.ConnectivityChecker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectivityChecker {

    private static final float CAMERA_DEFAULT_ZOOM = 16f;

    private VehicleListViewModel mViewModel;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupViewModel();

        fillScreen();
    }

    private void setupViewModel() {
        VehicleListViewModelFactory factory = new VehicleListViewModelFactory(getVehicleRepositoryInstance());
        mViewModel = ViewModelProviders.of(this, factory).get(VehicleListViewModel.class);
    }

    private void fillScreen() {
        // we can write an onlineAware component (bu the use of BroadcastReceivers) so that we don't need to check the connection every time
        // and we just listen to network changes. which I didn't have time
        boolean isOnline = isOnline();

        if (isOnline) {
            initializeMap();
        }
        else {
            mViewModel.getVehiclesLiveData().removeObserver(this::showVehiclesOnMap);

            mViewModel.loadVehicleList();
        }
    }

    private VehiclesRepository getVehicleRepositoryInstance() {
        // we can use dependencyInjection to improve or remove this function
        RemoteDataSource<Vehicle> remoteDataSource = VehiclesRemoteDataSource.getInstance(ApiManager.getInstance().getApiClient());
        LocalDataSource<Vehicle> localDataSource = VehiclesLocalDataSource.getInstance(AppDatabase.getInstance(getApplicationContext()).getDao());

        return VehiclesRepository.getInstance(remoteDataSource, localDataSource, MapsActivity.this);
    }

    @Override
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mViewModel.getSelectedVehicle().observe(this, vehicle -> {
            //we can show details about the selected vehicle here
        });

        mMap.setOnMarkerClickListener(marker -> {
            mViewModel.onMarkerClicked(marker);

            return true;
        });

        mViewModel.getVehiclesLiveData().observe(MapsActivity.this, this::showVehiclesOnMap);

        mViewModel.loadVehicleList();
    }

    private void showVehiclesOnMap(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.size() == 0) {
            return;
        }

        double latSum = 0;
        double lngSum = 0;
        for (Vehicle vehicle: vehicles) {
            addMarkerForVehicle(vehicle);

            latSum += vehicle.getLat();
            lngSum += vehicle.getLng();
        }

        double latMean = latSum / vehicles.size();
        double lngMean = lngSum / vehicles.size();

        moveCamera(new LatLng(latMean, lngMean), CAMERA_DEFAULT_ZOOM);
    }

    private void addMarkerForVehicle(Vehicle vehicle) {
        BitmapMarkerCreator.createMarker(MapsActivity.this, vehicle, marker -> {
            Marker addedMarker = mMap.addMarker(marker);

            mViewModel.markerAddedToMap(addedMarker, vehicle);
        });
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMap = null;
    }

}
