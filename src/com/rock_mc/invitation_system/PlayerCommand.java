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
        if (InvitSys.whitelist.contains(playerInfo.uid)) {
            Log.player(player, "gencode");
        } else {
            Log.player(player, "input <invttation code>");
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
                    } else {
                        String invitCode = args[1];
                        Log.player(player, "輸入驗證碼", ChatColor.GREEN, invitCode);
                        if (InvitSys.addWhitelist(player, invitCode)) {
                            Log.player(player, ChatColor.GREEN + "驗證通過!");

                            Event event = new InvitJoinEvent(player, false);
                            Bukkit.getPluginManager().callEvent(event);

                        } else {
                            Log.player(player, ChatColor.RED + "驗證失敗!");
                        }
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
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }
}
