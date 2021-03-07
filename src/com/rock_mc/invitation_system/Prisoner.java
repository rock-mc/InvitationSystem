package com.rock_mc.invitation_system;

public class Prisoner {
    public String uid;
    public long basicTime;
    public long expiryTime;

    public Prisoner(String uid, int day){
        this.uid = uid;

        basicTime = java.time.Instant.now().getEpochSecond();
        setExpiryTime(day, 0, 0, 0);
    }
    public Prisoner(String uid, int day, int hour, int min, int sec){
        this.uid = uid;

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
