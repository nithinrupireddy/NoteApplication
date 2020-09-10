package com.noteapplication.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.noteapplication.models.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    long[] insertNotes(Note... notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getAllNotes();

    @Delete
    int deleteNote(Note... note);

    @Update
    int updateNote(Note... note);

}
