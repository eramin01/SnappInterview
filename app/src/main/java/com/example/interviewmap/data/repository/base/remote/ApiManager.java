package com.example.interviewmap.data.repository.base.remote;

import com.example.interviewmap.data.repository.vehicle.remote.VehiclesApiService;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final String BASE_URL = "https://snapp.ir/";

    private static ApiManager sInstance;

    public static ApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new ApiManager();
        }

        return sInstance;
    }

    private Retrofit mRetforit;

    private ApiManager() {
        mRetforit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public VehiclesApiService getApiClient() {
        return mRetforit.create(VehiclesApiService.class);
    }

}
