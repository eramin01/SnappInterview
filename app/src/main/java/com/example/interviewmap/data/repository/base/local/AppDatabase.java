package com.example.interviewmap.data.repository.base.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.vehicle.local.VehicleDao;

@Database(entities = Vehicle.class, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "snappVehicles";

    private static volatile AppDatabase sInstance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        }

        return sInstance;
    }

    public abstract VehicleDao getDao();

}
