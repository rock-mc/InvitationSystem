package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;

import java.io.IOException;

public class InvitationSystem {

    public static final String APP_NAME = "InvitationSystem";
    public static final int NEW_QUOTA = 2;

    public static PlayerChain playerChain;
    public static PlayerList whitelist;
    public static PlayerList blacklist;

    public static void init(){
        Util.mkdir("plugins/" + APP_NAME);

        try {
            playerChain = new PlayerChain("plugins/" + APP_NAME + "/playerchain.json");
            whitelist = new PlayerList("plugins/" + APP_NAME + "/whitelist.json");
            blacklist = new PlayerList("plugins/" + APP_NAME + "/blacklist.json");
        } catch (Exception e) {
            return;
        }
    }

    public static boolean addWhitelist(Player player) throws IOException {
        PlayerInfo playerInfo = new PlayerInfo(player);
        playerChain.add(playerInfo);
        whitelist.add(playerInfo.uid);
        return true;
    }

    public static boolean addWhitelist(Player player, String invitationCode) throws IOException {

        PlayerInfo parent = null;
        for(PlayerInfo p : playerChain.playerInfos){
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

        playerChain.add(newPlayer);
        whitelist.add(newPlayer.uid);

        return true;
    }
}
