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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baoyz.airdata.model.Person;

public class AirSQLHelper {

    public static final String TABLE_NAME = "person";

    public SQLiteDatabase database;

    public AirSQLHelper(SQLiteDatabase database) {
        this.database = database;
    }

    public long insert(Person bean) {
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("age", bean.getAge());
        return database.insert(TABLE_NAME, null, values);
    }

    public int delete(Person bean) {
        return database.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(bean.getId())});
    }

    public int update(Person bean) {
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("age", bean.getAge());
        return database.update(TABLE_NAME, values, "id=?", new String[]{String.valueOf(bean.getId())});
    }

    public Cursor query(Person person) {
        return database.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public static void main(String[] args) {
//        MethodSpec constructor = MethodSpec.constructorBuilder()
//                .addModifiers(Modifier.PUBLIC)
//                .addParameter(ClassName.get("android.database.sqlite", "SQLiteDatabase"), "db")
//                .addStatement("this.$T = $T", "database", "db")
//                .build();
//
//        MethodSpec.Builder insertBuilder = MethodSpec.methodBuilder("insert")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(TypeName.LONG)
//                .addParameter(ClassName.get("com.baoyz.airdata.model", "Person"), "bean");
//
//        MethodSpec.Builder deleteBuilder = MethodSpec.methodBuilder("delete")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(TypeName.INT)
//                .addParameter(ClassName.get("com.baoyz.airdata.model", "Person"), "bean");
//
//        MethodSpec.Builder updateBuilder = MethodSpec.methodBuilder("update")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(TypeName.INT)
//                .addParameter(ClassName.get("com.baoyz.airdata.model", "Person"), "bean");
//
//        MethodSpec.Builder queryBuilder = MethodSpec.methodBuilder("query")
//                .addModifiers(Modifier.PUBLIC)
//                .returns(ClassName.get("android.database", "Cursor"))
//                .addParameter(ClassName.get("com.baoyz.airdata.model", "Person"), "bean");
//
//
//        TypeSpec typeSpec = TypeSpec.classBuilder("Person" + "$$DAO")
//                .addModifiers(Modifier.PUBLIC)
//                .addMethod(constructor)
//                .addMethod(insertBuilder.build())
//                .addMethod(deleteBuilder.build())
//                .addMethod(updateBuilder.build())
//                .addMethod(queryBuilder.build())
//                .build();
//
//        JavaFile javaFile = JavaFile.builder("com.baoyz.airdata", typeSpec)
//                .build();
//
//        try {
//            javaFile.writeTo(System.out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
