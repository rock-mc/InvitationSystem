package com.rock_mc.invitation_system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class Util {

    static final int SEC = 1;
    static final int MIN = 60 * SEC;
    static final int HOUR = 60 * MIN;
    static final int DAY = 24 * HOUR;

    static void mkdir(String folderPath) {
        File directory = new File(folderPath);
        if (directory.exists()) {
            return;
        }
        directory.mkdirs();
    }

    static void writeFile(String filePath, String data) throws IOException {
        File file = new File(filePath);
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

        String alphabet = "0123456789";
        for (int i = 0; i < InvitSys.INVIT_CODE_LENGTH; i++) {
            result += alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return result;
    }
    static String timeToStr(long expiryTime){
        long day = expiryTime / Util.DAY;
        expiryTime %= Util.DAY;
        long hour = expiryTime / Util.HOUR;
        expiryTime %= Util.HOUR;
        long min = expiryTime / Util.MIN;
        expiryTime %= Util.MIN;
        long sec = expiryTime;

        return timeToStr(day, hour, min, sec);
    }
    static String timeToStr(long day, long hour, long min, long sec){
        String result = null;
        if(day > 0){
            result = day + " 天";
        }
        if(hour > 0){
            if (result == null){
                result = hour + " 小時";
            }
            else {
                result += " " + hour + " 小時";
            }
        }
        if(min > 0){
            if(result == null){
                result = min + " 分鐘";
            }
            else{
                result += " " + min + " 分鐘";
            }
        }
        if(sec > 0){
            if (result == null){
                result = sec + " 秒";
            }
            else{
                result += " " + sec + " 秒";
            }
        }
        if (result == null){
            result = "∞";
        }
        return result;
    }
}