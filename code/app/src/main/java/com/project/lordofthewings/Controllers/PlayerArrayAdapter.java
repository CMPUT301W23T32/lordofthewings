package com.project.lordofthewings.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
/*
    * Custom Array Adapter for a list of Player objects to be displayed in a ListView on the Leaderboard page.
    * Known Bugs: None
 */
public class PlayerArrayAdapter extends ArrayAdapter<String> {
    LinearLayout listitem;
    /**
        * Constructor for the PlayerArrayAdapter
        * @param context The context of the activity that is using the adapter
     */
    public PlayerArrayAdapter(@NonNull Context context) {
        super(context, 0);
    }
    /**
        * Method that returns the view of the list item
        * @param position The position of the item in the list
        * @param convertView The view of the list item
        * @param parent The parent view group
        * @return The view of the list item
     */
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
        rank.setText("#"+rankString);

        return view;
    }

}
