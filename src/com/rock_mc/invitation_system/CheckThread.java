package com.rock_mc.invitation_system;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.io.IOException;

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

        PlayerInfo playerInfo = new PlayerInfo(player, 0);

        try {
            InvitSys.failList.add(playerInfo.uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FailVerifyPlayer failPlayer = InvitSys.failList.getPlayer(playerInfo.uuid);
        Log.player(player, "您尚有 " + ChatColor.RED + (InvitSys.MAX_RETRY_TIME - failPlayer.failTime) + ChatColor.WHITE + " 次輸入機會");

        long sleepTime = (long) (1000 * CHECK_TIME);
        for (int i = 0; i * CHECK_TIME < InvitSys.MAX_INPUT_CODE_TIME; i++) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (InvitSys.whitelist.contains(playerInfo.uuid)) {
                break;
            }
            if(!player.isOnline()){
                return;
            }
        }
        Event event;
        if (InvitSys.whitelist.contains(playerInfo.uuid)) {
            event = new InvitJoinEvent(true, player, "歡迎 " + ChatColor.YELLOW + player.getDisplayName() + ChatColor.WHITE + " 全新加入!");
            InvitSys.freezePlayerSet.remove(player.getUniqueId());
        }
        else{
            event = new InvitKickEvent(true, player, "抱歉未通過認證，請取得邀請碼後，參考官網教學輸入邀請碼");
            Log.broadcast(playerInfo.name + " 沒有通過驗證，被請出伺服器了 QQ");
        }
        Bukkit.getPluginManager().callEvent(event);
    }
}
