package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.io.IOException;

public class PlayerCommand implements CommandExecutor {
    private void showDefaultCmd(Player player) {

        if (player.isOp()) {
            Log.player(player, "verify | gencode | block | unblock");
        } else {
            if (player != null) {
                PlayerInfo playerInfo = new PlayerInfo(player);
                if (InvitSys.whitelist.contains(playerInfo.uid)) {
                    Log.player(player, "gencode");
                } else {
                    Log.player(player, "verify <invttation code>");
                }
            } else {
                Log.player(player, "verify | gencode | block | unblock");
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        Log.server("recv cmd", command.getName());
        Log.server("recv args", args);
        try {

            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;

                PlayerInfo playerInfo = new PlayerInfo(player, 0);

//            try {

                if (args.length == 0) {
                    showDefaultCmd(player);
                } else if (args[0].equalsIgnoreCase("verify")) {

                    if (InvitSys.whitelist.contains(playerInfo.uid)) {
                        Log.player(player, ChatColor.GREEN + "你已經在白名單中!");
                        return true;
                    }

                    if (args.length != 2) {
                        showDefaultCmd(player);
                        return true;
                    }
                    String invitCode = args[1];
                    Log.player(player, "輸入驗證碼", ChatColor.GREEN, invitCode);

                    if (InvitSys.addWhitelist(player, invitCode)) {
                        Log.player(player, ChatColor.GREEN + "驗證通過!");
                        InvitSys.failList.remove(playerInfo.uid);
                    } else {
                        Log.player(player, ChatColor.RED + "驗證失敗!");

                        InvitSys.failList.add(playerInfo.uid);
                        FailVerifyPlayer failPlayer = InvitSys.failList.getFailVerifyPlayer(playerInfo.uid);
                        failPlayer.failTime += 1;

                        Log.player(player, "您尚有 " + ChatColor.RED + (InvitSys.MAX_RETRY_TIME - failPlayer.failTime) + ChatColor.WHITE + " 次輸入機會");

                        if (failPlayer.failTime >= InvitSys.MAX_RETRY_TIME) {
                            InvitSys.addBlacklist(player, InvitSys.MAX_RETRY_FAIL_BLOCK_DAY, 0, 0, 0);
                            InvitSys.failList.remove(playerInfo.uid);

                            Event event = new InvitKickEvent(false, player, "抱歉，請勿亂猜驗證碼，冷靜個 " + InvitSys.MAX_RETRY_FAIL_BLOCK_DAY + " 天吧");
                            Bukkit.getPluginManager().callEvent(event);
                        }
                    }
                } else if (args[0].equalsIgnoreCase("gencode")) {
                    playerInfo = InvitSys.playerData.findPlayer(playerInfo.uid);

                    if (playerInfo.invitationQuota <= 0 && !player.isOp()) {
                        Log.player(player, ChatColor.RED + "抱歉!你已經沒有邀請配額");
                        return true;
                    }

                    if (player.isOp()) {
                        Log.player(player, "OP 擁有無限邀請配額");
                    }

                    String invitCode = Util.genUUID().substring(0, 6).toUpperCase();

                    while (InvitSys.playerData.isRepeatCode(invitCode)) {
                        invitCode = Util.genUUID().substring(0, 6).toUpperCase();
                    }

                    Log.player(player, "邀請碼 ", ChatColor.GREEN, invitCode);
                    Log.player(player, "請妥善保存");

                    if (!player.isOp()) {
                        playerInfo.invitationQuota -= 1;
                    }
                    playerInfo.invitationCode.add(invitCode);
                    InvitSys.playerData.save();
                }
            } else if (args[0].equalsIgnoreCase("verify")) {
                Log.server("請在遊戲中下指令");
            } else if (args[0].equalsIgnoreCase("gencode")) {
                Log.server("請在遊戲中下指令");
            }

            if (args[0].equalsIgnoreCase("info")) {
                if (player != null && !player.isOp()) {
                    Log.player(player, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }

                if (args.length < 2) {
                    showDefaultCmd(player);
                    return true;
                }

                String playerName = args[1];
                Log.player(player, "查詢使用者", ChatColor.GREEN, playerName);

                Player unblockPlayer = Bukkit.getPlayer(playerName);
                if (unblockPlayer == null) {
                    Log.player(player, "查無此玩家", playerName);
                    return true;
                }
                PlayerInfo playerInfo = new PlayerInfo(unblockPlayer);

                Log.player(player, "============================");
                Log.player(player, "玩家名稱", playerInfo.name);

                if (unblockPlayer.isOp()){
                    Log.player(player, "驗證狀態", ChatColor.GOLD, "Operator");
                    Log.player(player, "邀請配額", "∞");
                }
                else if (InvitSys.whitelist.contains(playerInfo.uid)){
                    Log.player(player, "驗證狀態", ChatColor.GREEN, "通過驗證");
                    Log.player(player, "邀請配額", playerInfo.invitationQuota + "");
                }
                else if (InvitSys.blacklist.contains(playerInfo.uid)){
                    Log.player(player, "驗證狀態", ChatColor.RED, "隔離中");

                    Prisoner p = InvitSys.blacklist.getPrisoner(playerInfo.uid);

                    long expiryTime = p.basicTime + p.expiryTime;
                    expiryTime -= java.time.Instant.now().getEpochSecond();

                    long day = expiryTime / Util.DAY;
                    expiryTime %= Util.DAY;
                    long hour = expiryTime / Util.HOUR;
                    expiryTime %= Util.HOUR;
                    long min = expiryTime / Util.MIN;
                    expiryTime %= Util.MIN;
                    long sec = expiryTime;

                    String prisonTime = Util.timeToStr(day, hour, min, sec);

                    Log.player(player, "刑期尚有", ChatColor.RED, prisonTime);
                    Log.player(player, "邀請配額", playerInfo.invitationQuota + "");
                }

                PlayerInfo parent = InvitSys.playerData.findPlayer(playerInfo.parentId);
                Log.player(player, "推薦人", parent.name);

                String kidStr = null;
                for(String playerUid : playerInfo.childId){
                    PlayerInfo childInfo = InvitSys.playerData.findPlayer(playerInfo.parentId);
                    if (kidStr == null){
                        kidStr = childInfo.name;
                    }
                    else {
                        kidStr += " " + childInfo.name;
                    }
                }
                Log.player(player, "推薦玩家: " + kidStr);

            } else if (args[0].equalsIgnoreCase("unblock")) {
                if (player != null && !player.isOp()) {
                    Log.player(player, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }

                if (args.length < 2) {
                    showDefaultCmd(player);
                    return true;
                }

                String unblockPlayerName = args[1];
                Log.player(player, "將使用者移出黑名單", ChatColor.GREEN, unblockPlayerName);

                Player unblockPlayer = Bukkit.getPlayer(unblockPlayerName);
                if (unblockPlayer == null) {
                    Log.player(player, "查無此玩家", unblockPlayerName);
                    return true;
                }

                PlayerInfo playerInfo = new PlayerInfo(unblockPlayer);
                InvitSys.blacklist.remove(playerInfo.uid);

                Log.player(player, "執行狀態", ChatColor.GREEN, "完成");
            }
            else if (args[0].equalsIgnoreCase("block")) {

                if (player != null && !player.isOp()) {
                    Log.player(player, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }

                if (args.length < 2) {
                    showDefaultCmd(player);
                    return true;
                }

                String blockPlayerName = args[1];
                Log.player(player, "將使用者加入黑名單", ChatColor.RED, blockPlayerName);

                int blockDay = 0;
                if (args.length >= 3) {
                    blockDay = Integer.parseInt(args[2]);
                }
                int blockHour = 0;
                if (args.length >= 4) {
                    blockHour = Integer.parseInt(args[3]);
                }
                int blockMin = 0;
                if (args.length >= 5) {
                    blockMin = Integer.parseInt(args[4]);
                }
                int blockSec = 0;
                if (args.length >= 6) {
                    blockSec = Integer.parseInt(args[5]);
                }

                Player blockPlayer = Bukkit.getPlayer(blockPlayerName);
                if (blockPlayer == null) {
                    Log.player(player, "查無此玩家", blockPlayerName);
                    return true;
                }
                PlayerInfo playerInfo = new PlayerInfo(blockPlayer);

                InvitSys.addBlacklist(blockPlayer, blockDay, blockHour, blockMin, blockSec);
                InvitSys.failList.remove(playerInfo.uid);

                if (blockPlayer.isOnline()) {
                    String blockMsg;
                    if (blockDay == 0 && blockHour == 0 && blockMin == 0 && blockSec == 0) {
                        blockMsg = "抱歉，你已經被永久加入黑名單";
                    } else {
                        blockMsg = "抱歉，你已經被加入黑名單，刑期 ";
                        blockMsg += Util.timeToStr(blockDay, blockHour, blockMin, blockSec);
                    }

                    Event event = new InvitKickEvent(false, blockPlayer, blockMsg);
                    Bukkit.getPluginManager().callEvent(event);
                }
                Log.player(player, "執行狀態", ChatColor.GREEN, "完成");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
