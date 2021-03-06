package com.example.interviewmap.data.repository.vehicle.remote;

import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.model.vehicle.VehiclesResponse;
import com.example.interviewmap.data.repository.base.RemoteDataSource;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class VehiclesRemoteDataSource implements RemoteDataSource<Vehicle> {

    private volatile static VehiclesRemoteDataSource sInstance = null;

    public static VehiclesRemoteDataSource getInstance(VehiclesApiService apiService) {
        if (sInstance == null) {
            sInstance = new VehiclesRemoteDataSource(apiService);
        }

        return sInstance;
    }

    private VehiclesApiService mApiService;

    private VehiclesRemoteDataSource(VehiclesApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<List<Vehicle>> getData() {
        return mApiService.getVehicles().flatMap(new Function<VehiclesResponse, Single<List<Vehicle>>>() {
            @Override
            public Single<List<Vehicle>> apply(VehiclesResponse vehiclesResponse) {
                return Single.just(vehiclesResponse.getCars());
            }
        });
    }

}
