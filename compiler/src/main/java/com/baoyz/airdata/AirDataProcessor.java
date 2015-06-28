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
import com.baoyz.airdata.annotation.ColumnIgnore;
import com.baoyz.airdata.annotation.Database;
import com.baoyz.airdata.annotation.Table;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

/**
 * AirData
 * Created by baoyz on 15/6/28.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({
        "com.baoyz.airdata.annotation.Database",
        "com.baoyz.airdata.annotation.Table"
})
public class AirDataProcessor extends AbstractProcessor {

    public static final boolean DEBUG = true;

    private Filer filer;
    private List<TableInfo> tables;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> tableElements = roundEnv.getElementsAnnotatedWith(Table.class);
        processTables((Set<? extends TypeElement>) tableElements);
        Set<? extends Element> databaseElements = roundEnv.getElementsAnnotatedWith(Database.class);
        processDatabases((Set<? extends TypeElement>) databaseElements);
        return true;
    }

    private void processTables(Set<? extends TypeElement> tableElements) {
        tables = new ArrayList<>();
        for (TypeElement tableElement : tableElements) {

            TableInfo table = new TableInfo();
            tables.add(table);

            String name = getTableName(tableElement);
            table.setName(name);

            // 获取列信息
            List<? extends Element> enclosedElements = tableElement
                    .getEnclosedElements();
            for (Element element : enclosedElements) {
                if (element instanceof VariableElement
                        && isValid((VariableElement) element)) {
                    ColumnInfo column = new ColumnInfo();
                    table.addColumn(column);
                    column.setName(getColumnName((VariableElement) element));
                    // TODO 暂时全部使用TEXT类型
                    column.setType("TEXT");
                }
            }
        }
    }

    private boolean isValid(VariableElement element) {
        Set<Modifier> modifiers = element.getModifiers();
        return !modifiers.contains(Modifier.STATIC)
                && !modifiers.contains(Modifier.FINAL)
                && !modifiers.contains(Modifier.TRANSIENT)
                && element.getAnnotation(ColumnIgnore.class) == null;
    }

    private String getTableName(TypeElement enclosingElement) {
        Table table = enclosingElement.getAnnotation(Table.class);
        String tableName = table.name();
        if (tableName == null || table.name().length() < 1) {
            return enclosingElement.getSimpleName().toString();
        }
        return table.name();
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

    private void processDatabases(Set<? extends TypeElement> databaseElements) {
        if (tables == null)
            return;
        for (TypeElement element : databaseElements) {
            String name = getDatabaseName(element);
            int version = getDatabaseVersion(element);

            // 生成SQLiteOpenHelper
            StringBuilder sb = new StringBuilder();
            String tableDefinition = "CREATE TABLE IF NOT EXISTS %s (%s);";
            for (TableInfo table : tables) {
                String sql = String.format(tableDefinition, table.getName(), table.getColumnDefinitions());
                log(sql);
                sb.append("db.execSQL(\"").append(sql).append("\");\n");
            }

            String qualifiedName = element.getQualifiedName().toString();
            String packageName = qualifiedName
                    .substring(0, qualifiedName.lastIndexOf("."));
            String className = qualifiedName.substring(packageName.length() + 1) + "Helper";

            String helperDefinition = "package " + packageName + ";\n" +
                    "\n" +
                    "import android.content.Context;\n" +
                    "import android.database.sqlite.SQLiteDatabase;\n" +
                    "import android.database.sqlite.SQLiteOpenHelper;" +
                    "\n" +
                    "public class " + className + " extends SQLiteOpenHelper {\n" +
                    "\n" +
                    "    public " + className + "(Context context) {\n" +
                    "        super(context, \"" + name + "\", null, " + version + ");\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onCreate(SQLiteDatabase db) {\n" +
                    "        " + sb.toString() +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {\n" +
                    "\n" +
                    "    }\n" +
                    "}";
            log(helperDefinition);
            String fullName = packageName + "." + className;
            try {
                JavaFileObject jfo = filer.createSourceFile(
                        fullName, element);
                Writer writer = jfo.openWriter();
                writer.write(helperDefinition);
                writer.flush();
                writer.close();
                log("Create " + fullName + " success");
            } catch (IOException e) {
                log("Create " + fullName + " failed");
                e.printStackTrace();
            }

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

    static class TableInfo {

        private String name;
        private List<ColumnInfo> columns;

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
    }

    static class ColumnInfo {

        private String type;
        private String name;

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
    }

    void log(String str) {
        if (DEBUG)
            System.out.println("AirData: " + str);
    }
}
