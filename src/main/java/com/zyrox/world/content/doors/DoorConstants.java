package com.zyrox.world.content.doors;

import java.util.ArrayList;
import java.util.List;

import com.zyrox.model.Position;
import com.zyrox.util.Misc;
import com.zyrox.world.HashedPosition;

/**
 * @author Jonny
 */
public class DoorConstants {

    public static List<Integer> DOUBLE_DOOR_IDS = new ArrayList<>();
    public static List<Integer> LEFT_HINGE_DOOR_IDS = new ArrayList<>();
    public static List<Integer> RIGHT_HINGE_DOOR_IDS = new ArrayList<>();
    public static List<Integer> DOUBLE_WIDE_GATE_IDS = new ArrayList<>();
    public static List<Integer> CUSTOM_DOOR_IDS = new ArrayList<>();
    public static List<HashedPosition> UNCLOSEABLE_POSITIONS = new ArrayList<>();

    static {
        /**
         * Left side hinge door ids
         * These are generally double doors
         */
        LEFT_HINGE_DOOR_IDS.add(1513);
        LEFT_HINGE_DOOR_IDS.add(1516);
        LEFT_HINGE_DOOR_IDS.add(1536);
        LEFT_HINGE_DOOR_IDS.add(1533);
        LEFT_HINGE_DOOR_IDS.add(28479);
        LEFT_HINGE_DOOR_IDS.add(1521);
        LEFT_HINGE_DOOR_IDS.add(1727);
        LEFT_HINGE_DOOR_IDS.add(6451);
        LEFT_HINGE_DOOR_IDS.add(1732);
        LEFT_HINGE_DOOR_IDS.add(1568);
        LEFT_HINGE_DOOR_IDS.add(1557);
        LEFT_HINGE_DOOR_IDS.add(1551);
        LEFT_HINGE_DOOR_IDS.add(1596);
        LEFT_HINGE_DOOR_IDS.add(15644);

        LEFT_HINGE_DOOR_IDS.add(1513 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1516 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1536 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1533 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(28479 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1521 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1727 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(6451 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1732 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1568 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1558 + 100_000);
        LEFT_HINGE_DOOR_IDS.add(1596 + 100_000);

        /**
         * Right side hinge door ids
         */
        RIGHT_HINGE_DOOR_IDS.add(1511);
        RIGHT_HINGE_DOOR_IDS.add(1519);
        RIGHT_HINGE_DOOR_IDS.add(1530);
        RIGHT_HINGE_DOOR_IDS.add(4465);
        RIGHT_HINGE_DOOR_IDS.add(4467);
        RIGHT_HINGE_DOOR_IDS.add(3014);
        RIGHT_HINGE_DOOR_IDS.add(3017);
        RIGHT_HINGE_DOOR_IDS.add(3018);
        RIGHT_HINGE_DOOR_IDS.add(3019);
        RIGHT_HINGE_DOOR_IDS.add(1535);
        RIGHT_HINGE_DOOR_IDS.add(1558);
        RIGHT_HINGE_DOOR_IDS.add(11775);
        RIGHT_HINGE_DOOR_IDS.add(28480);
        RIGHT_HINGE_DOOR_IDS.add(1524);
        RIGHT_HINGE_DOOR_IDS.add(1728);
        RIGHT_HINGE_DOOR_IDS.add(6452);
        RIGHT_HINGE_DOOR_IDS.add(1733);
        RIGHT_HINGE_DOOR_IDS.add(1569);
        RIGHT_HINGE_DOOR_IDS.add(1560);
        RIGHT_HINGE_DOOR_IDS.add(1553);
        RIGHT_HINGE_DOOR_IDS.add(1597);
        RIGHT_HINGE_DOOR_IDS.add(15641);

        RIGHT_HINGE_DOOR_IDS.add(1511 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1519 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1530 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(4465 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(4467 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(3014 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(3017 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(3018 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(3019 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1535 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(11775 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(28480 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1524 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1728 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(6452 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1733 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1569 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1560 + 100_000);
        RIGHT_HINGE_DOOR_IDS.add(1597 + 100_000);

        /**
         * These are doors that are meant to be custom
         * Example: warriors guild doors, etc
         */
        CUSTOM_DOOR_IDS.add(10043);
        CUSTOM_DOOR_IDS.add(11834);
        CUSTOM_DOOR_IDS.add(11727);
        CUSTOM_DOOR_IDS.add(11726);
        CUSTOM_DOOR_IDS.add(2309);

        CUSTOM_DOOR_IDS.add(10043 + 100_000);
        CUSTOM_DOOR_IDS.add(11834 + 100_000);
        CUSTOM_DOOR_IDS.add(11727 + 100_000);
        CUSTOM_DOOR_IDS.add(11726 + 100_000);

        /**
         * Gates that swing fully open together
         */
        DOUBLE_WIDE_GATE_IDS.add(1553);
        DOUBLE_WIDE_GATE_IDS.add(1560);

        DOUBLE_WIDE_GATE_IDS.add(1560 + 100_000);

        DOUBLE_DOOR_IDS.add(15644);
        DOUBLE_DOOR_IDS.add(15641);

        /**
         * Areas where doors can't be closed
         */
        UNCLOSEABLE_POSITIONS.add(new HashedPosition(new Position(2957, 3820)));
        UNCLOSEABLE_POSITIONS.add(new HashedPosition(new Position(2957, 3821)));
        UNCLOSEABLE_POSITIONS.add(new HashedPosition(new Position(3081, 3493)));
        UNCLOSEABLE_POSITIONS.add(new HashedPosition(new Position(1868, 5348)));
        UNCLOSEABLE_POSITIONS.add(new HashedPosition(new Position(1868, 5347)));
    }

    public enum Direction {

        WEST(0),
        NORTH(1),
        EAST(2),
        SOUTH(3);

        private int directionId;

        Direction(int direction) {
            this.directionId = direction;
        }

        public int getDirectionId() {
            return directionId;
        }

        public static Direction getDirection(int direction) {
            for(Direction dir : Direction.values()) {
                if(dir.getDirectionId() == direction) {
                    return dir;
                }
            }
            Misc.print("Invalid direction for door!");
            return Direction.WEST;
        }
    }
}
