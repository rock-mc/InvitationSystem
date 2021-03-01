package com.rock_mc.invitation_system;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.io.IOException;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        Log.logger = getLogger();

        Log.server("Enable", "Active");

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getCommand("invits").setExecutor(new PlayerCommand());

        InvitationSystem.init(this);

        Log.server("Enable", "Complete");
    }

    public static void main(String argv[]) throws IOException {
//        System.out.println("sdsdf");

//        PlayerInfo p = new PlayerInfo();
//        p.name = "test_name";
//        p.uid = "xxxxxxxx";
//
//        PlayerInfo p1 = new PlayerInfo();
//        p1.name = "test_name1";
//        p1.uid = "xxxxxxxx1";
//
//        PlayerInfo p2 = new PlayerInfo();
//        p2.name = "test_name2";
//        p2.uid = "xxxxxxxx2";
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(gson.toJson(p));
//
//        PlayerList playerList = new PlayerList();
//        playerList.add(p.uid);
//        playerList.add(p1.uid);
//        playerList.add(p2.uid);
//        System.out.println(gson.toJson(playerList));
//
//        PlayerChain c = new PlayerChain("sdfsdfsdf");
//        c.add(p);
//        c.add(p1);
//        c.add(p2);
//
//        System.out.println(gson.toJson(c));

    }
}
