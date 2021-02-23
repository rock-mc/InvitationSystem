package com.rock_mc.invitation_system;

import org.bukkit.entity.Player;

public class InvitationSystem {

    public static final String APP_NAME = "InvitationSystem";
    public static final int NEW_QUOTA = 2;

    public static PlayerList whitelist;
    public static PlayerList blacklist;
    public static PlayerList all_list;
    static {

        Util.mkdir(APP_NAME);

        whitelist = new PlayerList(APP_NAME + "/whitelist.json");
        blacklist = new PlayerList(APP_NAME + "/blacklist.json");

        all_list = new PlayerList();
        all_list.extend(whitelist);
        all_list.extend(blacklist);
    }

    public static boolean add_whitelist(Player player){
        PlayerInfo playerInfo = new PlayerInfo(player);
        whitelist.add(playerInfo);
        return true;
    }

    public static boolean add_whitelist(Player player, String invitation_code){

        boolean result = false;

        PlayerInfo playerInfo = new PlayerInfo(player);

        PlayerInfo parent_info = all_list.find_code_owner(invitation_code);
        if (parent_info != null){
            whitelist.add(playerInfo, parent_info);
            result = true;
        }
        return result;
    }
}
