package com.baoyz.airdata;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baoyz.airdata.model.Person;

public class MainActivity extends AppCompatActivity {

    private MyDatabase mDatabase;
    private TextView mResultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = new MyDatabase(this);

        mResultTv = (TextView) findViewById(R.id.tv_result);
    }

    public void savePerson(View view) {
        Person person = new Person();
        person.setAge(18);
        person.setId(2);
        person.setName("李四");
        long id = mDatabase.save(person);
        query();
    }

    public void query(){
        Cursor cursor = mDatabase.getDatabase().query("Person", null, null, null, null, null, null);
        StringBuilder sb = new StringBuilder();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int age = cursor.getInt(2);
            sb.append(id).append("\t").append(name).append("\t").append(age).append("\n");
        }
        mResultTv.setText(sb.toString());
    }
}
