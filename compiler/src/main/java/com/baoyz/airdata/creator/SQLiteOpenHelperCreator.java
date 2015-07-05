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
import com.baoyz.airdata.annotation.Database;
import com.baoyz.airdata.annotation.Table;
import com.baoyz.airdata.utils.LogUtils;
import com.baoyz.airdata.utils.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * AirData
 * Created by baoyz on 15/7/5.
 */
public class SQLiteOpenHelperCreator {

    private Filer filer;
    private ArrayList<TableInfo> tables;
    private String name;
    private int version;
    private String qualifiedName;

    public SQLiteOpenHelperCreator(Filer filer) {
        this.filer = filer;
    }

    public void create(RoundEnvironment roundEnv) {
        Set<? extends Element> tableElements = roundEnv.getElementsAnnotatedWith(Table.class);
        processTables((Set<? extends TypeElement>) tableElements);
        Set<? extends Element> databaseElements = roundEnv.getElementsAnnotatedWith(Database.class);
        processDatabases((Set<? extends TypeElement>) databaseElements);
        generate();
    }

    private void generate() {
        // generate SQLiteOpenHelper
        MethodSpec constructor = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addStatement("super(context, $S, null, $L)", name, version)
                .build();

        MethodSpec.Builder onCreateBuilder = MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeName.VOID)
                .addParameter(ClassName.get("android.database.sqlite", "SQLiteDatabase"), "db");

        // add create table sql statement
        for (TableInfo table : tables) {
            String sql = table.getTableDefinition();
            LogUtils.debug(sql);
            onCreateBuilder.addStatement("db.execSQL($S)", sql);
        }

        MethodSpec onCreate = onCreateBuilder.build();

        MethodSpec onUpgrade = MethodSpec.methodBuilder("onCreate")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(TypeName.VOID)
                .addParameter(ClassName.get("android.database.sqlite", "SQLiteDatabase"), "db")
                .addParameter(TypeName.INT, "oldVersion")
                .addParameter(TypeName.INT, "newVersion")
                .build();

        String packageName = qualifiedName
                .substring(0, qualifiedName.lastIndexOf("."));
        String className = qualifiedName.substring(packageName.length() + 1) + "Helper";

        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(constructor)
                .addMethod(onCreate)
                .addMethod(onUpgrade)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.debug(javaFile.toString());

//        StringBuilder sb = new StringBuilder();
//        String tableDefinition = "CREATE TABLE IF NOT EXISTS %s (%s);";
//        for (TableInfo table : tables) {
//            String sql = String.format(tableDefinition, table.getName(), table.getColumnDefinitions());
//            log(sql);
//            // 创建表的sql
//            sb.append("db.execSQL(\"").append(sql).append("\");\n");
//
//            // insert的方法
//
//        }
//
//
//        String helperDefinition = "package " + packageName + ";\n" +
//                "\n" +
//                "import android.content.Context;\n" +
//                "import android.database.sqlite.SQLiteDatabase;\n" +
//                "import android.database.sqlite.SQLiteOpenHelper;" +
//                "\n" +
//                "public class " + className + " extends SQLiteOpenHelper {\n" +
//                "\n" +
//                "    public " + className + "(Context context) {\n" +
//                "        super(context, \"" + name + "\", null, " + version + ");\n" +
//                "    }\n" +
//                "\n" +
//                "    @Override\n" +
//                "    public void onCreate(SQLiteDatabase db) {\n" +
//                "        " + sb.toString() +
//                "    }\n" +
//                "\n" +
//                "    @Override\n" +
//                "    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {\n" +
//                "\n" +
//                "    }\n" +
//                "}";
//        log(helperDefinition);
//        String fullName = packageName + "." + className;
//        try {
//            JavaFileObject jfo = filer.createSourceFile(
//                    fullName, element);
//            Writer writer = jfo.openWriter();
//            writer.write(helperDefinition);
//            writer.flush();
//            writer.close();
//            log("Create " + fullName + " success");
//        } catch (IOException e) {
//            log("Create " + fullName + " failed");
//            e.printStackTrace();
//        }
    }

    private void processTables(Set<? extends TypeElement> tableElements) {
        tables = new ArrayList<>();
        for (TypeElement tableElement : tableElements) {

            TableInfo table = new TableInfo(tableElement);
            tables.add(table);

            // 获取列信息
            List<? extends Element> enclosedElements = tableElement
                    .getEnclosedElements();
            for (Element element : enclosedElements) {
                if (element instanceof VariableElement
                        && Utils.isValid((VariableElement) element)) {
                    VariableElement columnElement = (VariableElement) element;
                    ColumnInfo column = new ColumnInfo(columnElement);
                    table.addColumn(column);
                }
            }
        }
    }


    private void processDatabases(Set<? extends TypeElement> databaseElements) {
        if (tables == null)
            return;
        for (TypeElement element : databaseElements) {
            name = getDatabaseName(element);
            version = getDatabaseVersion(element);
            qualifiedName = element.getQualifiedName().toString();
        }
    }

    private String getDatabaseName(TypeElement enclosingElement) {
        Database database = enclosingElement.getAnnotation(Database.class);
        String databaseName = database.name();
        if (databaseName == null || database.name().length() < 1) {
            return enclosingElement.getSimpleName().toString();
        }
        return databaseName;
    }

    private int getDatabaseVersion(TypeElement enclosingElement) {
        Database database = enclosingElement.getAnnotation(Database.class);
        return database.version();
    }
}