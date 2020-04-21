package com.varrock.net.packet.command.impl;

import org.apache.commons.lang.RandomStringUtils;

import com.varrock.GameServer;
import com.varrock.model.Item;
import com.varrock.model.definitions.ItemDefinition;
import com.varrock.model.input.Input;
import com.varrock.model.log.impl.StoreClaimLog;
import com.varrock.net.packet.command.Command;
import com.varrock.net.packet.command.CommandHeader;
import com.varrock.net.sql.SQLTable;
import com.varrock.util.Misc;
import com.varrock.world.content.PlayerPanel;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.content.introduction.IntroductionAutomation;
import com.varrock.world.content.introduction.IntroductionRedeemAutomation;
import com.varrock.world.entity.impl.player.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@CommandHeader(command = { "wallet" }, description = "Open your wallet.")
public class WalletCommand extends Command {

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
						"Check wallet",
						"Create wallet code",
						"Destroy wallet code",
						"View my wallet codes"
				};
			}

			@Override
			public boolean action(int option) {
				switch(option) {
					case 1:
						DialogueManager.sendStatement(player, "@mag@You have $"+player.wallet+" in your wallet.");
						break;
					case 2:
						player.setInputHandling(new Input() {
							@Override
							public boolean handleAmount(Player player, int amount) {
								if(amount > player.wallet) {
									DialogueManager.sendStatement(player, "@red@You don't have this much in your wallet.");
									return true;
								}

								player.wallet -= amount;

								String walletCode = RandomStringUtils.randomAlphanumeric(8);
								player.sendMessage("<shad=ff0000>You have created the wallet code "+walletCode+" and added $"+amount+" to it.");
								player.sendMessage("<shad=ff0000>Now enter this wallet code as your username at checkout (::donate) to spend it.");

								DialogueManager.sendStatement(player, "@mag@Created wallet code "+walletCode+" with $"+amount+" in it.");

								String query = "INSERT INTO "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_WALLET_CODES)+" (username, wallet_code, amount) VALUES (?, ?, ?)";
								GameServer.getSqlNetwork().submit(connection -> {
									try (PreparedStatement statement = connection.prepareStatement(query)) {
										//insert
										statement.setString(1, player.getUsername());
										statement.setString(2, walletCode);
										statement.setInt(3, amount);

										statement.executeUpdate();
									}
								});

								return true;
							}
						});
						player.getPA().sendEnterAmountPrompt("Enter amount to add to your wallet code:");
						break;
					case 3:
						player.setInputHandling(new Input() {
							@Override
							public void handleSyntax(Player player, String code) {

								DialogueManager.sendStatement(player, "Searching for wallet code...");

								String storeCheckQuery  = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_WALLET_CODES)+" WHERE wallet_code = '"+code+"' AND username = '"+player.getName()+"'";
								GameServer.getSqlNetwork().submit(connection -> {
									try {
										try(PreparedStatement statement = connection.prepareStatement(storeCheckQuery)) {
											try (ResultSet results = statement.executeQuery()){

												while (results.next()) {
													int walletId = results.getInt("id");
													int amountLeft = results.getInt("amount");

													deleteWalletCode(player, walletId, amountLeft);
													return;
												}
												DialogueManager.sendStatement(player, "@red@Wallet code not found.");
											} catch (Exception ex) {
												//Misc.print("No referral codes to retrieve.");
											}
										}
									} catch (SQLException ex) {
										System.out.println(ex);
									}
								});
							}
						});
						player.getPA().sendEnterInputPrompt("Enter code to destroy:");
						break;
					case 4:
						DialogueManager.sendStatement(player, "Searching for wallet codes...");

						String storeCheckQuery  = "SELECT * FROM "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_WALLET_CODES)+" WHERE username = '"+player.getName()+"'";
						GameServer.getSqlNetwork().submit(connection -> {
							try {
								try(PreparedStatement statement = connection.prepareStatement(storeCheckQuery)) {
									try (ResultSet results = statement.executeQuery()){

										boolean found = false;

										while (results.next()) {
											String walletCode = results.getString("wallet_code");
											int amountLeft = results.getInt("amount");

											player.sendMessage("<shad=ff0000>Found wallet code "+walletCode+" with $"+amountLeft+" on it.");

											found = true;
										}

										player.getPA().closeDialogueOnly();

										if(!found)
											DialogueManager.sendStatement(player, "@red@No wallet codes found.");
									} catch (Exception ex) {
										//Misc.print("No referral codes to retrieve.");
									}
								}
							} catch (SQLException ex) {
								System.out.println(ex);
							}
						});
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
			"::wallet"
		};
	}

	public static void deleteWalletCode(Player player, int walletId, int amountLeft) {
		String query = "DELETE FROM "+ SQLTable.getGameSchemaTable(SQLTable.SAVES_WALLET_CODES)+" WHERE id = "+walletId+"";
		GameServer.getSqlNetwork().submit(connection -> {
			try {
				try(PreparedStatement statement = connection.prepareStatement(query)) {
					int status = statement.executeUpdate();

					if(status == 0) {
						return;
					}

					player.wallet += amountLeft;
					player.save();
					DialogueManager.sendStatement(player, "@mag@Code is destroyed and $"+amountLeft+" was added back to your wallet.");
				}
			} catch (SQLException ex) {
				System.out.println(ex);
			}
		});
	}

}
