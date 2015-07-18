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
import com.baoyz.airdata.utils.DataType;
import com.baoyz.airdata.utils.LogUtils;
import com.baoyz.airdata.utils.Utils;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * AirData
 * Created by baoyz on 15/6/28.
 */
public class ColumnInfo {

    private String type;
    private String name;
    private String getter;
    private String setter;
    private TypeMirror typeMirror;
    private boolean primaryKey;
    private String definition;

    public ColumnInfo(VariableElement columnElement) {

        typeMirror = columnElement.asType();
        LogUtils.debug("type = " + typeMirror);

        setType(DataType.getTypeString(typeMirror));

        String simpleName = columnElement.getSimpleName().toString();
        if (Utils.isPublic(columnElement)) {
            setGetter(simpleName);
            setSetter(simpleName + " = $L");
        } else {
            if (DataType.isBoolean(typeMirror)) {
                setGetter("is" + new String(new char[]{simpleName.charAt(0)}).toString().toUpperCase() + simpleName.substring(1) + "()");
            } else {
                setGetter("get" + new String(new char[]{simpleName.charAt(0)}).toString().toUpperCase() + simpleName.substring(1) + "()");
            }
            setSetter("set" + new String(new char[]{simpleName.charAt(0)}).toString().toUpperCase() + simpleName.substring(1) + "($L)");
        }

        // generate column definition
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" ").append(type);
        if (isPrimaryKey()) {
            sb.append(" PRIMARY KEY AUTOINCREMENT");
        }

        String columnName = simpleName;
        Column annotation = columnElement.getAnnotation(Column.class);
        if (annotation != null) {
            String name = annotation.name();
            if (name != null && name.length() > 0) {
                columnName = name;
            }

            if (annotation.notNull()) {
                sb.append(" NOT NULL ON CONFLICT ");
                sb.append(annotation.onNullConflict().toString());
            }

            if (annotation.unique()) {
                sb.append(" UNIQUE ON CONFLICT ");
                sb.append(annotation.onUniqueConflict().toString());
            }
        }

        setName(columnName);

        definition = sb.toString();
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
        return definition;
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

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    public void setTypeMirror(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
