package com.inhatc.capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private GridLayout mLinearLayout;
    MyApplication myapp = new MyApplication();
    private TextView tvId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toast.makeText(Main2Activity.this,myapp.getMajor()+"",Toast.LENGTH_SHORT).show();
        data();
    }

//

    public void data(){
        String loginid = "201746014";
        String loginpwd = "1";

        /*try {
            String result  = new MainActivity.CustomTask().execute(loginid,"data").get();
            //String result  = new CustomTask().execute(loginid,loginpwd,"login").get();
            //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
            JSONObject json = new JSONObject(result);
            JSONArray jArr = json.getJSONArray("dataSend");
            // JSON이 가진 크기만큼 데이터를 받아옴
            String[] getJsonData = new String[0];
            String test="";

            mLinearLayout = (GridLayout) findViewById(R.id.ll);

            for (int i = 0; i < jArr.length(); i++) {
                json = jArr.getJSONObject(i);
                //getJsonData[0] = (String) json.getString("id");
                test = (String) json.getString("id");
                //getJsonData[1] = (String) json.getString("pw");

                test = test + (String) json.getString("lec");
                test = test + (String) json.getString("date");
                test = test + (String) json.getString("state");

                ImageView iv = new ImageView(this);
                TextView tv = new TextView(this);
                tv.setText("dd");
                iv.setImageResource(R.drawable.logo);
                iv.setAdjustViewBounds(true);
                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iv.setMaxHeight(300);
                iv.setMaxWidth(250);

                //mLinearLayout.addView(iv);
                mLinearLayout.addView(tv);


            }



            Toast.makeText(Main2Activity.this,test+"",Toast.LENGTH_SHORT).show();
            //tvId.setText(data+"ddd");


        }catch (Exception e) {}*/
    }

            }
