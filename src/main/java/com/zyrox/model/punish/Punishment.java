package com.zyrox.model.punish;

import com.zyrox.util.Stopwatch;

/**
 * Created by Jonny on 8/12/2019
 **/
public class Punishment {

    public String data;

    public PunishmentType type;

    public Stopwatch timer;

    public long duration;

    public Punishment(String data, PunishmentType type, long duration) {
        this.data = data;
        this.type = type;
        this.timer = new Stopwatch().reset();
        this.duration = duration;
    }


}
