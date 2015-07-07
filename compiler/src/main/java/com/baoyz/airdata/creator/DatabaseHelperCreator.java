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


import com.baoyz.airdata.AirDatabaseHelper;
import com.baoyz.airdata.TableInfo;
import com.baoyz.airdata.utils.LogUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * AirData
 * Created by baoyz on 15/7/7.
 */
public class DatabaseHelperCreator {

    private ArrayList<TableInfo> tables;
    private Filer filer;
    private String packageName;
    private String className;

    public DatabaseHelperCreator(ArrayList<TableInfo> tables, Filer filer, String packageName, String className) {
        this.tables = tables;
        this.filer = filer;
        this.packageName = packageName;
        this.className = className;
    }

    public void create(){

        // dao fields
        List<String> daoFields = new ArrayList<>();

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addStatement("this.helper = new $T(context)", ClassName.get(packageName, className))
                .addStatement("this.database = this.helper.getWritableDatabase()");

        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("AirDatabaseHelperImpl")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(AirDatabaseHelper.class))
                .addField(ClassName.get("android.database.sqlite", "SQLiteDatabase"), "database", Modifier.PRIVATE)
                .addField(ClassName.get(packageName, className), "helper", Modifier.PRIVATE);

        // create save method


        MethodSpec.Builder saveMethodBuidler = MethodSpec.methodBuilder("save")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.LONG)
                .addParameter(ClassName.get(Object.class), "bean");

        for (TableInfo table : tables) {
            String daoField = table.getDaoClassName().replace("$$", "").toLowerCase();

            constructorBuilder.addStatement("this.$L = new $L(this.database)", daoField, table.getDaoClassName());

            typeSpecBuilder.addField(ClassName.get(table.getPackageName(), table.getDaoClassName()), daoField, Modifier.PRIVATE);
            MethodSpec methodSpec = MethodSpec.methodBuilder("save")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.LONG)
                    .addParameter(ClassName.get(table.getPackageName(), table.getClassName()), "bean")
                    .addStatement("return $L.insert(bean)", daoField)
                    .build();
            typeSpecBuilder.addMethod(methodSpec);

            saveMethodBuidler.addStatement("if(bean instanceof $L) return save(($L)bean)", table.getClassName(), table.getClassName());
        }

        saveMethodBuidler.addStatement("return 0");

        typeSpecBuilder.addMethod(saveMethodBuidler.build());

        // destory method
        MethodSpec destoryMethod = MethodSpec.methodBuilder("destory")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.database.close()")
                .build();
        typeSpecBuilder.addMethod(destoryMethod);

        // getDatabase method
        MethodSpec getDatabaseMethod = MethodSpec.methodBuilder("getDatabase")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get("android.database.sqlite", "SQLiteDatabase"))
                .addStatement("return this.database")
                .build();
        typeSpecBuilder.addMethod(getDatabaseMethod);

        typeSpecBuilder.addMethod(constructorBuilder.build());

        JavaFile javaFile = JavaFile.builder("com.baoyz.airdata", typeSpecBuilder.build())
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.debug(javaFile.toString());

    }
}
