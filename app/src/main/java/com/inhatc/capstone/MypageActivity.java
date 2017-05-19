package com.inhatc.capstone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class MypageActivity extends AppCompatActivity {

    private TextView name;
    private TextView date;
    private TextView todayL;
    private TextView allL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // 빈 데이터 리스트 생성.
        final ArrayList<String> items = new ArrayList<String>() ;
        final ArrayList<String> items2 = new ArrayList<String>() ;
        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, items) ;
        final ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items2) ;

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(adapter) ;
        final ListView listview2 = (ListView) findViewById(R.id.listview2) ;
        listview2.setAdapter(adapter2) ;

        // modify button에 대한 이벤트 처리.
        Button modifyButton = (Button)findViewById(R.id.modify) ;
        modifyButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listview.getCheckedItemPosition();
                    if (checked > -1 && checked < count) {
                        TelephonyManager tm = null;
                        tm =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        String  telPhoneNo = tm.getLine1Number();
                        String tmpStr = "";
                        tmpStr +=  tm.getDeviceId().trim();

                        try {
                            String result  = new MainActivity.CustomTask().execute("mypage2",telPhoneNo,tmpStr,items.get(checked)).get();

                            Intent it = new Intent(MypageActivity.this,MyState.class);
                            it.putExtra("data",result);
                            startActivity(it);


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        // listview 갱신
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;



        try {
        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.date);
        //todayL = (TextView) findViewById(R.id.todayL);
        //allL = (TextView) findViewById(R.id.allL);

        Intent it = getIntent();
        String result = it.getStringExtra("data");

        String Alecture="";
        String Tlecture="";
        String TlectureState="";

        StringTokenizer st = new StringTokenizer(result.trim(), "$" );
        while( st.hasMoreTokens() ){
            st.nextToken();
            Alecture = st.nextToken();
            Tlecture = st.nextToken();
            TlectureState = st.nextToken();
        }

        JSONObject json = new JSONObject(Alecture);
        JSONArray jArr = json.getJSONArray("date");
        JSONArray jArr2 = json.getJSONArray("id");
        JSONArray jArr3 = json.getJSONArray("name");
        JSONArray jArr4 = json.getJSONArray("Alecture");

        JSONObject json2 = new JSONObject(Tlecture);
        JSONArray jArr5 = json2.getJSONArray("Tlecture");

        JSONObject json3 = new JSONObject(TlectureState);
        JSONArray jArr6 = json3.getJSONArray("TlectureState");

        String temp = "";


        for (int i = 0; i < jArr.length(); i++) {
            json = jArr.getJSONObject(i);
            temp = (String) json.getString("date");
            Log.i("데이터1", "임.");
            date.append(temp+" \n");
        }

        for (int i = 0; i < jArr2.length(); i++) {
            json = jArr2.getJSONObject(i);
            temp = (String) json.getString("id");
            Log.i("데이터2", "임.");
            name.append(temp+"  ");
        }

        for (int i = 0; i < jArr3.length(); i++) {
            json = jArr3.getJSONObject(i);
            temp = (String) json.getString("name");
            Log.i("데이터3", "임.");
            name.append(temp+" \n");
        }


            adapter.notifyDataSetChanged();
        for (int i = 0; i < jArr4.length(); i++) {
            json = jArr4.getJSONObject(i);
            temp = (String) json.getString("lecture");
            Log.i("데이터4", "임.");
            // 아이템 추가.
            items.add(temp);
            // listview 갱신
            adapter.notifyDataSetChanged();
            //allL.append(temp+" \n");
        }

        for (int i = 0; i < jArr5.length(); i++) {
            json2 = jArr5.getJSONObject(i);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String getTime = sdf.format(new Date());
            Log.i("날짜", getTime + "임.");
            String state = "";
            boolean check = sdf.parse(json2.getString("st")).after(sdf.parse(getTime));
            Log.i("날짜", check + "임.");

            Log.i("데이터5", "임.");
            json3 = jArr6.getJSONObject(i);
            if (json3.getString("state").trim().equals("1")) {
                state = "출석";
            }else if(json3.getString("state").equals("결석")){
                state = "결석";
            }

            if (sdf.parse(json2.getString("st")).after(sdf.parse(getTime)) == true) {
                state = "대기";
            }

            items2.add((String) json2.getString("lecture")+" "+(String) json2.getString("st")+" "+(String) json2.getString("en") + " " +state.trim());
            adapter2.notifyDataSetChanged();
        }

        /*for (int i = 0; i < jArr6.length(); i++) {
            json3 = jArr6.getJSONObject(i);
            Log.i("데이터6", "임.");
            // 아이템 추가.
            ;
            String state="";

            if(json3.getString("state").equals("true")) {
                    state="출석";
            }else{
                    state="결석";
            }

            items2.add(" "+state+" \n");
            // listview 갱신
            adapter.notifyDataSetChanged();
            //todayL.append((String) json3.getString("lecture")+" 시작시간 : "+(String) json3.getString("st")+" 종료시간 : "+(String) json3.getString("en")+" 상태 : "+(String) json3.getString("state")+" \n");
        }*/

        }catch (Exception e) {}

    }

}
