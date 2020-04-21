package com.varrock.world.content.greatolm.attacks;

import com.varrock.GameSettings;
import com.varrock.engine.task.Task;
import com.varrock.engine.task.TaskManager;
import com.varrock.model.CombatIcon;
import com.varrock.model.Skill;
import com.varrock.model.projectile.Projectile;
import com.varrock.util.Misc;
import com.varrock.world.content.combat.CombatType;
import com.varrock.world.content.combat.prayer.CurseHandler;
import com.varrock.world.content.combat.prayer.PrayerHandler;
import com.varrock.world.content.greatolm.OlmAnimations;
import com.varrock.world.content.greatolm.RaidsParty;
import com.varrock.world.entity.impl.player.Player;

public class OrbAttack {

	static CombatType[] randomTypes = new CombatType[] { CombatType.MAGIC, CombatType.RANGED, CombatType.MELEE };
	static CombatIcon[] randomIcons = new CombatIcon[] { CombatIcon.MAGIC, CombatIcon.RANGED, CombatIcon.MELEE };
	static String[] sphereNames = new String[] { "magical power", "accuracy and dexterity", "agression" };
	static String[] sphereColors = new String[] { "@blu@", "@gre@", "@red@" };

	public static void performAttack(RaidsParty party, int height) {
		party.getGreatOlmNpc().performGreatOlmAttack(party);
		party.setOlmAttackTimer(6);

		TaskManager.submit(new Task(1, party, true) {
			int tick = 0;

			@Override
			public void execute() {
				if (party.getGreatOlmNpc().isDying() || party.isSwitchingPhases()) {
					stop();
				}
				if (tick == 1) {

					for (int i = 0; i < 3; i++) {
						Player member = party.getPlayers().get(Misc.getRandom(party.getPlayers().size() - 1));
						if (member.isInsideRaids()) {
							if (!party.getPlayersToAttack().contains(member)) {
								member.sendMessage(sphereColors[i] + "The Great Olm fires a sphere of " + sphereNames[i]
										+ " your way. </col>Your prayers have been");
								member.sendMessage("</col>sapped.");
								PrayerHandler.deactivateAll(member);
								CurseHandler.deactivateAll(member);
								new Projectile(party.getGreatOlmNpc(), member, 1341 + (i * 2) + GameSettings.OSRS_GFX_OFFSET, 60, 1, 70, 31, 0)
										.sendProjectile();
								party.getPlayersToAttack().add(member);
							}
						}
					}
				}
				if (tick == 2) {
					OlmAnimations.resetAnimation(party);
				}
				if (tick == 4) {
					int i = 0;
					for (Player member : party.getPlayersToAttack()) {
						if (member.isInsideRaids()) {

							if ((PrayerHandler.isActivated(member, PrayerHandler.getProtectingPrayer(randomTypes[i]))
									|| CurseHandler.isActivated(member,
											CurseHandler.getProtectingPrayer(randomTypes[i])))) {
								member.getSkillManager().setCurrentLevel(Skill.PRAYER,
										member.getSkillManager().getCurrentLevel(Skill.PRAYER) / 2);
							} else {
								// Attacks.hitPlayer(member, member.getConstitution() / 2,
								// member.getConstitution() / 2,
								// randomTypes[i], randomIcons[i], 2, false);
							}
							i++;
						}
					}
					party.getPlayersToAttack().clear();
					stop();
				}
				tick++;
			}
		});
	}

}
