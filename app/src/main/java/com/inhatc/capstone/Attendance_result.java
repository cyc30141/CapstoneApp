package com.inhatc.capstone;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.StringTokenizer;

public class Attendance_result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent it = getIntent();
        final String result = it.getStringExtra("data").trim();

        String Alecture = "";
        String Tlecture = "";
        String TlectureState = "";
        String status = "";


        StringTokenizer st = new StringTokenizer(result.trim(), "$");
        status = st.nextToken();
        Log.i("결과", status);
        if (status.equals("true")) {
            setContentView(R.layout.activity_attendance_success);
            // 액션 바 감추기
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();

            // 2초 후 인트로 액티비티 제거
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Attendance_result.this, MypageActivity.class);
                    intent.putExtra("data", result);
                    startActivity(intent);

                    finish();
                }
            }, 2000);
        } else {
            setContentView(R.layout.activity_attendance_fail);
            // 액션 바 감추기
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();

            // 2초 후 인트로 액티비티 제거
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Attendance_result.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                }
            }, 2000);
        }
    }
}
