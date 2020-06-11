package com.example.interviewmap.ui.listfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.interviewmap.data.model.vehicle.Vehicle;

class VehicleViewHolder extends RecyclerView.ViewHolder {

    static VehicleViewHolder create(Context context) {
        return new VehicleViewHolder(new VehicleItemView(context));
    }

    VehicleViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void bind(Vehicle vehicle) {
        ((VehicleItemView)itemView).setData(vehicle);
    }

}
