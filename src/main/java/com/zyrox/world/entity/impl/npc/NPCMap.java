package com.zyrox.world.entity.impl.npc;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import com.zyrox.model.Position;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NPCMap {

    /**
     * The single instance of this class.
     */
    private static final NPCMap singleton = new NPCMap();

    /**
     * A mapping of npc types to their respective npc instance. This instance should never be
     * returned, however, {@link NPC#copy(int)} should be called on the reference.
     */
    private final Map<Integer, NPC> npcs = new HashMap<>();

    /**
     * A private constructor to satisfy the singleton pattern.
     */
    private NPCMap() {

    }

    /**
     * Populates the underlying {@link #npcs} mapping by searching reflectively for classes that are
     * annotated with {@link NPCMap}.
     */
    public void load() {
        Configuration configuration = new ConfigurationBuilder().useParallelExecutor().addScanners(new TypeAnnotationsScanner(), new SubTypesScanner()).forPackages("com.Zyrox");

        Set<Class<?>> custom = new Reflections(configuration).getTypesAnnotatedWith(NPCIdentity.class);

        if (!custom.isEmpty()) {
            for (Class<?> npcClass : custom) {
                NPCIdentity component = npcClass.getDeclaredAnnotation(NPCIdentity.class);

                if (component == null) {
                    continue;
                }
                int[] ids = component.ids();

                if (ids.length == 0) {
                    throw new RuntimeException(
                            String.format("Custom npc must contain an id: %s", npcClass));
                }

                for (int id : ids) {

                    if (npcs.containsKey(id)) {
                        throw new RuntimeException(
                                String.format("Duplicate Npc already exists: %s", npcClass));
                    }
                    try {
                        Object object =
                                npcClass.getConstructor(int.class, Position.class).newInstance(id, new Position(3085, 3500));

                        if (object instanceof NPC) {
                            NPC npc = (NPC) object;

                            try {
                                npc.copy(id,null);
                            } catch (UnsupportedOperationException unsupported) {
                                throw new RuntimeException(
                                        "Custom npc does not override copy which is required: "
                                                + component,
                                        unsupported);
                            }

                            npcs.put(id, npc);
                        } else {
                            throw new RuntimeException("Object is not an instance of npc: [component="
                                    + component + ", npc=" + npcClass + "]");
                        }
                    } catch (InstantiationException | IllegalAccessException
                            | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException("Unable to parse custom npc component: [component="
                                + component + ", npc=" + npcClass + "]", e);
                    }
                }
            }
        }
    }

    /**
     * Creates a new instance of the npc for the given type, with the given index, if it exists. If
     * it does not exist, null is returned.
     *
     * @param type the type of npc.
     * @param index the index of the npc.
     * @return creates the npc using the copy function using the given index, or returns null.
     */
    public NPC createCopyOrNull(int type, Position position) {
        NPC npc = npcs.get(type);

        if (npc == null) {
            return null;
        }

        return npc.copy(type, position);
    }

    /**
     * The single instance to this class for global access.
     *
     * @return the single instance.
     */
    public static NPCMap getSingleton() {
        return singleton;
    }

}