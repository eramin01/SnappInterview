package com.example.interviewmap.data.repository.vehicle.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.interviewmap.data.model.vehicle.Vehicle;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface VehicleDao {

    @Query("SELECT * FROM vehicles")
    Single<List<Vehicle>> getVehicles();

    @Insert
    void insertVehicles(List<Vehicle> data);

    @Query("DELETE FROM vehicles")
    void deleteAllVehicles();

}
