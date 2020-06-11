package com.example.interviewmap.data.repository.vehicle.local;

import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.base.LocalDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class VehiclesLocalDataSource implements LocalDataSource<Vehicle> {

    private volatile static VehiclesLocalDataSource sInstance = null;

    public static VehiclesLocalDataSource getInstance(VehicleDao dao) {
        if (sInstance == null) {
            sInstance = new VehiclesLocalDataSource(dao);
        }

        return sInstance;
    }

    private final VehicleDao mDao;

    private VehiclesLocalDataSource(VehicleDao dao) {
        mDao = dao;
    }

    @Override
    public Single<List<Vehicle>> getData() {
        return mDao.getVehicles();
    }

    @Override
    public Completable saveData(final List<Vehicle> list) {
        return Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                // we must do these works sequentially.
                mDao.deleteAllVehicles();//we don't want to add vehicles repeatedly.
                mDao.insertVehicles(list);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void clearData() {
        Completable.fromRunnable(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllVehicles();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

}
