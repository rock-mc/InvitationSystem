package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        for(PlayerInfo p : playerList){
            if(p.uid.equals(player_uid)){
                return true;
            }
        }
        return false;
    }

    public void add(PlayerInfo playerInfo) throws IOException {

        PlayerInfo tempPlayer = findPlayer(playerInfo.uid);
        if(tempPlayer == null){
            playerList.add(playerInfo);
        }
        else{
            if (tempPlayer.parentId == null) {
                tempPlayer.parentId = playerInfo.parentId != null ? playerInfo.parentId : null;
            }
            tempPlayer.childId.addAll(playerInfo.childId);
        }

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
