package com.example.interviewmap.util;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;
import com.example.interviewmap.data.model.vehicle.Vehicle;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BitmapMarkerCreator {

    public interface MarkerConsumer {

        void onMarkerCreated(MarkerOptions marker);

    }

    public static void createMarker(Context context, Vehicle vehicle, MarkerConsumer consumer) {
        Glide.with(context).asBitmap().load(vehicle.getImageUrl()).into(new SimpleBitmapTarget() {

            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition transition) {
                consumer.onMarkerCreated(createMarker(vehicle, bitmap));
            }

        });
    }

    private static MarkerOptions createMarker(Vehicle vehicle, Bitmap bitmap) {
        return new MarkerOptions()
                .title(vehicle.getType())
                .position(new LatLng(vehicle.getLat(), vehicle.getLng()))
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .rotation(vehicle.getBearing())
                .anchor(0.5f,0.5f)
                .flat(true)
                .draggable(false);
    }

}
