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

/**
 * AirData
 * Created by baoyz on 15/6/28.
 */
public class HelperCreator {

    public static String createCode(String packageName, TableInfo tableInfo){
        String code = "package com.baoyz.airdata;\n" +
                "\n" +
                "import android.content.ContentValues;\n" +
                "import android.database.Cursor;\n" +
                "import android.database.sqlite.SQLiteDatabase;\n" +
                "\n" +
                "public class AirSQLHelper {\n" +
                "\n" +
                "    public SQLiteDatabase database;\n" +
                "\n" +
                "    public long insert(Person bean) {\n" +
                "        ContentValues values = new ContentValues();\n" +
                "        values.put(\"name\", bean.getName());\n" +
                "        return database.insert(\"person\", null, values);\n" +
                "    }\n" +
                "\n" +
                "    public Cursor query(Person person) {\n" +
                "        return database.query(\"person\", null, null, null, null, null, null);\n" +
                "    }\n" +
                "\n" +
                "}\n";

        return code;
    }
}
