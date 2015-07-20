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

import android.database.Cursor;

import com.baoyz.airdata.AbstractDatabase;
import com.baoyz.airdata.AirDatabaseHelper;

import java.util.List;

/**
 * AirData
 * Created by baoyz on 15/7/19.
 */
public class Select<T> {

    /*
    boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit
     */

    private boolean distinct;
    private Class<T> table;
    private String[] columns;
    private String selection;
    private String[] selectionArgs;
    private String groupBy;
    private String having;
    private String orderBy;
    private Integer limit;
    private Integer offest;

    private AbstractDatabase database;
    private AirDatabaseHelper helper;

    public Select(AbstractDatabase database) {
        this.database = database;
        this.helper = database.getDatabaseHelper();
    }

    public Select columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public Select from(Class<T> table) {
        this.table = table;
        return this;
    }

    public Select distinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public Select where(String selection, String... selectionArgs) {
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        return this;
    }

    public Select groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public Select having(String having) {
        this.having = having;
        return this;
    }

    public Select orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public Select limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Select offset(int offset) {
        this.offest = offset;
        return this;
    }

    public int count() {
        String limitString = limit == null ? null : limit.toString();
        if (offest != null) {
            limitString += ", " + offest;
        }

        columns = new String[]{"COUNT(*)"};

        Cursor cursor = helper.rawQuery(table, distinct, columns, selection, selectionArgs, groupBy, having, orderBy, limitString);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }

    public T single() {
        List<T> list = list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<T> list() {
        String limitString = limit == null ? null : limit.toString();
        if (offest != null) {
            limitString += ", " + offest;
        }

        return helper.query(table, distinct, columns, selection, selectionArgs, groupBy, having, orderBy, limitString);
    }

}
