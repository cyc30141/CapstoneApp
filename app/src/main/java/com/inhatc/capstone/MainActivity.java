package com.inhatc.capstone;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.inhatc.capstone.cse.gyoo.capstone.dto.StudentDTO;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final Map<String, List<String>> PLACES_BY_BEACONS;
    private TextView tvId;

    private EditText identification;
    private EditText passWord;
    private String id;
    private String pw;
    private String url;
    private String type;
    private String transferData;
    private StudentDTO studentDTO;

    // TODO: replace "<major>:<minor>" strings to match your own beacons.
    static {
        Map<String, List<String>> placesByBeacons = new HashMap<>();
        placesByBeacons.put("1000:10000", new ArrayList<String>() {{
            add("Heavenly Sandwiches");
            // read as: "Heavenly Sandwiches" is closest
            // to the beacon with major 22504 and minor 48827
            add("Green & Green Salads");
            // "Green & Green Salads" is the next closest
            add("Mini Panini");
            // "Mini Panini" is the furthest away
        }});
        placesByBeacons.put("2000:20000", new ArrayList<String>() {{
            add("Mini Panini");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        placesByBeacons.put("3000:30000", new ArrayList<String>() {{
            add("Mini Panini");
            add("Green & Green Salads");
            add("Heavenly Sandwiches");
        }});
        PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons);
    }

    private List<String> placesNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            return PLACES_BY_BEACONS.get(beaconKey);
        }
        return Collections.emptyList();
    }

    private BeaconManager beaconManager;
    private Region region;

    EditText userId, userPwd;
    Button loginBtn, joinBtn;

    MyApplication myapp = new MyApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capstone_main);


        //tvId = (TextView) findViewById(R.id.tvId);

        /*userId = (EditText) findViewById(R.id.userId);
        userPwd = (EditText) findViewById(R.id.userPwd);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        joinBtn = (Button) findViewById(R.id.joinBtn);
        loginBtn.setOnClickListener(btnListener);
        joinBtn.setOnClickListener(btnListener);*/
        //TextView resultText = (TextView) findViewById(R.id.textView);

        beaconManager = new BeaconManager(this);

        myapp.setState(false);
        myapp.setState2(false);
        beaconManager.setForegroundScanPeriod(1000,0);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    List<String> places = placesNearBeacon(nearestBeacon);
                    // TODO: update the UI here
                    Log.d("Airport", "Nearest places: " + places);
                    Log.d("major", "Major: " + nearestBeacon.getMajor());
                    //Toast.makeText(MainActivity.this,nearestBeacon.getMajor()+"",Toast.LENGTH_SHORT).show();
                    //set
                    myapp.setUUID(nearestBeacon.getProximityUUID());
                    myapp.setMajor(nearestBeacon.getMajor());
                    myapp.setMinor(nearestBeacon.getMinor());

                    //tvId.append("dd"+myapp.getMajor()+myapp.getPremajor());

                    /*if(myapp.getMajor() != myapp.getPremajor()){    //강의실이 다른 경우
                        myapp.setState(false);
                    }*/

                    if(myapp.getMajor() != myapp.getPremajor()) {                 //강의실이 다른 경우
                        //myapp.setPremajor(myapp.getMajor());
                        //myapp.setState(false);  //같은 강의실로 표시.


                        if(myapp.getState2() == false) {              //출석시도가 성공이 아닌 경우.
                            myapp.setPremajor(myapp.getMajor());
                            test();
                        }else {                                          //출석 시도가 성공인 경우.
                            myapp.setPremajor(myapp.getMajor());
                            //test2();
                        }
                    }
                    else{                                           //강의실이 같은 경우.
                        if(myapp.getState2() == false) {              //출석시도가 성공이 아닌 경우.
                            myapp.setPremajor(myapp.getMajor());
                            //test();
                        }else {                                          //출석 시도가 성공인 경우.
                            myapp.setPremajor(myapp.getMajor());
                            myapp.setState2(false);
                            //test2();
                        }
                    }

                    //tvId.setMovementMethod(new ScrollingMovementMethod());
                    String tmpStr = "";
                    tmpStr += "ModelName : "   + Build.MODEL   + "\n";
                    tmpStr += "Device : "      + Build.DEVICE  + "\n";
                    tmpStr += "ProductName : " + Build.PRODUCT + "\n";
                    tmpStr += "ID : "          + Build.ID      + "\n";
                    tmpStr += "Tags : "        + Build.TAGS    + "\n";
                    tmpStr += "Type : "        + Build.TYPE    + "\n";
                    tmpStr += "User : "        + Build.USER    + "\n";
                    tmpStr += "Brand : "       + Build.BRAND;
                }
            }
        });
        region = new Region("ranged region", UUID.fromString("e2c56db5-dffb-48d2-b060-d0f5a71096e0"), null, null);

        (findViewById(R.id.login_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passWord = (EditText)findViewById(R.id.passWord);
                identification = (EditText) findViewById(R.id.identification);
                if(passWord.getText().length() > 0 && identification.getText().length() > 0){
                    id = identification.getText().toString();
                    pw = passWord.getText().toString();
                    url = "http://192.168.43.26:8080/inhatc/getStudent.do";
                    //url = "http://albin7046.cafe24.com/getStudent.do";
                    type = "signIn";
                    Log.d("LoginInfo", "identification : " + id + " pw : " + pw);

                    new com.inhatc.capstone.CustomTask(new com.inhatc.capstone.CustomTask.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {

                            try{
                                JSONObject jsonObject = new JSONObject(output);
                                JSONObject transferJson =  jsonObject.getJSONObject("studentInfo");

                                studentDTO = new StudentDTO();
                                studentDTO.setStudentID(transferJson.getString("student_id"));
                                studentDTO.setStudentPW(transferJson.getString("student_pw"));
                                studentDTO.setStudentName(transferJson.getString("student_name"));
                                studentDTO.setStudentPhoto(transferJson.getString("student_photo"));
                                studentDTO.setStudentPhoneNumber(transferJson.getString("student_phone_number"));
                                studentDTO.setStudentDeviceId(transferJson.getString("student_device_id"));
                                studentDTO.setGrade(transferJson.getString("grade"));

                            }catch(Exception e){
                                e.printStackTrace();
                            }

                            Log.d("TAG" , "capstone_login_output : " + studentDTO.toString());
                            if( id.equals(studentDTO.getStudentID()) && pw.equals(studentDTO.getStudentPW()) && "1".equals(studentDTO.getGrade()) ){
                                Toast.makeText(getApplication(),R.string.login_succese_text,Toast.LENGTH_SHORT).show();
                                test2();
                            }else if( id.equals(studentDTO.getStudentID()) && pw.equals(studentDTO.getStudentPW()) && "0".equals(studentDTO.getGrade()) ){
                                Toast.makeText(getApplication(),R.string.login_succese_text,Toast.LENGTH_SHORT).show();
                                move();
                            }else{
                                Toast.makeText(getApplication(),R.string.login_fail_text,Toast.LENGTH_SHORT).show();
                                identification.getText().clear();
                                passWord.getText().clear();
                            }

                        }
                    }).execute(url,type,id,pw);


                  /*if(body.equals(true)){

                  }else{
                      Toast.makeText(getApplication(),R.string.login_fail_text,Toast.LENGTH_SHORT).show();
                  }*/
                }else{
                    Toast.makeText(getApplication(),R.string.login_fail_text,Toast.LENGTH_SHORT).show();
                }
            }


        }); // 로그인버튼 끝

        (findViewById(R.id.signUp_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Capstone_SignUp.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        //beaconManager.stopRanging(region);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String urls="";

                if(strings[0].equals("join")) {
                    urls = "http://192.168.43.26:8080/inhatc/putStudent.do";
                    //urls = "http://albin7046.cafe24.com/putStudent.do";
                    sendMsg = "id=" + strings[1].trim() + "&pw=" + strings[2].trim() + "&name=" + strings[3].trim() + "&grade=" + strings[5].trim() + "&phoneNumber=" + strings[5].trim() + "&device=" + strings[6].trim();
                }
                else if(strings[0].equals("login")){
                    urls = "http://192.168.43.26:8080/inhatc/login.do";
                    //urls = "http://albin7046.cafe24.com/login.do";
                    sendMsg = "phone=" + strings[1].trim() + "&deviceid=" + strings[2].trim() + "&major=" + strings[3].trim();
                }else if(strings[0].equals("attendance")){
                    urls = "http://192.168.43.26:8080/inhatc/attendance.do";
                    //urls = "http://albin7046.cafe24.com/attendance.do";
                    sendMsg = "phone=" + strings[1].trim() + "&deviceid=" + strings[2].trim() + "&major=" + strings[3].trim();
                }else if(strings[0].equals("mypage")){
                    urls = "http://192.168.43.26:8080/inhatc/getMypage.do";
                    //urls = "http://albin7046.cafe24.com/getMypage.do";
                    sendMsg = "&phone=" + strings[1].trim() + "&deviceid=" + strings[2].trim();
                }else if(strings[0].equals("mypage2")){
                    urls = "http://192.168.43.26:8080/inhatc/getAllState.do";
                    //urls = "http://albin7046.cafe24.com/getAllState.do";
                    sendMsg = "&phone=" + strings[1].trim() + "&deviceid=" + strings[2].trim() + "&subject_name=" + strings[3].trim();
                }else{
                    urls = "";
                    sendMsg = "phone=" + strings[1] + "&deviceid=" + strings[2] + "&major=" + strings[3];
                }


                String str;
                URL url = new URL(urls);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    /*View.OnClickListener btnListener = new View.OnClickListener() {


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loginBtn : // 로그인 버튼 눌렀을 경우
                    String loginid = userId.getText().toString();
                    String loginpwd = userPwd.getText().toString();

                    TelephonyManager tm2 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String  telPhoneNo2 = tm2.getLine1Number();


                    String tmpStr2 = "";
                    tmpStr2 +=  Build.ID + "";

                    //get
                    UUID uuid =  myapp.getUUIDD();
                    int major =  myapp.getMajor();
                    int minor =  myapp.getMinor();

                    try {
                        String result  = new CustomTask().execute(loginid,loginpwd,"login",uuid.toString(),String.valueOf(major),String.valueOf(minor),telPhoneNo2,tmpStr2).get();

                        if(result.trim().equals("true")) {
                            Toast.makeText(MainActivity.this,"로그인",Toast.LENGTH_SHORT).show();
                            test();
                            finish();
                        } else if(result.trim().equals("false")) {
                            Toast.makeText(MainActivity.this,"아이디 또는 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                            userId.setText("");
                            userPwd.setText("");
                        }
                    }catch (Exception e) {}
                    break;
                case R.id.joinBtn : // 회원가입
                    String joinid = userId.getText().toString();
                    String joinpwd = userPwd.getText().toString();

                    String tmpStr = "";
                    tmpStr += Build.ID + "";

                    TelephonyManager tm = null;
                    tm =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String  telPhoneNo = tm.getLine1Number();

                    try {
                        //join 액티비티로 넘겨줌.
                        startActivity(it);
                    }catch (Exception e) {}
                    break;
            }
        }
    };*/

    public  void test(){
        //get
        UUID uuid =  myapp.getUUIDD();
        int major =  myapp.getMajor();
        int minor =  myapp.getMinor();

        TelephonyManager tm = null;
        tm =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String  telPhoneNo = tm.getLine1Number();


        String tmpStr = "";
        tmpStr =  tm.getDeviceId().trim();


        try {
            String result  = new CustomTask().execute("attendance",telPhoneNo,tmpStr.trim(),String.valueOf(major)).get();

            if(result.trim().equals("출석")){
                myapp.setState2(true);
            }else{
                myapp.setState2(false);
            }

            Intent it = new Intent(this,MypageActivity.class);
            it.putExtra("data",result);
            startActivity(it);
        }catch (Exception e) {}
    }


    public  void test2(){
        //get
        UUID uuid =  myapp.getUUIDD();
        int major =  myapp.getMajor();
        int minor =  myapp.getMinor();

        TelephonyManager tm = null;
        tm =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String  telPhoneNo = tm.getLine1Number();


        String tmpStr = "";
        tmpStr =  tm.getDeviceId().trim();


        try {
            String result  = new CustomTask().execute("mypage",telPhoneNo,tmpStr.trim()).get();

            if(result.trim().equals("출석")){
                myapp.setState2(true);
            }else{
                myapp.setState2(false);
            }

            Intent it = new Intent(this,MypageActivity.class);
            it.putExtra("data",result);
            startActivity(it);
        }catch (Exception e) {}
    }

    private void move() {

        Intent intent = new Intent(this,TeacherMyPage.class);
        intent.putExtra("data1",studentDTO.getStudentID());
        intent.putExtra("data2",studentDTO.getStudentPW());
        startActivity(intent);

    }

}