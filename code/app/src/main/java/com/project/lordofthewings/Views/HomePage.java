package com.project.lordofthewings.Views;

import android.app.Dialog;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Class for the home page of the app. This is the first page that the user sees after signing up.
 */

public class HomePage extends AppCompatActivity {
    private TextView usernametext;
    private String username;
    private RelativeLayout header;
    private LinearLayout scan_qr_code, nav_bar;
    private ImageButton profileButton, leaderBoard, mapButton, wallet_page, settingsButton;
    private ImageView profileQR;
    private Animation fade_in, fade_out;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.new_homepage);

        // getting the username from shared preferences
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sh.getString("username", "");
        this.username = username;

        // mapping variables from layout
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        header = findViewById(R.id.header);
        scan_qr_code = findViewById(R.id.scanButton);
        nav_bar = findViewById(R.id.navbar);
        usernametext = findViewById(R.id.usernameTextView);
        usernametext.setText(username);
        profileButton = findViewById(R.id.profileButton);
        leaderBoard = findViewById(R.id.leaderboardButton);
        profileQR = findViewById(R.id.qr_code_scan_profile);
        mapButton= findViewById(R.id.mapButton);
        wallet_page = findViewById(R.id.walletButton);
        settingsButton = findViewById(R.id.settingsButton);

        // Handler for delaying intents
        Handler handler = new Handler();

        // setting entry animations
        scan_qr_code.startAnimation(fade_in);
        header.startAnimation(fade_in);
        nav_bar.startAnimation(fade_in);

        // Loading the profile QRCode
        String url ="https://quickchart.io/qr?text=" + "https://lordofthewingswebsite.vercel.app/"+username;
        Picasso.get().load(url).into(profileQR);

        // setting on click listeners for buttons

        profileQR.setOnClickListener(v -> {
            ProfileQRCodeFragment dialog = new ProfileQRCodeFragment();
            dialog.show(getSupportFragmentManager(), "Profile QR Code");
        });

        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ProfilePage.class);
            intent.putExtra("username", username);
            runExitAnimation();
            // delay the intent so the animation can finish
            startActivity(intent);
            
        });

        leaderBoard.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, LeaderBoardPage.class);
            runExitAnimation();
            // delay the intent so the animation can finish
            startActivity(intent);
        });

        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MapsActivity.class);
            runExitAnimation();
            startActivity(intent);
        });

        wallet_page.setOnClickListener(c -> {
            Intent intent = new Intent(HomePage.this, WalletPage.class);
            runExitAnimation();
            startActivity(intent);
        });

        scan_qr_code.setOnClickListener(c -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("Scan a QR Code");
            integrator.setOrientationLocked(false);
            integrator.initiateScan();

        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenuSettings = new PopupMenu(HomePage.this, settingsButton);
                popupMenuSettings.getMenuInflater().inflate(R.menu.settings_menu, popupMenuSettings.getMenu());
                popupMenuSettings.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int menuItemId = menuItem.getItemId();
                        if (menuItemId == R.id.logoutMenuItem){
                            clearSharedPreferences();
                            Intent intent = new Intent(HomePage.this, MainActivity.class);
                            //destroying all activities before this so it doesn't keep login vals
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        else if (menuItemId == R.id.settingsMenuItem) {
                            //placeholder stuff in case we ever add a settings option
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                });
                popupMenuSettings.show();
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
                if (qr_code.contains("https://lordofthewingswebsite.vercel.app/")){;
                    Log.e("qr_code", qr_code.substring("https://lordofthewingswebsite.vercel.app/".length()));
                    qr_code = qr_code.substring("https://lordofthewingswebsite.vercel.app/".length());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    try{
                        DocumentReference docRef = db.collection("Users").document(qr_code);
                        String finalQr_code = qr_code;
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Intent intent = new Intent(HomePage.this, ProfilePage.class);
                                        intent.putExtra("username", finalQr_code);
                                        finish();
                                        startActivity(intent);
                                    } else {
                                        Log.d("MainActivity", "No such document");
                                    }
                                } else {
                                    Log.d( "get failed with ", String.valueOf(task.getException()));
                                }
                            }
                        });
                    }catch(Exception e){
                        Log.e("error is", String.valueOf(e));
                        Intent intent = new Intent(HomePage.this, QRCodeScan.class);
                        intent.putExtra("qr_code", qr_code);
                        finish();
                        startActivity(intent);
                    }
                }else{
                    Intent intent = new Intent(HomePage.this, QRCodeScan.class);
                    intent.putExtra("qr_code", qr_code);
                    finish();
                    startActivity(intent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     *  Clears the shared preferences to log out the user (testing purposes)
     */
    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    private void runExitAnimation(){
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        header = findViewById(R.id.header);
        header.startAnimation(fade_out);
        scan_qr_code = findViewById(R.id.scanButton);
        scan_qr_code.startAnimation(fade_out);
        nav_bar = findViewById(R.id.navbar);
        nav_bar.startAnimation(fade_out);
    }
}
