package com.project.lordofthewings.Views.CameraPages;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.lordofthewings.Models.QRLocation.QRLocation;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Models.Wallet.Wallet;
import com.project.lordofthewings.Models.Wallet.walletCallback;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.HomePage;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle the QRCode Scanning Activity and it's related functions.
 * Known Issues: Linking of the QRCode Image to the QRCode Object is not implemented yet.
 */
public class QRCodeScan extends AppCompatActivity implements walletCallback {
    private String url = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";
    private static final int CAMERA_REQUEST = 1888;
    // change this value when not debugging for qr codes
    boolean debug = true;
    ImageView imageView;
    Button add_photo;
    Button remove_photo;
    Button save_location;
    Button remove_location;
    Button save_button;
    Button cancel_button;
    TextView location_text;
    String qr_code;
    Wallet wallet;
    ProgressBar progressBar;
    QRCode qr;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
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
        save_button = findViewById(R.id.save_button);
        progressBar = findViewById(R.id.progressBar);
        Button cancel_button = findViewById(R.id.cancel_button);
        cancel_button = findViewById(R.id.cancel_button);

        EditText comment = findViewById(R.id.comment);
        // using the QRCode Class
        this.qr = new QRCode(qr_code);
        ImageView visual_rep = findViewById(R.id.qr_code_visual_representation);
        Picasso.get().load(url + qr.getHash()).into(visual_rep);
        //visual_rep.setText(qr.getVisualRepresentation());
        TextView points = findViewById(R.id.points);
        points.setText('+' + qr.getQRScore().toString() + " Points");
        TextView qr_code_name = findViewById(R.id.qr_code_name);
        qr_code_name.setText(qr.getQRName());
        cancel_button.setOnClickListener(c -> {
            Intent intent = new Intent(QRCodeScan.this, HomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        save_button.setOnClickListener(c -> {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
            String username = sh.getString("username", "");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            #kjsabdkjasbk
            DocumentReference docRef = db.collection("Users").document(username);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                            Integer count = qrCodes.size();
                            Integer present = 0;
                            for (int i = 0; i < count; i++){
                                if (qrCodes.get(i).get("hash").toString().equals(qr.getHash())){
                                    present = 1;
                                }
                            }
                            if (present == 0){
                                wallet = new Wallet(username, (ArrayList<QRCode>) document.get("QRCodes"), Math.toIntExact(((Long) document.get("Score"))), db);
                                wallet.addQRCode(qr, latitude, longitude, comment.getText().toString());
                                Intent intent = new Intent(QRCodeScan.this, HomePage.class);
                                progressBar.setVisibility(ProgressBar.GONE);
                                startActivity(intent);
                                finish();
                            }
                            if (present == 1){
                                Toast.makeText(getApplicationContext(), "QR ALREADY ADDED", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference qrCodeimageRef = storageRef.child("images/qrcodes/"+qr.getHash()+".png");
            if (imageView.getDrawable() != null){
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = qrCodeimageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Intent intent = new Intent(QRCodeScan.this, HomePage.class);
                        progressBar.setVisibility(ProgressBar.GONE);
                        startActivity(intent);
                        finish();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Intent intent = new Intent(QRCodeScan.this, HomePage.class);
                        progressBar.setVisibility(ProgressBar.GONE);
                        startActivity(intent);
                        finish();
                    }
                });
            }else{
                qrCodeimageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Intent intent = new Intent(QRCodeScan.this, HomePage.class);
                        progressBar.setVisibility(ProgressBar.GONE);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        Intent intent = new Intent(QRCodeScan.this, HomePage.class);
                        progressBar.setVisibility(ProgressBar.GONE);
                        startActivity(intent);
                        finish();
                    }
                });
            }

        });
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
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation(this);
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions();
//            }
//            if (!checkPermissions()){
//                return;
//            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            latitude = String.valueOf(location.getLatitude());
//            longitude = String.valueOf(location.getLongitude());
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
                imageView.setVisibility(ImageView.VISIBLE);
                add_photo.setVisibility(Button.GONE);
                remove_photo.setVisibility(Button.VISIBLE);
            }
        }
    }
    /**
     * Method to request location permissions from the user
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 44);
    }
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(walletCallback callback) {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());
                        }
                        callback.onCallback();
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
        }
    };
    /**
     * Method to check if location is enabled or not
     * @return
     *  Boolean value based on result
     */
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    /**
     * Callback for the result from requesting permissions.
     * @param requestCode The request code is passed
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *
     *
     */
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(this);
            }
        }
    }
    @Override
    public void onCallback() {
        location_text.setText("Location Added: " + latitude + ", " + longitude);
        location_text.setVisibility(TextView.VISIBLE);
        save_location.setVisibility(Button.GONE);
        remove_location.setVisibility(Button.VISIBLE);
    }
}