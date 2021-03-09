package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.io.IOException;
import java.util.UUID;

public class EventListener implements Listener {
    private final float CHECK_TIME = 0.5F; //    sec
    private final float DEFAULT_WALK_SPEED = 0.2F;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {

        final Player player = event.getPlayer();
        final String name = player.getDisplayName();
        final UUID uuid = player.getUniqueId();

        if (!InvitSys.enable) {
            return;
        }

        if (InvitSys.whitelist.contains(uuid)) {
            Log.broadcast("通過邀請系統驗證", player.getDisplayName());
            return;
        }
        if (player.isOp()) {
            if (InvitSys.addWhitelist(player, InvitSys.DEFAULT_INVIT_QUOTA)) {
                Log.player(player, "親愛的 OP 您已經自動被加入白名單");
            } else {
                Log.player(player, "自動加入白名單失敗", ChatColor.RED, "不明原因");
            }
            return;
        }
        Log.player(player, name, ChatColor.RED, "請在 " + InvitSys.MAX_INPUT_CODE_TIME + " 秒內輸入邀請碼");

        InvitSys.freezePlayerSet.add(player.getUniqueId());

        new CheckThread(player).start();
    }

    @EventHandler
    public void onInvitJoin(InvitJoinEvent event) {
        Player player = event.getPlayer();
        Log.broadcast(event.getMessage());
    }

    @EventHandler
    public void onInvitKick(InvitKickEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(InvitSys.plugin, new Runnable() {
            public void run() {
                player.kickPlayer(event.getMessage());
            }
        });
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) throws IOException {
        final Player player = event.getPlayer();
        final String name = player.getDisplayName();
        final UUID uuid = player.getUniqueId();

        if (!InvitSys.blacklist.contains(uuid)) {
            return;
        }
        Prisoner p = InvitSys.blacklist.getPrisoner(uuid);
        if (p.isExpiry()) {
            InvitSys.blacklist.remove(uuid);
            return;
        }
        // in black list and not expiry
        // BLOCK !!!!!!!

        String kickMsg;
        if (p.expiryTime == 0) {
            kickMsg = "抱歉!你被列為黑名單!";
        } else {
            kickMsg = "抱歉!你被列為黑名單!\n刑期尚有 ";

            long expiryTime = p.basicTime + p.expiryTime;
            expiryTime -= java.time.Instant.now().getEpochSecond();

            long day = expiryTime / Util.DAY;
            expiryTime %= Util.DAY;
            long hour = expiryTime / Util.HOUR;
            expiryTime %= Util.HOUR;
            long min = expiryTime / Util.MIN;
            expiryTime %= Util.MIN;
            long sec = expiryTime;

            kickMsg += Util.timeToStr(day, hour, min, sec);
        }

        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMsg);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getDisplayName();
        final UUID uuid = player.getUniqueId();

        if (InvitSys.whitelist.contains(uuid)) {
            return;
        }

        event.setCancelled(true);
        Log.player(player, "因為您尚未通過驗證，因此訊息並未送出", event.getMessage());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!InvitSys.freezePlayerSet.contains(player.getUniqueId())) {
            return;
        }
        event.setCancelled(true);
    }
}
