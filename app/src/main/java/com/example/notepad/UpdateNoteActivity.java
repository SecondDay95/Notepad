package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateNoteActivity extends AppCompatActivity {

    EditText etTitleChange, etNoteChange;
    String title, note;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        etTitleChange = (EditText) findViewById(R.id.etTitleChange);
        etNoteChange = (EditText) findViewById(R.id.etNoteChange);

        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        note = intent.getStringExtra("Note");
        id = intent.getIntExtra("Id", 1);
        System.out.println("ID = " + id);

        etTitleChange.setText(title);
        etNoteChange.setText(note);
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
            updateNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateNote () {
        String titleUpdated, noteUpdated;

        titleUpdated = etTitleChange.getText().toString();
        noteUpdated = etNoteChange.getText().toString();

        if (titleUpdated.equals("") || noteUpdated.equals("")) {
            Toast.makeText(UpdateNoteActivity.this, "Please enter all fields",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Note note = new Note(id, titleUpdated, noteUpdated);
            NoteDatabase db = new NoteDatabase(this);
            db.updateNote(note);
            db.close();

            Intent intent = new Intent(UpdateNoteActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getTitle1() {
        return title;
    }

    public int getId() {
        return id;
    }
}
