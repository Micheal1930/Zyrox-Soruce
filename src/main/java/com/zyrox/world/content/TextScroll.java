package com.zyrox.world.content;

import java.util.ArrayDeque;
import java.util.Queue;

import com.zyrox.world.entity.impl.player.Player;

/**
 * @author lare96 <http://github.com/lare96>
 */
public final class TextScroll {

    private String title;
    private int index = 0;
    private final Queue<String> lines = new ArrayDeque<>();

    public TextScroll(String title) {
        this.title = title;
    }

    public void addLine(Object obj) {
        lines.add(obj.toString());
    }

    public void newLine() {
        addLine("");
    }

    public void display(Player player) {
        player.getPacketSender().sendString(8144, title);
        for (; ; ) {
            String nextLine = lines.poll();
            if (nextLine == null) {
                break;
            }
            player.getPacketSender().sendString(getNextLineId(), nextLine);
        }

        for (int i = getCurrentLineIndex(); i <= 100; i++) {
            player.getPacketSender().sendString(getNextLineId(), "");
        }

        player.getPacketSender().sendInterface(8134);
    }

    public void reset() {
        lines.clear();
        title = null;
        index = 0;
    }

    private int getCurrentLineIndex() {
        return index;
    }

    private int getNextLineId() {
        return (index > 50 ? 12174 : 8145) + index++ % 51;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}