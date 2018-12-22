package org.androidtown.moappcal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotePreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_preview);
        Intent intent = getIntent();
        TextView note = (TextView) findViewById(R.id.note);
        if (intent != null) {
            Object event = intent.getParcelableExtra(CalendarMainActivity.EVENT);
            if (event instanceof MyEventDay) {
                MyEventDay myEventDay = (MyEventDay) event;
                note.setText(myEventDay.getNote());
                return;
            }
        }
    }
}
