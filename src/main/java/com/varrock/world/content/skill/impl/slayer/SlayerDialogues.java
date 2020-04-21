package com.varrock.world.content.skill.impl.slayer;

import com.varrock.util.Misc;
import com.varrock.world.content.dialogue.Dialogue;
import com.varrock.world.content.dialogue.DialogueExpression;
import com.varrock.world.content.dialogue.DialogueManager;
import com.varrock.world.content.dialogue.DialogueType;
import com.varrock.world.content.skill.impl.slayer.KonarQuoMaten.SlayerTask;
import com.varrock.world.entity.impl.player.Player;

public class SlayerDialogues {

	/**
	 * Dialogues that can't be handled by XML
	 */
	public static Dialogue dialogue(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return player.getSlayer().getSlayerMaster().getNpcId();
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.TALK_SWING;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "Hello.", "Do you need anything?" };
			}

			@Override
			public void specialAction() {

			}

			@Override
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.OPTION;
					}

					@Override
					public int npcId() {
						return -1;
					}

					@Override
					public DialogueExpression animation() {
						return null;
					}

					@Override
					public String[] dialogue() {
						return new String[] { "What's my current assignment?", "I'd like to reset my Slayer Task",
								"How many points do I currently receive per task?",
								player.getSlayer().getDuoPartner() != null ? "I'd like to reset my duo team"
										: "Nothing, thanks"

						};
					}

					@Override
					public void specialAction() {
						if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK
								&& player.getSlayer().getTask().equals(SlayerTask.NO_TASK)) {
							player.getPacketSender().sendInterfaceRemoval();
							DialogueManager.start(player, secondDialogue(player));
							player.setDialogueActionId(30);
							return;
						}
						player.setDialogueActionId(31);
					}
				};
			}
		};
	}

	public static Dialogue secondDialogue(final Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public int npcId() {
				return -1;
			}

			@Override
			public DialogueExpression animation() {
				return null;
			}

			@Override
			public String[] dialogue() {
				boolean inDuo = player.getSlayer().getDuoPartner() != null;
				return new String[] { "I'd like a Slayer task", "I'd like to view your Slayer rewards",
						"I'd like to view your stock of Slayer items",
						inDuo ? "I'd like to reset my duo team" : "Nothing, thanks" };
			}

			@Override
			public void specialAction() {

			}
		};
	}

	public static Dialogue receivedTask(final Player player, final SlayerMaster master, final SlayerTasks task) {
		return new Dialogue() {
			final int amountToKill = player.getSlayer().getAmountToSlay();

			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return master.getNpcId();
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public String[] dialogue() {
				boolean duoSlayer = player.getSlayer().getDuoPartner() != null;
				String you = duoSlayer ? "You and your partner" : "You";
				String line1 = "";
				String line2 = "";
				SlayerTask t = player.getSlayer().getTask();
				if (!t.equals(SlayerTask.NO_TASK)) {
					line1 = "You have been assigned to kill " + amountToKill + " "
							+ task.getName() + "s.";
				} else {
					line1 = "You have been assigned to kill " + amountToKill + " "
							+ task.getName() + "s.";
				}
				if (duoSlayer) {
					line1 = "" + you + " have been assigned to kill";
					if (!t.equals(SlayerTask.NO_TASK)) {
						line2 = "" + amountToKill + " "
								+ Misc.formatText(t.toString().toLowerCase().replaceAll("_", " ")) + "s.";
					} else {
						line2 = "" + amountToKill + " "
								+ Misc.formatText(task.toString().toLowerCase().replaceAll("_", " ")) + "s.";
					}
				}
				if (!t.equals(SlayerTask.NO_TASK)) {
					line1 = "" + you + " are doing great! Your new";
					line2 = "assignment is to kill " + amountToKill + " "
							+ Misc.formatText(t.toString().toLowerCase().replaceAll("_", " ")) + "s.";
				} else {
					if (player.getSlayer().getLastTask() != SlayerTasks.NO_TASK) {
						line1 = "" + you + " are doing great! Your new";
						line2 = "assignment is to kill " + amountToKill + " "
								+ Misc.formatText(task.toString().toLowerCase().replaceAll("_", " ")) + "s.";
					}
				}
				return new String[] { "" + line1 + "", "" + line2 + "" };
			}

			@Override
			public void specialAction() {

			}

			@Override
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.OPTION;
					}

					@Override
					public int npcId() {
						return master.getNpcId();
					}

					@Override
					public DialogueExpression animation() {
						return null;
					}

					@Override
					public String[] dialogue() {
						boolean inDuo = player.getSlayer().getDuoPartner() != null;
						return new String[] { "What's my current assignment?", "I'd like to reset my Slayer Task",
								"How many points do I currently receive per task?",
								inDuo ? "I'd like to reset my duo team" : "Nothing, thanks"

						};
					}

					@Override
					public void specialAction() {
						player.setDialogueActionId(31);
					}
				};
			}
		};
	}

	public static Dialogue findAssignment(final Player player) {
		final SlayerMaster master = player.getSlayer().getSlayerMaster();
		final SlayerTasks task = player.getSlayer().getSlayerTask();
		final SlayerTask t = player.getSlayer().getTask();
		return new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return master.getNpcId();
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public String[] dialogue() {
				String l = "";
				if (task != null)
					l = task.getNpcLocation();
				if (!t.equals(SlayerTask.NO_TASK)) {
					return new String[] {
							"Your current task is to kill " + (player.getSlayer().getAmountToSlay()) + " "
									+ Misc.formatText(t.toString().toLowerCase().replaceAll("_", " ")) + "s." };
				}
				return new String[] {
						"Your current task is to kill " + (player.getSlayer().getAmountToSlay()) + " "
								+ Misc.formatText(task.toString().toLowerCase().replaceAll("_", " ")) + "s.",
						"" + l + "" };
			}

			@Override
			public void specialAction() {

			}
		};
	}

	public static Dialogue resetTaskDialogue(final Player player) {
		final SlayerMaster master = player.getSlayer().getSlayerMaster();
		return new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return master.getNpcId();
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "It costs 5 points to reset or free for donators.",
						"You will also lose your current Task Streak.", "Are you sure you wish to continue?" };
			}

			@Override
			public void specialAction() {

			}

			@Override
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.OPTION;
					}

					@Override
					public DialogueExpression animation() {
						return null;
					}

					@Override
					public String[] dialogue() {
						return new String[] { "Yes, please", "Cancel"

						};
					}

					@Override
					public void specialAction() {
						player.setDialogueActionId(33);
					}
				};
			}
		};
	}

	public static Dialogue totalPointsReceived(final Player player) {
		return new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.NPC_STATEMENT;
			}

			@Override
			public int npcId() {
				return player.getSlayer().getSlayerMaster().getNpcId();
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public String[] dialogue() {
				int pointsReceived = 4;
				/*
				 * if(player.getSlayerMaster() == Slayer) //medium task pointsReceived = 7;
				 * if(player.getSlayerMaster().getTaskLevel() == 2) //hard/elite tasks
				 * pointsReceived = 10;
				 */
				int per5 = pointsReceived * 3;
				int per10 = pointsReceived * 5;
				return new String[] { "You currently receive " + pointsReceived + " points per task,",
						"" + per5 + " bonus points per 5 task-streak and",
						"" + per10 + " bonus points per 10 task-streak." };
			}

			@Override
			public void specialAction() {

			}
		};
	}

	public static Dialogue inviteDuo(final Player player, final Player inviteOwner) {
		return new Dialogue() {
			@Override
			public DialogueType type() {
				return DialogueType.STATEMENT;
			}

			@Override
			public DialogueExpression animation() {
				return null;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "" + inviteOwner.getUsername() + " has invited you to form a duo Slayer team.", };
			}

			@Override
			public Dialogue nextDialogue() {
				return new Dialogue() {

					@Override
					public DialogueType type() {
						return DialogueType.OPTION;
					}

					@Override
					public DialogueExpression animation() {
						return null;
					}

					@Override
					public String[] dialogue() {
						return new String[] { "Accept " + inviteOwner.getUsername() + "'s invitation",
								"Decline " + inviteOwner.getUsername() + "'s invitation"

						};
					}

					@Override
					public void specialAction() {
						player.setDialogueActionId(34);
						player.getSlayer().setDuoInvitation(inviteOwner.getUsername());
					}
				};
			}
		};
	}
}
