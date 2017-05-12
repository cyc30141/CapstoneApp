package com.inhatc.capstone;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class MyState extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mystate);
/*
        // 빈 데이터 리스트 생성.
        final ArrayList<String> items = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items) ;

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(adapter) ;*/
        GridView GridView ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        GridView = (GridView) findViewById(R.id.listview1);
        GridView.setAdapter(adapter);



        try {

            Intent it = getIntent();
            String result = it.getStringExtra("data");

            String date="";
            String state="";

            StringTokenizer st = new StringTokenizer(result.trim(), "$" );
            while( st.hasMoreTokens() ){
                date = st.nextToken();
                state = st.nextToken();
            }

            JSONObject json = new JSONObject(date);
            JSONArray jArr = json.getJSONArray("date");
            JSONObject json2 = new JSONObject(state);
            JSONArray jArr2 = json2.getJSONArray("state");

            String temp="";
            String Mydate="";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String getTime = sdf.format(new Date());
            //adapter.notifyDataSetChanged();
            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                Mydate = (String) json.getString("date");
                Log.i("데이터값"+jArr.length() +  " " + json.toString() + Mydate, "임.");
                //items.add(temp);
                if(sdf.parse(Mydate).after(sdf.parse(getTime))){
                    //items.add(Mydate + "                                                    대기");
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.uncheck1),
                            Mydate+"") ;
                }else{
                    json2 = jArr2.getJSONObject(i);
                    temp = (String) json2.getString("state");

                    if(temp.equals("0")) {
                        //items.add(Mydate + "                                                    결석");
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.uncheck1),
                                Mydate+"") ;
                    }else if(temp.equals("1")){
                        //items.add(Mydate + "                                                    출석");
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.check1),
                                Mydate+"") ;
                    }else{
                        //items.add(Mydate + "                                                    잘못된 값");
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.uncheck1),
                                Mydate+"") ;
                    }
                }
                Log.i("데이터값"+Mydate+temp, "임.");
                // listview 갱신
                //adapter.notifyDataSetChanged();
                //allL.append(temp+" \n");
            }

        }catch (Exception e) {}

    }

}
