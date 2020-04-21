package com.varrock.commands;


import java.util.HashMap;

import com.varrock.model.Password;
import com.varrock.model.PlayerRights;
import com.varrock.world.entity.impl.player.Player;

/**
 * @author Jack Daniels.
 */

public class CommandHandler {

    /**
     * HashMap to hold all the commands.
     */
    private static HashMap<String, Command> commands = new HashMap<String, Command>();

    /**
     * Returns the list of commands.
     *
     * @return list of commands
     */
    public static HashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * Use this method to add commands to the server.
     *
     * @param key
     * @param command
     */
    public static void submit(Command... cmds) {

        for (Command cmd : cmds) {
            //System.out.println("Submitting command: " + cmd.getKey());
            commands.put(cmd.getKey(), cmd);
        }
    }

    /**
     * Use this method to check whether your command input has been processed.
     *
     * @param key
     * @param player
     * @param input
     * @returns true if the command was found in the commands hashmap and had
     * the rights to execute.
     */
    public static boolean processed(String key, Player player, String input) {
        Command command = commands.get(key);
        if (command != null) {
            String requiredRank = null;
            switch (command.getRights()) {
                case Command.DONATOR_RIGHTS:
                    if (player.getRights().getDonatorPrivilegelLevel() < PlayerRights.DONATOR.getDonatorPrivilegelLevel()) {
                        requiredRank = PlayerRights.DONATOR.toString();
                    }
                    break;
                case Command.HELPER_RIGHTS:
                    if (player.getRights().getPrivilegeLevel() < PlayerRights.SUPPORT.getPrivilegeLevel()) {
                        requiredRank = PlayerRights.SUPPORT.toString();
                    }
                    break;
                case Command.MOD_RIGHTS:
                    if (player.getRights().getPrivilegeLevel() < PlayerRights.MODERATOR.getPrivilegeLevel()) {
                        requiredRank = PlayerRights.MODERATOR.toString();
                    }
                    break;
                case Command.ADMIN_RIGHTS:
                    if (player.getRights().getPrivilegeLevel() < PlayerRights.ADMINISTRATOR.getPrivilegeLevel()) {
                        requiredRank = "Administrator";
                    }
                    break;
                case Command.OWNER_RIGHTS:
                    if (player.getRights().getPrivilegeLevel() < PlayerRights.OWNER.getPrivilegeLevel()) {
                        requiredRank = "Owner";
                    }
                    break;
            }
            if (requiredRank != null) {
                if (command.getRights() != Command.OWNER_RIGHTS) {
                    player.sendMessage("You have to be a " + requiredRank + " to use this command. Your rank is:" + player.getRights());
                }
                return false;
            }
            return true;
        }

        return false;
    }

    /**
     * Store all commands here.
     */
    public static void init() {
        CommandHandler.submit(new Command("enc", Command.OWNER_RIGHTS) {

            @Override
            public boolean execute(Player player, String input) throws Exception {
                player.sendMessage("Salt: " + player.getPasswordNew().getSalt());
                player.sendMessage("Enc:" + player.getPasswordNew().getEncryptedPass());
                if (player.getPasswordNew().getEncryptedPass() == null || player.getPasswordNew().getEncryptedPass().equals("null")) {
                    String encrypted = Password.encryptPassword(player.getPasswordNew().getRealPassword(), player.getPasswordNew().getSalt());
                    player.sendMessage("New pass: " + encrypted);
                    player.getPasswordNew().setEncryptedPass(encrypted);
                } else {
                    player.sendMessage("Not null password:" + player.getPasswordNew().getEncryptedPass());
                }
                return false;
            }

        });
    }
}