package com.example.interviewmap.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.vehicle.VehiclesRepository;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VehicleListViewModel extends ViewModel {

    private final VehiclesRepository mRepository;

    private MutableLiveData<List<Vehicle>> mLiveVehicles = new MutableLiveData<>();

    //mapping from all markers shown on the map
    private HashMap<Marker, Vehicle> mMarkersHashMap = new HashMap<>();// maybe use this for onMarkerClickListener!
    //user clickedVehicle (on map)
    private MutableLiveData<Vehicle> mSelectedVehicle = new MutableLiveData<>();

    private MutableLiveData<Boolean> mIsLoadingVehicleData = new MutableLiveData<>();

    private Disposable mVehicleListDisposable;

    VehicleListViewModel(VehiclesRepository repository) {
        mRepository = repository;
    }

    void loadVehicleList() {
        mMarkersHashMap.clear();

        mIsLoadingVehicleData.setValue(true);

        mVehicleListDisposable = mRepository.getData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(vehicles -> {
            mIsLoadingVehicleData.setValue(false);

            mLiveVehicles.setValue(vehicles);
        });
    }

    public LiveData<List<Vehicle>> getVehiclesLiveData() {
        return mLiveVehicles;
    }

    void markerAddedToMap(Marker marker, Vehicle vehicle) {
        mMarkersHashMap.put(marker, vehicle);
    }

    void onMarkerClicked(Marker marker) {
        mSelectedVehicle.setValue(mMarkersHashMap.get(marker));
    }

    LiveData<Vehicle> getSelectedVehicle() {
        return mSelectedVehicle;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        mMarkersHashMap.clear();

        if (mVehicleListDisposable != null) {
            mVehicleListDisposable.dispose();
        }
    }

}
