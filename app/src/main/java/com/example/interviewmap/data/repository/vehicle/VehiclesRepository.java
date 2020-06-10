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
                    saveDataAndSubscribe(vehicles);

                    return Single.just(vehicles);
                }
            });
        }

        return mLocalDataSource.getData();
    }

    private void saveDataAndSubscribe(List<Vehicle> vehicles) {
        saveData(vehicles).subscribe();
    }

    @Override
    public Completable saveData(List<Vehicle> data) {
        return mLocalDataSource.saveData(data);
    }

}
