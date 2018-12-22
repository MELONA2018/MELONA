package org.androidtown.moappcal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Manage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        TextView textView = (TextView) findViewById(R.id.textView1);
        String str = "문항을\n선택해보세요.\n";

        textView.setText(str);

        SQLiteDatabase essaydb;

        essaydb = openOrCreateDatabase(
                "Essay.db",
                SQLiteDatabase.CREATE_IF_NECESSARY,
                null
        );

        essaydb.execSQL("CREATE TABLE IF NOT EXISTS Essay "
                +"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "question TEXT, essay TEXT);");


    }

    public void goToQue01 (View view) {
        Button btn = (Button) findViewById(R.id.button1_1);
        String str = btn.getText().toString();

        Intent i = new Intent (Manage.this, Que01.class);

        i.putExtra("question", str);

        startActivity(i);
    }



}
