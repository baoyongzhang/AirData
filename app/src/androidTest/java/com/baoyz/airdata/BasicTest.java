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

import com.baoyz.airdata.crud.Select;
import com.baoyz.airdata.model.Student;

import java.util.List;

/**
 * AirData
 * Created by baoyz on 15/7/20.
 */
public class BasicTest extends ApplicationTestCase<Application> {

    public BasicTest() {
        super(Application.class);
    }

    public void testAll(){
        testInsert();
        testUpdate();
        testDelete();
        testSelect();
    }

    public void testInsert() {
        Student stu = new Student();
        stu.setName("Jack");
        stu.setMark('a');
        stu.setScore(60);

        MyDatabase database = new MyDatabase(getContext());
        long save = database.save(stu);
        assertFalse(save == -1);
    }

    public void testUpdate() {
        MyDatabase database = new MyDatabase(getContext());
        Student single = new Select<Student>(database).from(Student.class).single();
        single.setName("updateName");

        long update = database.update(single);

        assertTrue(update > 0);
    }

    public void testDelete() {
        MyDatabase database = new MyDatabase(getContext());
        Student single = new Select<Student>(database).from(Student.class).single();

        long delete = database.delete(single);

        assertTrue(delete > 0);
    }

    public void testSelect() {
        List<Student> list = new Select<Student>(new MyDatabase(getContext())).from(Student.class).list();
        for (Student student : list) {
            System.out.println(student);
        }
        assertTrue(list.size() > 0);
    }

    public void testCount() {
        int count = new Select<Student>(new MyDatabase(getContext())).from(Student.class).count();
        List<Student> list = new Select<Student>(new MyDatabase(getContext())).from(Student.class).list();
        assertTrue(count > 0 && list.size() == count);
    }
}
