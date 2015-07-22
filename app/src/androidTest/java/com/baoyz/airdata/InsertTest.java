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

import android.app.Application;
import android.test.ApplicationTestCase;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.baoyz.airdata.crud.Select;
import com.baoyz.airdata.crud.Update;
import com.baoyz.airdata.model.Student;
import com.baoyz.airdata.model.Student2;

import java.util.List;

/**
 * AirData
 * Created by baoyz on 15/7/22.
 */
public class InsertTest extends ApplicationTestCase<Application> {

    public InsertTest() {
        super(Application.class);
    }

    public void testInsert() {
        MyDatabase database = new MyDatabase(getContext());

        Student stu = new Student();
    }

    public void testBulkInsertNoTransaction() {
        MyDatabase database = new MyDatabase(getContext());
        for (int i = 0; i < 1000; i++) {
            Student stu = new Student();
            stu.setMark((char) (i % 24 + 64));
            stu.setName("name" + i);
            stu.setScore(i);
            database.save(stu);
        }
    }

    public void testBulkInsertWithTransaction() {
        MyDatabase database = new MyDatabase(getContext());
        database.beginTransaction();
        for (int i = 0; i < 10000; i++) {
            Student stu = new Student();
            stu.setMark((char) (i % 24 + 64));
            stu.setName("name" + i);
            stu.setScore(i);
            database.save(stu);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public void testActiveAndroidBulkInsertWithTransaction() {
        ActiveAndroid.initialize(getContext());
        ActiveAndroid.beginTransaction();
        for (int i = 0; i < 10000; i++) {
            Student2 stu = new Student2();
            stu.setMark((char) (i % 24 + 64));
            stu.setName("name" + i);
            stu.setScore(i);
            stu.save();
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
    }

    public void testAirDataSelect() {
        MyDatabase database = new MyDatabase(getContext());
//        List<Student> list = database.query(Student.class);
        List<Student> list = new Select<Student>(database).from(Student.class).limit("0, 100000").list();
//        List<Student> list = new Select<Student>(database).from(Student.class).where("Score > ? and Score < 50000", "1000").limit("1000, 100").list();
//        assertEquals(list.size(), new Select<Student>(database).from(Student.class).count());
    }

    public void testActiveAndroidSelect(){
        ActiveAndroid.initialize(getContext());
//        List<Model> list = new com.activeandroid.query.Select().from(Student2.class).execute();
        List<Model> list = new com.activeandroid.query.Select().from(Student2.class).limit("0, 100000").execute();
//        List<Student2> list = new com.activeandroid.query.Select().from(Student2.class).where("Score > ? and Score < 50000", "1000").limit("1000, 100").execute();
//        assertEquals(list.size(), new com.activeandroid.query.Select().from(Student2.class).count());
    }

    public void testUpdateById() {
        MyDatabase database = new MyDatabase(getContext());
        int id = new Select<Student>(database).from(Student.class).single().getId();
        long update = new Update(database)
                .from(Student.class)
                .set("mark", 'D')
                .set("score", 70)
                .where("id=?", id)
                .execute();
        assertTrue(update == 1);
    }

}
