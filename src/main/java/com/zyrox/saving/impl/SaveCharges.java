package com.zyrox.saving.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zyrox.model.item.ItemCharge;
import com.zyrox.saving.SaveObject;
import com.zyrox.world.entity.impl.player.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class SaveCharges extends SaveObject {

    private static final String endOfFile = "chargeend";

    private GsonBuilder builder;
    private Gson gson;

    public SaveCharges(String name) {
        super(name);
        this.builder = new GsonBuilder();
        this.gson = builder.create();
    }

    @Override
    public boolean save(Player player, BufferedWriter writer) throws IOException {

        if (player.getItemCharges().isEmpty()) {
            return true;
        }

        writer.write(getName());
        writer.newLine();
        for (int chargeKey : player.getItemCharges().keySet()) {
            String build = chargeKey + "=";

            ItemCharge charge = player.getItemCharges().get(chargeKey);

            if (charge != null) {
                for (Integer key : charge.getCharges().keySet()) {
                    build += key + ":" + charge.getCharges(key) + ",";
                }
                writer.write(build);
                writer.newLine();
                writer.write(endOfFile);
                writer.newLine();
            }
        }
        return true;
    }

    @Override
    public void load(Player player, String values, BufferedReader reader) throws IOException {
        String readerStr;
        try {
            while (!(readerStr = reader.readLine()).equalsIgnoreCase(endOfFile)) {

                String line = readerStr;

                int chargeKey = Integer.parseInt(line.substring(0, line.indexOf("=")));

                line = line.substring(line.indexOf("=") + 1);

                ItemCharge charge = new ItemCharge();

                while (line.contains(":")) {
                    int key = Integer.parseInt(line.substring(0, line.indexOf(":")));
                    int value = Integer.parseInt(line.substring(line.indexOf(":") + 1, line.indexOf(",")));
                    charge.setCharges(key, value);
                    line = line.substring(line.indexOf(",") + 1);
                }

                player.getItemCharges().put(chargeKey, charge);
            }
        } catch (Exception e) {
            System.out.println("Error while loading " + player.getName() + "'s item charges.");
            e.printStackTrace();
        }

    }
}
