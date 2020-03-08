package com.ceresdata.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQlUtil {
    /**
     * 获取结果集中的表头
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static List<String> getColHeaders(ResultSet resultSet) throws SQLException{
        List<String> headline = new ArrayList<>();
        // 获取元数据头
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int temp = 1; temp <= columnCount; temp++) {
            String columnName = metaData.getColumnName(temp);
            headline.add(columnName);
        }
        return headline;
    }


    /**
     * 获取结果集中vlues
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static List<String> getColValues(ResultSet resultSet,int columnCount) throws SQLException{
        // 获取元数据头
        List<String> row = new ArrayList<>();
        for (int temp = 1; temp <= columnCount; temp++) {
            String rowValue = resultSet.getString(temp);
            row.add(rowValue);
        }
        return row;
    }
}
