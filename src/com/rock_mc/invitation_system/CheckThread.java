package com.rock_mc.invitation_system;

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
            Bukkit.getPluginManager().callEvent(new InvitJoinEvent(player, true));
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
            event = new InvitJoinEvent(player, true);
        }
        else{
            event = new InvitKickEvent(player);
        }
        Bukkit.getPluginManager().callEvent(event);
    }
}
