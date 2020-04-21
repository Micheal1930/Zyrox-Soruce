package com.varrock.world.content.social;

import java.util.ArrayList;

import com.varrock.model.Item;

/**
 * Created by Jonny on 10/21/2019
 **/
public class SocialCode {

    private final Item reward;
    private final int usesAllowed;

    private final ArrayList<String> usedUsernames = new ArrayList<>();
    private final ArrayList<String> usedIpAddresses = new ArrayList<>();
    private final ArrayList<String> usedSerialAddresses = new ArrayList<>();

    public SocialCode(Item reward, int usesAllowed) {
        this.reward = reward;
        this.usesAllowed = usesAllowed;
    }


    public Item getReward() {
        return reward;
    }

    public ArrayList<String> getUsedUsernames() {
        return usedUsernames;
    }

    public ArrayList<String> getUsedIpAddresses() {
        return usedIpAddresses;
    }

    public ArrayList<String> getUsedSerialAddresses() {
        return usedSerialAddresses;
    }

    public int getUsesAllowed() {
        return usesAllowed;
    }
}
