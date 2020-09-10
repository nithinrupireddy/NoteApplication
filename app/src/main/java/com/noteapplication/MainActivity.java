package com.noteapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.noteapplication.adapters.NotesRecyclerAdapter;
import com.noteapplication.databinding.ActivityMainBinding;
import com.noteapplication.models.Note;
import com.noteapplication.persistence.NoteRepository;
import com.noteapplication.util.RecyclerViewItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NotesRecyclerAdapter.OnNoteListener, View.OnClickListener {

    private static final String TAG = "MainActivity";

    //UI Components
    private ActivityMainBinding activityMainBinding;

    //Vars
    private NotesRecyclerAdapter mRecyclerAdapter;
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NoteRepository mnoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.fab.setOnClickListener(this);

        //Initializing views
        initRecylerView();

        mnoteRepository = new NoteRepository(this);

        // insertFakeNotes();

        retrieveNotes();
        setSupportActionBar(((Toolbar)findViewById(R.id.tool_bar)));
        setTitle("Notes");

    }


    private void initRecylerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityMainBinding.recyclerview.setLayoutManager(linearLayoutManager);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(activityMainBinding.recyclerview);
        RecyclerViewItemDecorator itemDecorator = new RecyclerViewItemDecorator(6);
        activityMainBinding.recyclerview.addItemDecoration(itemDecorator);
        mRecyclerAdapter = new NotesRecyclerAdapter(mNotes,this);
        activityMainBinding.recyclerview.setAdapter(mRecyclerAdapter);
    }

    private void retrieveNotes() {
        mnoteRepository.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                if(mNotes.size()>0){
                    mNotes.clear();
                }

                if(notes!=null){
                    mNotes.addAll(notes);
                }
                mRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: "+position);
        Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
        intent.putExtra("selected_note",mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
     Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
     startActivity(intent);
    }

    private void deleteNote(Note note){
        mNotes.remove(note);
        mnoteRepository.deleteNote(note);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getLayoutPosition()));
            //mnoteRepository.deleteNote(mNotes.get(viewHolder.getLayoutPosition()));
        }
    };
}
