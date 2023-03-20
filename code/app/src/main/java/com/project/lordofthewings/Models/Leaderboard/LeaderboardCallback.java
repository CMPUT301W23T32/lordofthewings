package com.project.lordofthewings.Models.Leaderboard;

import com.project.lordofthewings.Models.Player.Player;

import java.util.ArrayList;

public interface LeaderboardCallback {
    void myResponseCallback(ArrayList<Player> player);
}
