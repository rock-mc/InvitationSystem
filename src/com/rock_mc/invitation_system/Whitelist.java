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
import java.util.Set;

public class Whitelist {
    public HashSet<String> playerList;
    private String filePath;

    public Whitelist(String loadFile) throws Exception {
        filePath = loadFile;

        Path p = Path.of(loadFile);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerList = new Gson().fromJson(fileString, new TypeToken<Set<String>>(){}.getType());
        }
        else{
            playerList = new HashSet<>();
        }
    }

    public Whitelist() {
        filePath = null;
        playerList = new HashSet<>();
    }

    public void add(String playerUid) throws IOException {
        if (playerList.contains(playerUid)){
            return;
        }
        playerList.add(playerUid);
        save();
    }

    private void save() throws IOException {
        if (filePath == null) {
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json_str = gson.toJson(playerList);

        Util.writeFile(filePath, json_str);
    }

    public void remove(String playerUid) {
        if (!playerList.contains(playerUid)){
            return;
        }
        playerList.remove(playerUid);
    }

    public boolean contains(String player_uid) {
        return playerList.contains(player_uid);
    }
}
