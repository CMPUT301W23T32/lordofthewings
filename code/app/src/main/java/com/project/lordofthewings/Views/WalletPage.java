package com.project.lordofthewings.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.lordofthewings.Controllers.QRCodeArrayAdapter;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class that models the wallet page of the app. This page stores all
 * the QR codes that the user has scanned.
 * Known Issues: None
 */
public class WalletPage extends AppCompatActivity {
    private ListView qrCodeList;
    private ArrayAdapter<QRCode> qrCodeAdapter;
    private TextView points, qrCodeCount;
    private ProgressBar progressBar;
    private ImageButton back;
    private TextView usernametext;
    private Animation fade_in, fade_out;
    private ConstraintLayout linearLayout2;

    Spinner order_selector;
    String username;
    ArrayList<String> qrCodes_test;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.walletpage);

        back = findViewById(R.id.backIcon);
        progressBar = findViewById(R.id.progressBar);
        Button scan_qr_code = findViewById(R.id.scanButton);
        linearLayout2 = findViewById(R.id.linearLayout2);
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        fetchDataAndRefreshUIdefault();
        // setting entry animations
        linearLayout2.setAnimation(fade_in);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletPage.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });

        scan_qr_code.setOnClickListener(c -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("Scan a QR Code");
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        });

        qrCodeList.setOnItemClickListener((parent, view, position, id) -> {
            QRCode qrCode = qrCodeAdapter.getItem(position);
            Intent intent = new Intent(WalletPage.this, QRCodePage.class);
            intent.putExtra("hash", qrCode.getHash());
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchDataAndRefreshUIdefault();
        qrCodeList.setOnItemClickListener((parent, view, position, id) -> {
            QRCode qrCode = qrCodeAdapter.getItem(position);
            Intent intent = new Intent(WalletPage.this, QRCodePage.class);
            intent.putExtra("hash", qrCode.getHash());
            startActivity(intent);
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


    /**
     * Function to fetch data from Firestore and refresh the UI on data change
     *
     *
     */
    public void fetchDataAndRefreshUIdefault() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sh.getString("username", "");
        qrCodeList = findViewById(R.id.qrCodeListView);
        qrCodeAdapter = new QRCodeArrayAdapter(this);
        qrCodeList.setAdapter(qrCodeAdapter);
        usernametext = findViewById(R.id.usernameTextView);
        points = findViewById(R.id.points);
        qrCodeCount = findViewById(R.id.qrcodeCount);
        Chip defaultChip = findViewById(R.id.defaultChip);
        Chip ascendingChip = findViewById(R.id.ascendingChip);
        Chip descendingChip = findViewById(R.id.descendingChip);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                defaultChip.setChecked(true);
                ascendingChip.setChecked(false);
                descendingChip.setChecked(false);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                        ArrayList<Map<String, Object>> qrCodes2 = new ArrayList<>();
                        if (qrCodes != null) {
                            qrCodeAdapter.clear();
                            Integer count = qrCodes.size();
                            for (Map<String, Object> qrCode : qrCodes) {
                                Integer noOfQrCodes = qrCodes.size();
                                String hash = qrCode.get("hash").toString();
                                qrCodeAdapter.add(new QRCode(hash, 0));
                                qrCodeAdapter.notifyDataSetChanged();
                                points.setText(document.get("Score").toString() + " Points");
                                qrCodeCount.setText(noOfQrCodes.toString());
                            }
                        } if (qrCodes.size() == 0 && qrCodes != null) {
                            Integer count = qrCodes.size();
                            points.setText(document.get("Score").toString() + " Points");
                            qrCodeCount.setText(count.toString());
                        }
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Log.d("No Doc", "No such document");
                    }
                } else {
                    Log.d("Err", "get failed with ", task.getException());
                }

                defaultChip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        descendingChip.setChecked(false);
                        ascendingChip.setChecked(false);
                        defaultChip.setChecked(true);
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                                ArrayList<Map<String, Object>> qrCodes2 = new ArrayList<>();
                                if (qrCodes != null) {
                                    qrCodeAdapter.clear();
                                    Integer count = qrCodes.size();
                                    for (Map<String, Object> qrCode : qrCodes) {
                                        Integer noOfQrCodes = qrCodes.size();
                                        String hash = qrCode.get("hash").toString();
                                        qrCodeAdapter.add(new QRCode(hash, 0));
                                        qrCodeAdapter.notifyDataSetChanged();
                                        points.setText(document.get("Score").toString() + " Points");
                                        qrCodeCount.setText(noOfQrCodes.toString());
                                    }
                                } if (qrCodes.size() == 0 && qrCodes != null) {
                                    Integer count = qrCodes.size();
                                    points.setText(document.get("Score").toString() + " Points");
                                    qrCodeCount.setText(count.toString());
                                }
                            } else {
                                Log.d("No Doc", "No such document");
                            }
                        } else {
                            Log.d("Err", "get failed with ", task.getException());
                        }
                    }
                });
                ascendingChip.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        descendingChip.setChecked(false);
                        ascendingChip.setChecked(true);
                        defaultChip.setChecked(false);
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                                ArrayList<Map<String, Object>> qrCodes2 = new ArrayList<>();
                                if (qrCodes != null) {
                                    qrCodeAdapter.clear();
                                    Integer count = qrCodes.size();
                                    for (int i = 0; i < count; i++){
                                        Integer key = 0;
                                        for (int j = 1; j < qrCodes.size(); j++){
                                            if (new QRCode(qrCodes.get(key).get("hash").toString(), 0).getQRScore() > new QRCode(qrCodes.get(j).get("hash").toString(), 0).getQRScore())
                                                key = j;
                                        }
                                        qrCodes2.add(qrCodes.get(key));
                                        qrCodes.remove(qrCodes.get(key));
                                    }
                                    for (int i = 0; i < count; i++){
                                        qrCodes.add(qrCodes2.get(i));
                                    }
                                    for (Map<String, Object> qrCode : qrCodes) {
                                        Integer noOfQrCodes = qrCodes.size();
                                        String hash = qrCode.get("hash").toString();
                                        qrCodeAdapter.add(new QRCode(hash, 0));
                                        qrCodeAdapter.notifyDataSetChanged();
                                        points.setText(document.get("Score").toString() + " Points");
                                        qrCodeCount.setText(noOfQrCodes.toString());
                                    }
                                } if (qrCodes.size() == 0 && qrCodes != null) {
                                    Integer count = qrCodes.size();
                                    points.setText(document.get("Score").toString() + " Points");
                                    qrCodeCount.setText(count.toString());
                                }
                            } else {
                                Log.d("No Doc", "No such document");
                            }
                        } else {
                            Log.d("Err", "get failed with ", task.getException());
                        }
                    }
                });
                descendingChip.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        descendingChip.setChecked(true);
                        ascendingChip.setChecked(false);
                        defaultChip.setChecked(false);
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                                ArrayList<Map<String, Object>> qrCodes2 = new ArrayList<>();
                                if (qrCodes != null) {
                                    qrCodeAdapter.clear();
                                    Integer count = qrCodes.size();
                                    for (int i = 0; i < count; i++){
                                        Integer key = 0;
                                        for (int j = 1; j < qrCodes.size(); j++){
                                            if (new QRCode(qrCodes.get(key).get("hash").toString(), 0).getQRScore() < new QRCode(qrCodes.get(j).get("hash").toString(), 0).getQRScore())
                                                key = j;
                                        }
                                        qrCodes2.add(qrCodes.get(key));
                                        qrCodes.remove(qrCodes.get(key));
                                    }
                                    for (int i = 0; i < count; i++){
                                        qrCodes.add(qrCodes2.get(i));
                                    }
                                    for (Map<String, Object> qrCode : qrCodes) {
                                        Integer noOfQrCodes = qrCodes.size();
                                        String hash = qrCode.get("hash").toString();
                                        qrCodeAdapter.add(new QRCode(hash, 0));
                                        qrCodeAdapter.notifyDataSetChanged();
                                        points.setText(document.get("Score").toString() + " Points");
                                        qrCodeCount.setText(noOfQrCodes.toString());
                                    }
                                } if (qrCodes.size() == 0 && qrCodes != null) {
                                    Integer count = qrCodes.size();
                                    points.setText(document.get("Score").toString() + " Points");
                                    qrCodeCount.setText(count.toString());
                                }
                            } else {
                                Log.d("No Doc", "No such document");
                            }
                        } else {
                            Log.d("Err", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });
    }
    private void runExitAnimation(){

    }
}
