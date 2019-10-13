package com.example.notepad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvNotes;
   // RecyclerView.Adapter adapter;
   NotesAdapter adapter;
    List<Note> notesList;
    int id, position;
    private SharedPreferences sharedPreferences, sharedPreferences1, sharedPreferences2;
    private SharedPreferences.Editor editor;
    private String color, notesView;

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

        registerForContextMenu(rvNotes);

        initViews();
        loadNotes();
        System.out.println("Size " + notesList.size());

        if (notesList.size() != 0) {
            adapter.setListener(new NotesAdapter.Listener() {
                @Override
                public void onClick(int position) {

                    Intent intent = new Intent(MainActivity.this, UpdateNoteActivity.class);
                    int positionClick = adapter.getPosition1();
                    Note note = notesList.get(positionClick);
                    String title = note.getTitle();
                    String noteS = note.getNote();
                    id = note.getId();
                    intent.putExtra("Title", title);
                    intent.putExtra("Note", noteS);
                    intent.putExtra("Id", id);
                    System.out.println("Title: " + title);
                    System.out.println("Note: " + noteS);
                    startActivity(intent);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPostResume() {
        super.onPostResume();

        sharedPreferences = MainActivity.this.getSharedPreferences("Color", Context.MODE_PRIVATE);
        color = sharedPreferences.getString("ChooseColor", "Black");
        if (notesList.size() != 0) {
            if (color.equals("Black")) {
                rvNotes.setBackgroundColor(this.getResources().getColor(R.color.colorBlack, null));
            } else if (color.equals("Red")) {
                rvNotes.setBackgroundColor(this.getResources().getColor(R.color.colorRed, null));
            } else if (color.equals("Green")) {
                rvNotes.setBackgroundColor(this.getResources().getColor(R.color.colorGreen, null));
            } else if (color.equals("Blue")) {
                rvNotes.setBackgroundColor(this.getResources().getColor(R.color.colorBlue, null));
            } else if (color.equals("Gray")) {
                rvNotes.setBackgroundColor(this.getResources().getColor(R.color.colorGray, null));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.backgound_color) {
            Intent intent = new Intent(MainActivity.this, ChangeColorActivity.class);
            startActivity(intent);
        }

        if (id == R.id.notes_view_grid) {
            sharedPreferences1 = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
            editor = sharedPreferences1.edit();
            editor.clear();
            editor.putString("View", "Grid_View");
            editor.commit();
            initViews();
        }

        if (id == R.id.notes_view_list) {
            sharedPreferences1 = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
            editor = sharedPreferences1.edit();
            editor.clear();
            editor.putString("View", "List_View");
            editor.commit();
            initViews();
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//        getMenuInflater().inflate(R.menu.menu_delete, menu);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        position = adapter.getPosition1();
        System.out.println(position);
        Note note = notesList.get(position);
        NoteDatabase db = new NoteDatabase(this);
        db.deleteNote(note);
        db.close();
        notesList.remove(position);
        adapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    private void initViews() {
        sharedPreferences2 = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        notesView = sharedPreferences2.getString("View", "Grid_View");

        if (notesView.equals("Grid_View")) {
            rvNotes.setLayoutManager(new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
        }
        else {
            rvNotes.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void loadNotes() {
        NoteDatabase db = new NoteDatabase(this);

        notesList = db.getAllNotes();
        if (notesList.size() != 0) {
            Collections.reverse(notesList);
            adapter = new NotesAdapter(this, notesList);
            rvNotes.setAdapter(adapter);
            System.out.println("Date: " + notesList.get(0).getDate());
        }
    }
}
