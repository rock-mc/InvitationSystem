package com.rock_mc.invitation_system;

import java.util.UUID;

public class FailVerifyPlayer {
    public UUID uid;
    public int failTime;
    public FailVerifyPlayer(UUID playerUid){
        uid = playerUid;
        failTime = 0;
    }
}
