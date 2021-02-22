package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        final String uid = player.getUniqueId().toString();

        if(player.isOp()){
            return;
        }
        if(InvitationSystem.white_list.contains(uid)){
            return;
        }
        if(InvitationSystem.black_list.contains(uid)){
            player.kickPlayer("抱歉!你被列為黑名單!");
            return;
        }

        for(int i = 0 ; i < 15 ; i++){
            Log.cur_p(player, ChatColor.GREEN + name + ChatColor.WHITE + "! 您尚未通過認證", ChatColor.RED, "請輸入邀請碼");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        player.kickPlayer("未通過認證");
    }

//
//    public void onPlayerLogin(PlayerLoginEvent event) {
//        final Player player = event.getPlayer();
//        final String name = player.getName();
//        final String uid = player.getUniqueId().toString();
//
//        Bukkit.broadcastMessage("Welcome to the server!");
//
//        Log.cur_p(player, "Your name", name);
//        Log.cur_p(player, "Your uid", uid);
//    }
}
