package com.varrock.world.content.donation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.varrock.model.Item;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The donation deals.
 *
 * @author Gabriel || Wolfsdarker
 */
public class DonationDeal {

    /**
     * The deals and their items.
     */
    private static Map<String, List<Item>> deals = new HashMap<>();

    /**
     * Returns the items that belong to that deal price.
     *
     * @param dealName
     * @return the items
     */
    public static List<Item> get(String dealName) {
        return deals.getOrDefault(dealName, Collections.emptyList());
    }

    /**
     * Returns the current deals.
     *
     * @return the deals
     */
    public static Map<String, List<Item>> getDeals() {
        return deals;
    }

    /**
     * Adds a item to the deal.
     *
     * @param dealName
     * @param toAdd
     * @return the message to be sent to the player
     */
    public static String add(String dealName, Item toAdd) {
        List<Item> items = get(dealName);

        if (items.isEmpty()) {
            items = new ArrayList<>();
        }

        for (Item item : items) {
            if (item.getId() == toAdd.getId()) {
                return "This item is already added to this deal.";
            }
        }
        items.add(toAdd);
        deals.put(dealName, items);
        save();
        return "";
    }

    /**
     * Adds a item to the deal.
     *
     * @param dealName
     * @param toRemove
     * @return the message to be sent to the player
     */
    public static String remove(String dealName, int toRemove) {
        List<Item> items = get(dealName);

        if (items.isEmpty()) {
            return "Item not found for that deal.";
        }

        List<Item> newItems = new ArrayList<>(items);

        for (Item item : items) {
            if (item.getId() == toRemove) {
                newItems.remove(item);
                break;
            }
        }

        if(newItems.isEmpty()) {
            deals.remove(dealName);
        } else {
            deals.put(dealName, newItems);
        }
        save();
        return "";
    }

    private static final String DIR = "./data/saves/donation_deals.json";

    /**
     * Saves all the deals.
     */
    public static void save() {
        final Gson format = new GsonBuilder().setPrettyPrinting().create();
        try (final BufferedWriter composer = new BufferedWriter(new FileWriter(DIR))) {
            composer.write(format.toJson(deals));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Loads all the deals.
     */
    public static void load() {
        final Gson gson = new Gson();
        try {
            deals = gson.fromJson(new FileReader(DIR), new TypeToken<Map<Integer, List<Item>>>() {
            }.getType());
        } catch (IOException exception) {
            deals = new HashMap<>();
        }
    }

}
