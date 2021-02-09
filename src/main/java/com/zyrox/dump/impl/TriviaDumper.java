package com.zyrox.dump.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.zyrox.world.content.TriviaBot;

/**
 * Created by Jonny on 8/30/2019
 **/
public class TriviaDumper {

    /**
     * Dumps LootSystem into a .txt file.
     * @LootSystem
     */
    public static void dump() {
        try {
            File file = new File("dump/trivia_q&a.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();

            for(String[] trivia : TriviaBot.TRIVIA_DATA) {
                String question = trivia[0];
                String answer = trivia[1];

                bw.write("Question: "+question);
                bw.newLine();
                bw.write("Answer: "+answer);
                bw.newLine();
                bw.newLine();
            }

            write(bw, "");
            bw.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes to a BufferedWriter
     * @param bw
     * @param content
     */
    public static void write(BufferedWriter bw, String content) {
        try {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
