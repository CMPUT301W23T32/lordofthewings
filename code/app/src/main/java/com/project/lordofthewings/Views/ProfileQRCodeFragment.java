package com.project.lordofthewings.Views;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

public class ProfileQRCodeFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.profile_qrcode_scan, null);
        SharedPreferences sh = this.getActivity().getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sh.getString("username", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ImageView qrCode = view.findViewById(R.id.qr_code_scan_profile);
        String url ="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=";
        Picasso.get().load(url + username ).into(qrCode);
        return new MaterialAlertDialogBuilder(requireActivity(),R.style.MaterialAlertDialog_rounded)
                .setView(view)
                .setTitle("Share your Profile")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                }).create();

    }
}
