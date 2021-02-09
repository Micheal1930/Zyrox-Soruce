package com.zyrox.net.packet.command.impl;

import com.zyrox.Donation;
import com.zyrox.FoxVote;
import com.zyrox.model.GameMode;
import com.zyrox.model.PlayerRights;
import com.zyrox.net.packet.command.Command;
import com.zyrox.net.packet.command.CommandHeader;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.dialogue.Dialogue;
import com.zyrox.world.content.dialogue.DialogueManager;
import com.zyrox.world.content.dialogue.DialogueType;
import com.zyrox.world.entity.impl.player.Player;

@CommandHeader(command = { "claim", "recieve" }, description = "Checks for donations.")
public class ClaimCommand extends Command {

	@Override
	public boolean execute(Player player, String[] args) throws Exception {

		DialogueManager.start(player, new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public String[] dialogue() {
				return new String[] {
						"Check for donations",
						"Check for vote reward",
				};
			}

			@Override
			public boolean action(int option) {
				if(!player.sqlCheckTimer.elapsed(10_000)) {
					DialogueManager.sendStatement(player, "@red@You can only perform a website request every 10 seconds.");
					return true;
				}
				switch(option) {
					case 1:
						player.sqlCheckTimer.reset();

						DialogueManager.sendStatement(player, "Checking for donations...");

						new Thread(new Donation(player)).start();
						
						break;
					case 2:
						if(!player.getInventory().hasEmptySlot()) {
							DialogueManager.sendStatement(player, "@red@You must have a free inventory slot to do this.");
							return true;
						}

						player.sqlCheckTimer.reset();

						DialogueManager.sendStatement(player, "Checking for vote rewards...");
						new Thread(new FoxVote(player)).start();
						/*String voteCheckQuery  = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.LOGS_VOTED)+" WHERE vote_status = 0 AND username = '"+player.getName()+"' ORDER BY id";
						GameServer.getSqlNetwork().submit(connection -> {
							try {
								try(PreparedStatement statement = connection.prepareStatement(voteCheckQuery)) {
									try (ResultSet results = statement.executeQuery()){

										boolean claimedAnything = false;

										while (results.next()) {

											int id = results.getInt("id");
											String toplist = results.getString("toplist");

											claimVote(player, id, toplist);

											claimedAnything = true;

										}

										if(claimedAnything) {
											DialogueManager.sendStatement(player, "Thank you for voting.");
										} else {
											DialogueManager.sendStatement(player, "No votes have been found.");
										}

									} catch (Exception ex) {
										//Misc.print("No referral codes to retrieve.");
									}
								}
							} catch (SQLException ex) {
								System.out.println(ex);
							}
						});*/
						break;
				}
				return true;
			}
		});


