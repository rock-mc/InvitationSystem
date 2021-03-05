package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class InvitJoinEvent extends Event {

    private UUID player_uid;

    public InvitJoinEvent(Player joinPlayer, boolean isAsync) {
        super(isAsync);
        player_uid = joinPlayer.getUniqueId();
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public UUID getPlayerUid(){
        return player_uid;
    }
}

