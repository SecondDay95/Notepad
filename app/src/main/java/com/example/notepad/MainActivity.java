package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvNotes;
   // RecyclerView.Adapter adapter;
   NotesAdapter adapter;
    List<Note> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvNotes = (RecyclerView) findViewById(R.id.recycler);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        initViews();
        loadNotes();

        adapter.setListener(new NotesAdapter.Listener() {
            @Override
            public void onClick(int position) {

                Intent intent = new Intent(MainActivity.this, UpdateNoteActivity.class);
                int positionClick = adapter.getPosition1();
                Note note = notesList.get(positionClick);
                String title = note.getTitle();
                String noteS = note.getNote();
                int id = note.getId();
                intent.putExtra("Title", title);
                intent.putExtra("Note", noteS);
                intent.putExtra("Id", id);
                System.out.println("Title: " + title);
                System.out.println("Note: " + noteS);
                startActivity(intent);
            }
        });
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
    }

    private void loadNotes() {
        NoteDatabase db = new NoteDatabase(this);

        notesList = db.getAllNotes();
        if (notesList.size() != 0) {
            adapter = new NotesAdapter(this, notesList);
            rvNotes.setAdapter(adapter);
        }
    }
}
