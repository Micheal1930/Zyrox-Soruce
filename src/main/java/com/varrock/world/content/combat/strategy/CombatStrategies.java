package com.varrock.world.content.combat.strategy;

import java.util.HashMap;
import java.util.Map;

import com.varrock.world.content.combat.strategy.impl.*;
import com.varrock.world.content.combat.strategy.impl.kraken.Kraken;
import com.varrock.world.content.combat.strategy.impl.kraken.Tentacles;
import com.varrock.world.content.combat.strategy.zulrah.npc.Blue;
import com.varrock.world.content.combat.strategy.zulrah.npc.Green;
import com.varrock.world.content.combat.strategy.zulrah.npc.Red;


public class CombatStrategies {

	private static final DefaultMeleeCombatStrategy defaultMeleeCombatStrategy = new DefaultMeleeCombatStrategy();
	private static final DefaultMagicCombatStrategy defaultMagicCombatStrategy = new DefaultMagicCombatStrategy();
	private static final DefaultRangedCombatStrategy defaultRangedCombatStrategy = new DefaultRangedCombatStrategy();
	private static final EmptyCombatStrategy emptyCombatStrategy = new EmptyCombatStrategy();
	private static final Map<Integer, CombatStrategy> STRATEGIES = new HashMap<Integer, CombatStrategy>();
	
