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

import com.baoyz.airdata.AbstractDatabase;
import com.baoyz.airdata.AirDatabaseHelper;

/**
 * AirData
 * Created by baoyz on 15/7/19.
 */
public class Delete<T> {

    private Class<T> table;
    private String where;
    private String[] whereArgs;

    private AbstractDatabase database;
    private AirDatabaseHelper helper;

    public Delete(AbstractDatabase database) {
        this.database = database;
        this.helper = database.getDatabaseHelper();
    }

    public Delete from(Class<T> table) {
        this.table = table;
        return this;
    }

    public Delete where(String where, String... whereArgs) {
        this.where = where;
        this.whereArgs = whereArgs;
        return this;
    }

    public long execute() {
        return helper.delete(table, where, whereArgs);
    }

}
