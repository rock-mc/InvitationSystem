package com.rock_mc.invitation_system;

import java.util.HashMap;
import java.util.Map;

public class PlayerList {
    public HashMap<String, PlayerInfo> player_list;

    public PlayerList(String load_file) {
        player_list = new HashMap<>();
    }

    public PlayerList() {
        player_list = new HashMap<>();
    }

    public void add(PlayerInfo player_info) {
        player_list.put(player_info.uid, player_info);
    }

    public void add(PlayerInfo player_info, PlayerInfo parent_info) {

        parent_info.child_id.add(player_info.uid);
        player_info.parent_id = parent_info.uid;

        player_list.put(player_info.uid, player_info);
    }

    public void remove(PlayerInfo player_info) {
        player_list.remove(player_info.uid);
    }

    public boolean contains(String player_uid) {
        return player_list.containsKey(player_uid);
    }

    public PlayerInfo find_code_owner(String invitation_code) {
        PlayerInfo result = null;
        for (Map.Entry<String, PlayerInfo> entry : player_list.entrySet()) {
            PlayerInfo temp_player_info = entry.getValue();
            if (temp_player_info.invitation_code.contains(invitation_code)) {
                result = temp_player_info;
                break;
            }
        }

        return result;
    }

    public void extend(PlayerList playerList) {
        this.player_list.putAll(playerList.player_list);
    }
}
