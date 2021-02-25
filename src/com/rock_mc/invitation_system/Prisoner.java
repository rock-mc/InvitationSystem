package com.rock_mc.invitation_system;

public class Prisoner {
    public String uid;
    public long expiryTime;
    public Prisoner(String uid, int day){
        this.uid = uid;

        setExpiryTime(day);
    }
    public void setExpiryTime(int day){
        expiryTime = java.time.Instant.now().getEpochSecond();
        expiryTime += day * Util.DAY;
    }
    public boolean isExpiry(){
        return java.time.Instant.now().getEpochSecond() >= expiryTime;
    }
}
