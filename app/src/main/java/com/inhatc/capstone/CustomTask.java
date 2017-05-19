package com.inhatc.capstone;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Gyoo on 2017-04-25.
 */

public class CustomTask extends AsyncTask<String, Void, String> {

    private String transferData;
    private String sendMessage;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public CustomTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection httpURLConnection = null;
        URL url = null;
        try {
            url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept","application/json");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Log.d("TAG" , "params check : " + params[0] + " " +params[1]);

            httpURLConnection.setConnectTimeout(100);
            System.out.println(httpURLConnection.toString());
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            if("signIn".equals(params[1])){
                sendMessage = "id=" + params[2].trim() +"&pw=" + params[3].trim();
            }else if("signUp".equals(params[1].trim())){
                sendMessage = "id=" + params[2].trim() +"&pw=" + params[3].trim() +"&name=" + params[4].trim() + "&phoneNumber=" + params[5].trim() + "&grade=" + params[6].trim() + "&device=" + params[7].trim() + "&photo=" + params[8].trim() ;
            }else if("getTeacherInfo".equals(params[1].trim())){
                Log.d("getTeacherInfo",params[2] + params[3]);
                sendMessage = "id=" + params[2].trim()+"&pw=" + params[3].trim();
            }else if("getCourseInfo".equals(params[1].trim())){
                Log.d("getCourseInfo",params[2]);
                sendMessage = "subjectID=" + params[2].trim();
            }else if("getTeacherCourseWeek".equals(params[1].trim())){
                Log.d("getTeacherCourseWeek",params[2]);
                sendMessage = "subjectID=" + params[2].trim();
            }

            bufferedWriter.write(
                sendMessage
            );

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            httpURLConnection.connect();


            InputStream in = httpURLConnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buf = new byte[1024 * 8];
            int length = 0;
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            System.out.println(new String(out.toByteArray(), "UTF-8"));
            transferData = new String(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            ;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return transferData;
    }

    @Override
    protected void onPostExecute(String bodyData) {
        Log.d("TAG","bodyData : " + bodyData);
        delegate.processFinish(bodyData);
    }//onPostExecute end

}