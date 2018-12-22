package org.androidtown.moappcal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Que01 extends AppCompatActivity {
    SQLiteDatabase essaydb;
    int flag_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que01);

        TextView textView = (TextView) findViewById(R.id.textView1);

        Intent i = getIntent();

        String question = i.getExtras().getString("question");
        //question = question + "\n";
        textView.setText(question);

        essaydb = openOrCreateDatabase(
                "Essay.db",
                SQLiteDatabase.CREATE_IF_NECESSARY,
                null
        );


        EditText editText = (EditText) findViewById(R.id.edit);
        Log.d("aaaaa", "");
        String text;

        String sql = "select * from Essay " + " where question = '성장과정';";
        Log.d("bbbbbb", "");
        Cursor c = essaydb.rawQuery(sql, null); //
        Log.d("vvvvvv", "");

        flag_count = c.getCount();
        Log.d("c.getCOunt = ", String.valueOf(flag_count));
        if(c.getCount() > 0) {
            c.moveToFirst();
            text = c.getString(2);
            editText.setText(text);
        }
    }

    public void saveQueOne(View view) {

        TextView title = (TextView) findViewById(R.id.textView1);
        EditText edit = (EditText) findViewById(R.id.edit);
        String text = " ";
        String tit = title.getText().toString();
        String str = edit.getText().toString();

        if(flag_count == 0){
            Log.d("flag = 0", " if 들어왔음");
            String sql = "insert into Essay (question, essay) values ("
                    + "'" + tit + "','" + str + "');";
            essaydb.execSQL(sql);
            Log.d("sql", sql);
        }
        else{
            Log.d("flag = 0", " else 들어왔음");
            String sql = "update Essay set essay = '" + str + "' where question = '"+tit+"';";
            Log.d("sql", sql);
            essaydb.execSQL(sql);

        }
    }

}
