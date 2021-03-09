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
            Log.server("Load player data", ChatColor.GREEN,"Complete");
            whitelist = new Whitelist("plugins/" + APP_NAME + "/whitelist.json");
            Log.server("Load whitelist", ChatColor.GREEN, "Complete");
            blacklist = new Blacklist("plugins/" + APP_NAME + "/blacklist.json");
            Log.server("Load blacklist", ChatColor.GREEN, "Complete");
            failList = new FailList("plugins/" + APP_NAME + "/faillist.json");
            Log.server("Load faillist", ChatColor.GREEN, "Complete");
        } catch (Exception e) {
            Log.server(ChatColor.RED + "ERROR!! Load file fails");
            return;
        }
        freezePlayerSet = new HashSet<>();

        enable = true;
    }
    public static boolean createPlayerData(Player player) throws IOException {
        // 建立玩家資料
        PlayerInfo playerInfo = new PlayerInfo(player);
        playerData.add(playerInfo);
        return true;
    }

    public static boolean addBlacklist(Player player, int day, int hour, int min, int sec) throws IOException {
        PlayerInfo playerInfo = playerData.getPlayer(player.getUniqueId());

        // 從白名單中移除
        if(whitelist.contains(playerInfo.uuid)){
            whitelist.remove(playerInfo.uuid);
        }

        // 將邀請碼清空
        if(playerInfo.resetCode()){
            // 如果有變動，儲存
            playerData.save();
        }

        blacklist.add(playerInfo.uuid, day, hour, min, sec);
        Log.player(player, "將 " + ChatColor.YELLOW + playerInfo.name + ChatColor.WHITE + " 加入至黑名單");
        return true;
    }

    public static boolean addWhitelist(Player player, int invitQuota) throws IOException {
        PlayerInfo playerInfo = playerData.getPlayer(player.getUniqueId());

        // 從黑名單中移除
        if (blacklist.remove(playerInfo.uuid)){
            Log.player(player, "將 " + ChatColor.YELLOW + playerInfo.name + ChatColor.WHITE + " 從黑名單中移除");
        }

        whitelist.add(playerInfo.uuid);
        return true;
    }

    public static boolean addWhitelist(Player player, String invitCode) throws IOException {

        PlayerInfo parent = null;
        for(UUID uuid : whitelist.playerList){
            PlayerInfo p = playerData.getPlayer(uuid);
            if(p.invitationCode.contains(invitCode)){
                parent = p;
                break;
            }
        }
        if(parent == null){
            Log.player(player, ChatColor.RED + "查無此邀請碼");
            return false;
        }

        PlayerInfo newPlayer = playerData.getPlayer(player.getUniqueId());
        parent.childId.add(newPlayer.uuid);
        parent.invitationCode.remove(invitCode);
        newPlayer.parentId = parent.uuid;
        newPlayer.invitationQuota = InvitSys.DEFAULT_INVIT_QUOTA;
        playerData.save();

        whitelist.add(newPlayer.uuid);

        return true;
    }
}
