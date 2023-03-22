package com.project.lordofthewings.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

public class QRCodePage extends AppCompatActivity {
    String url = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";
    String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_page);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String savedUsername = sh.getString("username", "");
            ImageButton backButton = findViewById(R.id.backIcon);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(QRCodePage.this, WalletPage.class);
            startActivity(intent);
            finish();
        });
        hash = getIntent().getStringExtra("hash");
        //say i have a hash

        QRCode qr = new QRCode(hash,0);

        ImageView qrcodeImage = findViewById(R.id.qrcode_image);
        Picasso.get().load(url + hash).into(qrcodeImage);

        TextView qrcodeName = findViewById(R.id.qrcode_name);
        TextView qrcodeScore = findViewById(R.id.qrcode_score);
        qrcodeName.setText(qr.getQRName());
        qrcodeScore.setText("Points: " + qr.getQRScore() );
    }



}
