package com.example.interviewmap.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.interviewmap.data.repository.vehicle.VehiclesRepository;

public class VehicleListViewModelFactory implements ViewModelProvider.Factory {

    private VehiclesRepository mRepository;

    public VehicleListViewModelFactory(VehiclesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel > T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VehicleListViewModel.class)) {
            return (T) new VehicleListViewModel(mRepository);
        }

        throw new RuntimeException("ViewModel of type = " + modelClass.getName() + " Not supported");
    }
}
