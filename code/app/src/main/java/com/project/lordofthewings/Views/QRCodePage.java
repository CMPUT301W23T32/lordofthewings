package com.project.lordofthewings.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Controllers.AuthorArrayAdapter;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCodePage extends AppCompatActivity {
    String url = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";
    String hash;

    ExpandableListView authorList;
    AuthorArrayAdapter authorArrayAdapter;

    List<String> TitleList;

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
         authorList = findViewById(R.id.authorNames);
        //creating dummy data to test authors
        List<String> authors = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            authors.add("author" + i);
        }
        HashMap<String, List<String>> authorNames = new HashMap<>();
        authorNames.put("Scanned By", authors);

        TitleList = new ArrayList<>(authorNames.keySet());

        authorArrayAdapter = new AuthorArrayAdapter(this, TitleList, authorNames);
        authorList.setAdapter(authorArrayAdapter);

        authorList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //nice
            }
        });

        // This method is called when the group is collapsed
        authorList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //nice
            }
        });


        Log.d("AUTHORS", authorNames.toString());

    }

    //potential ways to get the author names listed below, will check tomorrow hella sleepy rn

//    public Map<String, List<String>> getAuthorNames(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, List<String>> authorNames = new HashMap<>();
//        db.collection("QRCodes").document(hash).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    Map<String, Object> data = document.getData();
//                    List<String> authors = (List<String>) data.get("Authors");
//                    authorNames.put("Scanned By", authors);
//                }
//            }
//        });
//        return authorNames;
//    }
//
//    public interface AuthorNamesCallback {
//        void onAuthorNamesReceived(Map<String, List<String>> authorNames);
//    }
//
//    public void getAuthorNames(AuthorNamesCallback callback) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("QRCodes").document(hash).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    Map<String, Object> data = document.getData();
//                    List<String> authors = (List<String>) data.get("Authors");
//                    Map<String, List<String>> authorNames = new HashMap<>();
//                    authorNames.put("Scanned By", authors);
//                    callback.onAuthorNamesReceived(authorNames);
//                }
//            }
//        });
//    }

    //usage:

//    getAuthorNames(new AuthorNamesCallback() {
//        @Override
//        public void onAuthorNamesReceived(Map<String, List<String>> authorNames) {
//            // handle authorNames object
//        }
//    });


}
