package com.rock_mc.invitation_system;

public class InvitationSystem {
    public static PlayerList white_list;
    public static PlayerList black_list;
    static {
        white_list = new PlayerList("invitation_whitelist.json");
        black_list = new PlayerList("invitation_blacklist.json");
    }
}
