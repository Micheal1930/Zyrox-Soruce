package com.varrock.world.content.skill.impl.farming;

import com.varrock.util.Misc;
import com.varrock.world.content.skill.impl.farming.attributes.GrowthState;
import com.varrock.world.content.skill.impl.farming.attributes.PatchAttribute;
import com.varrock.world.content.skill.impl.farming.patch.Patch;
import com.varrock.world.content.skill.impl.farming.patch.PatchType;
import com.varrock.world.content.skill.impl.farming.seed.MushroomSeedType;
import com.varrock.world.content.skill.impl.farming.seed.SeedClass;
import com.varrock.world.entity.impl.player.Player;

/**
 *
 * @author relex lawl
 */
public final class FarmingManager {
	
	public static void uponLogin(Player player) {
		StringBuilder grownPatches = new StringBuilder();

		int index = 0;

		for (Patch patch : player.getPatches().values()) {
			if(patch.isFullyGrown()) {
				if(!grownPatches.toString().contains(patch.getPatchType().getCityName())) {
					grownPatches.append(patch.getPatchType().getCityName());
					grownPatches.append(", ");
				}
			} else {
				patch.submitGrowthTask(player);
			}

			if (patch != null
					&& patch.getPatchType() != null) {
				updatePatch(player, patch.getPatchType(), patch);
			}

			index++;
		}

		String patches = grownPatches.toString();

		if(patches.endsWith(", ")) {
			patches = Misc.removeLastChar(patches);
		}

		if(patches.length() > 0) {
			player.sendMessage("<col=627E2D>Your patches have grown in:</col> "+patches);
		}
	}

	public static void updatePatch(Player player, PatchType patchType, Patch patch) {
		long value;
		switch (patchType) {
		case VARROCK_CASTLE_TREE_PATCH:
		case FALADOR_TREE_PATCH:
		case TAVERLEY_TREE_PATCH:
			final Patch varrockTree = player.getPatches().get(PatchType.VARROCK_CASTLE_TREE_PATCH);
			final Patch faladorTree = player.getPatches().get(PatchType.FALADOR_TREE_PATCH);
			final Patch taverleyTree = player.getPatches().get(PatchType.TAVERLEY_TREE_PATCH);
			value = ((0 << 24))
					+ ((long)getPatchValue(varrockTree) << 16)
					+ ((long)getPatchValue(faladorTree) << 8)
					+ ((long)getPatchValue(taverleyTree));
			player.getPacketSender().sendConfig(patchType.getConfigId(), value);
			break;
		case CATHERBY_NORTH_ALLOTMENT:
		case CATHERBY_SOUTH_ALLOTMENT:
		case FALADOR_NORTH_WEST_ALLOTMENT:
		case FALADOR_SOUTH_EAST_ALLOTMENT:
			final Patch catherbyN = player.getPatches().get(PatchType.CATHERBY_NORTH_ALLOTMENT);
			final Patch catherbyS = player.getPatches().get(PatchType.CATHERBY_SOUTH_ALLOTMENT);
			final Patch faladorNW = player.getPatches().get(PatchType.FALADOR_NORTH_WEST_ALLOTMENT);
			final Patch faladorSE = player.getPatches().get(PatchType.FALADOR_SOUTH_EAST_ALLOTMENT);
			value = (((long)getPatchValue(catherbyS) << 24))
					+ ((long)getPatchValue(catherbyN) << 16)
					+ ((long)getPatchValue(faladorSE) << 8)
					+ ((long)getPatchValue(faladorNW));
			player.getPacketSender().sendConfig(patchType.getConfigId(), value);
			break;
		case ARDOUGNE_NORTH_ALLOTMENT:
		case ARDOUGNE_SOUTH_ALLOTMENT:
		case CANIFIS_NORTH_WEST_ALLOTMENT:
		case CANIFIS_SOUTH_EAST_ALLOTMENT:
			final Patch ardougneN = player.getPatches().get(PatchType.ARDOUGNE_NORTH_ALLOTMENT);
			final Patch ardougneS = player.getPatches().get(PatchType.ARDOUGNE_SOUTH_ALLOTMENT);
			final Patch canafisNW = player.getPatches().get(PatchType.CANIFIS_NORTH_WEST_ALLOTMENT);
			final Patch canafisSE = player.getPatches().get(PatchType.CANIFIS_SOUTH_EAST_ALLOTMENT);
			value = (((long)getPatchValue(canafisSE) << 24))
					+ ((long)getPatchValue(canafisNW) << 16)
					+ ((long)getPatchValue(ardougneS) << 8)
					+ ((long)getPatchValue(ardougneN));

			player.getPacketSender().sendConfig(patchType.getConfigId(), value);
			break;
		case FALADOR_FLOWER_PATCH:
		case CATHERBY_FLOWER_PATCH:
		case ARDOUGNE_FLOWER_PATCH:
		case CANIFIS_FLOWER_PATCH:
			final Patch faladorFlower = player.getPatches().get(PatchType.FALADOR_FLOWER_PATCH);
			final Patch catherbyFlower = player.getPatches().get(PatchType.CATHERBY_FLOWER_PATCH);
			final Patch ardougneFlower = player.getPatches().get(PatchType.ARDOUGNE_FLOWER_PATCH);
			final Patch canifisFlower = player.getPatches().get(PatchType.CANIFIS_FLOWER_PATCH);
			value = (((long)getPatchValue(canifisFlower) << 24))
					+ ((long)getPatchValue(ardougneFlower) << 16)
					+ ((long)getPatchValue(catherbyFlower) << 8)
					+ ((long)getPatchValue(faladorFlower));

			player.getPacketSender().sendConfig(patchType.getConfigId(), value);
			break;
		case FALADOR_HERB_PATCH: //3057, 3310
		case CATHERBY_HERB_PATCH: //2812, 3463
		case ARDOUGNE_HERB_PATCH: //2669, 3375
		case CANIFIS_HERB_PATCH: //3600, 3525
			final Patch faladorHerb = player.getPatches().get(PatchType.FALADOR_HERB_PATCH);
			final Patch catherbyHerb = player.getPatches().get(PatchType.CATHERBY_HERB_PATCH);
			final Patch ardougneHerb = player.getPatches().get(PatchType.ARDOUGNE_HERB_PATCH);
			final Patch canifisHerb = player.getPatches().get(PatchType.CANIFIS_HERB_PATCH);
			value = (((long)getPatchValue(canifisHerb) << 24))
					+ ((long)getPatchValue(ardougneHerb) << 16)
					+ ((long)getPatchValue(catherbyHerb) << 8)
					+ ((long)getPatchValue(faladorHerb));

			player.getPacketSender().sendConfig(patchType.getConfigId(), value);
			break;
		case VARROCK_BUSH_PATCH:
		case RIMMINGTON_BUSH_PATCH:
		case ARDOUGNE_BUSH_PATCH:
		case ETCETERIA_BUSH_PATCH:
			final Patch varrockBush = player.getPatches().get(PatchType.VARROCK_BUSH_PATCH);
			final Patch rimmingtonBush = player.getPatches().get(PatchType.RIMMINGTON_BUSH_PATCH);
			final Patch ardougneBush = player.getPatches().get(PatchType.ARDOUGNE_BUSH_PATCH);
			final Patch etceteriaBush = player.getPatches().get(PatchType.ETCETERIA_BUSH_PATCH);
			value = (((long)getPatchValue(ardougneBush) << 24))
					+ ((long)getPatchValue(etceteriaBush) << 16)
					+ ((long)getPatchValue(rimmingtonBush) << 8)
					+ ((long)getPatchValue(varrockBush));
			player.getPacketSender().sendConfig(patchType.getConfigId(), value);
			break;
		case CANAFIS_MUSHROOM_PATCH:
			final Patch canafisMushroom = player.getPatches().get(PatchType.CANAFIS_MUSHROOM_PATCH);
			value = ((long)getPatchValue(canafisMushroom) << 8)
					+ (0);
			player.getPacketSender().sendConfig(patchType.getConfigId(), value);
			break;
		}
	}

