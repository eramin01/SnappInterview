package com.example.interviewmap.ui;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.interviewmap.R;
import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.base.local.AppDatabase;
import com.example.interviewmap.data.repository.base.LocalDataSource;
import com.example.interviewmap.data.repository.base.RemoteDataSource;
import com.example.interviewmap.data.repository.base.remote.ApiManager;
import com.example.interviewmap.data.repository.vehicle.VehiclesRepository;
import com.example.interviewmap.data.repository.vehicle.local.VehiclesLocalDataSource;
import com.example.interviewmap.data.repository.vehicle.remote.VehiclesRemoteDataSource;
import com.example.interviewmap.util.ConnectivityChecker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        MapView mapView = null;
//        mapView.

        List<Vehicle> vehicles = getMockVehicles();


        RemoteDataSource<Vehicle> remoteDataSource = new VehiclesRemoteDataSource(ApiManager.getInstance().getApiClient());
        LocalDataSource<Vehicle> localDataSource = new VehiclesLocalDataSource(AppDatabase.getInstance(getApplicationContext()).getDao());
        final VehiclesRepository repository = new VehiclesRepository(remoteDataSource, localDataSource, new ConnectivityChecker() {
            @Override
            public boolean isOnline() {
                return true;
            }
        });

        repository.getData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Vehicle>>() {
            @Override
            public void accept(List<Vehicle> vehicles) throws Exception {
                Log.e("rx", "onDataReady for ui, thread = " + Thread.currentThread().getName());
                Toast.makeText(MapsActivity.this, "تعداد ماشینها = " + (vehicles == null ? 0 : vehicles.size()), Toast.LENGTH_SHORT).show();
            }
        });
//        repository.saveData(vehicles).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action() {
//            @Override
//            public void run() throws Exception {
//                repository.getData().subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Vehicle>>() {
//                    @Override
//                    public void accept(List<Vehicle> vehicles) {
//                        Toast.makeText(MapsActivity.this, "تعداد ماشینها = " + (vehicles == null ? 0 : vehicles.size()), Toast.LENGTH_SHORT).show();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
//                        Toast.makeText(MapsActivity.this, "error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//
//            }
//        });

//        List<String> s = new ArrayList<>();
//        s.add("a");
//        s.add("b");
//        s.add("c");
//        s.add("d");
//        s.add("e");
//        s.add("f");
//        s.add("g");


//        Observable.fromIterable(s).map(x -> {
//            return (x + " t");
//        }).doOnNext(new Consumer<String>() {
//            @Override
//            public void accept(String string) throws Exception {
//                Log.e("rx", string);
//            }
//        }).subscribe();
    }

    private List<Vehicle> getMockVehicles() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setType("ECO");
        vehicle1.setLat(35.7575154);
        vehicle1.setLng(51.4104956);
        vehicle1.setBearing(54);
        vehicle1.setImageUrl("https://snapp.ir/assets/test/snapp_map@2x.png");

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setType("ECO");
        vehicle2.setLat(35.7580966);
        vehicle2.setLng(51.4094662);
        vehicle2.setBearing(205);
        vehicle2.setImageUrl("https://snapp.ir/assets/test/snapp_map@2x.png");

        Vehicle vehicle3 = new Vehicle();
        vehicle3.setType("PLUS");
        vehicle3.setLat(35.7577213);
        vehicle3.setLng(51.4092553);
        vehicle3.setBearing(324);
        vehicle3.setImageUrl("https://snapp.ir/assets/test/snapp_map_st2.png");

        Vehicle vehicle4 = new Vehicle();
        vehicle4.setType("ECO");
        vehicle4.setLat(35.7571681);
        vehicle4.setLng(51.4091973);
        vehicle4.setBearing(287);
        vehicle4.setImageUrl("https://snapp.ir/assets/test/snapp_map@2x.png");

        Vehicle vehicle5 = new Vehicle();
        vehicle5.setType("PLUS");
        vehicle5.setLat(35.7570448);
        vehicle5.setLng(51.4092871);
        vehicle5.setBearing(168);
        vehicle5.setImageUrl("https://snapp.ir/assets/test/snapp_map_st2.png");

        List<Vehicle> list = new ArrayList<>();

        list.add(vehicle1);
        list.add(vehicle2);
        list.add(vehicle3);
        list.add(vehicle4);
        list.add(vehicle5);

        return list;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}
