package com.inhatc.capstone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.inhatc.capstone.cse.gyoo.capstone.dto.CourseListDTO;
import com.inhatc.capstone.cse.gyoo.capstone.dto.CourseWeekDTO;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TeacherWeekPage extends AppCompatActivity {

    ListView listView;
    List<CourseWeekDTO> list;
    String url;
    String type=null;
    String subjectID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_week_page);

        url = "http://172.30.1.26/inhatc/getTeacherCourseWeek.do";

        final Intent intent = getIntent();
        subjectID= intent.getStringExtra("data");
        type = "getTeacherCourseWeek";

        listView = (ListView)findViewById(R.id.TeacherWeekListView);

        new CustomTask(new CustomTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                if(output != null){
                    Log.d("CustomTask " , "output!=null : " + output);
                    try {
                        JSONObject jsonObject = new JSONObject(output);
                        list = new ArrayList();
                        int size = jsonObject.getInt("size");
                        Log.d("size" , String.valueOf(size));

                        for (int i = 0; i < size; i++){
                            CourseWeekDTO courseWeekDTO = new CourseWeekDTO();
                            courseWeekDTO.setWeek(i);
                            courseWeekDTO.setDate(jsonObject.getString(String.valueOf(i)));

                            list.add(courseWeekDTO);
                        }
                        Log.d("list",list.get(13).getDate());
                        listView.setAdapter(new ListViewAdapter(list));

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent1 = new Intent(getApplicationContext(),CoursePage.class);
                                intent1.putExtra("week",list.get(i).getWeek());
                                intent1.putExtra("subjectID", subjectID);
                                startActivity(intent1);
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).execute(url,type,subjectID);

    }

    class ListViewAdapter extends BaseAdapter {

        // 아이템 데이터 리스트.
        private List<CourseWeekDTO> listViewItemList = new ArrayList() ;

        public ListViewAdapter(List listViewItemList) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();


            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.activity_teacher_week_listitem, parent, false);
            }
            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득


            TextView week = (TextView) convertView.findViewById(R.id.TeacherWeek) ;
            TextView date = (TextView) convertView.findViewById(R.id.TeacherWeekDate);
            week.setText((listViewItemList.get(position).getWeek()+1) + " 주차");
            date.setText(listViewItemList.get(position).getDate());

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

}
