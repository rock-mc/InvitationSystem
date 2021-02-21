package com.rock_mc.invitation_system;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Log {
    public static Logger logger;

    static void cur_p(Player player, String prefix_msg, String postfix_msg){
//        player.sendMessage(prefix_msg + " [" + postfix_msg + "]");

        Bukkit.getPlayer(player.getUniqueId()).sendMessage(prefix_msg + " [" + postfix_msg + "]");
    }
    static void server(String prefix_msg, String postfix_msg){
        logger.info(prefix_msg + " [" + postfix_msg + "]");
    }
}
