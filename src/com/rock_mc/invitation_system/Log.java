package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Log {
    public static Logger logger;
    private static String LOG_PREFIX = "[" + ChatColor.GOLD + InvitSys.APP_NAME + ChatColor.WHITE + "] ";

    static String arrayToString(String[] postfix_msg) {
        String result = null;

        if (null == postfix_msg) {
            return result;
        }
        result = String.join(", ", postfix_msg);

        return result;
    }

    static void player(Player player, String msg) {
        if (player != null) {
            player.sendMessage(LOG_PREFIX + msg);
        }
        else{
            server(msg);
        }
    }

    static void player(Player player, String prefixMsg, String postfixMsg) {
        player(player, prefixMsg, ChatColor.WHITE, postfixMsg);
    }

    static void player(Player player, String prefixMsg, ChatColor textColor, String postfixMsg) {
        player(player, prefixMsg + " [" + textColor + postfixMsg + ChatColor.WHITE + "]");
    }

    static void broadcast(String msg) {
        Bukkit.broadcastMessage(LOG_PREFIX + msg);
    }
    static void broadcast(String prefixMsg, ChatColor chatColor, String postfixMsg) {
        broadcast(prefixMsg + " [" + chatColor + postfixMsg + ChatColor.WHITE + "]");
    }
    static void broadcast(String prefixMsg, String postfixMsg) {
        broadcast(prefixMsg, ChatColor.WHITE, postfixMsg);
    }

    static void broadcast(String prefixMsg, String[] postfixMsg) {
        broadcast(prefixMsg, ChatColor.WHITE, arrayToString(postfixMsg));
    }

    static void server(String msg) {
        logger.info(msg);
    }

    static void server(String prefixMsg, ChatColor chatColor, String postfixMsg) {
        server(prefixMsg + " [" + chatColor + postfixMsg + ChatColor.WHITE + "]");
    }

    static void server(String prefixMsg, String postfixMsg) {
        server(prefixMsg, ChatColor.WHITE, postfixMsg);
    }

    static void server(String prefixMsg, String[] postfixMsg) {
        server(prefixMsg, ChatColor.WHITE, arrayToString(postfixMsg));
    }
}