		return true;
	}

	@Override
	public String[] getUsage() {
		return new String[] {
			"::claim", "::recieve"
		};
	}

	/*public static void claimDonations(Player player, int id, String cart) {
		String query = "UPDATE "+ SQLTable.getGameSchemaTable(SQLTable.STORE_TRANSACTIONS)+" SET status = 2 WHERE id = '"+id+"'";
		GameServer.getSqlNetwork().submit(connection -> {
			try {
				try(PreparedStatement statement = connection.prepareStatement(query)) {
					int status = statement.executeUpdate();

					if(status == 0) {
						return;
					}

					String[] itemList = cart.split("],");

					for(String itemData : itemList) {

						String[] itemDataSplit = itemData.split("\",\"");

						try {
							String itemRaw = itemDataSplit[0].replaceAll("[^a-zA-Z0-9]", "");

							int amount = Integer.parseInt(itemDataSplit[1].replaceAll("[^a-zA-Z0-9]", ""));
							double price = Integer.parseInt(itemDataSplit[3].replaceAll("[^a-zA-Z0-9]", ""));

							String itemName;

							int itemId = -1;

							boolean couldntAdd = false;

							if(itemRaw.toLowerCase().contains("ankou_set")) {
								player.getBank(player.getCurrentBankTab()).add(new Item(50095, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50098, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50101, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50104, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50107, amount));
								itemName = "Ankou set";
							} else if(itemRaw.toLowerCase().contains("prereleasepete")) {
								player.getBank(player.getCurrentBankTab()).add(new Item(14534, amount));
								itemName = "Pre-Release Pete";
							} else if(itemRaw.toLowerCase().contains("antisanta")) {
								player.getBank(player.getCurrentBankTab()).add(new Item(42892, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(42893, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(42894, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(42895, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(42896, amount));
								itemName = "Anti-Santa set";
							} else if(itemRaw.toLowerCase().contains("bansheeset")) {
								player.getBank(player.getCurrentBankTab()).add(new Item(50773, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50775, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50777, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50779, amount));
								itemName = "Banshee set";
							} else if(itemRaw.toLowerCase().contains("evilchicken")) {
								player.getBank(player.getCurrentBankTab()).add(new Item(50433, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50436, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50439, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(50442, amount));
								itemName = "Evil chicken set";
							} else if(itemRaw.toLowerCase().contains("royalset")) {
								player.getBank(player.getCurrentBankTab()).add(new Item(15509, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(15507, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(15503, amount));
								player.getBank(player.getCurrentBankTab()).add(new Item(15505, amount));
								itemName = "Royal set";
							} else {
								itemId = Integer.parseInt(itemRaw);
								itemName = ItemDefinition.forId(itemId).getName();

								WebsiteStoreItem websiteStoreItem = WebsiteStoreManager.WEBSITE_ITEMS.getOrDefault(itemId, null);
								if(websiteStoreItem != null) {
									if(!websiteStoreItem.isIronman() && player.isIronman()) {
										player.sendMessage("<col=ff0000>You couldn't claim "+itemName+" because it is not an ironman item.");
										player.sendMessage("<col=ff0000>Please contact staff through ::support.");
										couldntAdd = true;
									}
								}

								if(!couldntAdd) {
									player.getBank(player.getCurrentBankTab()).add(new Item(itemId, amount));
								}
							}

							if(!couldntAdd) {
								player.sendMessage("<img=678> <shad=786518>" + amount + "x " + itemName + " has been added to your bank.");
							}

							player.incrementAmountDonated((int) Math.round(price));

							checkForRankUpdate(player);
							PlayerPanel.refreshPanel(player);

							new StoreClaimLog(player.getName(), itemName, itemId, amount, price, Misc.getTime()).submit();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (SQLException ex) {
				System.out.println(ex);
			}
		});
	}*/

	/*public static void claimVote(Player player, int id, String toplist) {
		String query = "UPDATE "+ SQLTable.getGameSchemaTable(SQLTable.LOGS_VOTED)+" SET vote_status = 1 WHERE id = '"+id+"'";
		GameServer.getSqlNetwork().submit(connection -> {
			try {
				try(PreparedStatement statement = connection.prepareStatement(query)) {
					int status = statement.executeUpdate();

					if(status == 0) {
						return;
					}

					player.getInventory().add(13077, 1);
					player.sendMessage("<img=678> <shad=786518>Thank you for voting on "+toplist+"!");
					player.sendMessage("<img=678> <shad=786518>A voting casket has been added to your inventory.");

				}
			} catch (SQLException ex) {
				System.out.println(ex);
			}
		});
	}*/

	public static void checkForRankUpdate(Player player) {
		if (player.getRights().isStaff()) {
			return;
		}
		if (player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN || player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
			player.getPacketSender().sendMessage("@red@You did not receive donator rank because you are an iron man!");
			return;
		}

		PlayerRights rights = PlayerRights.getDonatorRights(player);

		if (rights != null && rights != player.getRights()) {
			player.getPacketSender().sendMessage("You've become a " + Misc.formatText(rights.toString().toLowerCase()) + "! Congratulations!");
			player.setRights(rights);
			player.getPacketSender().sendRights();
			player.save();
			World.sendMessage("<img=678> <shad=d4af37>"+player.getUsername() + " has upgraded their donator rank to " +
					rights.getYellPrefix() + rights.toString() + "</col></shad>!");

		}

	}

}
