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
    private void showDefaultCmd(Player player, PlayerInfo playerInfo) {

        if(player.isOp()){
            Log.player(player, "verify | gencode | block | unblock");
        }
        else{
            if (InvitSys.whitelist.contains(playerInfo.uid)) {
                Log.player(player, "gencode");
            } else {
                Log.player(player, "verify <invttation code>");
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String cmd = command.getName().toLowerCase();

        Log.server("recv cmd", cmd);
        Log.server("recv args", args);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Log.player(player, "You input cmd", cmd);

            PlayerInfo playerInfo = new PlayerInfo(player, 0);

            try {

                if (args.length == 0) {
                    showDefaultCmd(player, playerInfo);
                } else if (args[0].equalsIgnoreCase("verify")) {

                    if (InvitSys.whitelist.contains(playerInfo.uid)) {
                        Log.player(player, ChatColor.GREEN + "你已經在白名單中!");
                        return true;
                    }

                    if (args.length != 2) {
                        showDefaultCmd(player, playerInfo);
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

                        if (failPlayer.failTime >= InvitSys.MAX_RETRY_TIME) {
                            InvitSys.addBlacklist(player, InvitSys.MAX_RETRY_FAIL_BLOCK_DAY);
                            InvitSys.failList.remove(playerInfo.uid);

                            Event event = new InvitKickEvent(false, player, "抱歉，請勿亂猜驗證碼，冷靜個 " + InvitSys.MAX_RETRY_FAIL_BLOCK_DAY + " 天吧");
                            Bukkit.getPluginManager().callEvent(event);
                        }
                        InvitSys.failList.save();
                    }
                } else if (args[0].equalsIgnoreCase("gencode")) {
                    playerInfo = InvitSys.playerData.findPlayer(playerInfo.uid);

                    if(playerInfo.invitationQuota <= 0 && !player.isOp()){
                        Log.player(player, ChatColor.RED + "抱歉!你已經沒有邀請配額");
                        return true;
                    }

                    if(player.isOp()){
                        Log.player(player, "OP 擁有無限邀請配額");
                    }

                    String invitCode = Util.genUUID().substring(0, 6).toUpperCase();

                    while (InvitSys.playerData.isRepeatCode(invitCode)) {
                        invitCode = Util.genUUID().substring(0, 6).toUpperCase();
                    }

                    Log.player(player, "邀請碼 ", ChatColor.GREEN, invitCode);
                    Log.player(player, "請妥善保存");

                    if(!player.isOp()) {
                        playerInfo.invitationQuota -= 1;
                    }
                    playerInfo.invitationCode.add(invitCode);
                    InvitSys.playerData.save();
                } else if (args[0].equalsIgnoreCase("block")) {

                    if(!player.isOp()){
                        Log.player(player, ChatColor.RED + "抱歉!你沒有使用權限");
                        return true;
                    }

                    if (args.length < 2) {
                        showDefaultCmd(player, playerInfo);
                        return true;
                    }

                    String blockPlayerName = args[1];
                    Log.player(player, "將使用者加入黑名單", ChatColor.RED, blockPlayerName);

                    int blockDay = 0;
                    if (args.length >= 3) {
                        blockDay = Integer.parseInt(args[2]);
                    }

                    Player blockPlayer = Bukkit.getPlayer(blockPlayerName);
                    if(blockPlayer == null){
                        showDefaultCmd(player, playerInfo);
                        return true;
                    }

                    InvitSys.addBlacklist(blockPlayer, blockDay);
                    InvitSys.failList.remove(playerInfo.uid);
                    InvitSys.failList.save();

                    if(blockPlayer.isOnline()) {
                        String blockMsg;
                        if(blockDay == 0) {
                            blockMsg = "抱歉，你已經被永久加入黑名單";
                        }
                        else{
                            blockMsg = "抱歉，你已經被加入黑名單，刑期 " + blockDay + " 天";
                        }

                        Event event = new InvitKickEvent(false, blockPlayer, blockMsg);
                        Bukkit.getPluginManager().callEvent(event);
                    }
                    Log.player(player, "執行狀態", ChatColor.GREEN, "完成");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.server("請在遊戲中下指令");
        }
        return true;
    }
}
