package com.rock_mc.invitation_system;

import java.util.UUID;

public class FailVerifyPlayer {
    public UUID uuid;
    public int failTime;
    public FailVerifyPlayer(UUID playerUUID){
        uuid = playerUUID;
        failTime = 0;
    }
}
