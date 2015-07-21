/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 baoyongzhang <baoyz94@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.baoyz.airdata.crud;

import android.content.ContentValues;

import com.baoyz.airdata.AbstractDatabase;
import com.baoyz.airdata.AirDatabaseHelper;
import com.baoyz.airdata.ContentValuesWrapper;

/**
 * AirData
 * Created by baoyz on 15/7/19.
 */
public class Update {

    private Class table;
    private String where;
    private String[] whereArgs;
    private ContentValuesWrapper values;

    private AbstractDatabase database;
    private AirDatabaseHelper helper;

    public Update(AbstractDatabase database) {
        this.database = database;
        this.helper = database.getDatabaseHelper();
        values = ContentValuesWrapper.wrap(new ContentValues());
    }

    public Update from(Class table) {
        this.table = table;
        return this;
    }

    public Update where(String where, Object... whereArgs) {
        this.where = where;
        if (whereArgs != null) {
            this.whereArgs = new String[whereArgs.length];
            for (int i = 0; i < whereArgs.length; i++) {
                Object obj = whereArgs[i];
                this.whereArgs[i] = obj == null ? null : obj.toString();
            }
        }
        return this;
    }

    public Update set(String name, Boolean value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, Byte value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, Short value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, Integer value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, Long value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, Double value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, Float value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, String value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, byte[] value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, Byte[] value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, char value) {
        values.put(name, value);
        return this;
    }

    public Update set(String name, Character value) {
        values.put(name, value);
        return this;
    }

    public long execute() {
        if (table == null) {
            throw new NullPointerException("table is null");
        }
        return helper.update(table, values, where, whereArgs);
    }

}
