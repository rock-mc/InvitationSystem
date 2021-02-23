package com.rock_mc.invitation_system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Log.logger = getLogger();

        Log.server("Enable", "Active");

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getCommand("invits").setExecutor(new PlayerCommand());

        Log.server("Enable", "Complete");
    }

    public static void main(String argv[]) throws IOException {
//        System.out.println("sdsdf");

        PlayerInfo p = new PlayerInfo();
        p.name = "test_name";
        p.uid = "xxxxxxxx";

        PlayerInfo p1 = new PlayerInfo();
        p1.name = "test_name1";
        p1.uid = "xxxxxxxx1";

        PlayerInfo p2 = new PlayerInfo();
        p2.name = "test_name2";
        p2.uid = "xxxxxxxx2";

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String json = objectMapper.writeValueAsString(p);

        System.out.println(json);


        PlayerList playerList = new PlayerList();
        playerList.add(p);
        playerList.add(p1);
        playerList.add(p2);

        json = objectMapper.writeValueAsString(playerList);
        System.out.println(json);
    }
}
