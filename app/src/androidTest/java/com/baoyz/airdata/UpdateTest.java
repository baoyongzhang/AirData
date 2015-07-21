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
import com.baoyz.airdata.crud.Update;
import com.baoyz.airdata.model.Student;

/**
 * AirData
 * Created by baoyz on 15/7/21.
 */
public class UpdateTest extends ApplicationTestCase<Application> {

    public UpdateTest() {
        super(Application.class);
    }

    public void testUpdateAll() {
        MyDatabase database = new MyDatabase(getContext());
        long update = new Update(database).from(Student.class).set("mark", 'T').set("score", 60).execute();
        assertTrue(update > 0);
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
