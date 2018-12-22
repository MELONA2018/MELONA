package org.androidtown.moappcal;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListAct extends ListActivity {
    SQLiteDatabase db;
    String end_date;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        loadDB();

    }

    public void addCalender(View view) {
        Intent intent1 = new Intent(this, AddNoteActivity.class);
        TextView t = (TextView)findViewById(R.id.name);
        String n = t.getText().toString();
        Toast.makeText(getApplicationContext(), end_date + "에 "+n+"을 추가하세요.", Toast.LENGTH_LONG).show();
        startActivity(intent1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDB();
    }

    public void loadDB(){
        ListView listView = (ListView)findViewById(android.R.id.list);
        db = openOrCreateDatabase(
                "CompanyInfo.db",
                Context.MODE_PRIVATE,
                null
        );

        Cursor c = db.rawQuery("SELECT * FROM CompanyInfo;", null); //query를 날림.
        if(c != null){
            int count = c.getCount();
            c.moveToFirst(); // 커서 처음으로 보냄

            for(int i = 0; i<count; i++) {
                String[] from = new String[]{"name", "field", "major", "end_date", "imageurl", "image"}; //가져올 디비 필드 이름
                int[] to = new int[]{R.id.name, R.id.field, R.id.major, R.id.end_date, R.id.imageurl, R.id.image}; //필드 각각에 대응되는 xml의 아이디

                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        listView.getContext(),
                        R.layout.company,
                        c,
                        from,
                        to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
                );

                adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                    @Override  //특정 view를 세팅하기 위해 setViewValue함수를 override하였다.
                    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                        int imageurlIndex = cursor.getColumnIndex("imageurl"); //imageurl의 칼럼이 몇번 째 index인지 가져온다.
                        int imageIndex = cursor.getColumnIndex("image"); //image 칼럼이 몇번 째 index인지 가져온다.

                        int dateIndex = cursor.getColumnIndex("end_date");
                        end_date = cursor.getString(dateIndex);

                        int nameIndex = cursor.getColumnIndex("name");
                        name = cursor.getString(nameIndex);

                        if (columnIndex == imageIndex) { //image칼럼이 현재 view의 column index일때, image를 나타낸다.
                            String imageurl = cursor.getString(imageurlIndex); //imageurl을 데이터베이스로부터 가져오고
                            int resID = getResources().getIdentifier(imageurl, "drawable", getPackageName()); //이미지의 리소스 아이디 알아낸다.
                            Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), resID); //비트맵을 구성함.

                            ImageView imageView = (ImageView) view;
                            imageView.setImageBitmap(bitmap); //해당 imageView에 bitmap을 set해준다.
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                listView.setAdapter(adapter); //마지막으로 adapter를 listView에 set해준다.
            }

        }
    }
}

