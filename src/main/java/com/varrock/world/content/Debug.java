package com.varrock.world.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Log stuff to debug folders
 * @author Jonny
 */
public class Debug {

    public static boolean ENABLED = false;

    public static void write(String name, String title, String[] actions) {
        if(!ENABLED) {
            return;
        }
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtfTime.format(now);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate).replaceAll("/", "-");

        if(!new File("./data/debug/"+date).exists()) {
            new File("./data/debug/"+date).mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./data/debug/"+date+"/"+name+".txt", true))) {

            writer.write("------------------");
            writer.newLine();
            writer.write("[" + time + "] " + title);
            writer.newLine();

            for(String action : actions) {
                writer.write(action);
                writer.newLine();
            }

            writer.newLine();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
