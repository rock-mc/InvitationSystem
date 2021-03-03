package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class InvitationSystem {

    public static final String APP_NAME = "InvitSys";
    public static final int NEW_QUOTA = 2;
    public static final int MAX_INPUT_CODE_TIME = 15;

    public static Plugin plugin;

    public static PlayerData playerData;
    public static Whitelist whitelist;
    public static Blacklist blacklist;

    public static void init(Plugin inputPlugin){

        plugin = inputPlugin;

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
        PlayerInfo currentPlayer = new PlayerInfo(player);

        if(blacklist.contains(currentPlayer.uid)){
            blacklist.remove(currentPlayer.uid);
        }

        playerData.add(currentPlayer);
        whitelist.add(currentPlayer.uid);
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

        Log.player(player, "成功新增使用者至白名單", ChatColor.GREEN, newPlayer.name);
        return true;
    }
}
