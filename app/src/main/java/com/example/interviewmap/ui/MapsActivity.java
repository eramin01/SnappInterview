package com.example.interviewmap.ui;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.interviewmap.R;
import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.base.local.AppDatabase;
import com.example.interviewmap.data.repository.base.LocalDataSource;
import com.example.interviewmap.data.repository.base.RemoteDataSource;
import com.example.interviewmap.data.repository.base.remote.ApiManager;
import com.example.interviewmap.data.repository.vehicle.VehiclesRepository;
import com.example.interviewmap.data.repository.vehicle.local.VehiclesLocalDataSource;
import com.example.interviewmap.data.repository.vehicle.remote.VehiclesRemoteDataSource;
import com.example.interviewmap.ui.listfragment.VehiclesBottomSheetFragment;
import com.example.interviewmap.util.BitmapMarkerCreator;
import com.example.interviewmap.util.ConnectivityDetector;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectivityDetector {

    private static final float CAMERA_DEFAULT_ZOOM = 16f;

    private VehicleListViewModel mViewModel;

    private GoogleMap mMap;
    private DialogFragment mVehicleListDialogFragment;
    private View mRetryContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setupViewModel();

        mRetryContainerView = findViewById(R.id.container_retry);

        findViewById(R.id.button_retry).setOnClickListener(v -> fillScreen());

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

        mRetryContainerView.setVisibility(View.VISIBLE);

//        if (isOnline) {
//            if (mVehicleListDialogFragment != null) {
//                mVehicleListDialogFragment.dismiss();
//            }
//
//            initializeMap();
//        }
//        else {
            mViewModel.getVehiclesLiveData().observe(MapsActivity.this, mShowLocalListObserver);// in case it registered before but we're not online anymore
            mViewModel.loadVehicleList();
//        }
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
    }

    private Observer<List<Vehicle>> mShowLocalListObserver =  new Observer<List<Vehicle>>() {
        @Override
        public void onChanged(List<Vehicle> vehicles) {
            showVehiclesInBottomSheetFragment();
        }
    };

    private void showVehiclesInBottomSheetFragment() {
        if (mVehicleListDialogFragment == null) {
            mVehicleListDialogFragment = VehiclesBottomSheetFragment.newInstance();
            mVehicleListDialogFragment.show(getSupportFragmentManager(), VehiclesBottomSheetFragment.TAG);
        }
        else if (mVehicleListDialogFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(mVehicleListDialogFragment);
        }
        else {
            mVehicleListDialogFragment.show(getSupportFragmentManager(), VehiclesBottomSheetFragment.TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMap = null;
        mVehicleListDialogFragment = null;
        mShowLocalListObserver = null;
    }

}
