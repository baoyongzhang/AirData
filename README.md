# AirData(Developing)
A high performance ORM library

# Introduction

> Can you speak Chinese?
> Yes, I can.
> OK.

AirData是一个“高性能”的Android平台的ORM库，现在只是一个雏形，目前具备最基本的CRUD。
想要自己写一个ORM库的想法已经存在很久了，由于个人比较低产，而且很懒，一拖再拖，再加上经验不足，总是想不好该怎么做。
2015年06月28日，我终于创建了这个项目，在将近一个月的时间（到7月22日）里完成了这个初版（比起Linus两周写Git我真是弱爆了）。

目前AirData还不是一个成熟的库，我还在继续开发中，直到AirData长大成人（但愿我会一直坚持下去）。

### AirData的特点

* 集成简单，尽可能的少配置，约定优于配置。（我在扯犊子？...）
* 高性能、0反射，没有一行反射代码！！！

既然我在强调“高性能”，那就要有数据说话，下面是和[ActiveAndroid](https://github.com/pardom/ActiveAndroid)的对比（为啥我非要和ActiveAndroid作对比捏？）。

<img src="https://raw.githubusercontent.com/baoboy/baoboy.github.io/master/images/airdata/bar_chart.png" width="500"/>

我做了一下简单的测试，插入10w条，和查询10w条的速度测试，速度相差还是很明显的。

# 使用方法

### Step 1

创建一个数据库对象，继承自`AbstractDatabase`，并用`@Database`注解修饰。

```java
@Database(name = "mydatabase", version = 1)
public class MyDatabase extends AbstractDatabase {

    public MyDatabase(Context context) {
        super(context);
    }

}
```

### Step 2

创建表，也就是一个`Model`类，使用`@Table`修饰，类的所有成员变量默认都是表字段。

```java
@Table
public class Student {

    @PrimaryKey
    private int id;
    private String name;
    private int score;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

```
### Step 3

Insert 操作

```java
Student stu = new Student();
stu.setName("Jack");
stu.setScore(60);

MyDatabase database = new MyDatabase(getContext());
database.save(stu);
```

Update 操作

```java
stu.setName("updateName")

MyDatabase database = new MyDatabase(getContext());
database.update(stu);
```

Delete 操作

```java
MyDatabase database = new MyDatabase(getContext());
database.delete(stu);
```

Select 操作

```java
MyDatabase database = new MyDatabase(getContext());
List<Student> list = database.query(Student.class);
```

### 接下来

上面的那些废话看看就好，如果你想使用本库的话（仅供测试），可以添加以下依赖

```groovy
dependencies {
    provided 'com.baoyz.airdata:compiler:0.1.0'
    compile 'com.baoyz.airdata:core:0.1.0'
}
```

当前库的版本是`0.1.0`  

如果有对本库的任何意见，欢迎带着肥皂来找我哈~  

等到本库正式版的时候，我会把这个README删掉哒~~~