package com.varrock.world.content.freeforall;

import com.google.common.collect.ImmutableMap;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class FFASetDatabase {
    private final Path databasePath = Paths.get("./data/saves/ffa");
    private ImmutableMap<FFASet, FFASetData> data = ImmutableMap.of();

    public void load() {
        EnumMap<FFASet, FFASetData> loadedData = new EnumMap<>(FFASet.class);

        data = ImmutableMap.copyOf(loadedData);
    }

    public ImmutableMap<FFASet, FFASetData> getData() {
        return data;
    }
}