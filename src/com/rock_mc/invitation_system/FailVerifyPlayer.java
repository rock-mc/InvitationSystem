package com.rock_mc.invitation_system;

public class FailVerifyPlayer {
    public String uid;
    public int failTime;
    public FailVerifyPlayer(String player_uid){
        uid = player_uid;
        failTime = 0;
    }
}
