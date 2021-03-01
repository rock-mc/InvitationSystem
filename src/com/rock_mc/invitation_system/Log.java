package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Log {
    public static Logger logger;

    static String arrayToString(String []postfix_msg){
        String result = null;

        if(null == postfix_msg){
            return result;
        }
        result = String.join(", ", postfix_msg);

        return result;
    }

    static void cur_p(Player player, String prefix_msg, String postfix_msg){
        Log.cur_p(player, prefix_msg, ChatColor.GREEN, postfix_msg);
    }
    static void cur_p(Player player, String prefix_msg, ChatColor text_color, String postfix_msg){
        player.sendMessage(prefix_msg + " [" + text_color + postfix_msg + ChatColor.WHITE + "]");
    }
    static void server(String prefix_msg, String postfix_msg){
        logger.info(prefix_msg + " [" + postfix_msg + "]");
    }
    static void server(String prefix_msg, String []postfix_msg){
        logger.info(prefix_msg + " [" + arrayToString(postfix_msg) + "]");
    }
}
