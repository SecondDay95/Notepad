package com.example.notepad;

import android.content.Intent;
import android.os.AsyncTask;
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

public class UpdateNoteActivity extends AppCompatActivity {

    EditText etTitleChange, etNoteChange;
    TextView tv_WatcherUpdate;
    String title, note;
    String titleUpdated, noteUpdated;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        etTitleChange = (EditText) findViewById(R.id.etTitleChange);
        etNoteChange = (EditText) findViewById(R.id.etNoteChange);
        tv_WatcherUpdate = (TextView) findViewById(R.id.tv_textWatcherUpdate);

        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        note = intent.getStringExtra("Note");
        id = intent.getIntExtra("Id", 1);
        System.out.println("ID = " + id);

        etTitleChange.setText(title);
        etNoteChange.setText(note);
        String dateS = prepareDate();
        tv_WatcherUpdate.setText(dateS + " | Characters: " + String.valueOf(note.length()));

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String dateS = prepareDate();
                tv_WatcherUpdate.setText(dateS + " | Characters: " + String.valueOf(s.length()));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etNoteChange.addTextChangedListener(textWatcher);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_delete, menu);
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
            new MyTask().execute();
        }

        if (id == R.id.action_delete) {
            new MyTask1().execute();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNote () {
        titleUpdated = etTitleChange.getText().toString();
        noteUpdated = etNoteChange.getText().toString();

        if (!titleUpdated.equals("") || !noteUpdated.equals("")) {

            String dateS = prepareDate();
            Note note = new Note(id, titleUpdated, noteUpdated, dateS);
            NoteDatabase db = new NoteDatabase(this);
            //db.updateNote(note);
            db.deleteNote(note);
            db.addNote(note);
            db.close();
        }
    }

    private void deleteNote () {
        Note note = new Note(id);
        NoteDatabase db = new NoteDatabase(this);
        db.deleteNote(note);
        db.close();

        Intent intent = new Intent(UpdateNoteActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public String getTitle1() {
        return title;
    }

    public int getId() {
        return id;
    }

    private String prepareDate() {
        long date_ms = System.currentTimeMillis();
        Date date = new Date(date_ms);
        DateFormat dateFormat = new SimpleDateFormat("dd:MM:YYYY, HH:mm");
        String dateS = dateFormat.format(date).toString();
        return dateS;
    }

    protected class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            updateNote();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (titleUpdated.equals("") || noteUpdated.equals("")) {
                Toast.makeText(UpdateNoteActivity.this, "Please enter all fields",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(UpdateNoteActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            super.onPostExecute(aVoid);
        }
    }
    protected class MyTask1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            deleteNote();
            return null;
        }
    }
}
