package com.rock_mc.invitation_system;

import java.util.HashMap;

public class PlayerList {
    public HashMap<String, PlayerInfo> player_list;

    public PlayerList(String load_file){
        player_list = new HashMap<String, PlayerInfo>();
    }
    public void add(PlayerInfo player_info){
        player_list.put(player_info.uid, player_info);
    }
    public void remove(PlayerInfo player_info){
        player_list.remove(player_info.uid);
    }
    public boolean contains(String player_uid){
        return player_list.containsKey(player_uid);
    }
}
