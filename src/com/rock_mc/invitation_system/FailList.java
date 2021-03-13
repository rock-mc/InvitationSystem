package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.UUID;

public class FailList {
    public HashSet<FailVerifyPlayer> playerList;
    private String filePath;

    public FailList(String loadFile) throws Exception {
        filePath = loadFile;

        Path p = Path.of(loadFile);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerList = new Gson().fromJson(fileString, new TypeToken<HashSet<FailVerifyPlayer>>(){}.getType());
        }
        else{
            playerList = new HashSet<>();
        }
    }

    public FailList() {
        filePath = null;
        playerList = new HashSet<>();
    }

    public FailVerifyPlayer getPlayer(UUID uuid){
        FailVerifyPlayer result = null;
        for(FailVerifyPlayer failVerifyPlayer : playerList){
            if(failVerifyPlayer.uuid.equals(uuid)){
                result = failVerifyPlayer;
                break;
            }
        }
        return result;
    }

    public void add(UUID uuid) throws IOException {

        FailVerifyPlayer currentPrisoner = getPlayer(uuid);
        if(currentPrisoner != null) {
            return;
        }
        currentPrisoner = new FailVerifyPlayer(uuid);
        playerList.add(currentPrisoner);
        save();
    }

    public void save() throws IOException {
        if (filePath == null) {
            return;
        }
        Util.mkdir("plugins/" + InvitSys.APP_NAME);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json_str = gson.toJson(playerList);

        Util.writeFile(filePath, json_str);
    }

    public void remove(UUID uuid) throws IOException {

        FailVerifyPlayer currentPrisoner = null;
        for(FailVerifyPlayer failVerifyPlayer : playerList){
            if(failVerifyPlayer.uuid.equals(uuid)){
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

    public boolean contains(UUID uuid) {
        return getPlayer(uuid) != null;
    }
}
