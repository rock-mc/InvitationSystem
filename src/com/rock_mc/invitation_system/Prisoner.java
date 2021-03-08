package com.rock_mc.invitation_system;

import java.util.UUID;

public class Prisoner {
    public UUID uuid;
    public long basicTime;
    public long expiryTime;

    public Prisoner(UUID uid, int day, int hour, int min, int sec){
        this.uuid = uid;

        basicTime = java.time.Instant.now().getEpochSecond();
        setExpiryTime(day, hour, min, sec);
    }
    public void setExpiryTime(int day, int hour, int min, int sec){
        expiryTime = day * Util.DAY + hour * Util.HOUR + min * Util.MIN + sec * Util.SEC;
    }
    public boolean isExpiry(){
        if(expiryTime == 0){
            return false;
        }

        return java.time.Instant.now().getEpochSecond() >= basicTime + expiryTime;
    }
}
