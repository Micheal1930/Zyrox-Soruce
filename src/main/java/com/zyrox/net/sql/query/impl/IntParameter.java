package com.zyrox.net.sql.query.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.zyrox.net.sql.query.Parameter;

/**
 * Created by Jason MK on 2018-07-12 at 3:01 PM
 */
public class IntParameter extends Parameter<Integer> {

    public IntParameter(int parameterId, Integer value) {
        super(parameterId, value);
    }

    @Override
    public void set(PreparedStatement statement) throws SQLException {
        statement.setInt(getParameterId(), super.getValue());
    }

}
