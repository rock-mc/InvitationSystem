package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class InvitSys {

    public static final String APP_NAME = "InvitSys";
    public static final int DEFAULT_INVIT_QUOTA = 2;
    public static final int MAX_INPUT_CODE_TIME = 30;
    public static final int INVIT_CODE_LENGTH = 6;
    public static final int MAX_RETRY_TIME = 3;
    public static final int MAX_RETRY_FAIL_BLOCK_DAY = 3;


    public static Plugin plugin;

    public static PlayerData playerData;
    public static Whitelist whitelist;
    public static Blacklist blacklist;
    public static FailList failList;

    public static boolean enable;

    public static void init(Plugin inputPlugin){

        plugin = inputPlugin;

        Util.mkdir("plugins/" + APP_NAME);

        try {
            playerData = new PlayerData("plugins/" + APP_NAME + "/playerdata.json");
            whitelist = new Whitelist("plugins/" + APP_NAME + "/whitelist.json");
            blacklist = new Blacklist("plugins/" + APP_NAME + "/blacklist.json");
            failList = new FailList("plugins/" + APP_NAME + "/faillist.json");
        } catch (Exception e) {
            return;
        }

        enable = true;
    }

    public static boolean addBlacklist(Player player, int day) throws IOException {
        PlayerInfo currentPlayer = new PlayerInfo(player, 0);

        if(whitelist.contains(currentPlayer.uid)){
            whitelist.remove(currentPlayer.uid);
        }

        playerData.add(currentPlayer);
        currentPlayer = playerData.findPlayer(currentPlayer.uid);
        currentPlayer.resetCode();
        playerData.save();

        blacklist.add(currentPlayer.uid, day);
        Log.player(player, "將 " + ChatColor.YELLOW + currentPlayer.name + ChatColor.WHITE + " 加入至黑名單");
        return true;
    }

    public static boolean addWhitelist(Player player, int invitQuota) throws IOException {
        PlayerInfo currentPlayer = new PlayerInfo(player, invitQuota);

        if(blacklist.contains(currentPlayer.uid)){
            Log.player(player, "將 " + ChatColor.YELLOW + currentPlayer.name + ChatColor.WHITE + " 從黑名單中移除");
            blacklist.remove(currentPlayer.uid);
        }

        playerData.add(currentPlayer);
        whitelist.add(currentPlayer.uid);
        return true;
    }

    public static boolean addWhitelist(Player player, String invitCode) throws IOException {

        PlayerInfo parent = null;
        for(PlayerInfo p : playerData.playerList){
            if(p.invitationCode.contains(invitCode)){
                parent = p;
                break;
            }
        }
        if(parent == null){
            Log.player(player, "查無此邀請碼", ChatColor.RED, invitCode);
            return false;
        }
        PlayerInfo newPlayer = new PlayerInfo(player, InvitSys.DEFAULT_INVIT_QUOTA);
        parent.childId.add(newPlayer.uid);
        newPlayer.parentId = parent.uid;

        playerData.add(newPlayer);
        whitelist.add(newPlayer.uid);

        return true;
    }
}
