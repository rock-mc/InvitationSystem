package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class PlayerData {
    public ArrayList<PlayerInfo> playerList;
    private String filePath;
    public PlayerData(String path) throws IOException {
        filePath = path;

        Path p = Path.of(filePath);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerList = new Gson().fromJson(fileString, ArrayList.class);
        }
        else {
            playerList = new ArrayList<>();
        }
    }
    public boolean contains(PlayerInfo playerInfo){
        boolean result = false;

        for(PlayerInfo p : playerList){
            if(p.uid.equals(playerInfo.uid)){
                result = true;
                break;
            }
        }
        return result;
    }

    public void add(PlayerInfo playerInfo){
        if (contains(playerInfo)){
            return;
        }
        playerList.add(playerInfo);
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