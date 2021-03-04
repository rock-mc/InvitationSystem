package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InvitKickEvent extends Event {

    private Player player;

    public InvitKickEvent(Player kickPlayer) {
        super(true);
        player = kickPlayer;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer(){
        return player;
    }
}

