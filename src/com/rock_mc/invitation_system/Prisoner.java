package com.rock_mc.invitation_system;

public class Prisoner {
    public String uid;
    public long expiryDate;
    public Prisoner(String uid, int day){
        this.uid = uid;

        expiryDate = java.time.Instant.now().getEpochSecond();
        expiryDate += day * Util.DAY;
    }
}
