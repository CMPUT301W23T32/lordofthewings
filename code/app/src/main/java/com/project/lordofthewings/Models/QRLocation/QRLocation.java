package com.project.lordofthewings.Models.QRLocation;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.MapsActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Class that models the location of all the scanned QR codes in the game
 *  Also handles all location based operations between the QR code objects and the map
 */
public class QRLocation {
    ArrayList<QRCode> qrCodes;
    QRCodeCallback callback;
    FirebaseController fbController = new FirebaseController();
    FirebaseFirestore db = fbController.getDb();
    ArrayList<QRCode> locatedQrs;

    MapsActivity activity;

    public QRLocation(QRCodeCallback callback, MapsActivity activity){
        Log.d("manan", "2");
        this.qrCodes = new ArrayList<>();
        this.callback = callback;
        this.activity = activity;
        // get QRCodes from Firebase
            db.collection("QRCodes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        LatLng pos = snapshot.get("Location") == null ? null : new LatLng(snapshot.getGeoPoint("Location").getLatitude(), snapshot.getGeoPoint("Location").getLongitude());
                        Map<String, Object> qrObject = (Map<String, Object>) snapshot.get("QRCode");
                        QRCode qrCode = new QRCode(qrObject.get("hash").toString(), pos);
                        qrCodes.add(qrCode);
                    }
                    Log.d("manan", "3");
                    callback.onQrCodesRecieved();
                }
            });

    }


    /**
     * Returns the list of QRCodes that have been found by all users of the game to locate
     * on the map
     * @return list of all known QRCode objects
     */
    public ArrayList<QRCode> getLocatedQRArray(){
        ArrayList<QRCode> locatedQrs = new ArrayList<>();
        for (QRCode code: this.qrCodes) {
            if (code.getLocation() != null){
                locatedQrs.add(code);
            }
        }
        this.locatedQrs = locatedQrs;
        this.db.terminate();
        return locatedQrs;
    }

    private static Bitmap getBitmapFromURL(String src) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy gfgPolicy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
        }
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 10; // adjust this value for the desired corner radius

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private Bitmap makeIconFromBitmap(Bitmap result){
        ImageView mImageView = new ImageView(activity.getApplicationContext());
        IconGenerator mIconGenerator = new IconGenerator(activity.getApplicationContext());
        int size = activity.getResources().getDimensionPixelSize(R.dimen.marker_icon); // adjust this value for the desired size
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(result, size, size, false);
        Bitmap roundedBitmap = getRoundedCornerBitmap(resizedBitmap);
        mImageView.setImageBitmap(roundedBitmap);
        mImageView.setBackground(activity.getApplicationContext().getDrawable(R.drawable.rounded_md));
        mIconGenerator.setContentView(mImageView);
        Bitmap iconBitmap = mIconGenerator.makeIcon();

        return iconBitmap;
    }


    public HashMap<QRCode, Bitmap> locatedQRArraywithBitmap(){
        HashMap<QRCode, Bitmap> locatedQrswithbm = new HashMap<>();
        for (QRCode code: this.locatedQrs) {
            if (code.getLocation() != null){
                String url = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";
                Bitmap result = getBitmapFromURL(url+code.getHash());
                Bitmap iconBitmap = makeIconFromBitmap(result);
                locatedQrswithbm.put(code, iconBitmap);
            }
        }
        return locatedQrswithbm;
    }

    public

}
