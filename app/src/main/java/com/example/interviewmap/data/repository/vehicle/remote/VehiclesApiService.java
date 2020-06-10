package com.example.interviewmap.data.repository.vehicle.remote;

import com.example.interviewmap.data.model.vehicle.VehiclesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface VehiclesApiService {

    @GET("assets/test/document.json")
    Single<VehiclesResponse> getVehicles();

}
