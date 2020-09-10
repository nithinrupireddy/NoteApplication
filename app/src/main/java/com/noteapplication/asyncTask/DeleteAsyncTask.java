package com.noteapplication.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.noteapplication.models.Note;
import com.noteapplication.persistence.NoteDAO;

public class DeleteAsyncTask extends AsyncTask<Note,Void,Void> {


    private static final String TAG = "DeleteAsyncTask";
    private NoteDAO mDao;

    public DeleteAsyncTask(NoteDAO dao){
        mDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {

        Log.d(TAG, "doInBackground: "+Thread.currentThread().getName());
        mDao.deleteNote(notes);
        return null;
    }
}
