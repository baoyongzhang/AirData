package com.baoyz.airdata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baoyz.airdata.crud.Select;
import com.baoyz.airdata.model.Person;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyDatabase mDatabase;
    private TextView mResultTv;
    private List<Person> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = new MyDatabase(this);

        mResultTv = (TextView) findViewById(R.id.tv_result);

    }

    public void updatePerson(View view) {
        if (list != null && list.size() > 0){
            Person person = list.get(0);
            person.setName("XXX");
            person.setAge(100);
            mDatabase.update(person);
            query();
        }
    }

    public void deletePerson(View view) {
        if (list != null && list.size() > 0){
            Person bean = list.get(0);
            mDatabase.delete(bean);
            query();
        }
    }

    public void savePerson(View view) {
        Person person = new Person();
        person.setAge(18);
        person.setName("李四");
        person.setBool(true);
        person.setD(100.2222);
        person.setF(100.2222f);
        person.setL(123112323);
        long id = mDatabase.save(person);
        query();
    }

    public void query(){
//        list = mDatabase.query(Person.class);
        list = new Select<Person>(mDatabase).from(Person.class).list();
        StringBuilder sb = new StringBuilder();
        for (Person person : list) {
            sb.append(person.toString()).append("\n");
        }

        mResultTv.setText(sb.toString());
    }
}
