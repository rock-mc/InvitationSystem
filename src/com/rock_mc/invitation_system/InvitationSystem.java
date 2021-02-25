package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;

import java.io.IOException;

public class InvitationSystem {

    public static final String APP_NAME = "InvitationSystem";
    public static final int NEW_QUOTA = 2;

    public static PlayerData playerData;
    public static Whitelist whitelist;
    public static Blacklist blacklist;

    public static void init(){
        Util.mkdir("plugins/" + APP_NAME);

        try {
            playerData = new PlayerData("plugins/" + APP_NAME + "/playerdata.json");
            whitelist = new Whitelist("plugins/" + APP_NAME + "/whitelist.json");
            blacklist = new Blacklist("plugins/" + APP_NAME + "/blacklist.json");
        } catch (Exception e) {
            return;
        }
    }

    public static boolean addWhitelist(Player player) throws IOException {
        PlayerInfo tempPlayer = new PlayerInfo(player);
        playerData.add(tempPlayer);
        whitelist.add(tempPlayer.uid);
        return true;
    }

    public static boolean addWhitelist(Player player, String invitationCode) throws IOException {

        PlayerInfo parent = null;
        for(PlayerInfo p : playerData.playerList){
            if(p.invitationCode.contains(invitationCode)){
                parent = p;
                break;
            }
        }
        if(parent == null){
            return false;
        }
        PlayerInfo newPlayer = new PlayerInfo(player);
        parent.childId.add(newPlayer.uid);
        newPlayer.parentId = parent.uid;

        playerData.add(newPlayer);
        whitelist.add(newPlayer.uid);

        return true;
    }
}
