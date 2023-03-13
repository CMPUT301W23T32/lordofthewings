package com.project.lordofthewings.Views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Controllers.FirebaseController;
//import com.project.lordofthewings.Models.Leaderboard.Leaderboard;
import com.project.lordofthewings.Models.Player.Player;
import com.project.lordofthewings.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardPage extends AppCompatActivity {
    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_page);
        cityList = findViewById(R.id.list);
        dataList = new ArrayList<>();
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);
        FirebaseController fbController = new FirebaseController();
        FirebaseFirestore db = fbController.getDb();

        db
                .collection("Users")
                .orderBy("Score", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "on success: we getting data");
                        List<DocumentSnapshot> users = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : users) {
                            String playerToBeInitialized = snapshot.getString("username");

                            dataList.add(playerToBeInitialized);
                            cityAdapter.notifyDataSetChanged();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                        dataList.add("lol");
                        cityAdapter.notifyDataSetChanged();

                    }
                });


















    }
}