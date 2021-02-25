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

    public Prisoner getPrisoner(String playerUid){
        Prisoner result = null;
        for(Prisoner prisoner : playerList){
            if(prisoner.uid.equals(playerUid)){
                result = prisoner;
                break;
            }
        }
        return result;
    }

    public void add(String playerUid, int day) throws IOException {

        if(contains(playerUid)){
            Prisoner currentPrisoner = getPrisoner(playerUid);
            currentPrisoner.setExpiryTime(day);
        }
        else{
            Prisoner prisoner = new Prisoner(playerUid, day);
            playerList.add(prisoner);
        }
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

        Prisoner currentPrisoner = null;
        for(Prisoner prisoner : playerList){
            if(prisoner.uid.equals(playerUid)){
                currentPrisoner = prisoner;
                break;
            }
        }
        if (currentPrisoner == null){
            return;
        }

        playerList.remove(currentPrisoner);
    }

    public boolean contains(String playerUid) {
        return getPrisoner(playerUid) != null;
    }
}
