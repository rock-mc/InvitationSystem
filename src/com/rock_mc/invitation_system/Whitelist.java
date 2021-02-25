package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Whitelist {
    public ArrayList<String> playerList;
    private String filePath;

    public Whitelist(String loadFile) throws Exception {
        filePath = loadFile;

        Path p = Path.of(loadFile);
        if(p.toFile().exists()) {
            String fileString = Files.readString(p);
            playerList = new Gson().fromJson(fileString, ArrayList.class);
        }
        else{
            playerList = new ArrayList<>();
        }
    }

    public Whitelist() {
        filePath = null;
        playerList = new ArrayList<>();
    }

    public void add(String player_uid) throws IOException {
        playerList.add(player_uid);
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

    public void remove(String player_uid) {
        playerList.remove(player_uid);
    }

    public boolean contains(String player_uid) {
        return playerList.contains(player_uid);
    }
}
