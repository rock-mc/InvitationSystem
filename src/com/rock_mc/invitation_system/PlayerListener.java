package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.IOException;

public class PlayerListener implements Listener {
    private final float CHECK_TIME = 0.5F; //    sec
    private final float DEFAULT_WALK_SPEED = 0.2F;

    private void resetPlayer(Player player){
        player.setWalkSpeed(DEFAULT_WALK_SPEED);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {

        final Player player = event.getPlayer();
        final String name = player.getDisplayName();
        final String uid = player.getUniqueId().toString();

        if(!InvitSys.enable){
            return;
        }

        if (player.isOp()) {
            if(InvitSys.addWhitelist(player, InvitSys.DEFAULT_INVIT_QUOTA)){
                Log.player(player, "因為您具有OP權限", ChatColor.GREEN, "自動被加入白名單");
            }
            else{
                Log.player(player, "自動加入白名單失敗", ChatColor.RED, "不明原因");
            }
            resetPlayer(player);
            return;
        }
        if (InvitSys.whitelist.contains(uid)) {
            Log.server("通過邀請系統驗證", player.getDisplayName());
            resetPlayer(player);
            return;
        }
        Log.player(player, name, ChatColor.RED, "請在 15 秒內輸入邀請碼");
        player.setWalkSpeed(0.0F);

        new CheckThread(player).start();
    }

    public void onInvitJoin(InvitJoinEvent event){
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(InvitSys.plugin, new Runnable() {
            public void run() {
                Log.broadcastLog("歡迎 " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + " 全新加入!");
                resetPlayer(player);
            }
        });
    }

    public void onInvitKick(InvitKickEvent event){
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(InvitSys.plugin, new Runnable() {
            public void run() {
                player.kickPlayer("抱歉未通過認證，請取得邀請碼後，參考官網教學輸入邀請碼");
            }
        });
    }

    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        final String uid = player.getUniqueId().toString();

        if (!InvitSys.blacklist.contains(uid)) {
            return;
        }
        player.kickPlayer("抱歉!你被列為黑名單!");
    }
}
