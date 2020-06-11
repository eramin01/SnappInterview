package com.example.interviewmap.ui.listfragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interviewmap.R;
import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.example.interviewmap.data.repository.base.LocalDataSource;
import com.example.interviewmap.data.repository.base.RemoteDataSource;
import com.example.interviewmap.data.repository.base.local.AppDatabase;
import com.example.interviewmap.data.repository.base.remote.ApiManager;
import com.example.interviewmap.data.repository.vehicle.VehiclesRepository;
import com.example.interviewmap.data.repository.vehicle.local.VehiclesLocalDataSource;
import com.example.interviewmap.data.repository.vehicle.remote.VehiclesRemoteDataSource;
import com.example.interviewmap.ui.MapsActivity;
import com.example.interviewmap.ui.VehicleListViewModel;
import com.example.interviewmap.ui.VehicleListViewModelFactory;
import com.example.interviewmap.util.ConnectivityDetector;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class VehiclesBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "VehiclesBottomSheetFrag";

    public static VehiclesBottomSheetFragment newInstance() {
        return new VehiclesBottomSheetFragment();
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vehicles_bottomsheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        VehicleListViewModelFactory factory = new VehicleListViewModelFactory(getVehicleRepositoryInstance());

        if (getActivity() instanceof MapsActivity) {
            //we use shared viewModel to get vehicles
            VehicleListViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(VehicleListViewModel.class);

            viewModel.getVehiclesLiveData().observe(this, vehicles -> {
                if (vehicles == null || vehicles.size() == 0) {
                    //Todo: handle empty state
                }

                mAdapter = new VehiclesAdapter(vehicles);
                mRecyclerView.setAdapter(mAdapter);
            });
        }
    }

    private VehiclesRepository getVehicleRepositoryInstance() {
        // we can use dependencyInjection to improve or remove this function
        RemoteDataSource<Vehicle> remoteDataSource = VehiclesRemoteDataSource.getInstance(ApiManager.getInstance().getApiClient());
        LocalDataSource<Vehicle> localDataSource = VehiclesLocalDataSource.getInstance(AppDatabase.getInstance(getContext().getApplicationContext()).getDao());

        return VehiclesRepository.getInstance(remoteDataSource, localDataSource, (ConnectivityDetector) getActivity());
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        mRecyclerView = null;
        mAdapter = null;
    }

}
