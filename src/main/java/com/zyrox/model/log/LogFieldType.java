package com.zyrox.model.log;

import com.zyrox.net.sql.query.Parameter;
import com.zyrox.net.sql.query.impl.IntParameter;
import com.zyrox.net.sql.query.impl.LongParameter;
import com.zyrox.net.sql.query.impl.StringParameter;

/**
 * Created by Jonny on 7/25/2019
 **/
public enum LogFieldType {

    STRING {
        @Override
        public Parameter getParameter(int index, Object data) {
            return new StringParameter(index, String.valueOf(data));
        }
    },
    INTEGER {
        @Override
        public Parameter getParameter(int index, Object data) {
            return new IntParameter(index, (int) data);
        }
    },
    LONG {
        @Override
        public Parameter getParameter(int index, Object data) {
            return new LongParameter(index, (long) data);
        }
    };

    public Parameter getParameter(int index, Object data) {
        return null;
    }
}
