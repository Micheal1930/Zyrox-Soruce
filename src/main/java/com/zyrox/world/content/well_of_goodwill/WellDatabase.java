package com.zyrox.world.content.well_of_goodwill;

import com.google.common.collect.Multiset;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class WellDatabase {

    private final Path wellDatabase = Paths.get("./data/saves/wellofgoodwill.txt");
    private final WellOfGoodwill well;

    public WellDatabase(WellOfGoodwill well) {
        this.well = well;
    }

    void load() {
        if (Files.exists(wellDatabase)) {
            try (Scanner scanner = new Scanner(wellDatabase)) {
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine == null || nextLine.isEmpty()) {
                        continue;
                    }
                    String[] token = scanner.nextLine().split(":");
                    String username = token[0];
                    int donated = Integer.parseInt(token[1]);
                    System.out.println(username+", "+donated);
                    well.playersDonated.add(username, donated);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void delete() {
        try {
            if (Files.exists(wellDatabase)) {
                Files.delete(wellDatabase);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void saveOne(String username, int amount) {
        try {
            createFileIfNeeded();
            File wellDatabaseFile = wellDatabase.toFile();
            try (BufferedWriter out = new BufferedWriter(new FileWriter(wellDatabaseFile, true))) {
                out.newLine();
                out.write(username);
                out.write(":");
                out.write(Integer.toString(amount));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void save() {
        try {
            createFileIfNeeded();

            File wellDatabaseFile = wellDatabase.toFile();
            try (BufferedWriter out = new BufferedWriter(new FileWriter(wellDatabaseFile))) {
                out.newLine();
                for (Multiset.Entry<String> entry : well.playersDonated.entrySet()) {
                    out.write(entry.getElement());
                    out.write(":");
                    out.write(Integer.toString(entry.getCount()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFileIfNeeded() throws IOException {
        if (!Files.exists(wellDatabase)) {
            Files.createFile(wellDatabase);
        }
    }
}