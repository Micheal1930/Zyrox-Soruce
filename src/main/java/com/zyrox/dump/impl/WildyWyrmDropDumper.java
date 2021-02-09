package com.zyrox.dump.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.world.content.Wildywyrm;

/**
 * Created by Jonny on 8/30/2019
 **/
public class WildyWyrmDropDumper {

    /**
     * Dumps LootSystem into a .txt file.
     * @LootSystem
     */
    public static void dump() {
        try {
            File file = new File("dump/wildy_wyrm_drops.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();

            for(int dropId : Wildywyrm.COMMONLOOT) {
                write(bw, "common: "+ItemDefinition.getDefinitions()[dropId].getName()+" - ["+dropId+"]");
               // bw.newLine();
            }

            for(int dropId : Wildywyrm.MEDIUMLOOT) {
                write(bw, "medium: "+ItemDefinition.getDefinitions()[dropId].getName()+" - ["+dropId+"]");
                //bw.newLine();
            }

            for(int dropId : Wildywyrm.RARELOOT) {
                write(bw, "rare: "+ItemDefinition.getDefinitions()[dropId].getName()+" - ["+dropId+"]");
                //bw.newLine();
            }

            for(int dropId : Wildywyrm.SUPERRARELOOT) {
                write(bw, "super rare: "+ItemDefinition.getDefinitions()[dropId].getName()+" - ["+dropId+"]");
                //bw.newLine();
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
