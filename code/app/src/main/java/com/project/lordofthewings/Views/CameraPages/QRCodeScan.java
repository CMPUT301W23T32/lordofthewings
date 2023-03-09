package com.project.lordofthewings.Views.CameraPages;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class QRCodeScan extends AppCompatActivity implements LocationListener {
    private String url = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";
    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    Button add_photo;
    Button remove_photo;
    Button save_location;
    Button remove_location;
    TextView location_text;
    String qr_code;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.qr_code_scan);
        Bundle b = getIntent().getExtras();
        qr_code = b.getString("qr_code");
        TextView code = findViewById(R.id.qr_code_text);
        code.setText(qr_code);
        add_photo = findViewById(R.id.add_image_button);
        imageView = findViewById(R.id.location_image);
        location_text = findViewById(R.id.location_added);
        remove_photo = findViewById(R.id.remove_image_button);
        remove_location = findViewById(R.id.remove_location_button);
        location_text.setText("Location Not Added");
        location_text.setVisibility(TextView.VISIBLE);

        // using the QRCode Class
        QRCode qr = new QRCode(qr_code);
        ImageView visual_rep = findViewById(R.id.qr_code_visual_representation);
        Picasso.get().load(url + qr.getHash()).into(visual_rep);
        //visual_rep.setText(qr.getVisualRepresentation());
        TextView points = findViewById(R.id.points);
        points.setText('+' + qr.getQRScore().toString() + " Points");
        TextView qr_code_name = findViewById(R.id.qr_code_name);
        qr_code_name.setText(qr.getQRName());

        remove_location.setOnClickListener(c -> {
            location_text.setText("Location Not Added");
            latitude = "";
            longitude = "";
            remove_location.setVisibility(Button.GONE);
            save_location.setVisibility(Button.VISIBLE);
        });
        add_photo.setOnClickListener(c -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });
        remove_photo.setOnClickListener(c -> {
            imageView.setVisibility(ImageView.GONE);
            add_photo.setVisibility(Button.VISIBLE);
            remove_photo.setVisibility(Button.GONE);
        });
        save_location = findViewById(R.id.add_location_button);
        save_location.setOnClickListener(c -> {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                });
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            location_text.setText("Location Added: " + latitude + ", " + longitude);
            location_text.setVisibility(TextView.VISIBLE);
            save_location.setVisibility(Button.GONE);
            remove_location.setVisibility(Button.VISIBLE);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            imageView.setVisibility(ImageView.VISIBLE);
            add_photo.setVisibility(Button.GONE);
            remove_photo.setVisibility(Button.VISIBLE);
        }
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                        } else {
                            // No location access granted.
                        }
                    }
            );

    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }


}
