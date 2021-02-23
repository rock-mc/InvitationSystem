package com.rock_mc.invitation_system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PlayerList {
    public HashMap<String, PlayerInfo> playerList;
    private String filePath;

    public PlayerList(String loadFile) throws Exception {
        filePath = loadFile;


        Path p = Path.of(loadFile);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
//            playerList = objectMapper.readValue(fileString, HashMap.class);
        }
        else{
            playerList = new HashMap<>();
        }
    }

    public PlayerList() {

        filePath = null;

        playerList = new HashMap<>();
//        objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void add(PlayerInfo player_info) throws IOException {

        playerList.put(player_info.uid, player_info);

        save();
    }

    public void add(PlayerInfo player_info, PlayerInfo parent_info) throws IOException {

        parent_info.childId.add(player_info.uid);
        player_info.parentId = parent_info.uid;

        playerList.put(player_info.uid, player_info);

        save();
    }

    private void save() throws IOException {
        if (filePath == null) {
            return;
        }

//        String json_str = objectMapper.writeValueAsString(playerList);

//        Util.writeFile(filePath, json_str);
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
            if (temp_player_info.invitationCode.contains(invitation_code)) {
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
