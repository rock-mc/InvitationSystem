package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.io.IOException;
import java.util.UUID;

public class PlayerCommand implements CommandExecutor {
    private void showDefaultCmd(Player player) {
        if (player != null) {
            PlayerInfo playerInfo = new PlayerInfo(player);
            if (player.isOp()) {
                Log.player(player, "verify | gencode | block | unblock | give | list");
            } else if (InvitSys.whitelist.contains(playerInfo.uuid)) {
                Log.player(player, "gencode");
            } else {
                Log.player(player, "verify <invttation code>");
            }
        } else {
            Log.player(player, "verify | gencode | block | unblock | give | list");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

        Log.server("recv cmd", command.getName());
        Log.server("recv args", args);
        try {

            Player senderPlayer = null;
            if (sender instanceof Player) {
                senderPlayer = (Player) sender;

                if (args.length == 0) {
                    showDefaultCmd(senderPlayer);
                } else if (args[0].equalsIgnoreCase("verify")) {

                    // 取得下指令玩家資料
                    PlayerInfo senderInfo = InvitSys.playerData.getPlayer(senderPlayer.getUniqueId());

                    if (InvitSys.whitelist.contains(senderInfo.uuid)) {
                        Log.player(senderPlayer, ChatColor.GREEN + "你已經在白名單中!");
                        return true;
                    }

                    if (args.length != 2) {
                        showDefaultCmd(senderPlayer);
                        return true;
                    }
                    String invitCode = args[1];
//                    Log.senderPlayer(senderPlayer, "輸入驗證碼", ChatColor.GREEN, invitCode);

                    if (InvitSys.addWhitelist(senderPlayer, senderPlayer, invitCode)) {
                        Log.player(senderPlayer, ChatColor.GREEN + "驗證通過!");
                        InvitSys.failList.remove(senderInfo.uuid);
                    } else {
                        Log.player(senderPlayer, ChatColor.RED + "驗證失敗!");

                        // 將使用者新增至嘗試失敗清單
                        InvitSys.failList.add(senderInfo.uuid);
                        FailVerifyPlayer failPlayer = InvitSys.failList.getPlayer(senderInfo.uuid);
                        // 次數++
                        failPlayer.failTime += 1;

                        Log.player(senderPlayer, "您尚有 " + ChatColor.RED + (InvitSys.MAX_RETRY_TIME - failPlayer.failTime) + ChatColor.WHITE + " 次輸入機會");

                        if (failPlayer.failTime >= InvitSys.MAX_RETRY_TIME) {
                            // 錯太多次，鎖定 InvitSys.MAX_RETRY_FAIL_BLOCK_DAY 天
                            InvitSys.addBlacklist(senderPlayer, senderPlayer, InvitSys.MAX_RETRY_FAIL_BLOCK_DAY, 0, 0, 0);
                            // 重置錯誤嘗試次數
                            InvitSys.failList.remove(senderInfo.uuid);

                            // 踢掉玩家
                            Event event = new InvitKickEvent(false, senderPlayer, "抱歉，請勿亂猜驗證碼，冷靜個 " + InvitSys.MAX_RETRY_FAIL_BLOCK_DAY + " 天吧");
                            Bukkit.getPluginManager().callEvent(event);
                        } else {
                            // 如果不用踢掉，就儲存錯誤嘗試次數
                            InvitSys.failList.save();
                        }
                    }
                } else if (args[0].equalsIgnoreCase("gencode")) {

                    PlayerInfo senderInfo = null;
                    if (senderPlayer != null) {
                        // 取得下指令玩家資料
                        senderInfo = InvitSys.playerData.getPlayer(senderPlayer.getUniqueId());

                        if (senderInfo.invitationQuota <= 0 && !senderPlayer.hasPermission("invits.gencode")) {
                            Log.player(senderPlayer, ChatColor.RED + "抱歉!你已經沒有邀請額度");
                            return true;
                        }
                    }

                    // 產生邀請碼，內建重複重新產生機制
                    String invitCode = InvitSys.genInvitCode();

                    Log.player(senderPlayer, "邀請碼 ", ChatColor.GREEN, invitCode);
                    Log.player(senderPlayer, "請妥善保存");

                    if (senderPlayer != null) {

                        // OP 產生邀請碼不會扣自身邀請碼額度
                        if (!senderPlayer.isOp() && !senderPlayer.hasPermission("invits.gencode")) {
                            senderInfo.invitationQuota -= 1;
                        }
                        // 將邀請碼紀錄在自己名下
                        senderInfo.invitationCode.add(invitCode);
                        InvitSys.playerData.save();
                    }

                }
            } else if (args.length == 0) {
                Log.server("請詳讀說明手冊");
            } else if (args[0].equalsIgnoreCase("verify")) {
                Log.server("請在遊戲中下指令");
            } else if (args[0].equalsIgnoreCase("gencode")) {
                Log.server("請在遊戲中下指令");
            }

            if (args[0].equalsIgnoreCase("off")) {
                if (senderPlayer != null && !senderPlayer.isOp()) {
                    Log.player(senderPlayer, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }

                InvitSys.enable = false;
                Log.player(senderPlayer, "邀請系統已經關閉", ChatColor.BLACK, "Off");

            } else if (args[0].equalsIgnoreCase("on")) {
                if (senderPlayer != null && !senderPlayer.isOp()) {
                    Log.player(senderPlayer, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }

                InvitSys.enable = true;
                Log.player(senderPlayer, "邀請系統已經啟動", ChatColor.GREEN, "On");

            } else if (args[0].equalsIgnoreCase("give")) {
                if (senderPlayer != null && !senderPlayer.isOp()) {
                    Log.player(senderPlayer, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }

                if (args.length != 3) {
                    showDefaultCmd(senderPlayer);
                    return true;
                }

                String playerName = args[1];
                int newQuota = Integer.parseInt(args[2]);

                if (playerName.equalsIgnoreCase("<all>")) {
                    Log.player(senderPlayer, "給予所有線上使用者邀請額度");

                    // 線上玩家 Only
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        // 取得玩家資料
                        PlayerInfo givePlayerInfo = InvitSys.playerData.getPlayer(p.getUniqueId());
                        if (givePlayerInfo == null) {
                            Log.player(senderPlayer, "查無此玩家", p.getDisplayName());
                            continue;
                        }
                        int quotaBefore = givePlayerInfo.invitationQuota;
                        // 給予額度
                        givePlayerInfo.invitationQuota += newQuota;
                        int quotaAfter = givePlayerInfo.invitationQuota;

                        Log.player(senderPlayer, ChatColor.YELLOW + givePlayerInfo.name + ChatColor.WHITE + " 邀請額度從 " + quotaBefore + " 變更為 " + quotaAfter);
                    }
                } else {
                    Log.player(senderPlayer, "給予使用者邀請額度", ChatColor.GREEN, playerName);

                    // 取得玩家資料
                    PlayerInfo givePlayerInfo = InvitSys.playerData.getPlayer(playerName);
                    if (givePlayerInfo == null) {
                        Log.player(senderPlayer, "查無此玩家", playerName);
                        return true;
                    }
                    int quotaBefore = givePlayerInfo.invitationQuota;
                    // 給予額度
                    givePlayerInfo.invitationQuota += newQuota;
                    int quotaAfter = givePlayerInfo.invitationQuota;

                    Log.player(senderPlayer, ChatColor.YELLOW + givePlayerInfo.name + ChatColor.WHITE + " 邀請額度從 " + quotaBefore + " 變更為 " + quotaAfter);
                }
                // 儲存資料
                InvitSys.playerData.save();
                Log.player(senderPlayer, "執行狀態", ChatColor.GREEN, "完成");

            } else if (args[0].equalsIgnoreCase("info")) {

                PlayerInfo playerInfo = null;
                if (args.length == 2) {
                    String playerName = args[1];

                    Log.player(senderPlayer, "查詢使用者", ChatColor.GREEN, playerName);

                    // 取得玩家資料
                    playerInfo = InvitSys.playerData.getPlayer(playerName);
                    if (playerInfo == null) {
                        Log.player(senderPlayer, "查無此玩家", playerName);
                        return true;
                    }
                } else if (args.length == 1) {
                    playerInfo = InvitSys.playerData.getPlayer(senderPlayer.getUniqueId());
                } else {
                    showDefaultCmd(senderPlayer);
                    return true;
                }

                OfflinePlayer BukkitPlayer = Bukkit.getOfflinePlayer(playerInfo.uuid);
                if (BukkitPlayer == null) {
                    Log.player(senderPlayer, "伺服器查無此玩家", args[1]);
                    return true;
                }

                if (BukkitPlayer.isOp()) {
                    Log.player(senderPlayer, "驗證狀態", ChatColor.GOLD, "Operator");
                    Log.player(senderPlayer, "邀請額度", "∞");
                } else if (InvitSys.whitelist.contains(playerInfo.uuid)) {
                    Log.player(senderPlayer, "驗證狀態", ChatColor.GREEN, "通過驗證");
                    Log.player(senderPlayer, "邀請額度", playerInfo.invitationQuota + "");
                } else if (InvitSys.blacklist.contains(playerInfo.uuid)) {
                    Log.player(senderPlayer, "驗證狀態", ChatColor.RED, "隔離中");

                    Prisoner p = InvitSys.blacklist.getPrisoner(playerInfo.uuid);

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

                    Log.player(senderPlayer, "刑期尚有", ChatColor.RED, prisonTime);
                    Log.player(senderPlayer, "邀請額度", playerInfo.invitationQuota + "");
                }

                PlayerInfo parent = InvitSys.playerData.getPlayer(playerInfo.parentId);
                if (parent != null) {
                    Log.player(senderPlayer, "推薦人", parent.name);
                }

                String kidStr = null;
                for (UUID uuid : playerInfo.childId) {
                    PlayerInfo childInfo = InvitSys.playerData.getPlayer(uuid);
                    if (kidStr == null) {
                        kidStr = childInfo.name;
                    } else {
                        kidStr += ", " + childInfo.name;
                    }
                }
                Log.player(senderPlayer, "推薦玩家: " + kidStr);

                if (senderPlayer != null ) {
                    if (senderPlayer.isOp() || BukkitPlayer.getPlayer().getUniqueId().equals(senderPlayer.getUniqueId())) {
                        String UnusedCodeStr = null;
                        for (String UnusedCode : playerInfo.invitationCode) {
                            if (UnusedCodeStr == null) {
                                UnusedCodeStr = UnusedCode;
                            } else {
                                UnusedCodeStr += ", " + UnusedCode;
                            }
                        }
                        Log.player(senderPlayer, "待使用驗證碼: " + (UnusedCodeStr == null ? "無" : UnusedCodeStr));
                    }
                }

            } else if (args[0].equalsIgnoreCase("unblock")) {
                if (senderPlayer != null && !senderPlayer.isOp()) {
                    Log.player(senderPlayer, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }

                if (args.length != 2) {
                    showDefaultCmd(senderPlayer);
                    return true;
                }

                String unblockPlayerName = args[1];
                Log.player(senderPlayer, "將使用者移出黑名單", ChatColor.GREEN, unblockPlayerName);

                PlayerInfo unblockPlayer = InvitSys.playerData.getPlayer(unblockPlayerName);
                if (unblockPlayer == null) {
                    Log.player(senderPlayer, "查無此玩家", unblockPlayerName);
                    return true;
                }
                InvitSys.blacklist.remove(unblockPlayer.uuid);

                Log.player(senderPlayer, "執行狀態", ChatColor.GREEN, "完成");
            } else if (args[0].equalsIgnoreCase("block")) {

                if (senderPlayer != null && !senderPlayer.isOp()) {
                    Log.player(senderPlayer, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }

                if (args.length < 2 || 6 < args.length) {
                    showDefaultCmd(senderPlayer);
                    return true;
                }

                String blockPlayerName = args[1];
                Log.player(senderPlayer, "將使用者加入黑名單", ChatColor.RED, blockPlayerName);

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
                if (args.length == 6) {
                    blockSec = Integer.parseInt(args[5]);
                }

                PlayerInfo playerInfo = InvitSys.playerData.getPlayer(blockPlayerName);
                if (playerInfo == null) {
                    Log.player(senderPlayer, "查無此玩家", blockPlayerName);
                    return true;
                }
                OfflinePlayer blockPlayer = Bukkit.getOfflinePlayer(playerInfo.uuid);
                if (blockPlayer == null) {
                    Log.player(senderPlayer, "伺服器查無此玩家", blockPlayerName);
                    return true;
                }

                // 設定黑名單與時間
                InvitSys.addBlacklist(senderPlayer, blockPlayer, blockDay, blockHour, blockMin, blockSec);
                // 從錯誤嘗試清單中移除
                InvitSys.failList.remove(playerInfo.uuid);

                if (blockPlayer.isOnline()) {
                    // 如果在線上踢掉
                    // 順便告訴他刑期，很棒吧
                    String blockMsg;
                    if (blockDay == 0 && blockHour == 0 && blockMin == 0 && blockSec == 0) {
                        blockMsg = "抱歉，你已經被永久加入黑名單";
                    } else {
                        blockMsg = "抱歉，你已經被加入黑名單，刑期 ";
                        blockMsg += Util.timeToStr(blockDay, blockHour, blockMin, blockSec);
                    }

                    Player kickPlayer = Bukkit.getPlayer(playerInfo.uuid);

                    Event event = new InvitKickEvent(false, kickPlayer, blockMsg);
                    Bukkit.getPluginManager().callEvent(event);
                }

                Log.player(senderPlayer, "執行狀態", ChatColor.GREEN, "完成");
            } else if (args[0].equalsIgnoreCase("list")) {
                if (senderPlayer != null && !senderPlayer.isOp()) {
                    Log.player(senderPlayer, ChatColor.RED + "抱歉!你沒有使用權限");
                    return true;
                }
                if (2 < args.length) {
                    showDefaultCmd(senderPlayer);
                    return true;
                }

                if (args.length == 2) {
                    if (!args[1].equalsIgnoreCase("whitelist") && !args[1].equalsIgnoreCase("blocklist")) {

                    }
                }

                if (args.length == 1 || args[1].equalsIgnoreCase("whitelist")) {
                    String resultStr = null;
                    for (UUID playerUUID : InvitSys.whitelist.playerList) {
                        if (resultStr == null) {
                            resultStr = InvitSys.playerData.getPlayer(playerUUID).name;
                        } else {
                            resultStr += ", " + InvitSys.playerData.getPlayer(playerUUID).name;
                        }
                    }
                    Log.player(senderPlayer, "白名單: ", resultStr == null ? "無" : resultStr);
                }
                if (args.length == 1 || args[1].equalsIgnoreCase("blocklist")) {
                    String resultStr = null;
                    for (Prisoner prisoner : InvitSys.blacklist.playerList) {
                        if (resultStr == null) {
                            resultStr = InvitSys.playerData.getPlayer(prisoner.uuid).name;
                        } else {
                            resultStr += ", " + InvitSys.playerData.getPlayer(prisoner.uuid).name;
                        }
                    }
                    Log.player(senderPlayer, "黑名單: ", resultStr == null ? "無" : resultStr);
                }

            } else {
                showDefaultCmd(senderPlayer);
                return true;
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
