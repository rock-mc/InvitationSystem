package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.UUID;

public class PlayerData {
    public HashSet<PlayerInfo> playerList;
    private String filePath;
    public PlayerData(String path) throws IOException {
        filePath = path;

        Path p = Path.of(filePath);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerList = new Gson().fromJson(fileString, new TypeToken<HashSet<PlayerInfo>>(){}.getType());
        }
        else {
            playerList = new HashSet<>();
        }
    }
    public boolean isRepeatCode(String invitCode){
        for(PlayerInfo p : playerList){
            if(p.invitationCode.contains(invitCode)){
                return true;
            }
        }
        return false;
    }
    public PlayerInfo getPlayer(UUID uuid){
        PlayerInfo result = null;

        for(PlayerInfo p : playerList){
            if(p.uuid.equals(uuid)){
                result = p;
                break;
            }
        }

        return result;
    }
    public PlayerInfo getPlayer(String name){
        PlayerInfo result = null;

        for(PlayerInfo p : playerList){
            if(p.name.equalsIgnoreCase(name)){
                result = p;
                break;
            }
        }

        return result;
    }
    public boolean contains(UUID uuid){
        for(PlayerInfo p : playerList){
            if(p.uuid.equals(uuid)){
                return true;
            }
        }
        return false;
    }

    public void add(PlayerInfo playerInfo) throws IOException {
        if (contains(playerInfo.uuid)){
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
