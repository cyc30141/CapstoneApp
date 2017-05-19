package com.inhatc.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;

public class CoursePage extends AppCompatActivity {

    String url = null;
    String subjectID = null;
    String teacherpw = null;
    String type=null;
    List<String> arrayList;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        url = "http://192.168.43.26:8080/inhatc/getCourseInfo.do";

        Intent intent = getIntent();
        subjectID= intent.getStringExtra("data");
        type = "getCourseInfo";

        Log.d("intent test " , subjectID);
        new CustomTask(new CustomTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {

                Log.d("CustomTask " , "start : " +output);



            }
        }).execute(url,type,subjectID);

    }
}
