package com.rock_mc.invitation_system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerList {
    public HashMap<String, PlayerInfo> playerList;
    private ObjectMapper objectMapper;
    private String filePath;

    public PlayerList(String loadFile) {
        filePath = loadFile;

        playerList = new HashMap<>();
        objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public PlayerList() {

        filePath = null;

        playerList = new HashMap<>();
        objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void add(PlayerInfo player_info) throws IOException  {

        playerList.put(player_info.uid, player_info);

        save();
    }

    public void add(PlayerInfo player_info, PlayerInfo parent_info) throws IOException {

        parent_info.child_id.add(player_info.uid);
        player_info.parent_id = parent_info.uid;

        playerList.put(player_info.uid, player_info);

        save();
    }
    private void save() throws IOException {
        if(filePath == null){
            return;
        }

        String json_str = objectMapper.writeValueAsString(playerList);

        Util.writeFile(filePath, json_str);
    }

    public void remove(PlayerInfo player_info) {
        playerList.remove(player_info.uid);
    }

    public boolean contains(String player_uid) {
        return playerList.containsKey(player_uid);
    }

    public PlayerInfo find_code_owner(String invitation_code) {
        PlayerInfo result = null;
        for (Map.Entry<String, PlayerInfo> entry : playerList.entrySet()) {
            PlayerInfo temp_player_info = entry.getValue();
            if (temp_player_info.invitation_code.contains(invitation_code)) {
                result = temp_player_info;
                break;
            }
        }

        return result;
    }

    public void extend(PlayerList playerList) {
        this.playerList.putAll(playerList.playerList);
    }
}
