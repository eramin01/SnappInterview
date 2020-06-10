package com.example.interviewmap.data.repository.vehicle;

import android.util.Log;

import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.base.LocalDataSource;
import com.example.interviewmap.data.repository.base.RemoteDataSource;
import com.example.interviewmap.data.repository.base.SavableDataSource;
import com.example.interviewmap.util.ConnectivityChecker;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class VehiclesRepository implements SavableDataSource<List<Vehicle>> {

    private final RemoteDataSource<Vehicle> mRemoteDataSource;
    private final LocalDataSource<Vehicle> mLocalDataSource;

    private final ConnectivityChecker mConnectivityChecker;

    public VehiclesRepository(RemoteDataSource<Vehicle> remoteDataSource, LocalDataSource<Vehicle> localDataSource, ConnectivityChecker connectivityChecker) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;

        mConnectivityChecker = connectivityChecker;
    }

    @Override
    public Single<List<Vehicle>> getData() {
        if (mConnectivityChecker.isOnline()) {
            return mRemoteDataSource.getData().flatMap(new Function<List<Vehicle>, SingleSource<? extends List<Vehicle>>>() {
                @Override
                public SingleSource<? extends List<Vehicle>> apply(List<Vehicle> vehicles) throws Exception {
                    Log.e("rx", "onApiDataReady, size = " + (vehicles == null ? 0 : vehicles.size()) + ", thread = " + Thread.currentThread().getName());

                    saveData(vehicles).subscribe();

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.e("rx", "onApiDataReady, after save called" + ", thread = " + Thread.currentThread().getName());

                    Log.e("rx", "onApiDataReady, sending event for ui" + ", thread = " + Thread.currentThread().getName());
                    return Single.just(vehicles);
                }
            });
        }

        return mLocalDataSource.getData();
    }

    @Override
    public Completable saveData(List<Vehicle> data) {
        return mLocalDataSource.saveData(data);
    }

}
