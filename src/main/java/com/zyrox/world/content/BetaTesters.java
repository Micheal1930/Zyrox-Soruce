package com.zyrox.world.content;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Loads accounts permitted to login to Beta Server
 * @Jonny
 */
public class BetaTesters {

    public static ArrayList<String> BETA_TESTERS = new ArrayList<>();

    public static void load() {
        BETA_TESTERS.clear();
        String[] args;
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("./data/beta_testers.txt")));
            while ((line = reader.readLine()) != null) {
                if (line.contains("beta_tester: ")) {
                    args = line.split(": ");
                    if (args.length > 1) {
                        BETA_TESTERS.add(args[1].toLowerCase());
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a tester to the beta list
     * @param tester
     */
    public static void addBetaTester(String tester) {
        BETA_TESTERS.add(tester.toLowerCase());
        try {
            FileWriter fw = new FileWriter("./data/beta_testers.txt", true);
            if (fw != null) {
                fw.write(System.lineSeparator());
                fw.write("beta_tester: "+tester.toLowerCase());
                fw.write(System.lineSeparator());
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a tester to the beta list
     * @param tester
     */
    public static void removeBetaTester(String tester) {
        BETA_TESTERS.removeAll(Collections.singleton(tester.toLowerCase()));
        try {
            File f = new File("./data/beta_testers.txt");
            if(f.exists()){
                f.delete();
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileWriter fw = new FileWriter("./data/beta_testers.txt", true);
            if (fw != null) {

                for(String testerName : BETA_TESTERS) {
                    fw.write(System.lineSeparator());
                    fw.write("beta_tester: " + testerName.toLowerCase());
                    fw.write(System.lineSeparator());
                }

                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isTester(String username) {
        return BETA_TESTERS.contains(username.toLowerCase());
    }
}
