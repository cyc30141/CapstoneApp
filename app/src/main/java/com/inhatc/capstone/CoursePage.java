package com.inhatc.capstone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.capstone.cse.gyoo.capstone.dto.CourseListDTO;
import com.inhatc.capstone.cse.gyoo.capstone.dto.CoursePageDTO;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CoursePage extends AppCompatActivity {

    boolean checkMenu = false;
    String url = null;
    String subjectID = null;
    int week;
    String type=null;
    List<CoursePageDTO> list;
    GridView gridView;
    Bitmap bitmap;
    ImageTask imageTask;

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_page);

        //url = "http://192.168.43.214/inhatc/getCourseInfo.do";
        url = "http://192.168.43.26:8080/inhatc/getCourseInfo.do";

        Intent intent = getIntent();
        week = intent.getIntExtra("week",0);
        subjectID = intent.getStringExtra("subjectID");
        type = "getCourseInfo";

        week += 1;//Week의 시작은 1부터 이고 배열은 0부터여서 싱크를 맞춰주기위한 +1

        Toast.makeText(getApplicationContext(),week+" 주차",Toast.LENGTH_SHORT).show();

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

                    for (int i = 0; i < size; i ++){ //서버에서 가져온것을 DTO라는 객체에 감싸서 객체상태로 리스트에 넣어줌
                        CoursePageDTO coursePageDTO = new CoursePageDTO();
                        JSONObject transferJson = jsonObject.getJSONObject(String.valueOf(i));

                        coursePageDTO.setWeek(Integer.parseInt( transferJson.getString("WEEK" + String.valueOf(week)) ));
                        coursePageDTO.setStudentName(transferJson.getString("STUDENT_NAME"));
                        coursePageDTO.setStudentID(transferJson.getString("STUDENT_ID"));
                        coursePageDTO.setStudentPhoto(transferJson.getString("STUDENT_PHOTO"));

                        Log.d("coursePageDTO : ", coursePageDTO.toString() );
                        list.add(coursePageDTO);
                    }

                    new CustomTask(new CustomTask.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            for (int i=0; i< list.size(); i++){

                            }
                        }
                    }).execute();

                    gridView.setAdapter(new GridViewAdapter(list)); //어답터는 ListView,GridView 마다 들어가는 객체가 달라서 각자 이너클래스로 작성

                }catch(Exception e){

                }
            }
        }).execute(url,type,subjectID,String.valueOf(week));

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d("onCreateOptionMenu",menu.toString());

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(checkMenu){ //편집 누르기 전 상태
            menu.getItem(1).setEnabled(true);
        }else{
            menu.getItem(1).setEnabled(false);
        }
        checkMenu = !checkMenu;
        return super.onPrepareOptionsMenu(menu);
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

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.coursePageGridViewBackground);
            viewHolder.id= (TextView) convertView.findViewById(R.id.coursePageGridViewNumber);
            viewHolder.name = (TextView) convertView.findViewById(R.id.coursePageGridViewName);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.coursePageGridViewPhoto);
            viewHolder.spinner = (Spinner) convertView.findViewById(R.id.coursePageSpinner);

            viewHolder.id.setText(listViewItemList.get(position).getStudentID());
            viewHolder.name.setText(listViewItemList.get(position).getStudentName());

            switch (listViewItemList.get(position).getWeek()){
                case 0: viewHolder.layout.setBackgroundColor(Color.rgb(255, 000, 127));
                    viewHolder.spinner.setSelection(listViewItemList.get(position).getWeek()+1);
                    Log.d("first background Test","0번쨰");
                    break;
                case 1:  viewHolder.layout.setBackgroundColor(Color.rgb(134, 229, 127));
                    viewHolder.spinner.setSelection(listViewItemList.get(position).getWeek()+1);
                    Log.d("first background Test","1번쨰");
                    break;
                case 2: viewHolder.layout.setBackgroundColor(Color.rgb(255, 228, 0));
                    viewHolder.spinner.setSelection(listViewItemList.get(position).getWeek()+1);
                    Log.d("first background Test","2번쨰");
                    break;
            }

               /* new Thread(){//이미지 적용시키기 위한 쓰레드
                    public void run(){
                       photoStart(position,viewHolder.imageView,listViewItemList);
                    }
                }.start();*/
            try {
                imageTask = new ImageTask();
                listViewItemList.get(position).setImageBitmap( imageTask.execute(listViewItemList.get(position).getStudentPhoto()).get() );
                Log.d("imageBitmap",listViewItemList.get(position).getImageBitmap().toString());
                viewHolder.imageView.setImageBitmap( listViewItemList.get(position).getImageBitmap() );
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("spinner Test", "spinner run");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            switch (i) {
                                case 0:
                                    Log.d("list week check","대기 상태");
                                    break;
                                case 1:
                                    viewHolder.layout.setBackgroundColor(Color.rgb(255, 000, 127));
                                    listViewItemList.get(position).setWeek(i-1);
                                    Log.d("list week check",String.valueOf(listViewItemList.get(position).getWeek()));
                                    break;
                                case 2:
                                    viewHolder.layout.setBackgroundColor(Color.rgb(134, 229, 127));
                                    listViewItemList.get(position).setWeek(i-1);
                                    Log.d("list week check",String.valueOf(listViewItemList.get(position).getWeek()));
                                    break;
                                case 3:
                                    viewHolder.layout.setBackgroundColor(Color.rgb(255, 228, 0));
                                    listViewItemList.get(position).setWeek(i-1);
                                    Log.d("list week check",String.valueOf(listViewItemList.get(position).getWeek()));
                                    break;
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }).run();

            Log.d("spinner Test", "spinner exit");

            return convertView;
        }

        private class ViewHolder {
            public LinearLayout layout;
            public ImageView imageView;
            public TextView id;
            public TextView name;
            public Spinner spinner;
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

 /*   public void photoStart(int position,ImageView imageView,List<CoursePageDTO> list){
        try {

            URL url = new URL(list.get(position).getStudentPhoto());
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Log.d("bis check",bis.toString());
            if(bis != null) {
                Bitmap bm = BitmapFactory.decodeStream(bis);
                Log.d("bm check",bm.toString());
                bis.close();
                imageView.setImageBitmap(bm);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//Thread로 돌려야 하기 떄문에 따로 메소드로 뺌(이미지 Url로 가져와서 이미지뷰에 적용시키기)*/

    class ImageTask extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {

            try{

                URL imageUrl = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)imageUrl.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

            }catch(Exception e){
                e.printStackTrace();
            }

            return bitmap;
        }
    }

}
