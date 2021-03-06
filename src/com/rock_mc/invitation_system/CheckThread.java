package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CheckThread extends Thread {
    private Player player;
    private final float CHECK_TIME = 0.5F;

    public CheckThread(Player checkPlayer) {
        player = checkPlayer;
    }

    public void run() {

        if(!InvitSys.enable){
            Bukkit.getPluginManager().callEvent(new InvitJoinEvent(true, player, "歡迎 " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + " 全新加入!"));
            return;
        }

        PlayerInfo currentPlayer = new PlayerInfo(player, 0);

        long sleepTime = (long) (1000 * CHECK_TIME);
        for (int i = 0; i * CHECK_TIME < InvitSys.MAX_INPUT_CODE_TIME; i++) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (InvitSys.whitelist.contains(currentPlayer.uid)) {
                break;
            }
        }
        Event event;
        if (InvitSys.whitelist.contains(currentPlayer.uid)) {
            event = new InvitJoinEvent(true, player, "歡迎 " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + " 全新加入!");
        }
        else{
            event = new InvitKickEvent(true, player, "抱歉未通過認證，請取得邀請碼後，參考官網教學輸入邀請碼");
        }
        Bukkit.getPluginManager().callEvent(event);
    }
}
