package com.zyrox.world.content;

import com.zyrox.model.Flag;
import com.zyrox.model.Skill;
import com.zyrox.util.Misc;
import com.zyrox.world.World;
import com.zyrox.world.content.Achievements.AchievementData;
import com.zyrox.world.entity.impl.player.Player;

public class LoyaltyProgramme {

	public enum LoyaltyTitles {

		NONE(0, 0, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},		
		KILLER(-1, 43011, 43008) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[1]) {
					if(sendMessage)
						p.getPacketSender().sendMessage("To unlock this title, you must kill another player.");
					return false;
				}
				return true;
			}
		},
		SLAUGHTERER(-1, 43015, 43012) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[2]) {
					if(sendMessage)
						p.getPacketSender().sendMessage("To unlock this title, you must kill 20 other players.");
					return false;
				}
				return true;
			}
		},
		GENOCIDAL(-1, 43019, 43016) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[3]) {
					if(sendMessage)
						p.getPacketSender().sendMessage("To unlock this title, you must kill 50 other players.");
					return false;
				}
				return true;
			}
		},
		IMMORTAL(-1, 43023, 43020) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[4]) {
					if(sendMessage)
						p.getPacketSender().sendMessage("To unlock this title, you must kill 15 players without dying.");
					return false;
				}
				return true;
			}
		},
		SKILLER(-1, 43027, 43024) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[5]) {
					for(int i = 7; i < Skill.values().length; i++) {
						if(i == 21 || i == 24 || i == 23 || i == 18)
							continue;
						if(p.getSkillManager().getMaxLevel(i) < 99) {
							if(sendMessage)
								p.getPacketSender().sendMessage("You must be at least level 99 in every non-combat skill for this title.");
							return false;
						}
					}
				}
				unlock(p, this);
				return true;
			}
		},
		COMBATANT(-1, 43031, 43028) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[6]) {
					for(int i = 0; i <= 6; i++) {
						if(p.getSkillManager().getMaxLevel(i) < (i == 3 || i == 5 ? 990 : 99)) {
							if(sendMessage)
								p.getPacketSender().sendMessage("You must be at least level 99 in every combat skill for this title.");
							return false;
						}
					}
				}
				unlock(p, this);
				return true;
			}
		},
		MAXED(-1, 43035, 43032) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[7]) {
					for(int i = 0; i < Skill.values().length; i++) {
						if(i == Skill.CONSTRUCTION.ordinal()) {
							continue;
						}
						if(p.getSkillManager().getMaxLevel(i) < (i == 3 || i == 5 ? 990 : 99)) {
							if(sendMessage)
								p.getPacketSender().sendMessage("You must be at least level 99 in every skill for this title.");
							return false;
						}
					}
				}
				Achievements.finishAchievement(p, AchievementData.REACH_LEVEL_99_IN_ALL_SKILLS);

				unlock(p, this);
				return true;
			}
		},
		GODSLAYER(-1, 43039, 43036) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[8]) {
					for(boolean b : p.getAchievementAttributes().getGodsKilled()) {
						if(!b) {
							if(sendMessage)
								p.getPacketSender().sendMessage("To unlock this title, you must slay all of the 5 gods in the Godwars Dungeon.");
							return false;
						}
					}
				}
				unlock(p, this);
				return true;
			}
		},
		LOYALIST(-1, 43043, 43040) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[9]) {
					if(p.getPointsHandler().getLoyaltyPoints() < 100000) {
						if(sendMessage)
							p.getPacketSender().sendMessage("To unlock this title, you must gain 100,000 Loyalty Points simultaneously.");
						return false;
					}
				}
				unlock(p, this);
				return true;
			}
		},
		VETERAN(-1, 43047, 43044) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[10]) {
					if(p.getAchievementAttributes().getTotalLoyaltyPointsEarned() < 500000) {
						if(sendMessage)
							p.getPacketSender().sendMessage("To unlock this title, you must have earned 500,000 Loyalty Points in total.");
						return false;
					}
				}
				unlock(p, this);
				return true;
			}
		},
		GAMBLER(-1, 43051, 43048) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[11]) {
					if(!p.getInventory().contains(15084)) {
						if(sendMessage)
							p.getPacketSender().sendMessage("To unlock this title, you must have a Dice in your inventory.");
						return false;
					}
				}
				unlock(p, this);
				return true;
			}
		},


		KING(25000, 43055, 43052) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		QUEEN(25000,  43059, 43056) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		LORD(20000,  43063, 43060) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		DUKE(15000, 43067, 43064) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		DUCHESS(15000,  43071, 43068) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		BARON(10000, 43075, 43072) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		BARONESS(10000, 43079, 43076) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		SIR(8000, 43083, 43080) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		LADY(8000, 43087, 43084) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		EVIL(5000, 43091, 43088) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		GOOD(5000, 43095, 43092) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		};

		private LoyaltyTitles(int cost, int frame, int button) {
			this.cost = cost;
			this.frame = frame;
			this.button = button;
		}

		private int cost;
		private int frame;
		private int button;
		abstract boolean canBuy(Player p, boolean sendMessage);

		public static LoyaltyTitles getTitle(int button) {
			for(LoyaltyTitles t : LoyaltyTitles.values()) {
				if(t.button == button)
					return t;
			}
			return null;
		}
	}

	public static void unlock(Player player, LoyaltyTitles title) {
		if(player.getUnlockedLoyaltyTitles()[title.ordinal()])
			return;
		Achievements.doProgress(player, AchievementData.UNLOCK_ALL_LOYALTY_TITLES);
		player.setUnlockedLoyaltyTitle(title.ordinal());
		player.getPacketSender().sendMessage("You've unlocked the "+Misc.formatText(title.name().toLowerCase())+" loyalty title!");
	}

	public static boolean handleButton(Player player, int button) {
		LoyaltyTitles title = LoyaltyTitles.getTitle(button);
		if(title != null) {
			if(title.canBuy(player, true)) {
				
				if(player.getPointsHandler().getLoyaltyPoints() >= title.cost) {
				
					player.setTitle("@or2@"+title);
					
					if (!player.getUnlockedLoyaltyTitles()[title.ordinal()]) {
						player.getPointsHandler().setLoyaltyPoints(-title.cost, true);
					}

					player.getPacketSender().sendMessage("You've changed your title.");
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					open(player);
				} else {
					player.getPacketSender().sendMessage("You need at least "+title.cost+" Loyalty Points to buy this title.");
				}
			}
			return true;
		}
		return false;
	}

	public static void open(Player player) {
		for(LoyaltyTitles title : LoyaltyTitles.values()) {
			if(title.cost > 0) {
				player.getPacketSender().sendString(title.frame, title.cost + " LP");
			} else {
				if(title.canBuy(player, false)) {
					player.getPacketSender().sendString(title.frame, "@gre@Unlocked");
				} else {
					player.getPacketSender().sendString(title.frame, "  @red@Locked");
				}
			}
		}
		player.getPacketSender().sendString(43120, "Your Loyalty Points: "+player.getPointsHandler().getLoyaltyPoints()).sendInterface(43000);
	}

	public static void reset(Player player) {
		player.setLoyaltyTitle(LoyaltyTitles.NONE);
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	public static void incrementPoints(Player player) {
		double pts = player.getRights().getLoyaltyPointsGainModifier();
		if(World.getWell().isBonusLoyaltyPoints(player))
			pts *= 1.5;
		player.getPointsHandler().incrementLoyaltyPoints(pts);
		player.getAchievementAttributes().incrementTotalLoyaltyPointsEarned(pts);
		
		int totalPoints = (int)player.getPointsHandler().getLoyaltyPoints();
		if(totalPoints >= 100000) {
			unlock(player, LoyaltyTitles.LOYALIST);
		}
		
		if(player.getInterfaceId() == 43000) {
			player.getPacketSender().sendString(43120, "Your Loyalty Points: "+totalPoints);
		}
		player.getPacketSender().sendString(39178, "@or2@Loyalty Points: @yel@"+totalPoints);
		
		if(player.getAchievementAttributes().getTotalLoyaltyPointsEarned() >= 500000) {
			unlock(player, LoyaltyTitles.VETERAN);
		}
	}
}
