package org.androidtown.moappcal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int num = 0;
    private String line = null;
    private String rLine[] = new String[50];
    private String data[] = new String[20]; //데이터베이스에 실제 들어갈 데이터들.
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteDatabase("CompanyInfo.db");
        createDB();
        read_data_from_file(); //2. 디비 안에 넣을 데이터 구축.

        ImageView imageView1 = (ImageView) findViewById(R.id.back);
        final Button go_Calendar = (Button)findViewById(R.id.calendar);
        go_Calendar.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v) {
                        Intent intent1 = new Intent(MainActivity.this, CalendarMainActivity.class);
                        startActivity(intent1);
                    }
                }
        );
        Button show_company = findViewById(R.id.company);
        show_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListAct.class);
                startActivity(i);
            }
        });



    }

    public void createDB(){

        db = openOrCreateDatabase(
                "CompanyInfo.db",
                SQLiteDatabase.CREATE_IF_NECESSARY,
                null
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS CompanyInfo "
                +"(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, field TEXT, major TEXT, end_date DATE, imageurl TEXT, image BLOB);");
        //이미지는 블롭으로 저장.

    }

    public void read_data_from_file(){
        String name, field, major, end_date;
        int i;
        int line_count = 0;
        try{
            BufferedReader bfRead = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.rawtext)));
            // 한줄씩 NULL이 아닐때까지 읽어 rLine 배열에 넣는다

            while ((line = bfRead.readLine()) != null){
                rLine[line_count] = line; //rLine sttring배열에 한 line씩 넣고,
                line_count++; //count를 증가시켜준다.
            }
            bfRead.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        Log.d("line count =",  String.valueOf(line_count));
        for(i=0; i<line_count; i++){
            line = rLine[i];
            data = line.split("#"); //rLine string배열에서 한 line씩 읽어와서 특정문자'#'으로 split하여 데이터를 분할한다.

            if(i == line_count-1) { //line이 파일에서 마지막 line이라면
                insert_into_db(data[0], data[1], data[2], data[3], data[4], 1);
                // flag값을 1로 주고 실제 데이터 베이스에 넣고, finish
            }
            else {
                insert_into_db(data[0], data[1], data[2], data[3], data[4], 0);
                // flag값을 0로 주고 실제 데이터 베이스에 넣고, continue
            }
        }
    }

    public void insert_into_db(String name, String field, String major, String end_date, String imageurl,  int flag){

        String sql = "insert into CompanyInfo (name, field, major, end_date, imageurl) values ("
                + "'" + name + "','" + field + "', '" + major +"', '" +end_date+"', '" +imageurl+"');";
        Log.d("State : ", "sql 문 작성 후 "+sql);
        db.execSQL(sql);

        int resID = getResources().getIdentifier(imageurl, "drawable", getPackageName()); //이미지의 리소스 아이디 알아내기
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), resID); //비트맵 이미지로 변환
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //png파일로 compress시킴
        byte[] data = stream.toByteArray(); //byte 배열로 stream을 통해 받은 데이터를 넘겨줌.

        SQLiteStatement p = db.compileStatement("INSERT INTO CompanyInfo (image) values(?)"); //image 칼럼에 위에서 가공한 데이터를 넣어줌
        p.bindBlob(1, data);

        Log.d("State : ", "imageurl 가공 끝!");
        if(flag == 1) //flag가 1이면 마지막 데이터라는 의미이므로, 데이터베이스를 닫아준다.
            db.close();
    }


    public void goToManage(View view) {
        Intent i = new Intent(MainActivity.this, Manage.class);

        startActivity(i);
    }

}
