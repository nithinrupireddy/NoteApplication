package com.noteapplication.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.noteapplication.asyncTask.DeleteAsyncTask;
import com.noteapplication.asyncTask.InsertAsyncTask;
import com.noteapplication.asyncTask.UpdateAsyncTask;
import com.noteapplication.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mnoteDatabase;

    public NoteRepository(Context context) {
        mnoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNote(Note note){

        new InsertAsyncTask(mnoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNote(Note note){

        new UpdateAsyncTask(mnoteDatabase.getNoteDao()).execute(note);
    }

    public void deleteNote(Note note){
        new DeleteAsyncTask(mnoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> getAllNotes(){
        return mnoteDatabase.getNoteDao().getAllNotes();
    }
}
