package com.project.lordofthewings.Controllers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.WalletPage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Custom Array Adapter for a list of QRCode objects to be displayed in a ListView
 * throughout the app.
 */
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
        SharedPreferences sh = getContext().getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sh.getString("username", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
