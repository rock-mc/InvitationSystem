package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerInfo {
    public String name;
    public UUID uuid;
    public UUID parentId;
    public ArrayList<UUID> childId;
    public int invitationQuota;
    public ArrayList<String> invitationCode;

    public PlayerInfo() {
        name = null;
        uuid = null;
        parentId = null;
        childId = new ArrayList<>();
        invitationQuota = InvitSys.DEFAULT_INVIT_QUOTA;
        invitationCode = new ArrayList<>();
    }

    public PlayerInfo(Player player) {
        name = player.getName();
        uuid = player.getUniqueId();
        parentId = null;
        childId = new ArrayList<>();
        invitationQuota = InvitSys.DEFAULT_INVIT_QUOTA;
        invitationCode = new ArrayList<>();
    }

    public PlayerInfo(Player player, int invitQuota) {
        name = player.getName();
        uuid = player.getUniqueId();
        parentId = null;
        childId = new ArrayList<>();
        invitationQuota = invitQuota;
        invitationCode = new ArrayList<>();
    }

    public void resetCode(){
        invitationCode = new ArrayList<>();
    }
}
