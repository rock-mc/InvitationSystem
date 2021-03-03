package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    public ArrayList<PlayerInfo> playerList;
    private String filePath;
    public PlayerData(String path) throws IOException {
        filePath = path;

        Path p = Path.of(filePath);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerList = new Gson().fromJson(fileString, new TypeToken<List<PlayerInfo>>(){}.getType());
        }
        else {
            playerList = new ArrayList<>();
        }
    }
    public PlayerInfo findPlayer(String player_uid){
        PlayerInfo result = null;

        for(PlayerInfo p : playerList){
            if(p.uid.equals(player_uid)){
                result = p;
                break;
            }
        }

        return result;
    }
    public boolean contains(String player_uid){
        boolean result = false;

        for(PlayerInfo p : playerList){
            if(p.uid.equals(player_uid)){
                result = true;
                break;
            }
        }
        return result;
    }

    public void add(PlayerInfo playerInfo) throws IOException {
        if (contains(playerInfo.uid)){
            return;
        }
        playerList.add(playerInfo);
        save();
    }

    public void save() throws IOException {
        if (filePath == null) {
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json_str = gson.toJson(playerList);

        Util.writeFile(filePath, json_str);
    }
}
