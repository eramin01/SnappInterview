package com.example.interviewmap.data.model.vehicle;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class VehiclesResponse {

    @SerializedName("vehicles")
    private List<Vehicle> mVehicles;

    public List<Vehicle> getCars() {
        return mVehicles;
    }

}
