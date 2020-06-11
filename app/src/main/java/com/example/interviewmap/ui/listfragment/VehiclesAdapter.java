package com.example.interviewmap.ui.listfragment;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interviewmap.data.model.vehicle.Vehicle;

import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehicleViewHolder> {

    private List<Vehicle> mData;

    public VehiclesAdapter(List<Vehicle> vehicles) {
        mData = vehicles;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VehicleViewHolder.create(parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        holder.bind(getItemAt(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private Vehicle getItemAt(int position) {
        return mData == null ? null : mData.get(position);
    }

}
