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
package com.baoyz.airdata.model;

import com.baoyz.airdata.annotation.ColumnIgnore;
import com.baoyz.airdata.annotation.PrimaryKey;
import com.baoyz.airdata.annotation.Table;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

/**
 * AirData
 * Created by baoyz on 15/6/28.
 */
@Table
public class Person {

    @PrimaryKey
    private int id;
    private String name;
    private int age;
    private byte[] bytes;
    private short s;
    private byte b;
    @ColumnIgnore
    private long l;
    @ColumnIgnore
    private double d;
    private float f;
    @ColumnIgnore
    private char c;
    @ColumnIgnore
    private boolean bool;
    @ColumnIgnore
    private Integer i2;
    private Byte b2;
    @ColumnIgnore
    private Short s2;
    @ColumnIgnore
    private Byte[] bytes2;
    @ColumnIgnore
    private Long l2;
    @ColumnIgnore
    private Double d2;
    @ColumnIgnore
    private Character c2;
    @ColumnIgnore
    private Boolean bool2;

    private Date date;

    public List list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public short getS() {
        return s;
    }

    public void setS(short s) {
        this.s = s;
    }

    public byte getB() {
        return b;
    }

    public void setB(byte b) {
        this.b = b;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public Integer getI2() {
        return i2;
    }

    public void setI2(Integer i2) {
        this.i2 = i2;
    }

    public Byte getB2() {
        return b2;
    }

    public void setB2(Byte b2) {
        this.b2 = b2;
    }

    public Short getS2() {
        return s2;
    }

    public void setS2(Short s2) {
        this.s2 = s2;
    }

    public Byte[] getBytes2() {
        return bytes2;
    }

    public void setBytes2(Byte[] bytes2) {
        this.bytes2 = bytes2;
    }

    public Long getL2() {
        return l2;
    }

    public void setL2(Long l2) {
        this.l2 = l2;
    }

    public Double getD2() {
        return d2;
    }

    public void setD2(Double d2) {
        this.d2 = d2;
    }

    public Character getC2() {
        return c2;
    }

    public void setC2(Character c2) {
        this.c2 = c2;
    }

    public Boolean getBool2() {
        return bool2;
    }

    public void setBool2(Boolean bool2) {
        this.bool2 = bool2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", bytes=" + Arrays.toString(bytes) +
                ", s=" + s +
                ", b=" + b +
                ", l=" + l +
                ", d=" + d +
                ", f=" + f +
                ", c=" + c +
                ", bool=" + bool +
                ", i2=" + i2 +
                ", b2=" + b2 +
                ", s2=" + s2 +
                ", bytes2=" + Arrays.toString(bytes2) +
                ", l2=" + l2 +
                ", d2=" + d2 +
                ", c2=" + c2 +
                ", bool2=" + bool2 +
                '}';
    }
}
