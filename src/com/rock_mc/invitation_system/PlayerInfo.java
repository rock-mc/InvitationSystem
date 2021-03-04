package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;

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
        invitationQuota = InvitSys.DEFAULT_INVIT_QUOTA;
        invitationCode = new ArrayList<>();
    }

    public PlayerInfo(Player player, int invitQuota) {
        name = player.getName();
        uid = player.getUniqueId().toString();
        parentId = null;
        childId = new ArrayList<>();
        invitationQuota = invitQuota;
        invitationCode = new ArrayList<>();
    }
}
