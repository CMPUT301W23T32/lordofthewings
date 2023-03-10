package com.project.lordofthewings.Views;
import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Controllers.QRCodeArrayAdapter;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Models.Wallet.Wallet;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.project.lordofthewings.Views.StartUpPages.StartUpPage;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class for the home page of the app. This is the first page that the user sees after signing up.
 */

public class HomePage extends AppCompatActivity {
    TextView usernametext;
    String username;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.home_page);
        //TextView hometext = findViewById(R.id.TextView01);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sh.getString("username", "");
        usernametext = findViewById(R.id.usernameTextView);
        usernametext.setText("Welcome " + username + "!");

        ImageButton mapButton= findViewById(R.id.mapButton);
        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MapsActivity.class);
            startActivity(intent);});

        this.username = username;
        ImageButton wallet_page = findViewById(R.id.walletButton);
        wallet_page.setOnClickListener(c -> {
            Intent intent = new Intent(HomePage.this, WalletPage.class);
            startActivity(intent);
        });
        Button scan_qr_code = findViewById(R.id.scanButton);
        scan_qr_code.setOnClickListener(c -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("Scan a QR Code");
            integrator.setOrientationLocked(false);
            integrator.initiateScan();

        });
        ImageButton settingsButton = findViewById(R.id.settingsButton);
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
                Intent intent = new Intent(HomePage.this, QRCodeScan.class);
                intent.putExtra("qr_code", qr_code);
                startActivity(intent);
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


}
