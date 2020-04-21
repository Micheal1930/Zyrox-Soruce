package com.varrock.saving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.varrock.model.MagicSpellbook;
import com.varrock.model.Prayerbook;
import com.varrock.world.content.presets.Preset;
import com.varrock.world.entity.impl.player.Player;

public class SavePresetSettings extends SaveObject {

	public SavePresetSettings(String name) {
		super(name);
	}

	@Override
	public boolean save(Player player, BufferedWriter writer) throws IOException {
		writer.write(getName());
		writer.newLine();
		for (Preset preset : player.getPresets().getPresets()) {
			if (preset != null) {
				writer.write(preset.getIndex() + " " + preset.getName().replaceAll(" ", "_") + " " + preset.getPrayerBook().ordinal() + " " + preset.getSpellBook().ordinal());
				writer.newLine();
			}
		}
		return true;
	}

	@Override
	public void load(Player player, String values, BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()).length() > 0) {
			String[] parts = line.split(" ");
			int index = Integer.parseInt(parts[0]);
			String name = parts[1].replaceAll("_", " ");
			int prayer = Integer.parseInt(parts[2]);
			int spell = Integer.parseInt(parts[3]);
			player.getPresets().get(index).setName(name);
			player.getPresets().get(index).setPrayerBook(Prayerbook.forId(prayer));
			player.getPresets().get(index).setSpellBook(MagicSpellbook.forId(spell));
		}
	}

}
