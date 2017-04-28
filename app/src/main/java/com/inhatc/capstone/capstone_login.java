package com.inhatc.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONObject;

import com.inhatc.capstone.cse.gyoo.capstone.dto.StudentDTO;

public class capstone_login extends AppCompatActivity {

    private EditText identification;
    private EditText passWord;
    private String id;
    private String pw;
    private String url;
    private String type;
    private String transferData;
    private StudentDTO studentDTO;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capstone_main);

        (findViewById(R.id.login_button)).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

                passWord = (EditText)findViewById(R.id.passWord);
                identification = (EditText) findViewById(R.id.identification);
              if(passWord.getText().length() > 0 && identification.getText().length() > 0){
                  id = identification.getText().toString();
                  pw = passWord.getText().toString();
                  url = "http://albin7046.cafe24.com/getStudent.do";
                  type = "signIn";
                  Log.d("LoginInfo", "identification : " + id + " pw : " + pw);

                  new CustomTask(new CustomTask.AsyncResponse() {
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
                            if( id.equals(studentDTO.getStudentID()) && pw.equals(studentDTO.getStudentPW()) ){
                                Toast.makeText(getApplication(),R.string.login_succese_text,Toast.LENGTH_SHORT).show();
                                //Todo: 로그인 성공시 인텐트로 값 넘기면서 화면 전환

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

}
