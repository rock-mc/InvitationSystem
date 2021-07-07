package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

public class InvitSys {

    public static final String APP_NAME = "InvitSys";
    public static final int DEFAULT_INVIT_QUOTA = 2;
    public static final int MAX_INPUT_CODE_TIME = 60;
    public static final int INVIT_CODE_LENGTH = 6;
    public static final int MAX_RETRY_TIME = 5;
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

    public static boolean addBlacklist(Player player, OfflinePlayer blockPlayer, int day, int hour, int min, int sec) throws IOException {
        PlayerInfo playerInfo = playerData.getPlayer(blockPlayer.getUniqueId());

        // 不從白名單中剔除，保留驗證結果

        // 將邀請碼清空
        if(playerInfo.resetCode()){
            // 如果有變動，儲存
            playerData.save();
        }

        blacklist.add(playerInfo.uuid, day, hour, min, sec);
        return true;
    }

    public static boolean addWhitelist(Player player, OfflinePlayer addPlayer, int invitQuota) throws IOException {
        PlayerInfo playerInfo = playerData.getPlayer(addPlayer.getUniqueId());

        // 從黑名單中移除
        if (blacklist.remove(playerInfo.uuid)){
            Log.player(player, "將 " + ChatColor.YELLOW + playerInfo.name + ChatColor.WHITE + " 從黑名單中移除");
        }

        // 如果玩家的邀請額度就等於新的額度就不用動
        if (playerInfo.invitationQuota != invitQuota){
            playerInfo.invitationQuota = invitQuota;
            playerData.save();
        }

        whitelist.add(playerInfo.uuid);
        return true;
    }

    public static boolean addWhitelist(Player player, OfflinePlayer addPlayer, String invitCode) throws IOException {

        // 找到是誰邀請 addPlayer
        PlayerInfo parent = null;
        for(UUID uuid : whitelist.playerList){
            PlayerInfo p = playerData.getPlayer(uuid);
            if(p.invitationCode.contains(invitCode)){
                parent = p;
                break;
            }
        }
        // 找不到，表示沒有相符邀請碼
        if(parent == null){
            Log.player(player, ChatColor.RED + "查無此邀請碼");
            return false;
        }

        // 取得玩家資料
        PlayerInfo newPlayer = playerData.getPlayer(addPlayer.getUniqueId());
        // 設定玩家的推薦人與推薦人的推薦玩家
        parent.childId.add(newPlayer.uuid);
        newPlayer.parentId = parent.uuid;
        // 將此邀請碼清除
        parent.invitationCode.remove(invitCode);
        // 設定新玩家邀請額度
        newPlayer.invitationQuota = InvitSys.DEFAULT_INVIT_QUOTA;
        // 儲存資料
        playerData.save();

        // 加入至白名單
        whitelist.add(newPlayer.uuid);

        return true;
    }
    public static String genInvitCode(){

        String result;
        do {
            result = Util.genUUID().substring(0, INVIT_CODE_LENGTH).toUpperCase();
        } while (InvitSys.playerData.isRepeatCode(result));

        return result;
    }
}
