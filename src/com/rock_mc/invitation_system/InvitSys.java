package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

public class InvitSys {

    public static final String APP_NAME = "InvitSys";
    public static final int DEFAULT_INVIT_QUOTA = 2;
    public static final int MAX_INPUT_CODE_TIME = 30;
    public static final int INVIT_CODE_LENGTH = 6;
    public static final int MAX_RETRY_TIME = 3;
    public static final int MAX_RETRY_FAIL_BLOCK_DAY = 1;


    public static Plugin plugin;

    public static PlayerData playerData;
    public static Whitelist whitelist;
    public static Blacklist blacklist;
    public static FailList failList;
    public static HashSet<UUID> freezePlayerSet;

    public static boolean enable;

    public static void init(Plugin inputPlugin){

        plugin = inputPlugin;

        Util.mkdir("plugins/" + APP_NAME);

        try {
            playerData = new PlayerData("plugins/" + APP_NAME + "/playerdata.json");
            Log.server("Load player data", "Complete");
            whitelist = new Whitelist("plugins/" + APP_NAME + "/whitelist.json");
            Log.server("Load whitelist", "Complete");
            blacklist = new Blacklist("plugins/" + APP_NAME + "/blacklist.json");
            Log.server("Load blacklist", "Complete");
            failList = new FailList("plugins/" + APP_NAME + "/faillist.json");
            Log.server("Load faillist", "Complete");
        } catch (Exception e) {
            Log.server(ChatColor.RED + "ERROR!! Load file fails");
            return;
        }
        freezePlayerSet = new HashSet<>();

        enable = true;
    }

    public static boolean addBlacklist(Player player, int day, int hour, int min, int sec) throws IOException {
        PlayerInfo currentPlayer = new PlayerInfo(player, 0);

        // 從白名單中移除
        if(whitelist.contains(currentPlayer.uuid)){
            whitelist.remove(currentPlayer.uuid);
        }

        // 設定使用者資料
        playerData.add(currentPlayer);
        // 取得使用者資料
        currentPlayer = playerData.findPlayer(currentPlayer.uuid);
        // 將邀請碼清空
        currentPlayer.resetCode();
        // 儲存
        playerData.save();

        blacklist.add(currentPlayer.uuid, day, hour, min, sec);
        Log.player(player, "將 " + ChatColor.YELLOW + currentPlayer.name + ChatColor.WHITE + " 加入至黑名單");
        return true;
    }

    public static boolean addWhitelist(Player player, int invitQuota) throws IOException {
        PlayerInfo currentPlayer = new PlayerInfo(player, invitQuota);

        // 從黑名單中移除
        if(blacklist.contains(currentPlayer.uuid)){
            Log.player(player, "將 " + ChatColor.YELLOW + currentPlayer.name + ChatColor.WHITE + " 從黑名單中移除");
            blacklist.remove(currentPlayer.uuid);
        }

        // 設定使用者資料
        playerData.add(currentPlayer);
        whitelist.add(currentPlayer.uuid);
        return true;
    }

    public static boolean addWhitelist(Player player, String invitCode) throws IOException {

        PlayerInfo parent = null;
        for(UUID uuid : whitelist.playerList){
            PlayerInfo p = playerData.findPlayer(uuid);
            if(p.invitationCode.contains(invitCode)){
                parent = p;
                break;
            }
        }
        if(parent == null){
            Log.player(player, ChatColor.RED + "查無此邀請碼");
            return false;
        }
        PlayerInfo newPlayer = new PlayerInfo(player, InvitSys.DEFAULT_INVIT_QUOTA);
        parent.childId.add(newPlayer.uuid);
        newPlayer.parentId = parent.uuid;

        playerData.add(newPlayer);
        whitelist.add(newPlayer.uuid);

        return true;
    }
}
