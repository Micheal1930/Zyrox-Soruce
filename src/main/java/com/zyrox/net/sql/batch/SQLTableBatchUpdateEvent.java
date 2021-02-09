package com.zyrox.net.sql.batch;

import java.util.ArrayList;
import java.util.List;

import com.zyrox.GameServer;
import com.zyrox.model.event.CycleEvent;
import com.zyrox.model.event.CycleEventContainer;
import com.zyrox.net.sql.table.SQLTableEntry;
import com.zyrox.net.sql.transactions.SQLNetworkTransaction;

/**
 * Created by Jason MK on 2018-08-13 at 10:43 AM
 */
public abstract class SQLTableBatchUpdateEvent<T extends SQLTableEntry> extends CycleEvent<Object> {

    private final List<T> pending;

    private final int thresholdBeforeUpdate;

    private boolean forceUpdate;

    public SQLTableBatchUpdateEvent(int thresholdBeforeUpdate) {
        this.thresholdBeforeUpdate = thresholdBeforeUpdate;
        this.pending = createBackingList();
    }

    public void submit(T entry) {
        pending.add(entry);
    }

    @Override
    public void execute(CycleEventContainer<Object> container) {
        update();
    }

    private void update() {
        if (!forceUpdate && pending.size() < thresholdBeforeUpdate) {
            return;
        }
        forceUpdate = false;
        GameServer.getSqlNetwork().submit(createTransaction(new ArrayList<>(pending)));
        pending.clear();
    }

    public void forceUpdate() {
        forceUpdate = true;
    }

    public void forceUpdateNow() {
        forceUpdate();
        update();
    }

    protected abstract List<T> createBackingList();

    protected abstract SQLNetworkTransaction createTransaction(List<T> pending);

    @Override
    public void stop() {
        forceUpdateNow();
    }
}
