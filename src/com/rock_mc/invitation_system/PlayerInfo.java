package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerInfo {
    public String name;
    public String uid;
    public PlayerInfo parent;
    public ArrayList<PlayerInfo> child;
    public int invitation_quota;

    public PlayerInfo(Player player) {
        name = player.getName();
        uid = player.getUniqueId().toString();
        parent = null;
        child = null;
        invitation_quota = 0;
    }
}