	public static void init() {
		DefaultMagicCombatStrategy defaultMagicStrategy = new DefaultMagicCombatStrategy();
		STRATEGIES.put(13, defaultMagicStrategy);
		STRATEGIES.put(172, defaultMagicStrategy);
		STRATEGIES.put(174, defaultMagicStrategy);
		STRATEGIES.put(2025, defaultMagicStrategy);
		STRATEGIES.put(3495, defaultMagicStrategy);
		STRATEGIES.put(3496, defaultMagicStrategy);
		STRATEGIES.put(3491, defaultMagicStrategy);
		STRATEGIES.put(2882, defaultMagicStrategy);
		STRATEGIES.put(13451, defaultMagicStrategy);
		STRATEGIES.put(13452, defaultMagicStrategy);
		STRATEGIES.put(13453, defaultMagicStrategy);
		STRATEGIES.put(13454, defaultMagicStrategy);
		STRATEGIES.put(1643, defaultMagicStrategy);
		STRATEGIES.put(6254, defaultMagicStrategy);
		STRATEGIES.put(6257, defaultMagicStrategy);
		STRATEGIES.put(6278, defaultMagicStrategy);
		STRATEGIES.put(6221, defaultMagicStrategy);
		STRATEGIES.put(133, defaultMagicStrategy);
	;
		
		DefaultRangedCombatStrategy defaultRangedStrategy = new DefaultRangedCombatStrategy();
		STRATEGIES.put(688, defaultRangedStrategy);
		STRATEGIES.put(2028, defaultRangedStrategy);
		STRATEGIES.put(6220, defaultRangedStrategy);
		STRATEGIES.put(6256, defaultRangedStrategy);
		STRATEGIES.put(6276, defaultRangedStrategy);
		STRATEGIES.put(6252, defaultRangedStrategy);
		STRATEGIES.put(27, defaultRangedStrategy);
		
		STRATEGIES.put(3847, new Kraken());
		STRATEGIES.put(148, new Tentacles());
		STRATEGIES.put(2745, new Jad());
		STRATEGIES.put(8528, new Nomad());
		STRATEGIES.put(8349, new TormentedDemon());
		STRATEGIES.put(3200, new ChaosElemental());
		STRATEGIES.put(4540, new BandosAvatar());
		STRATEGIES.put(12841, new Warmonger());
		STRATEGIES.put(22542, new Tekton());
		STRATEGIES.put(21475, new TarnRazorlor());
		STRATEGIES.put(21477, new TarnRazorlor());
		STRATEGIES.put(8133, new CorporealBeast());
		STRATEGIES.put(22795, new AncientWyvernCombatScript());
		STRATEGIES.put(13447, new Nex());
		STRATEGIES.put(2896, new Spinolyp());
		STRATEGIES.put(3334, new WildyWyrmCombatStrategy());
		STRATEGIES.put(21332, new DraculaCombatStrategy());
		STRATEGIES.put(2881, new DagannothSupreme());
		STRATEGIES.put(6260, new Graardor());
		STRATEGIES.put(6263, new Steelwill());
		STRATEGIES.put(6265, new Grimspike());
		STRATEGIES.put(6222, new KreeArra());
		STRATEGIES.put(6223, new WingmanSkree());
		STRATEGIES.put(6225, new Geerin());
		STRATEGIES.put(6203, new Tsutsuroth());
		STRATEGIES.put(3340, new GiantMole());
		STRATEGIES.put(6208, new Kreeyath());
		STRATEGIES.put(6206, new Gritch());
		STRATEGIES.put(6247, new Zilyana());
		STRATEGIES.put(6250, new Growler());
		STRATEGIES.put(1382, new Glacor());
		STRATEGIES.put(9939, new PlaneFreezer());
		STRATEGIES.put(135, new Fear());
		STRATEGIES.put(133, new Cobra());
		STRATEGIES.put(1472, new Death());
		STRATEGIES.put(132, new Death());
		Dragon dragonStrategy = new Dragon();
		STRATEGIES.put(50, dragonStrategy);
		STRATEGIES.put(941, dragonStrategy);
		STRATEGIES.put(55, dragonStrategy);
		STRATEGIES.put(53, dragonStrategy);
		STRATEGIES.put(54, dragonStrategy);
		STRATEGIES.put(51, dragonStrategy);
		STRATEGIES.put(1590, dragonStrategy);
		STRATEGIES.put(1591, dragonStrategy);
		STRATEGIES.put(1592, dragonStrategy);
		STRATEGIES.put(5362, dragonStrategy);
		STRATEGIES.put(5363, dragonStrategy);
		STRATEGIES.put(23030, dragonStrategy);
		STRATEGIES.put(23031, dragonStrategy);

		STRATEGIES.put(23060, new VorkathCombatScript());
		
		Aviansie aviansieStrategy = new Aviansie();
		STRATEGIES.put(6246, aviansieStrategy);
		STRATEGIES.put(6230, aviansieStrategy);
		STRATEGIES.put(6231, aviansieStrategy);
		
		KalphiteQueen kalphiteQueenStrategy = new KalphiteQueen();
		STRATEGIES.put(1158, kalphiteQueenStrategy);
		STRATEGIES.put(1160, kalphiteQueenStrategy);

		STRATEGIES.put(22145, new DemonicGorilla());
		STRATEGIES.put(22146, new DemonicGorilla());
		STRATEGIES.put(22147, new DemonicGorilla());
		
		Revenant revenantStrategy = new Revenant();
		STRATEGIES.put(6715, revenantStrategy);
		STRATEGIES.put(6716, revenantStrategy);
		STRATEGIES.put(6701, revenantStrategy);
		STRATEGIES.put(6725, revenantStrategy);
		STRATEGIES.put(6691, revenantStrategy);
		STRATEGIES.put(22938, revenantStrategy);
		STRATEGIES.put(22940, revenantStrategy);
		STRATEGIES.put(8832, revenantStrategy);
		STRATEGIES.put(3975, new MaxHitStone());
		STRATEGIES.put(2000, new Venenatis());
		STRATEGIES.put(2006, new Vetion());
		STRATEGIES.put(2010, new Callisto());
		STRATEGIES.put(1999, new Cerberus());
		STRATEGIES.put(6766, new LizardMan());
		STRATEGIES.put(499, new Thermonuclear());
		STRATEGIES.put(7286, new Skotizo());
		STRATEGIES.put(5886, new Sire());
		STRATEGIES.put(10126, new UnholyCursebearer());
		STRATEGIES.put(ElectroWyrmCombatStrategy.WYRM, new ElectroWyrmCombatStrategy());
		STRATEGIES.put(NazastaroolCombatStrategy.NAZASTAROOL, new NazastaroolCombatStrategy());
		STRATEGIES.put(DrakeCombatStrategy.DRAKE, new DrakeCombatStrategy());
		STRATEGIES.put(23095, new GalvekCombatStrategy(23095));
		STRATEGIES.put(23096, new GalvekCombatStrategy(23096));
		STRATEGIES.put(23097, new GalvekCombatStrategy(23097));
		STRATEGIES.put(23098, new GalvekCombatStrategy(23098));
	}
	
	public static CombatStrategy getStrategy(int npc) {
		if(STRATEGIES.get(npc) != null) {
			return STRATEGIES.get(npc);
		}
		
		if (npc == 2042) {
			return new Blue();
		} else if (npc == 2043) {
			return new Green();
		} else if (npc == 2044) {
			return new Red();
		}
		
		return defaultMeleeCombatStrategy;
	}
	
	public static CombatStrategy getDefaultMeleeStrategy() {
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMagicStrategy() {
		return defaultMagicCombatStrategy;
	}


	public static CombatStrategy getDefaultRangedStrategy() {
		return defaultRangedCombatStrategy;
	}

	public static CombatStrategy getEmptyCombatStrategy() {
		return emptyCombatStrategy;
	}

}
