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
    static void playerLog(Player player, String prefixMsg, ChatColor textColor, String postfixMsg){
        player.sendMessage("[" + InvitationSystem.APP_NAME + "] " + prefixMsg + " [" + textColor + postfixMsg + ChatColor.WHITE + "]");
    }
    static void player(Player player, String prefixMsg, String postfixMsg){
        Log.player(player, prefixMsg, ChatColor.WHITE, postfixMsg);
    }
    static void player(Player player, String prefixMsg, ChatColor textColor, String postfixMsg){
        playerLog(player, prefixMsg, textColor, postfixMsg);
    }

    static void serverLog(String prefixMsg, ChatColor chatColor, String postfixMsg){
        logger.info(prefixMsg + " [" + chatColor + postfixMsg + ChatColor.WHITE + "]");
    }
    static void server(String prefixMsg, String postfixMsg){
        serverLog(prefixMsg, ChatColor.WHITE, postfixMsg);
    }
    static void server(String prefixMsg, String []postfixMsg){
        serverLog(prefixMsg, ChatColor.WHITE, arrayToString(postfixMsg));
    }
}
