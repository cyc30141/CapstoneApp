package com.inhatc.capstone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.inhatc.capstone.cse.gyoo.capstone.dto.CourseListDTO;
import com.inhatc.capstone.cse.gyoo.capstone.dto.CoursePageDTO;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CoursePage extends AppCompatActivity {

    String url = null;
    String subjectID = null;
    int week;
    String type=null;
    List<CoursePageDTO> list;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        url = "http://172.30.1.26/inhatc/getCourseInfo.do";

        Intent intent = getIntent();
        week = intent.getIntExtra("week",0);
        subjectID = intent.getStringExtra("subjectID");
        type = "getCourseInfo";

        week += 1;
        gridView = (GridView) findViewById(R.id.courseGridVIew);
        Log.d("intent test " , subjectID + " " + week);
        new CustomTask(new CustomTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {

                Log.d("CustomTask " , "start : " +output);
                list = new ArrayList<CoursePageDTO>();
                try {
                    JSONObject jsonObject = new JSONObject(output.toString());
                    Integer size = Integer.parseInt(jsonObject.getString("size"));

                    for (int i = 0; i < size; i ++){
                        CoursePageDTO coursePageDTO = new CoursePageDTO();
                        JSONObject transferJson = jsonObject.getJSONObject(String.valueOf(i));

                        coursePageDTO.setWeek(Integer.parseInt( transferJson.getString("WEEK" + String.valueOf(week)) ));
                        coursePageDTO.setStudentName(transferJson.getString("STUDENT_NAME"));
                        coursePageDTO.setStudentID(transferJson.getString("STUDENT_ID"));
                        coursePageDTO.setStudentPhoto(transferJson.getString("STUDENT_PHOTO"));

                        Log.d("coursePageDTO : ", coursePageDTO.toString() );
                        list.add(coursePageDTO);
                    }

                    gridView.setAdapter(new GridViewAdapter(list));

                }catch(Exception e){

                }
            }
        }).execute(url,type,subjectID,String.valueOf(week));

    }

    class GridViewAdapter extends BaseAdapter {

        // 아이템 데이터 리스트.
        private List<CoursePageDTO> listViewItemList = new ArrayList() ;

        public GridViewAdapter(List listViewItemList) {
            this.listViewItemList = listViewItemList;
        }


        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            Log.d("size : " ,listViewItemList.toString());
            if(listViewItemList != null) {
                return listViewItemList.size();
            }else{
                return 0;
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_coursepage_gridview_item, parent, false);
            }
            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

            TextView numberTextView = (TextView) convertView.findViewById(R.id.coursePageGridViewNumber);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.coursePageGridViewName);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.coursePageGridViewPhoto);
            numberTextView.setText(listViewItemList.get(position).getStudentID());
            nameTextView.setText(listViewItemList.get(position).getStudentName());
            //TODO: 이미지뷰 적용

                new Thread(){
                    public void run(){
                        photoStart(position,imageView,listViewItemList);
                    }
                }.start();





            return convertView;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position ;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position) ;
        }

    }

    public void photoStart(int position,ImageView imageView,List<CoursePageDTO> list){
        try {
            URL url = new URL(list.get(position).getStudentPhoto());
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imageView.setImageBitmap(bm);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
