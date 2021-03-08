package com.rock_mc.invitation_system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class Util {

    static int SEC = 1;
    static int MIN = 60 * SEC;
    static int HOUR = 60 * MIN;
    static int DAY = 24 * HOUR;

    static void mkdir(String foler_path) {
        File directory = new File(foler_path);
        if (directory.exists()) {
            return;
        }
        directory.mkdirs();
    }

    static void writeFile(String file_path, String data) throws IOException {
        File file = new File(file_path);
        // creates the file
        file.createNewFile();
        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);
        // Writes the content to the file
        writer.write(data);
        writer.flush();
        writer.close();
    }

    static String genUUID() {
//        String uuid = UUID. randomUUID().toString().replace("-", "");
//        return uuid;

        String result = "";

        Random r = new Random();

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < InvitSys.INVIT_CODE_LENGTH; i++) {
//            System.out.println(alphabet.charAt(r.nextInt(alphabet.length())));

            result += alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return result;
    }
    static String timeToStr(long day, long hour, long min, long sec){
        String result = "";
        if(day != 0){
            result += " " + day + " 天";
        }
        if(hour != 0){
            result += " " + hour + " 小時";
        }
        if(min != 0){
            result += " " + min + " 分鐘";
        }
        if(sec != 0){
            result += " " + sec + " 秒";
        }
        return result;
    }
}