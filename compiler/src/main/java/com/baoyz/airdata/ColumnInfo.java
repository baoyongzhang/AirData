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

import com.baoyz.airdata.annotation.Column;
import com.baoyz.airdata.utils.Utils;

import javax.lang.model.element.VariableElement;

/**
 * AirData
 * Created by baoyz on 15/6/28.
 */
public class ColumnInfo {

    private String type;
    private String name;
    private String getter;

    public ColumnInfo(VariableElement columnElement) {
        setName(getColumnName(columnElement));
        // TODO 暂时全部使用TEXT类型
        setType("TEXT");

        String simpleName = columnElement.getSimpleName().toString();
        if (Utils.isPublic(columnElement)) {
            setGetter(simpleName);
        } else {
            setGetter("get" + new String(new char[]{simpleName.charAt(0)}).toString() + simpleName.substring(1));
        }
    }

    private String getColumnName(VariableElement element) {
        Column c = element.getAnnotation(Column.class);
        if (c != null) {
            String columnName = c.name();
            if (columnName != null && columnName.length() > 0) {
                return columnName;
            }
        }
        return element.getSimpleName().toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        StringBuilder definition = new StringBuilder();
        return definition.append(name).append(" ").append(type).toString();
    }

    @Override
    public String toString() {
        return getDefinition();
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }
}