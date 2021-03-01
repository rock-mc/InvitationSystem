package com.rock_mc.invitation_system;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String cmd = command.getName().toLowerCase();

        Log.server("recv cmd", cmd);
        Log.server("recv args", args);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Log.cur_p(player, "You input cmd", cmd);
        }
        return false;
    }
}
