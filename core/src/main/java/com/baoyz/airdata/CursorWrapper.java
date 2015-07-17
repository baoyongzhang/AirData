package com.baoyz.airdata;

import android.database.Cursor;

/**
 * Created by baoyz on 15/7/17.
 */
public class CursorWrapper {

    private Cursor cursor;

    public static CursorWrapper wrap(Cursor cursor) {
        CursorWrapper cw = new CursorWrapper();
        cw.cursor = cursor;
        return cw;
    }

    public byte getByte(int index) {
        return (byte) cursor.getInt(index);
    }

    public short getShort(int index) {
        return cursor.getShort(index);
    }

    public int getInt(int index) {
        return cursor.getInt(index);
    }

    public long getLong(int index) {
        return cursor.getLong(index);
    }

    public float getFloat(int index) {
        return cursor.getFloat(index);
    }

    public double getDouble(int index) {
        return cursor.getDouble(index);
    }

    public boolean getBoolean(int index) {
        return cursor.getInt(index) != 0;
    }

    public String getString(int index) {
        return cursor.getString(index);
    }

    public char getChar(int index) {
        return (char) cursor.getInt(index);
    }

    public byte[] getBlob(int index) {
        return cursor.getBlob(index);
    }

    public Byte[] getBytes(int index) {
        byte[] arr = cursor.getBlob(index);
        if (arr == null){
            return null;
        }
        Byte[] bytes = new Byte[arr.length];
        System.arraycopy(arr, 0, bytes, 0, arr.length);
        return bytes;
    }

}
