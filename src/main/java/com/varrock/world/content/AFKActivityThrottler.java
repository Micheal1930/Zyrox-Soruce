package com.varrock.world.content;

import com.google.common.collect.ImmutableSet;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.entity.impl.player.Player;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class AFKActivityThrottler {

    public enum AFKActivity {
        HERBS,
        FISH,
        LOGS;
        public static final ImmutableSet<AFKActivity> ALL = ImmutableSet.copyOf(values());
    }

    private static final class NotAllowedDialogue extends Dialogue {

        @Override
        public DialogueType type() {
            return DialogueType.STATEMENT;
        }

        @Override
        public DialogueExpression animation() {
            return DialogueExpression.NORMAL;
        }

        @Override
        public String[] dialogue() {
            return new String[]{"There is a limit of 1 account for AFK money making methods."};
        }
    }


    private static final EnumMap<AFKActivity, Map<String, Player>> afkMap = new EnumMap<>(AFKActivity.class);

    static {
        for (AFKActivity activity : AFKActivity.ALL) {
            afkMap.put(activity, new HashMap<>());
        }
    }


    public static boolean registerAfk(Player player, AFKActivity activity) {
        Player other = afkMap(activity).putIfAbsent(player.getHostAddress(), player);
        if (other == null || other.getUsername().equals(player.getUsername())) {
            return false;
        }
        sendNotAllowed(player);
        return true;
    }

    public static void unregisterAfk(Player player) {
        for (Map<String, Player> map : afkMap.values()) {
            map.computeIfPresent(player.getHostAddress(), (k, v) -> {
                if (v.getCurrentTask() == null) {
                    return null;
                }
                return v;
            });
        }
    }

    public static void sendNotAllowed(Player player) {
        DialogueManager.start(player, new NotAllowedDialogue());
    }

    public static boolean isAfking(Player player, AFKActivity activity) {
        return afkMap(activity).containsKey(player.getHostAddress());
    }

    public static Map<String, Player> afkMap(AFKActivity activity) {
        return afkMap.get(activity);
    }
}