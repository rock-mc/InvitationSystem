package com.rock_mc.invitation_system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Util {
    static void mkdir(String foler_path){
        File directory = new File(foler_path);
        if (directory.exists()){
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
}