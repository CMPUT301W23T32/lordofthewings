package com.project.lordofthewings.Views;

import static android.content.Context.MODE_PRIVATE;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.Player.Player;
import com.project.lordofthewings.R;

public class EditProfileFragment extends DialogFragment {

    FirebaseController fbcontroller = new FirebaseController();
    FirebaseFirestore db = fbcontroller.getDb();
    String firstName1;
    String lastName1;
    String email1;


    public EditProfileFragment(){
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SharedPreferences sh = this.getActivity().getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sh.getString("username", "");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_fragment, null);
        EditText firstName = view.findViewById(R.id.first_name_edit_text);
        EditText lastName = view.findViewById(R.id.last_name_edit_text);
        EditText email = view.findViewById(R.id.email_edit_text);

        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
//                firstName1 = documentSnapshot.getString("firstName");
//                lastName1 = documentSnapshot.getString("lastName");
//                email1 = documentSnapshot.getString("email");
                firstName.setText(documentSnapshot.getString("firstName"));
                lastName.setText(documentSnapshot.getString("lastName"));
                email.setText(documentSnapshot.getString("email"));


            } else {
                Log.d("TAG", "No such document");
            }
        }).addOnFailureListener(e -> Log.d("TAG", "get failed with ", e));

//        firstName.setText(firstName1);
//        lastName.setText(lastName1);
//        email.setText(email1);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    DocumentReference docRef2 = db.collection("Users").document(username);
                    docRef2.update("firstName", firstName.getText().toString(), "lastName", lastName.getText().toString(), "email", email.getText().toString()
                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully updated!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error updating document", e);
                                }
                            });
                    if (getActivity() instanceof ProfilePage) {
                        ((ProfilePage) getActivity()).fetchDataAndRefreshUI();
                        Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }
}
