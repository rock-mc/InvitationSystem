package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Whitelist {
    public HashSet<UUID> playerList;
    private String filePath;

    public Whitelist(String loadFile) throws Exception {
        filePath = loadFile;

        Path p = Path.of(loadFile);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerList = new Gson().fromJson(fileString, new TypeToken<HashSet<UUID>>(){}.getType());
        }
        else{
            playerList = new HashSet<>();
        }
    }

    public Whitelist() {
        filePath = null;
        playerList = new HashSet<>();
    }

    public void add(UUID uuid) throws IOException {
        if (playerList.contains(uuid)){
            return;
        }
        playerList.add(uuid);
        save();
    }

    private void save() throws IOException {
        if (filePath == null) {
            return;
        }
        Util.mkdir("plugins/" + InvitSys.APP_NAME);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json_str = gson.toJson(playerList);

        Util.writeFile(filePath, json_str);
    }

    public void remove(UUID uuid) {
        if (!playerList.contains(uuid)){
            return;
        }
        playerList.remove(uuid);
    }

    public boolean contains(UUID player_uid) {
        return playerList.contains(player_uid);
    }
}
