package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.io.IOException;
import java.util.UUID;

public class EventListener implements Listener {
    private final float CHECK_TIME = 0.5F; //    sec
    private final float DEFAULT_WALK_SPEED = 0.2F;

    @EventHandler(priority = EventPriority.LOWEST)
    public synchronized void onPlayerJoin(PlayerJoinEvent event) throws IOException {

        final Player player = event.getPlayer();
        final String name = player.getDisplayName();
        final UUID uuid = player.getUniqueId();

        // 進來就建立玩家資料
        InvitSys.createPlayerData(player);

        // 如果元件關閉就不檢查是否在白名單中
        if (!InvitSys.enable) {
            return;
        }

        // 檢查是否在白名單中
        if (InvitSys.whitelist.contains(uuid)) {
            Event joinEvent = new InvitJoinEvent(false, player, ChatColor.BOLD + player.getDisplayName() + ChatColor.WHITE + " 通過驗證");
            Bukkit.getPluginManager().callEvent(joinEvent);
            return;
        }
        // 如果不在白名單，檢查是否為 Op
        // 給予禮遇自動加入白名單
        if (player.isOp()) {
            if (InvitSys.addWhitelist(player, player, InvitSys.DEFAULT_INVIT_QUOTA)) {
                Event joinEvent = new InvitJoinEvent(false, player, "親愛的 OP " + ChatColor.BOLD + name + ChatColor.WHITE + " 已經自動被加入白名單");
                Bukkit.getPluginManager().callEvent(joinEvent);
            } else {
                Log.player(player, "自動加入白名單失敗", ChatColor.RED, "不明原因");
            }
            return;
        }
        // 未通過驗證，凍結玩家
        InvitSys.freezePlayerSet.add(player.getUniqueId());
        // 要求輸入邀請碼，啟動檢查線程
        new CheckThread(player).start();
    }

    @EventHandler
    public void onInvitJoin(InvitJoinEvent event) {
        Log.broadcast(event.getMessage());
        InvitSys.freezePlayerSet.remove(event.getPlayer().getUniqueId());
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
    public synchronized void onPlayerLogin(PlayerLoginEvent event) throws IOException {
        final Player player = event.getPlayer();
        final String name = player.getDisplayName();
        final UUID uuid = player.getUniqueId();

        // 如果不在黑名單可以先結束了
        if (!InvitSys.blacklist.contains(uuid)) {
            return;
        }
        // 取得犯人資料
        Prisoner p = InvitSys.blacklist.getPrisoner(uuid);
        if (p.isExpiry()) {
            // 如果刑期已經過了，那就放對方進來
            InvitSys.blacklist.remove(uuid);
            return;
        }
        // 在黑名單中並且尚未過期
        // 踢掉。
        String kickMsg;
        if (p.expiryTime == 0) {
            kickMsg = "抱歉!你被列為黑名單!";
        } else {
            // 很好心的告訴犯人還有多久刑期
            kickMsg = "抱歉!你被列為黑名單!\n刑期尚有 ";

            long expiryTime = p.basicTime + p.expiryTime;
            expiryTime -= java.time.Instant.now().getEpochSecond();
            kickMsg += Util.timeToStr(expiryTime);
        }

        // 踢。
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMsg);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getDisplayName();
        final UUID uuid = player.getUniqueId();
        final String msg = event.getMessage();

        // 如果在白名單中就讓事件通過
        if (InvitSys.whitelist.contains(uuid)) {
            return;
        }

        // 尚未通過驗證檔掉對話
        event.setCancelled(true);
        Log.player(player, "因為您尚未通過驗證，因此訊息並未送出", msg);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!InvitSys.freezePlayerSet.contains(player.getUniqueId())) {
            return;
        }
        LivingEntity livingEntity = player;
        if(!livingEntity.isOnGround()){
//            Log.player(player, "is not on ground");
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (!InvitSys.freezePlayerSet.contains(player.getUniqueId())) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (!InvitSys.freezePlayerSet.contains(player.getUniqueId())) {
                return;
            }
            e.setCancelled(true);
        }
    }
}
