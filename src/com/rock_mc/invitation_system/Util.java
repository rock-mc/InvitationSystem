package com.rock_mc.invitation_system;

import java.io.File;

public class Util {
    static void mkdir(String foler_path){
        File directory = new File(foler_path);
        if (directory.exists()){
            return;
        }
        directory.mkdirs();
    }
}