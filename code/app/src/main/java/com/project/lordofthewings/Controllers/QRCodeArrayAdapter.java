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

import java.util.List;

public class QRCodeArrayAdapter extends ArrayAdapter<QRCode> {
    public QRCodeArrayAdapter(@NonNull Context context) {
        super(context, 0);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.qrcodecontent,
                    parent, false);
        } else {
            view = convertView;
        }
        QRCode qrCode = (QRCode)getItem(position);
        String url = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";
        TextView qrCodeName = view.findViewById(R.id.qrcode_name);
        TextView qrCodePoints = view.findViewById(R.id.qrcode_points);
        ImageView qrCodeImage = view.findViewById(R.id.qrcode_image);

        qrCodeName.setText(qrCode.getQRName());
        qrCodePoints.setText(qrCode.getQRScore() + " Points");
        Picasso.get().load(url + qrCode.getHash()).into(qrCodeImage);

        return view;
    }
}
