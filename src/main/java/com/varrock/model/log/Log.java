package com.varrock.model.log;

import java.util.ArrayList;

import com.varrock.net.sql.SQLNetwork;
import com.varrock.net.sql.SQLTable;
import com.varrock.net.sql.query.Parameter;

/**
 * Created by Jonny on 7/25/2019
 **/
public class Log {

    public Log() {

    }

    public SQLTable getSqlTable() {
        return null;
    }

    public void submit(LogField... data) {

        int totalFields = data.length;

        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO " + SQLTable.getGameSchemaTable(getSqlTable()) + " (");

        int fieldsProcessed = 0;

        for (LogField logField : data) {
            query.append(logField.getTitle());

            if (fieldsProcessed < totalFields - 1) {
                query.append(",");
            }

            fieldsProcessed++;
        }

        query.append(") VALUES(");

        fieldsProcessed = 0;

        ArrayList<Parameter> parameters = new ArrayList<>();

        for (LogField logField : data) {

            parameters.add(logField.getLogFieldType().getParameter(fieldsProcessed + 1, logField.getData()));

            query.append("?");

            if (fieldsProcessed < totalFields - 1) {
                query.append(",");
            }

            fieldsProcessed++;
        }

        query.append(")");

        SQLNetwork.insert(
                query.toString(),
                parameters.toArray(new Parameter[0])
        );
    }

}
