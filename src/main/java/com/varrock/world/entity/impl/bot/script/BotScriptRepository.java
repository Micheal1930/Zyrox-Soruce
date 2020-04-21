package com.varrock.world.entity.impl.bot.script;

import java.util.HashMap;
import java.util.Map;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class BotScriptRepository {
	
	private static final String SCRIPT_LOCATION = BotScript.class.getPackage().getName() + ".impl";
	
	private static final Map<String, BotScript> SCRIPTS = new HashMap<>();
	
	static {
		try {
			ClassPath cp = ClassPath.from(BotScriptRepository.class.getClassLoader());
			
			for (ClassInfo ci : cp.getTopLevelClasses(SCRIPT_LOCATION)) {
				SCRIPTS.put(ci.getSimpleName().toLowerCase(), ((BotScript) Class.forName(ci.getName()).newInstance()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, BotScript> getScripts() {
		return SCRIPTS;
	}

}
