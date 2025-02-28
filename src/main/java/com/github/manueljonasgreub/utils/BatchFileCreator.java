package com.github.manueljonasgreub.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BatchFileCreator {

    public static void createBatchFileIfNotExists() {
        File batchFile = new File("restart.bat");
        if (!batchFile.exists()) {
            try {
                File dir = new File(".");
                File[] files = dir.listFiles((d, name) -> name.endsWith(".jar"));
                if (files != null && files.length > 0) {
                    String jarFileName = files[0].getName();

                    // Create the batch file with the appropriate content
                    try (FileWriter writer = new FileWriter(batchFile)) {
                        writer.write("@echo off\n");
                        writer.write("timeout /t 5\n");
                        writer.write("java -Xmx1024M -Xms1024M -jar " + jarFileName + " nogui\n");
                    }
                } else {
                    System.out.println("No .jar file found in the directory.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
