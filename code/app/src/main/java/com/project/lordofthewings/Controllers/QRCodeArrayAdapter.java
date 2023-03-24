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


        ImageButton qrCodeDelete = view.findViewById(R.id.qrDeleteButton);

        qrCodeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCode qrCode = getItem(position);
                String hash = qrCode.getHash();
                DocumentReference userRef = db.collection("Users").document(username);
                final Integer[] Score = new Integer[1];
                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<QRCode> qrCodes = (ArrayList<QRCode>) document.get("QRCodes");
                                if (qrCodes != null) {
                                    for (int i = 0; i < qrCodes.size(); i++) {
                                        Map<String, Object> qrObject = (Map<String, Object>) qrCodes.get(i);
                                        QRCode qrCode = new QRCode(qrObject.get("hash").toString(), 1);
                                        if (qrCode.getHash().equals(hash)) {
                                            qrCodes.remove(i);
                                            Score[0] = qrCode.getQRScore();
                                            break;
                                        }
                                    }
                                    userRef.update("Score", FieldValue.increment(-Score[0]));
                                    userRef.update("QRCodes", qrCodes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            remove(qrCode);
                                            notifyDataSetChanged();
                                            Toast.makeText(getContext(), "QRCode deleted successfully!", Toast.LENGTH_SHORT).show();
                                            if (getContext() instanceof WalletPage) {
                                                Log.d("breakpoint", "breakpoint");
                                                ((WalletPage)getContext()).fetchDataAndRefreshUIdefault();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Error deleting QRCode: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        });

        qrCodeName.setText(qrCode.getQRName());
        qrCodePoints.setText(qrCode.getQRScore() + " Points");
        Picasso.get().load(url + qrCode.getHash()).into(qrCodeImage);
        return view;
    }
}
