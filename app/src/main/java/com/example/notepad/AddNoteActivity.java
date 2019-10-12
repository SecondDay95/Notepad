package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    EditText etTitle, etNote;
    TextView tv_TextWathcer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //Toolbar toolbar = findViewById(R.id.toolbar_addNote);
//        setSupportActionBar(toolbar);
        //toolbar.setTitle("Add a Note");

        etTitle = (EditText) findViewById(R.id.etTitle);
        etNote = (EditText) findViewById(R.id.etNote);
        tv_TextWathcer = (TextView) findViewById(R.id.tv_textWatcher);
        String dateS = prepareDate();
        tv_TextWathcer.setText(dateS);

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String dateS = prepareDate();
                tv_TextWathcer.setText(dateS + " | Characters: " + String.valueOf(s.length()));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etNote.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote(){

        String title = etTitle.getText().toString();
        String note_text = etNote.getText().toString();

        String dateS = prepareDate();
        System.out.println("Date: " + dateS);

        if(title.equals("") || note_text.equals("")){
            Toast.makeText(this, "Please fill all the fields before saving",
                    Toast.LENGTH_SHORT).show();
        }else{
            NoteDatabase db = new NoteDatabase(this);
            //db.onUpgrade(db.getWritableDatabase(), 3, 4);
            Note note = new Note(title,note_text, dateS);
            db.addNote(note);
            db.close();

            Intent i = new Intent(AddNoteActivity.this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }

    }

    private String prepareDate() {

        long date_ms = System.currentTimeMillis();
        Date date = new Date(date_ms);
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:YYYY, HH:mm");
        String dateS = dateFormat.format(date).toString();
        return dateS;
    }


}
