package com.baoyz.airdata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.baoyz.airdata.model.Person;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        View view = new View(this);
//        view.setBackgroundColor(Color.argb(0x55, 0x00, 0x00, 0x00));
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
//        params.type = WindowManager.LayoutParams.TYPE_PHONE;
//        params.gravity = Gravity.TOP | Gravity.LEFT;
//        params.alpha = 0.3f;
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        wm.addView(view, params);
//
//        view.buildDrawingCache();
//        try {
//            view.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream("/sdcard/screen.png"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
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
        list = mDatabase.query(Person.class);
        StringBuilder sb = new StringBuilder();
        for (Person person : list) {
            sb.append(person.toString()).append("\n");
        }

        mResultTv.setText(sb.toString());
    }
}
