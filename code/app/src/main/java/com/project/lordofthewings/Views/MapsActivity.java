package com.project.lordofthewings.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.maps.android.ui.IconGenerator;
import com.project.lordofthewings.Controllers.MapsArrayAdapter;
import com.project.lordofthewings.Controllers.QRCodeArrayAdapter;
import com.project.lordofthewings.Models.QRLocation.QRCodeCallback;
import com.project.lordofthewings.Models.QRLocation.QRLocation;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.project.lordofthewings.databinding.ActivityMapsBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An activity that implements the map functionality of the app to display the QRCodes.
 *
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, QRCodeCallback{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;

    private QRLocation qrLocation = new QRLocation(this, this);
    private ArrayList<QRCode> locatedCodes;
    private EditText search_bar;
    private BottomSheetBehavior bottomSheetBehavior;
    private ArrayList<HashMap<QRCode, Float>> sortedQrCodes;
    private ArrayAdapter<HashMap<QRCode, Float>> mapArrayAdapter;
    private ListView qrListView;
    private TextView qrListTitle;
    private ImageButton listback;
    private ProgressBar progressBar;
    private ChipGroup chipGroup;
    private Chip chip_20, chip_all, chip_100, chip_60, chip_40;
    private TextView noQrCodes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.pulloutList));
        bottomSheetBehavior.setPeekHeight(150);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        search_bar = findViewById(R.id.search_text);
        qrListView = findViewById(R.id.qrListForMap);
        qrListTitle = findViewById(R.id.qrListTitle);
        listback = findViewById(R.id.map_list_back_button);
        progressBar = findViewById(R.id.progress_bar_map);
        chipGroup = findViewById(R.id.chip_group);
        chip_20 = findViewById(R.id.chip_1);
        chip_40 = findViewById(R.id.chip_2);
        chip_60 = findViewById(R.id.chip_3);
        chip_100 = findViewById(R.id.chip_4);
        chip_all = findViewById(R.id.chip_5);
        noQrCodes = findViewById(R.id.noQrCodeFound);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ImageButton back_button = findViewById(R.id.back_button_map);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, HomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("manan", "6");
        // checks if permission is granted or not
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            onQrCodesRecieved();
            mainMap();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); // requests permission if not granted jump to onRequestPermissionsResult
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); // requests permission if not granted jump to onRequestPermissionsResult
            }
        }
    }

    /**
     * This method is used to enable the user's location
     */
    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    /**
     * This method is used to zoom into the user location
     */
    private void snapToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null);
        locationTask.addOnSuccessListener(location -> {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        });
    }

    /**
     * uses GeoCoder to search for a location
     */
    private void search() {
        String location_text = search_bar.getText().toString();
        List<Address> addressList = new ArrayList<>();

        if (location_text != null || !location_text.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location_text, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList.size() == 0) {
                Toast.makeText(this, "No location found", Toast.LENGTH_SHORT).show();
            } else {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                search_bar.setText("");
                Location addressLocation = new Location("");
                addressLocation.setLatitude(address.getLatitude());
                addressLocation.setLongitude(address.getLongitude());
                String addressName = address.getLocality();
                if (addressName == null) {
                    addressName = address.getFeatureName();
                }
                cardUpdate(addressLocation, addressName);
            }
        }

    }

    /**
     * main function for maps, is the main body of the map which is called after the permission is granted
     */
    private void mainMap() {
        enableUserLocation();
        snapToUserLocation();

        // set the bottomsheet to collapsed when anything other than the bottomsheet is clicked
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // set the bottomsheet to collapsed when the map is clicked or swiped on without onMapClick
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        search_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                return false;
            }
        });

        search_bar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
                return true;
            } else {
                return false;
            }
        });

        listback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultCard();
                listback.setVisibility(View.GONE);
                snapToUserLocation();
            }
        });

        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                if (checkedIds.get(0) == R.id.chip_1) {
                    listFilter( 20000);
                } else if (checkedIds.get(0) == R.id.chip_2) {
                    listFilter(40000);
                } else if (checkedIds.get(0) == R.id.chip_3) {
                    listFilter(60000);
                } else if (checkedIds.get(0) == R.id.chip_4) {
                    listFilter(100000);
                } else if (checkedIds.get(0) == R.id.chip_5) {
                    listFilter(Integer.MAX_VALUE);
                }
            }
        });

        qrListView.setOnItemClickListener((parent, view, position, id) -> {
            QRCode qrCode = (QRCode) (mapArrayAdapter.getItem(position)).keySet().toArray()[0];
            Intent intent = new Intent(MapsActivity.this, QRCodePage.class);
            intent.putExtra("hash", qrCode.getHash());
            startActivity(intent);
        });

        mMap.setOnMarkerClickListener((marker) -> {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
            marker.showInfoWindow();
            return true;
        });


    }

    /**
     * function for when the location permissions are not granted
     * @param requestCode The request code passed in {@link #requestPermissions(
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // requests permission
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("manan", "7");
                // permission was granted snap to user location
                onQrCodesRecieved();
                mainMap();
            } else {
                // permission was denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * Loads the overall list (has multiple load options for filtering)
     */
    @SuppressLint("MissingPermission")
    private void listLoader(){

        defaultCard();

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                defaultCard();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
    }

    /**
     * default card which shows the qr codes near the User's Location
     */
    private void defaultCard(){
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null);
        locationTask.addOnSuccessListener(curlocation -> {
            cardUpdate(curlocation, "me");
        });
    }

    /**
     * updates the card with the qr codes near the provided location
     * @param location
     * @param place
     */
    private void cardUpdate(Location location, String place){


        if (!place.equals("me")){
            listback.setVisibility(View.VISIBLE);
        }
        qrListTitle.setText("QRCodes near "+place);
        chipGroup.setVisibility(View.VISIBLE);
        ArrayList<HashMap<QRCode, Float>> sortedList = qrLocation.sortLocatedQRArray(location);
        sortedQrCodes = sortedList;
        mapArrayAdapter = new MapsArrayAdapter(MapsActivity.this);
        qrListView.setAdapter(mapArrayAdapter);
        mapArrayAdapter.clear();
        for (HashMap<QRCode, Float> code: sortedQrCodes) {
                mapArrayAdapter.add(code);

        }
        mapArrayAdapter.notifyDataSetChanged();

        if (chip_20.isChecked()){
            listFilter(20000);
        }
        else if (chip_40.isChecked()){
            listFilter(40000);
        }
        else if (chip_60.isChecked()){
            listFilter(60000);
        }
        else if (chip_100.isChecked()){
            listFilter(100000);
        }
        else if (chip_all.isChecked()){
            listFilter(Integer.MAX_VALUE);
        }

    }

    /**
     * filters the list view based on selected distance chip
     * @param filterCode
     */
    private void listFilter(Integer filterCode){
        noQrCodes.setVisibility(View.GONE);
        qrListView.setVisibility(View.VISIBLE);
        mapArrayAdapter.clear();
        for (HashMap<QRCode, Float> code: sortedQrCodes) {
            if (code.get(code.keySet().toArray()[0]) < filterCode) {
                mapArrayAdapter.add(code);
            }

        }
        mapArrayAdapter.notifyDataSetChanged();

        if (mapArrayAdapter.getCount() == 0){
            noQrCodes.setVisibility(View.VISIBLE);
            qrListView.setVisibility(View.GONE);
        }

    }

    /**
     * callback function for when the QRCodes are fetched from firebase
     */
    @Override
    public void onQrCodesRecieved() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

                Log.d("manan", "4");
                locatedCodes = qrLocation.getLocatedQRArray();
                Log.d("manan", "locatedCodes: " + locatedCodes.size());
                HashMap<QRCode, Bitmap> locatedCodeswithbmap = qrLocation.locatedQRArraywithBitmap();
                Log.d("manan", "tick?");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mMap != null && locatedCodes != null && !locatedCodes.isEmpty()) {
                            Log.d("manan", "5");
                            mMap.clear();
                            for (int i = 0; i < locatedCodes.size(); i++) {
                                QRCode qrCode = locatedCodes.get(i);
                                Bitmap iconBitmap = locatedCodeswithbmap.get(locatedCodes.get(i));
                                mMap.addMarker(new MarkerOptions()
                                        .position(qrCode.getLocation())
                                        .title(qrCode.getQRName())
                                        .snippet(String.valueOf(qrCode.getQRScore()) + " points")
                                        .icon(BitmapDescriptorFactory.fromBitmap(iconBitmap)));
                            }
                        }
                        listLoader();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * array Adapter getter method
     * @return
     */
    public ArrayAdapter<HashMap<QRCode, Float>> getList() {
        return this.mapArrayAdapter;

    }

}
