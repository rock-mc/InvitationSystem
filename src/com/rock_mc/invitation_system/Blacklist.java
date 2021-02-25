package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Blacklist {
    public ArrayList<Prisoner> playerList;
    private String filePath;

    public Blacklist(String loadFile) throws Exception {
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

    public Blacklist() {
        filePath = null;
        playerList = new ArrayList<>();
    }

    public void add(String player_uid, int day) throws IOException {

        Prisoner prisoner = new Prisoner(player_uid, day);

        playerList.add(prisoner);
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

        Prisoner current_prisoner = null;
        for(Prisoner prisoner : playerList){
            if(prisoner.uid.equals(player_uid)){
                current_prisoner = prisoner;
                break;
            }
        }
        if (current_prisoner == null){
            return;
        }

        playerList.remove(player_uid);
    }

    public boolean contains(String player_uid) {

        for(Prisoner prisoner : playerList){
            if(prisoner.uid.equals(player_uid)){
                return true;
            }
        }

        return false;
    }
}
