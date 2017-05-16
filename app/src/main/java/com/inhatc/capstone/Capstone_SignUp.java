package com.inhatc.capstone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

public class Capstone_SignUp extends AppCompatActivity {

    private String deviceId;
    private EditText id;
    private EditText name;
    private EditText phoneNumber;
    private EditText photo;
    private EditText pw;
    private Intent intent;
    private String transferData;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capstone_sign_up);

        photo = (EditText)findViewById(R.id.signup_photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTakeAlbumAction();
            }
        });

        (findViewById(R.id.signup_page_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = (EditText) findViewById(R.id.signup_id);
                pw = (EditText) findViewById(R.id.signup_pw);
                name = (EditText) findViewById(R.id.signup_name);
                //phoneNumber = (EditText) findViewById(R.id.signup_phone_number);
                photo = (EditText)findViewById(R.id.signup_photo);
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(radioButtonId);

                if (id.getText().length() > 0 || pw.getText().length() > 0 || name.getText().length() > 0 || phoneNumber.getText().length() > 0) {
                    String url = "http://192.168.43.26:8080/inhatc/putStudent.do";
                    //String url = "http://172.30.1.15/inhatc/putStudent.do";
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String type = "signUp";
                    String signUpId = id.getText().toString();
                    String signUppw = pw.getText().toString();
                    String signUpName = name.getText().toString();
                    String signUpPhoto = photo.getText().toString();
                    String signUpPhoneNumber = tm.getLine1Number().trim();
                    String signUpGrade = radioButton.getText().toString();

                    //deviceId = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    deviceId = tm.getDeviceId().trim();
                    Log.d("TAG", "Device_ID : " + deviceId + " " + signUpPhoto);


                    new CustomTask(new CustomTask.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            Log.d("TAG", "signup output : " + output);

                            try {
                                if (output != null) {
                                    JSONObject jsonObject = new JSONObject(output.toString());
                                    transferData = jsonObject.getString("check");
                                    Log.d("TAG", "transferdata : " + transferData);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if ("true".equals(transferData)) {
                                Toast.makeText(getApplication(), R.string.signup_succese_text, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplication(), R.string.signup_fail_text, Toast.LENGTH_SHORT).show();
                                id.setText("");
                            }
                        }
                    }).execute(url, type, signUpId, signUppw, signUpName, signUpPhoneNumber, signUpGrade, deviceId, signUpPhoto);
                }else{
                    Toast.makeText(getApplication(), R.string.signup_fail2_text, Toast.LENGTH_SHORT).show();
                }
            }
        });

        (findViewById(R.id.signup_page_cancle_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode)
        {
            case PICK_FROM_ALBUM:
            {
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel",mImageCaptureUri.getPath().toString());
                photo.setText(mImageCaptureUri.toString());
            }
        }

    }
}
