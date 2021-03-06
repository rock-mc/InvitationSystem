package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FailList {
    public ArrayList<FailVerifyPlayer> playerList;
    private String filePath;

    public FailList(String loadFile) throws Exception {
        filePath = loadFile;

        Path p = Path.of(loadFile);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerList = new Gson().fromJson(fileString, new TypeToken<List<FailVerifyPlayer>>(){}.getType());
        }
        else{
            playerList = new ArrayList<>();
        }
    }

    public FailList() {
        filePath = null;
        playerList = new ArrayList<>();
    }

    public FailVerifyPlayer getFailVerifyPlayer(String playerUid){
        FailVerifyPlayer result = null;
        for(FailVerifyPlayer failVerifyPlayer : playerList){
            if(failVerifyPlayer.uid.equals(playerUid)){
                result = failVerifyPlayer;
                break;
            }
        }
        return result;
    }

    public void add(String playerUid) throws IOException {

        FailVerifyPlayer currentPrisoner = getFailVerifyPlayer(playerUid);
        if(currentPrisoner != null) {
            return;
        }
        currentPrisoner = new FailVerifyPlayer(playerUid);
        playerList.add(currentPrisoner);
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

    public void remove(String playerUid) throws IOException {

        FailVerifyPlayer currentPrisoner = null;
        for(FailVerifyPlayer failVerifyPlayer : playerList){
            if(failVerifyPlayer.uid.equals(playerUid)){
                currentPrisoner = failVerifyPlayer;
                break;
            }
        }
        if (currentPrisoner == null){
            return;
        }

        playerList.remove(currentPrisoner);
        save();
    }

    public boolean contains(String playerUid) {
        return getFailVerifyPlayer(playerUid) != null;
    }
}
