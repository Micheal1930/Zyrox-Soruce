package com.zyrox.world.clip.region;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jonny
 */
public class OldschoolMaps {

    private static final Map<Integer, Integer> OLDSCHOOL_REGIONS = new HashMap<>();

    public static int[] OLDSCHOOL_REGION_IDS = {
            5947, //south of blast mine
            8536, //demonic gorillas
            8280, //demonic gorillas
            8789,
            9619,
            6457, // Great Kourand

            12867, //tob treasure

            10894, //monkey skeletons
            11150, //monkey skeletons

            12342,
            10279,
            10023,
            10280,
            1024,
            11561,

14386, 14642,
            4919, 5022, 5023, 5279, 5280, 5535, 5945,
            5946, 6201, 5536, 4663, 6810, 9023, 9043, 11850, 11851, 12090, 12106, 12362, 12363, 12347, 12605, 12611,
            12701, 12702, 12703, 12861, 12889, 12957, 12958, 12959, 12961,

            7987, 8243, 7986, 8242,

            12192,

            11837, //wildy agility
            13972,
    };

    static {
        for(Integer region : OLDSCHOOL_REGION_IDS) {
            OLDSCHOOL_REGIONS.put(region, -1);
        }
    }

    public static boolean isOldschoolRegion(int regionId) {
        return OLDSCHOOL_REGIONS.get(regionId) != null;
    }
}
