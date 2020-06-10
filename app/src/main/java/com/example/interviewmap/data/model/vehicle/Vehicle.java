package com.example.interviewmap.data.model.vehicle;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = Vehicle.TABLE_NAME)
public final class Vehicle {

    static final String TABLE_NAME = "vehicles";

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_TYPE = "type";
    private static final String COLUMN_NAME_LATITUDE = "lat";
    private static final String COLUMN_NAME_LONGITUDE = "lng";
    private static final String COLUMN_NAME_BEARING = "bearing";
    private static final String COLUMN_NAME_IMAGE_URL = "image_url";

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_NAME_ID)
    @SerializedName(COLUMN_NAME_ID)
    private Integer id;

    @NonNull
    @ColumnInfo(name = COLUMN_NAME_TYPE)
    @SerializedName(COLUMN_NAME_TYPE)
    private String type;

    @NonNull
    @ColumnInfo(name = COLUMN_NAME_LATITUDE)
    @SerializedName(COLUMN_NAME_LATITUDE)
    private double lat;

    @NonNull
    @ColumnInfo(name = COLUMN_NAME_LONGITUDE)
    @SerializedName(COLUMN_NAME_LONGITUDE)
    private double lng;

    @NonNull
    @ColumnInfo(name = COLUMN_NAME_BEARING)
    @SerializedName(COLUMN_NAME_BEARING)
    private int bearing;

    @NonNull
    @ColumnInfo(name = COLUMN_NAME_IMAGE_URL)
    @SerializedName(COLUMN_NAME_IMAGE_URL)
    private String imageUrl;

    @NonNull
    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getBearing() {
        return bearing;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }

    public void setImageUrl(@NonNull String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
