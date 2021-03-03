package com.rock_mc.invitation_system;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CheckThread extends Thread {
    private Player player;
    private final float CHECK_TIME = 0.5F;

    public CheckThread(Player checkPlayer) {
        player = checkPlayer;
    }

    public void run() {

        PlayerInfo currentPlayer = new PlayerInfo(player);

        long sleepTime = (long) (1000 * CHECK_TIME);
        for (int i = 0; i * CHECK_TIME < InvitationSystem.MAX_INPUT_CODE_TIME; i++) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (InvitationSystem.whitelist.contains(currentPlayer.uid)) {
                return;
            }
        }

        if (InvitationSystem.whitelist.contains(currentPlayer.uid)) {
            return;
        }
        Log.server("發出執行 踢掉", currentPlayer.name);
        InvitatKickEvent event = new InvitatKickEvent(player);
        Bukkit.getPluginManager().callEvent(event);
//
//        Bukkit.getScheduler().runTask(InvitationSystem.plugin, () -> Bukkit.getPluginManager().callEvent(event));
    }
}
