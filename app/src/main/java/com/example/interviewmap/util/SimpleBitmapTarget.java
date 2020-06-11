package com.example.interviewmap.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public abstract class SimpleBitmapTarget extends CustomTarget<Bitmap> {

    @Override
    public abstract void onResourceReady(@NonNull Bitmap resource, @Nullable Transition transition);

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
        // do nothing
    }

}
