package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class PlayerListener implements Listener {
    private final float CHECK_TIME = 0.5F; //    sec

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
        final Player player = event.getPlayer();
        final String name = player.getName();
        final String uid = player.getUniqueId().toString();

        if (player.isOp()) {
            InvitationSystem.addWhitelist(player);
            return;
        }
        if (InvitationSystem.whitelist.contains(uid)) {
            return;
        }

        float walkSpeed = player.getWalkSpeed();
        player.setWalkSpeed(0.0F);

        for (int i = 0; i * CHECK_TIME < InvitationSystem.MAX_INPUT_CODE_TIME; i++) {
            try {
                Thread.sleep((long) (CHECK_TIME * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (InvitationSystem.whitelist.contains(player.getUniqueId().toString())) {
                break;
            }
        }

        if (!InvitationSystem.whitelist.contains(player.getUniqueId().toString())) {
            player.kickPlayer("抱歉未通過認證，請取得邀請碼後，參考官網教學輸入邀請碼");
            return;
        }

        player.setWalkSpeed(walkSpeed);
    }


    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        final String uid = player.getUniqueId().toString();

        if (!InvitationSystem.blacklist.contains(uid)) {
            return;
        }
        player.kickPlayer("抱歉!你被列為黑名單!");
    }
}
