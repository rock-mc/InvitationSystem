package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class InvitJoinEvent extends Event {

    private Player player;
    private boolean isCancelled;

    public InvitJoinEvent(Player joinPlayer, boolean isAsync) {
        super(isAsync);
        player = joinPlayer;
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

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}

