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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherMyPage extends AppCompatActivity {

    String url = null;
    String teacherId = null;
    String teacherpw = null;
    String type=null;
    List<String> arrayList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_my_page);

        //url = "http://192.168.43.214/inhatc/getTeacherInfo.do";
        url = "http://192.168.43.26:8080/inhatc/getTeacherInfo.do";

        Intent intent = getIntent();
        teacherId = intent.getStringExtra("data1");
        teacherpw = intent.getStringExtra("data2");
        type = "getTeacherInfo";
        Log.d("Intent Test  " , teacherId +" " + teacherpw);

        listView = (ListView)findViewById(R.id.TeacherListView);

        new CustomTask(new CustomTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.d("CustomTask " , "start");

                if(output != null){
                    Log.d("CustomTask " , "output!=null : " + output);
                    arrayList = new ArrayList<String>();
                    try {
                        JSONObject jsonObject = new JSONObject(output.toString());
                        Log.d("JsonObject Test ","");
                        Integer size = Integer.parseInt(jsonObject.getString("size"));

                        final List<CourseListDTO> list = new ArrayList();
                        for (int i = 0; i< size; i++){
                            CourseListDTO courseListDTO = new CourseListDTO();
                            JSONObject transferJson = jsonObject.getJSONObject(String.valueOf(i));

                            courseListDTO.setSubjectID(transferJson.getString("SUBJECT_ID"));
                            courseListDTO.setStudentID(transferJson.getString("STUDENT_ID"));
                            courseListDTO.setdATE(transferJson.getString("DATE"));
                            courseListDTO.setStudentName(transferJson.getString("STUDENT_NAME"));
                            courseListDTO.setSubjectName(transferJson.getString("SUBJECT_NAME"));

                            Log.d("courseListDTO " , courseListDTO.toString() );
                            list.add(courseListDTO);
                        }

                        listView.setAdapter(new ListViewAdapter(list));

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent1 = new Intent(getApplicationContext(),TeacherWeekPage.class);
                                intent1.putExtra("data",list.get(i).getSubjectID());
                                Log.d("subjectId", list.get(i).getSubjectID());
                                startActivity(intent1);
                            }
                        });

                        //arrayList.add(jsonObject.getString)
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }).execute(url,type,teacherId,teacherpw);
    }

    class ListViewAdapter extends BaseAdapter {

        // 아이템 데이터 리스트.
        private List<CourseListDTO> listViewItemList = new ArrayList() ;

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
                convertView = inflater.inflate(R.layout.activity_teacher_list_view_item, parent, false);
            }
            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득


            TextView nameTextView = (TextView) convertView.findViewById(R.id.teacherListViewText) ;
            nameTextView.setText(listViewItemList.get(position).getSubjectName());

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
