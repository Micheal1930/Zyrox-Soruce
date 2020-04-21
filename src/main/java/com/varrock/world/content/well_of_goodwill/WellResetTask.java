package com.varrock.world.content.well_of_goodwill;

import java.time.Duration;

import com.varrock.GameServer;
import com.varrock.GameSettings;
import com.varrock.engine.task.Task;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class WellResetTask extends Task {

    private static final int RESET_TICKS = 12_000; // 12,000 ticks, 2 hours.
    private final WellOfGoodwill well;

    WellResetTask(WellOfGoodwill well) {
        super(RESET_TICKS, false);
        this.well = well;
    }

    @Override
    protected void execute() {
        if (well.isActive()) {
            well.setCurrentBenefit(WellBenefit.NONE);
            well.playersDonated.clear();
            well.sendGlobalMessage("The well has expired! All benefits are now inactive and donations have been reset.");
            GameServer.getEngine().submit(well.wellDatabase::delete);
            stop();
        }
    }

    @Override
    public void stop() {
        setEventRunning(false);
        well.wellResetTask = null;
    }

    String getRemainingTime() {
        Duration durationLeft = Duration.ofMillis(getCountdown() * 600);
        long hours = durationLeft.toHours();
        long minutes = durationLeft.minusHours(hours).toMinutes();
        if (hours > 0 && minutes > 0) {
            return hours + " hour(s) and " + minutes + " minute(s)";
        } else if (hours > 0) {
            return hours + " hour(s)";
        } else if (minutes > 0) {
            return minutes + " minute(s)";
        }
        return null;
    }
}