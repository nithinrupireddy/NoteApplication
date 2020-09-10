package com.noteapplication.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.noteapplication.models.Note;

@Database(entities = {Note.class},version = 1,exportSchema = true)
public abstract class NoteDatabase extends RoomDatabase {

    public static final String DATABASE_NAME="notes_db";
    public static NoteDatabase instance;

    static NoteDatabase getInstance(final Context context){

        if(instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract NoteDAO getNoteDao();
}
