package com.project.lordofthewings.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsArrayAdapter  extends ArrayAdapter<HashMap<QRCode, Float>> {
    public MapsArrayAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.qr_map_content, parent, false);
        } else {
            view = convertView;
        }
        HashMap<QRCode, Float> qrMap = getItem(position);
        // get first key from qrMap
        QRCode qrCode = (QRCode) qrMap.keySet().toArray()[0];
        Float distance = qrMap.get(qrCode);
        Integer estimatedDistance = Math.round(distance)/1000;

        String url = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";
        TextView qrCodeName = view.findViewById(R.id.qrcode_name);
        TextView qrCodePoints = view.findViewById(R.id.qrcode_points);
        TextView qrCodeDistance = view.findViewById(R.id.qrcode_distance);
        ImageView qrCodeImage = view.findViewById(R.id.qrcode_image);
        qrCodeName.setText(qrCode.getQRName());
        qrCodePoints.setText(qrCode.getQRScore() + " Points");
        if (distance < 1000) {
            qrCodeDistance.setText(Math.round(distance) + " m");
        }
        else{
            qrCodeDistance.setText(estimatedDistance + " km");
        }


        Picasso.get().load(url + qrCode.getHash()).into(qrCodeImage);

        return view;
    }

}
