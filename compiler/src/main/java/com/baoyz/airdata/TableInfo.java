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
package com.baoyz.airdata;

import com.baoyz.airdata.annotation.Table;
import com.baoyz.airdata.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * AirData
 * Created by baoyz on 15/6/28.
 */
public class TableInfo {

    public static final String TABLE_DEFINITION = "CREATE TABLE IF NOT EXISTS %s (%s);";

    private String qualifiedName;
    private String name;
    private List<ColumnInfo> columns;
    private ColumnInfo primaryKeyColumn;

    public TableInfo(TypeElement tableElement) {
        Table table = tableElement.getAnnotation(Table.class);
        String tableName = table.name();
        if (tableName == null || table.name().length() < 1) {
            name = tableElement.getSimpleName().toString();
        } else {
            name = table.name();
        }
        qualifiedName = tableElement.getQualifiedName().toString();

    }

    public void addColumn(ColumnInfo column) {
        if (columns == null)
            columns = new ArrayList<>();
        columns.add(column);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public String getColumnDefinitions() {
        return TextUtils.join(",", getColumns());
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getTableDefinition(){
        return String.format(TABLE_DEFINITION, getName(), getColumnDefinitions());
    }

    public String getPackageName(){
        return qualifiedName
                .substring(0, qualifiedName.lastIndexOf("."));
    }

    public String getDaoClassName(){
        return getClassName() + "$$DAO";
    }

    public String getClassName(){
        return qualifiedName.substring(getPackageName().length() + 1);
    }

    public ColumnInfo getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setPrimaryKeyColumn(ColumnInfo primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }
}
