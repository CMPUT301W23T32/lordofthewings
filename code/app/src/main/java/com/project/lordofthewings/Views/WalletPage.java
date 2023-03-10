package com.project.lordofthewings.Views;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.lordofthewings.Controllers.QRCodeArrayAdapter;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;

import java.util.ArrayList;
import java.util.Map;

public class WalletPage extends AppCompatActivity {
    private ListView qrCodeList;
    private ArrayAdapter<QRCode> qrCodeAdapter;
    TextView points;
    TextView qrcodecount;
    TextView usernametext;
    String username;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.walletpage);
        //TextView hometext = findViewById(R.id.TextView01);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sh.getString("username", "");
        qrCodeList = findViewById(R.id.qrCodeListView);
        qrCodeAdapter = new QRCodeArrayAdapter(this);
        qrCodeList.setAdapter(qrCodeAdapter);
        usernametext = findViewById(R.id.usernameTextView);
        points = findViewById(R.id.points);
        qrcodecount = findViewById(R.id.qrcodeCount);
        this.username = username;
        ImageButton back = findViewById(R.id.backIcon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletPage.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e("data", document.get("QRCodes").toString());
                        ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");

                        if (qrCodes != null){
                            qrCodeAdapter.clear();
                            for (Map<String, Object> qrCode : qrCodes) {
                                String hash = qrCode.get("hash").toString();
                                qrCodeAdapter.add(new QRCode(hash, 0));
                                qrCodeAdapter.notifyDataSetChanged();
                                int count = qrCodes.size();
                                points.setText(document.get("Score").toString() + " Points");
                                qrcodecount.setText(count);
                            }
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        Button scan_qr_code = findViewById (R.id.scanButton);
        scan_qr_code.setOnClickListener(c -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("Scan a QR Code");
            integrator.setOrientationLocked(false);
            integrator.initiateScan();

        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e("data", document.get("QRCodes").toString());
                        ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                        if (qrCodes != null){
                            qrCodeAdapter.clear();
                            for (Map<String, Object> qrCode : qrCodes) {
                                int count = qrCodes.size();
                                String hash = qrCode.get("hash").toString();
                                qrCodeAdapter.add(new QRCode(hash, 0));
                                qrCodeAdapter.notifyDataSetChanged();
                                points.setText(document.get("Score").toString() + " Points");
                                qrcodecount.setText(count);
                            }
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                String qr_code = result.getContents();
                Intent intent = new Intent(WalletPage.this, QRCodeScan.class);
                intent.putExtra("qr_code", qr_code);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
