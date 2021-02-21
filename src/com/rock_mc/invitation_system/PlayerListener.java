package com.rock_mc.invitation_system;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        final String uid = player.getUniqueId().toString();

        Bukkit.broadcastMessage("Welcome to the server!");

        Log.cur_p(player, "Your name", name);
        Log.cur_p(player, "Your uid", uid);
    }
}
