package com.project.lordofthewings.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.base.Joiner;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.lordofthewings.Controllers.AuthorArrayAdapter;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.Authors.AuthorNamesCallback;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCodePage extends AppCompatActivity implements AuthorNamesCallback {
    String url = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";
    String hash;

    ExpandableListView authorList;
    AuthorArrayAdapter authorArrayAdapter;

    String savedUsername;

    ImageButton deleteButton;


    ImageButton starButton;

    List<String> authors = new ArrayList<>();
    List<String> QRComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_page);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        savedUsername = sh.getString("username", "");
            ImageButton backButton = findViewById(R.id.backIcon);
        backButton.setOnClickListener(v -> {
//            Intent intent = new Intent(QRCodePage.this, WalletPage.class);
//            startActivity(intent);
            //dont uncomment the stuff up there, since the backbutton will go to wherever it was clicked from

            finish();
        });
        ImageView qrCodeLocationImage = findViewById(R.id.qrCodeLocationImage);
        hash = getIntent().getStringExtra("hash");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference qrCodeimageRef = storageRef.child("images/qrcodes/"+hash+".png");

        if (qrCodeimageRef != null) {
            qrCodeimageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(qrCodeLocationImage);
            });
        }
         deleteButton = findViewById(R.id.deleteIcon);
         Log.d("Hash", hash);
         starButton = findViewById(R.id.starIcon);



        FirebaseFirestore db = FirebaseFirestore.getInstance();

        QRCode qr = new QRCode(hash,0);

        ImageView qrcodeImage = findViewById(R.id.qrcode_image);
        Picasso.get().load(url + hash).into(qrcodeImage);

        TextView qrcodeName = findViewById(R.id.qrcode_name);
        TextView qrcodeScore = findViewById(R.id.qrcode_score);
        qrcodeName.setText(qr.getQRName());
        qrcodeScore.setText("Points: " + qr.getQRScore() );
         authorList = findViewById(R.id.authorNames);

        getAuthorNames(this);

        checkIfQRCodeIsOwned(this);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder verifyDelete = new MaterialAlertDialogBuilder(QRCodePage.this, R.style.MaterialAlertDialog_rounded)
                        .setTitle("Delete QRCode")
                        .setMessage("This will delete the QRCode from your wallet. Are you sure you want to continue?")
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteQRCode(hash,db);
                            }
                        });
                verifyDelete.create();
                verifyDelete.show();
            }
        });

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(QRCodePage.this, "You own this QRCode!", Toast.LENGTH_SHORT).show();
            }
        });


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


    }


    public void getAuthorNames(AuthorNamesCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, List<String>> authorNames = new HashMap<>();
        db.collection("QRCodes").document(hash).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    ArrayList<String> authorsDB = (ArrayList<String>) document.get("Authors");
                    for (int i = 0; i < authorsDB.size(); i++) {
                        String author;
                        author = authorsDB.get(i);
                        authors.add(author);
                    }


                    ArrayList<HashMap<String,String>> comments = (ArrayList<HashMap<String,String>>) document.get("Comments");
                    for (int i = 0; i < comments.size(); i++) {
                        String comment;
                        //use joiner on a single comment object key and val
                        comment = Joiner.on(" ").withKeyValueSeparator(": ").join(comments.get(i));
                        QRComments.add(comment);
                    }
                }
                callback.onAuthorNamesReceived();
            }
        });
    }



    public void checkIfQRCodeIsOwned(AuthorNamesCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRCodes").document(hash).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    ArrayList<String> authorsDB = (ArrayList<String>) document.get("Authors");
                    if (authorsDB.contains(savedUsername)) {
                        callback.checkQRCodeOwner();
                    }
                }
            }
        });
    }



    public void deleteQRCode(String hash, FirebaseFirestore db) {
        QRCode qr = new QRCode(hash, 0);
        DocumentReference userRef = db.collection("Users").document(savedUsername);
        final Integer[] Score = new Integer[1];
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<QRCode> qrCodes = (ArrayList<QRCode>) document.get("QRCodes");
                        if (qrCodes != null) {
                            for (int i = 0; i < qrCodes.size(); i++) {
                                Map<String, Object> qrObject = (Map<String, Object>) qrCodes.get(i);
                                QRCode qrCode = new QRCode(qrObject.get("hash").toString(), 1);
                                if (qrCode.getHash().equals(hash)) {
                                    qrCodes.remove(i);
                                    Score[0] = qrCode.getQRScore();
                                    break;
                                }
                            }

                            userRef.update("Score", FieldValue.increment(-Score[0]));
                            userRef.update("QRCodes", qrCodes);
                        }
                    }
                }
            }
        });
        DocumentReference qrRef = db.collection("QRCodes").document(hash);
        qrRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> qrauthors = (ArrayList<String>) document.get("Authors");
                        if (qrauthors != null) {
                            qrauthors.remove(savedUsername);
                            qrRef.update("Authors", qrauthors).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(QRCodePage.this, "QRCode deleted successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(QRCodePage.this, "Error deleting QRCode: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onAuthorNamesReceived() {
        HashMap<String, List<String>> authorNames = new HashMap<>();
        authorNames.put("Scanned By", authors);
        authorNames.put("Comments", QRComments);
        List<String> TitleList = new ArrayList<>(authorNames.keySet());
        Log.d("Title Check", TitleList.toString());
        authorArrayAdapter = new AuthorArrayAdapter(this, TitleList, authorNames);
        authorList.setAdapter(authorArrayAdapter);
    }

    @Override
    public void checkQRCodeOwner() {
        deleteButton.setVisibility(ImageButton.VISIBLE);
        starButton.setVisibility(ImageButton.VISIBLE);
    }

}