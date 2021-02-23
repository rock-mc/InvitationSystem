package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerInfo {
    public String name;
    public String uid;
    public String parent_id;
    public ArrayList<String> child_id;
    public int invitation_quota;
    public ArrayList<String> invitation_code;


    public PlayerInfo() {
        name = null;
        uid = null;
        parent_id = null;
        child_id = new ArrayList<>();
        invitation_quota = InvitationSystem.NEW_QUOTA;
        invitation_code = new ArrayList<>();
    }

    public PlayerInfo(Player player) {
        name = player.getName();
        uid = player.getUniqueId().toString();
        parent_id = null;
        child_id = new ArrayList<>();
        invitation_quota = InvitationSystem.NEW_QUOTA;
        invitation_code = new ArrayList<>();
    }

}
