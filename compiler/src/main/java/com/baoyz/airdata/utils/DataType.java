package com.baoyz.airdata.utils;

import com.baoyz.airdata.serializer.ValueSerializer;

import java.util.HashMap;

import javax.lang.model.type.TypeMirror;

/**
 * Created by baoyz on 15/7/17.
 */
public class DataType {

//    AirData: type = java.lang.String
//    AirData: type = int
//    AirData: type = byte[]
//    AirData: type = short
//    AirData: type = byte
//    AirData: type = long
//    AirData: type = double
//    AirData: type = float
//    AirData: type = char
//    AirData: type = boolean
//    AirData: type = java.lang.Integer
//    AirData: type = java.lang.Byte
//    AirData: type = java.lang.Short
//    AirData: type = java.lang.Byte[]
//    AirData: type = java.lang.Long
//    AirData: type = java.lang.Double
//    AirData: type = java.lang.Character
//    AirData: type = java.lang.Boolean
//    AirData: type = java.util.Date


    public enum SQLiteType {
        INTEGER, REAL, TEXT, BLOB
    }

    private static final HashMap<String, SQLiteType> TYPE_MAP = new HashMap<String, SQLiteType>() {
        {
            put("byte", SQLiteType.INTEGER);
            put("short", SQLiteType.INTEGER);
            put("int", SQLiteType.INTEGER);
            put("long", SQLiteType.INTEGER);
            put("float", SQLiteType.REAL);
            put("double", SQLiteType.REAL);
            put("boolean", SQLiteType.INTEGER);
            put("char", SQLiteType.TEXT);
            put("byte[]", SQLiteType.BLOB);
            put("java.lang.Byte", SQLiteType.INTEGER);
            put("java.lang.Short", SQLiteType.INTEGER);
            put("java.lang.Integer", SQLiteType.INTEGER);
            put("java.lang.Long", SQLiteType.INTEGER);
            put("java.lang.Float", SQLiteType.REAL);
            put("java.lang.Double", SQLiteType.REAL);
            put("java.lang.Boolean", SQLiteType.INTEGER);
            put("java.lang.Character", SQLiteType.TEXT);
            put("java.lang.String", SQLiteType.TEXT);
            put("java.lang.Byte[]", SQLiteType.BLOB);
            put("java.util.Date", SQLiteType.INTEGER);
        }
    };

//    cursor.getBlob();
//    cursor.getDouble();
//    cursor.getFloat();
//    cursor.getInt();
//    cursor.getLong();
//    cursor.getShort();
//    cursor.getString();

    private static final HashMap<String, String> METHOD_MAP = new HashMap<String, String>() {
        {
            put("byte", "getByte");
            put("short", "getShort");
            put("int", "getInt");
            put("long", "getLong");
            put("float", "getFloat");
            put("double", "getDouble");
            put("boolean", "getBoolean");
            put("char", "getChar");
            put("byte[]", "getBlob");
            put("java.lang.Byte", "getByte");
            put("java.lang.Short", "getShort");
            put("java.lang.Integer", "getInt");
            put("java.lang.Long", "getLong");
            put("java.lang.Float", "getFloat");
            put("java.lang.Double", "getDouble");
            put("java.lang.Boolean", "getBoolean");
            put("java.lang.Character", "getChar");
            put("java.lang.String", "getString");
            put("java.lang.Byte[]", "getBytes");
            put("java.util.Date", "getDate");
        }
    };

    public static boolean isBoolean(TypeMirror type) {
        return "boolean".equals(type.toString());
    }

    public static String getTypeString(TypeMirror type) {
        SQLiteType sqLiteType = TYPE_MAP.get(type.toString());
        if (sqLiteType != null) {
            switch (sqLiteType) {
                case INTEGER:
                    return "INTEGER";
                case REAL:
                    return "REAL";
                case TEXT:
                    return "TEXT";
                case BLOB:
                    return "BLOB";
            }
        }
        return "NULL";
    }

    public static String getCursorMethod(TypeMirror type) {
        return METHOD_MAP.get(type.toString());
    }

    public static boolean isSupport(TypeMirror type) {
        return getCursorMethod(type) != null;
    }

    public static ValueSerializer getSerializer(TypeMirror type) {
        return null;
    }
}
