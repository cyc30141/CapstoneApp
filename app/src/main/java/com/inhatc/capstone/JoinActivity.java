package com.inhatc.capstone;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {

    EditText userId, userPwd, userName;
    Button joinBtn;
    RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        userId = (EditText) findViewById(R.id.userId);
        userPwd = (EditText) findViewById(R.id.userPwd);
        userName = (EditText) findViewById(R.id.userName);
        rg = (RadioGroup)findViewById(R.id.radioGroup1);
        joinBtn = (Button) findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(btnListener);


    }


    View.OnClickListener btnListener = new View.OnClickListener() {


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.joinBtn : // 회원가입
                    String joinid = userId.getText().toString();
                    String joinpwd = userPwd.getText().toString();
                    String joinname = userName.getText().toString();
                    int id = rg.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(id);
                    String grade = rb.getText().toString();

                    String tmpStr = "";
                    tmpStr += Build.ID + "";

                    TelephonyManager tm = null;
                    tm =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String  telPhoneNo = tm.getLine1Number();

                    try {
                        String result  = new MainActivity.CustomTask().execute("join",joinid,joinpwd,joinname,grade,telPhoneNo,tmpStr).get();
                        JSONObject json = new JSONObject(result);
                        json.getString("check");
                        if(json.getString("check").equals("id")) {
                            Toast.makeText(JoinActivity.this,"이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT).show();
                            userId.setText("");
                            userPwd.setText("");
                            userName.setText("");
                        } else if(json.getString("check").equals("true")) {
                            userId.setText("");
                            userPwd.setText("");
                            userName.setText("");
                            Toast.makeText(JoinActivity.this,"회원가입을 축하합니다.",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }catch (Exception e) {}
                    break;
            }
        }
    };



}