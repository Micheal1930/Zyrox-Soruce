package com.zyrox.dump.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.zyrox.model.definitions.ItemDefinition;
import com.zyrox.model.definitions.NPCDrops;
import com.zyrox.model.definitions.NpcDefinition;

/**
 * Created by Jonny on 8/30/2019
 **/
public class DropListDumper {

    /**
     * Dumps LootSystem into a .txt file.
     * @LootSystem
     */
    public static void dump() {
        try {
            File file = new File("dump/npc_drop_list.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();

            for (Map.Entry entry : NPCDrops.getDrops().entrySet()) {

                int npcId = (int)entry.getKey();

                NPCDrops drops = (NPCDrops)entry.getValue();

                NpcDefinition npcDef = NpcDefinition.getDefinitions()[npcId];

                if(npcId == 50) {
                    System.out.println("TEST!");
                }

                if(npcDef == null) {
                    bw.write("Npc: NULL! ["+npcId+"]");
                } else {
                    bw.write("Npc: " + npcDef.getName()+" ["+npcId+"]");
                }


                bw.newLine();
                for(NPCDrops.NpcDropItem drop : drops.getDropList()) {
                    if(drop != null) {

                        /*if (drop.getItem().getId() <= 0 || drop.getItem().getId() > ItemDefinition.getMaxAmountOfItems() || drop.getItem().getAmount() <= 0) {
                            continue;
                        }*/

                        int itemId = drop.getItem().getId();
                        int amount = drop.getItem().getAmount();

                        if(itemId == 995) {
                            if(amount >= 25_000_000) {
                                System.out.println(amount+" of coins dropped by "+npcId);
                            }
                        }

                        String chance = drop.getChance().toString();

                        bw.write("Item: " + ItemDefinition.getDefinitions()[itemId].getName() + " [" + itemId + "], Amount=" + amount + ", Chance=" + chance);
                        bw.newLine();
                    }
                }
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
