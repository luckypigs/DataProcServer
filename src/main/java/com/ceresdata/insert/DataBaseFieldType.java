package com.ceresdata.insert;

/**
 * 数据库所有字段类型整理表
 */
public class DataBaseFieldType {
    private static final String DBNULL = "NULL";
    // 流类型的文件
    private static final String[] column_types_Binary = new String[] { "CLOB",
            "BLOB", "DBCLOB", "MEDIUMBLOB", "TINYBLOB", "LONGBLOB", "NCLOB",
            "BFILE", "LONG" };
    // 浮点类型
    private static final String[] column_types_Double = new String[] {
            "NUMBER" ,"DECIMAL", "FLOAT", "DOUBLE", "BINARY_FLOAT", "BINARY_DOUBLE",
            "DOUBLE UNSIGNED", "REAL" };
    // 整数类型
    private static final String[] column_types_Integer = new String[] {
            "BIGINT", "INTEGER", "SMALLINT", "TINYINT", "MEDIUMINT", "INT",
            "INTEGER UNSIGNED"};
    // 字符类型
    private static final String[] column_types_String = new String[] { "CHAR",
            "GRAPHIC", "LONG VARCHAR", "LONG GRAPHIC", "VARCHAR", "VARCHAR2",
            "TEXT", "NCHAR", "VARGRAPHIC", "LONG VARCHAR FOR BIT DATA" };
    // 日期类型
    private static final String[] colunm_types_Date = new String[] {
            "DATETIME", "DATE", "TIMESTAMP" };
    // 特殊的类型：(有的数据库中有，有的则没有)
    private static final String[] column_tyupes_Spesc = new String[] { "BIT",
            "BINARY", "VARBINARY", "YEAR", "TIME", "TIMESTAMPLTZ",
            "TIMESTAMPTZ", "INTERVALDS", "INTERVALYM", "RAW" };


    private boolean findObject(String colType,String[] colArray){
        for(String str : colArray){
            colType.equalsIgnoreCase(str);
            return true;
        }
        return false;
    }

    /**
     * 验证是否是数据库中特别的类型
     *
     * @param col
     *            数据库的列类型
     * @return 如果是特别类型true
     */
    public boolean isSpescColmnType(String col) {
        for (String str : column_tyupes_Spesc) {
            if (str.equalsIgnoreCase(col))
                return true;
        }
        return false;
    }

    /**
     * 验证是否为日期类型
     *
     * @param colType
     *            数据列类型
     *
     * @return 如果是，返回true
     */
    public boolean isDateType(String colType) {
        for (String str : colunm_types_Date) {
            if (str.equalsIgnoreCase(colType))
                return true;
        }
        return false;
    }

    /**
     * 验证是否是整形类型
     *
     * @param colType
     *            列类型
     * @return
     */
    public boolean isIntegerType(String colType) {
        for (int i = 0; i < column_types_Integer.length; i++) {
            if (column_types_Integer[i].equalsIgnoreCase(colType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是否是浮点型类型
     *
     * @param colType
     *            列类型
     * @return
     */
    public boolean isDoubleType(String colType) {
        for (int i = 0; i < column_types_Double.length; i++) {
            if (column_types_Double[i].equalsIgnoreCase(colType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是否是流类型
     *
     * @param colType
     *            列类型
     * @return
     */
    public boolean isBinaryType(String colType) {
        for (int i = 0; i < column_types_Binary.length; i++) {
            if (column_types_Binary[i].equalsIgnoreCase(colType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是否是字符类型类型
     *
     * @param colType
     *            列类型
     * @return
     */
    public boolean isStringType(String colType) {
        for (int i = 0; i < column_types_String.length; i++) {
            if (column_types_String[i].equalsIgnoreCase(colType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证数据列内容是否为NULL
     *
     * @param value
     *            数据的值
     * @return
     */
    public boolean isDBNull(String value) {
        if (DBNULL.equalsIgnoreCase(value)) {
            return true;
        }
        return false;

    }

    public boolean isSameType(String type1, String type2) {
        boolean b = true;
        boolean c = true;
        b = isStringType(type1);
        c = isStringType(type2);
        if (b == c && b == true) {
            return true;
        }
        b = isBinaryType(type1);
        c = isBinaryType(type2);
        if (b == c && b == true) {
            return true;
        }
        return false;
    }

}
