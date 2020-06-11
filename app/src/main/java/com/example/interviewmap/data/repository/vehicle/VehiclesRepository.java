package com.example.interviewmap.data.repository.vehicle;

import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.base.LocalDataSource;
import com.example.interviewmap.data.repository.base.RemoteDataSource;
import com.example.interviewmap.data.repository.base.SavableDataSource;
import com.example.interviewmap.util.ConnectivityDetector;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class VehiclesRepository implements SavableDataSource<List<Vehicle>> {

    private volatile static VehiclesRepository sInstance = null;

    public static VehiclesRepository getInstance(RemoteDataSource<Vehicle> remoteDataSource, LocalDataSource<Vehicle> localDataSource, ConnectivityDetector connectivityDetector) {
        if (sInstance == null) {
            sInstance = new VehiclesRepository(remoteDataSource, localDataSource, connectivityDetector);
        }

        return sInstance;
    }

    private final RemoteDataSource<Vehicle> mRemoteDataSource;
    private final LocalDataSource<Vehicle> mLocalDataSource;

    private final ConnectivityDetector mConnectivityDetector;

    private VehiclesRepository(RemoteDataSource<Vehicle> remoteDataSource, LocalDataSource<Vehicle> localDataSource, ConnectivityDetector connectivityDetector) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;

        mConnectivityDetector = connectivityDetector;
    }

    @Override
    public Single<List<Vehicle>> getData() {
        if (mConnectivityDetector.isOnline()) {
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
