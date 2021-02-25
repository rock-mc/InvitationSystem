package com.rock_mc.invitation_system;

public class Prisoner {
    public String uid;
    public long expiryDate;
    public Prisoner(String uid, int day){
        this.uid = uid;

        setExpiryDate(day);
    }
    public void setExpiryDate(int day){
        expiryDate = java.time.Instant.now().getEpochSecond();
        expiryDate += day * Util.DAY;
    }
}
