package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class PlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String cmd = command.getName().toLowerCase();

        Log.server("recv cmd", cmd);
        Log.server("recv args", args);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Log.player(player, "You input cmd", cmd);


            try {

                String invitCode = null;
                Log.player(player, "輸入驗證碼", ChatColor.GREEN, invitCode);
                if(InvitSys.addWhitelist(player, invitCode)){
                    Log.playerLog(player, ChatColor.GREEN + "驗證通過!");
                }
                else{
                    Log.playerLog(player, ChatColor.RED + "驗證失敗!");
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
