package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class PlayerChain {
    public ArrayList<PlayerInfo> playerInfos;
    private String filePath;
    public PlayerChain(String path) throws IOException {
        filePath = path;

        Path p = Path.of(filePath);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerInfos = new Gson().fromJson(fileString, ArrayList.class);
        }
        else {
            playerInfos = new ArrayList<>();
        }
    }
    public boolean contains(PlayerInfo playerInfo){
        boolean result = false;

        for(PlayerInfo p : playerInfos){
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
        playerInfos.add(playerInfo);
    }

    public void save() throws IOException {
        if (filePath == null) {
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json_str = gson.toJson(playerInfos);

        Util.writeFile(filePath, json_str);
    }
}
