package com.example.interviewmap.ui.listfragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.interviewmap.R;
import com.example.interviewmap.data.model.vehicle.Vehicle;

/*I used relative layout cause it's fairly simple and works for this project
     and did'nt need to extend view or viewGroup classes directly.
 */
public class VehicleItemView extends RelativeLayout {

    private ImageView mImageVehicleIcon;
    private TextView mTextVehicleType;
    private TextView mTextVehicleLat;
    private TextView mTextVehicleLng;
    private TextView mTextVehicleBearing;

    public VehicleItemView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public VehicleItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public VehicleItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.itemview_vehicle, this, true);

        mImageVehicleIcon = findViewById(R.id.image_icon);
        mTextVehicleType = findViewById(R.id.text_type);
        mTextVehicleBearing = findViewById(R.id.text_bearing);
        mTextVehicleLat = findViewById(R.id.text_lat);
        mTextVehicleLng = findViewById(R.id.text_lng);

        //initialize listeners
    }

    public void setData(Vehicle vehicle) {
        Glide.with(getContext()).load(vehicle.getImageUrl()).into(mImageVehicleIcon);

        mTextVehicleType.setText(vehicle.getType());
        mTextVehicleLat.setText(String.valueOf(vehicle.getLat()));
        mTextVehicleLng.setText(String.valueOf(vehicle.getLng()));
        mTextVehicleBearing.setText(String.valueOf(vehicle.getBearing()));

        //handle other ui config base on viewLogic and vehicle parameters
    }

}
