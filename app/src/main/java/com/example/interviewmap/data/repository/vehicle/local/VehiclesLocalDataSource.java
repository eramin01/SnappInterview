package com.example.interviewmap.data.repository.vehicle.local;

import android.util.Log;

import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.base.LocalDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class VehiclesLocalDataSource implements LocalDataSource<Vehicle> {

    private final VehicleDao mDao;

    public VehiclesLocalDataSource(VehicleDao dao) {
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
                Log.e("rx", "onSaveStarted" + ", thread = " + Thread.currentThread().getName());
                // we must do these works sequentially.
                mDao.deleteAllVehicles();//we don't want to add vehicles repeatedly.
                Log.e("rx", "onOldDataDeleted" + ", thread = " + Thread.currentThread().getName());
                mDao.insertVehicles(list);
                Log.e("rx", "onNewDataInserted" + ", thread = " + Thread.currentThread().getName());

//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                Log.e("rx", "onSaveCompleted" + ", thread = " + Thread.currentThread().getName());
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
