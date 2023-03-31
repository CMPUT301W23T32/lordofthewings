package com.project.lordofthewings.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.lordofthewings.Models.Player.Player;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PlayerArrayAdapter extends ArrayAdapter<String> {
    LinearLayout listitem;
    public PlayerArrayAdapter(@NonNull Context context) {
        super(context, 0);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent){
        View view;
        String url = "https://api.dicebear.com/5.x/pixel-art/png?seed=";

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.usercontent,
                    parent, false);
        } else {
            view = convertView;
        }
        SharedPreferences sh = getContext().getSharedPreferences("leaderboard", Context.MODE_PRIVATE);

        ImageView profilePic = view.findViewById(R.id.profile_pic_leaderboard);
        Picasso.get().load(url + getItem(position)).into(profilePic);

        TextView username = view.findViewById(R.id.username_leaderboard);
        username.setText(getItem(position));
        TextView rank = view.findViewById(R.id.position_leaderboard);

        String rankString = sh.getString(getItem(position),"0");
        //checkrank(rankString, view);
        rank.setText("#"+rankString);


        return view;
    }

    private void checkrank(String position, View view) {
        listitem = view.findViewById(R.id.leaderboard_list_item);
        if (position.equals("1")) {
            Drawable background = listitem.getBackground();
            background.setColorFilter(Color.parseColor("#D4AC2B"), PorterDuff.Mode.MULTIPLY );
            listitem.setBackground(background);
        } else if (position.equals("2")) {
            Drawable background = listitem.getBackground();
            background.setColorFilter(Color.parseColor("#C0C0C0"), PorterDuff.Mode.MULTIPLY );
        } else if (position.equals("3")) {
            Drawable background = listitem.getBackground();
            background.setColorFilter(Color.parseColor("#CD7F32"), PorterDuff.Mode.MULTIPLY );
        }
    }
}
