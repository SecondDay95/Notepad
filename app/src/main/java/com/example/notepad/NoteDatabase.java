package com.example.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes_manager";
    private static final String TABLE_NAME = "notes";

    // Coloumn Names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTE = "note";
    private static final String KEY_DATE = "date";

    // Coloumn Combinations
    private static final String[] COLS_ID_TITLE_NOTE_DATE = new String[] {KEY_ID,KEY_TITLE,KEY_NOTE, KEY_DATE};

    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+", "
                + KEY_TITLE + " TEXT NOT NULL"+ ", "
                + KEY_NOTE + " TEXT" + ", "
                + KEY_DATE + " TEXT"
                + ")";

        db.execSQL(CREATE_NOTES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS "+ TABLE_NAME;

        db.execSQL(DROP_TABLE);

        onCreate(db);
    }

    public void addNote (Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_NOTE, note.getNote());
        //values.put(KEY_DATE, System.currentTimeMillis());
        values.put(KEY_DATE, note.getDate());


        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public Note getNote(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE_DATE,KEY_ID +"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(c != null){
            c.moveToFirst();
        }
        db.close();
        Note note = new Note(Integer.parseInt(c.getString(0)),
                c.getString(1),c.getString(2));
        return note;
    }

    public List<Note> getAllNotes(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Note> noteList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,COLS_ID_TITLE_NOTE_DATE,null,null,
                null,null,null);


        if(cursor!= null && cursor.moveToFirst()){

            do{
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setNote(cursor.getString(2));
                note.setDate(cursor.getString(3));
                System.out.println("cursor.getString(3) = " + cursor.getString(3));
                noteList.add(note);

            }while (cursor.moveToNext());


        }
        db.close();
        return noteList;

    }

    public void updateNote (Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, note.getTitle());
        contentValues.put(KEY_NOTE, note.getNote());
        contentValues.put(KEY_DATE, note.getDate());
        //UpdateNoteActivity updateNoteActivity = new UpdateNoteActivity();
        MainActivity mainActivity = new MainActivity();
        System.out.println("String id = " + note.getId());

        db.update(TABLE_NAME, contentValues,KEY_ID + "=?" ,
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public void deleteNote (Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        //UpdateNoteActivity updateNoteActivity = new UpdateNoteActivity();
        MainActivity mainActivity = new MainActivity();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});
        db.close();

    }
}
