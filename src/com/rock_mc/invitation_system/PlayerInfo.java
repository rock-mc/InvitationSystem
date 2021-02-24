package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.json.simple.*;

import java.util.ArrayList;

public class PlayerInfo {
    public String name;
    public String uid;
    public String parentId;
    public ArrayList<String> childId;
    public int invitationQuota;
    public ArrayList<String> invitationCode;

    public PlayerInfo() {
        name = null;
        uid = null;
        parentId = null;
        childId = new ArrayList<>();
        invitationQuota = InvitationSystem.NEW_QUOTA;
        invitationCode = new ArrayList<>();
    }

    public PlayerInfo(Player player) {
        name = player.getName();
        uid = player.getUniqueId().toString();
        parentId = null;
        childId = new ArrayList<>();
        invitationQuota = InvitationSystem.NEW_QUOTA;
        invitationCode = new ArrayList<>();
    }
}