	public static int getPatchValue(Patch patch) {
		if (patch == null)
			return 0;
		if (patch.getSeedType() != null) {
			int growthShift = patch.getGrowthState().getShift();
			if (growthShift != GrowthState.GROWING.getShift()
					&& patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
				growthShift -= 0x01;
			} else if (growthShift != GrowthState.GROWING.getShift()
					&& patch.getSeedType().getSeedClass() == SeedClass.TREES) {
				growthShift = GrowthState.GROWING.getShift();
				patch.setGrowthState(GrowthState.GROWING);
			}
			int growth = patch.getGrowthState() != null ? (growthShift << 6) : 0;
			int value = patch.getSeedType().getValues()[patch.getStage()];
			if (patch.getSeedType().getSeedClass() == SeedClass.BUSHES) {
				value -= patch.getHarvestedItem().getAmount();
			} else if (patch.getSeedType().getSeedClass() == SeedClass.TREES) {
				if (patch.getHarvestedItem().getAmount() >= patch.getProduct().getAmount()) {
					value += 2;
				} else if (patch.hasAttribute(PatchAttribute.CHECKED_HEALTH)) {
					value += 1;
				}
			} else if (patch.getSeedType() == MushroomSeedType.BITTERCAP
					&& patch.getHarvestedItem().getAmount() > 0) {
				value += patch.getHarvestedItem().getAmount();
			}
			return growth | value;
		} else {
			return patch.getWeedStage() < FarmingConstants.WEED_CONFIG.length ?
					FarmingConstants.WEED_CONFIG[patch.getWeedStage()] : FarmingConstants.WEED_CONFIG[3];
		}
	}
}