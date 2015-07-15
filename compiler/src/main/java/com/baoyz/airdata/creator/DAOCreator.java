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
package com.baoyz.airdata.creator;


import com.baoyz.airdata.ColumnInfo;
import com.baoyz.airdata.TableInfo;
import com.baoyz.airdata.utils.LogUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * AirData
 * Created by baoyz on 15/7/6.
 */
public class DAOCreator {

    private TableInfo table;
    private Filer filer;
    private ClassName beanClassName;

    public DAOCreator(TableInfo table, Filer filer) {
        this.table = table;
        this.filer = filer;
        beanClassName = ClassName.get(table.getPackageName(), table.getClassName());
    }


//    public long insert(Person bean) {
//        ContentValues values = new ContentValues();
//        values.put("name", bean.getName());
//        values.put("age", bean.getAge());
//        return database.insert(TABLE_NAME, null, values);
//    }
//
//    public int delete(Person bean) {
//        return database.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(bean.getId())});
//    }
//
//    public int update(Person bean) {
//        ContentValues values = new ContentValues();
//        values.put("name", bean.getName());
//        values.put("age", bean.getAge());
//        return database.update(TABLE_NAME, values, "id=?", new String[]{String.valueOf(bean.getId())});
//    }
//
//    public Cursor query(Person person) {
//        return database.query(TABLE_NAME, null, null, null, null, null, null);
//    }

    public void create() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("android.database.sqlite", "SQLiteDatabase"), "db")
                .addStatement("this.$L = $L", "database", "db")
                .build();

        MethodSpec insertMethod = generatorInsertMethod(beanClassName);
        MethodSpec updateMethod = generatorUpdateMethod(beanClassName);
        MethodSpec deleteMethod = generatorDeleteMethod(beanClassName);

//        MethodSpec.Builder deleteBuilder = MethodSpec.methodBuilder("delete")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(TypeName.INT)
//                .addParameter(person, "bean");
//
//        MethodSpec.Builder updateBuilder = MethodSpec.methodBuilder("update")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(TypeName.INT)
//                .addParameter(person, "bean");
//
//        MethodSpec.Builder queryBuilder = MethodSpec.methodBuilder("query")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(ClassName.get("android.database", "Cursor"))
//                .addParameter(person, "bean");

        String qualifiedName = table.getQualifiedName();
        String packageName = qualifiedName
                .substring(0, qualifiedName.lastIndexOf("."));
        String className = qualifiedName.substring(packageName.length() + 1) + "$$DAO";

        FieldSpec tableNameField = FieldSpec
                .builder(ClassName.get(String.class), "TABLE_NAME", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$S", table.getName()).build();

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor)
                .addMethod(insertMethod)
                .addMethod(updateMethod)
                .addMethod(deleteMethod)
                .addMethod(generatorQueryMethod())
                .addMethod(generatorFillDataMethod())
//                .addMethod(deleteBuilder.build())
//                .addMethod(updateBuilder.build())
//                .addMethod(queryBuilder.build())
                .addField(tableNameField)
                .addField(ClassName.get("android.database.sqlite", "SQLiteDatabase"), "database", Modifier.PRIVATE)
                .build();

        JavaFile javaFile = JavaFile.builder(table.getPackageName(), typeSpec)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.debug(javaFile.toString());
    }

    private MethodSpec generatorInsertMethod(ClassName person) {
        MethodSpec.Builder insertBuilder = MethodSpec.methodBuilder("insert")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.LONG)
                .addParameter(person, "bean")
                .addStatement("$T values = new $T()", ClassName.get("android.content", "ContentValues"), ClassName.get("android.content", "ContentValues"));

        List<ColumnInfo> columns = table.getColumns();
        for (ColumnInfo column : columns) {
            insertBuilder.addStatement("values.put($S, bean.$L)", column.getName(), column.getGetter());
        }

        insertBuilder.addStatement("return database.insert(TABLE_NAME, null, values)");
        return insertBuilder.build();
    }

    private MethodSpec generatorUpdateMethod(ClassName person) {
        MethodSpec.Builder insertBuilder = MethodSpec.methodBuilder("update")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.LONG)
                .addParameter(person, "bean")
                .addStatement("$T values = new $T()", ClassName.get("android.content", "ContentValues"), ClassName.get("android.content", "ContentValues"));

        List<ColumnInfo> columns = table.getColumns();
        for (ColumnInfo column : columns) {
            if (column.isPrimaryKey())
                continue;
            insertBuilder.addStatement("values.put($S, bean.$L)", column.getName(), column.getGetter());
        }

        insertBuilder.addStatement("return database.update(TABLE_NAME, values, $S, new String[]{String.valueOf(bean.$L)})", table.getPrimaryKeyColumn().getName() + "=?", table.getPrimaryKeyColumn().getGetter());
        return insertBuilder.build();
    }

    private MethodSpec generatorDeleteMethod(ClassName person) {
        MethodSpec.Builder insertBuilder = MethodSpec.methodBuilder("delete")
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.LONG)
                .addParameter(person, "bean")
                .addStatement("return database.delete(TABLE_NAME, $S, new String[]{String.valueOf(bean.$L)})", table.getPrimaryKeyColumn().getName() + "=?", table.getPrimaryKeyColumn().getGetter());
        return insertBuilder.build();
    }

    private MethodSpec generatorQueryMethod() {
        MethodSpec.Builder queryBuilder = MethodSpec.methodBuilder("query")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(List.class))
                .addStatement("$T cursor = database.query(TABLE_NAME, null, null, null, null, null, null)", ClassName.get("android.database", "Cursor"))
                .addCode("");
        queryBuilder.addStatement("$T list = new $T()", ArrayList.class, ArrayList.class);
        queryBuilder.addCode("while (cursor.moveToNext()) {list.add(fillData(cursor));}");
        queryBuilder.addStatement("return list");
        return queryBuilder.build();
    }

    private MethodSpec generatorFillDataMethod() {
        MethodSpec.Builder fillDataMethod = MethodSpec.methodBuilder("fillData")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("android.database", "Cursor"), "cursor")
                .returns(beanClassName)
                .addStatement("$T bean = new $T()", beanClassName, beanClassName);

        List<ColumnInfo> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            ColumnInfo column = columns.get(i);
            // TODO 暂时全部使用getString
            String type;
            if ("int".equals(column.getTypeMirror().toString())) {
                type = "Int";
            } else {
                type = "String";
            }
            fillDataMethod.addStatement("bean." + column.getSetter(), "cursor.get" + type + "(" + i + ")");
        }
        fillDataMethod.addStatement("return bean");

        return fillDataMethod.build();
    }
}
