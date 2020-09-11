package com.noteapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noteapplication.databinding.ActivityEditNoteBinding;
import com.noteapplication.models.Note;
import com.noteapplication.persistence.NoteRepository;
import com.noteapplication.util.Utility;

public class EditNoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    private static final String TAG = "EditNoteActivity";
    public static final int EDIT_MODE_ENABLED=1;
    public static final int EDIT_MODE_DISABLED=0;

    // UI Components
    private ActivityEditNoteBinding meditNoteBinding;
    private EditText note_edit_title;
    private TextView note_tv_title;
    private RelativeLayout mCheckContainer,mBackContainer;
    private ImageButton mCheck,mBack;


    //Vars
    private boolean isNewNote;
    private Note mInitialNote,mFinalNote;
    private GestureDetector mgestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meditNoteBinding = ActivityEditNoteBinding.inflate(getLayoutInflater());
        setContentView(meditNoteBinding.getRoot());


        //This is another test comment to activity
        initializing_views();
        setListeners();

        if(getIncommingNote()){
            // This is a new note ,Edit Mode
            setNewNoteProperties();
            enableEditMode();
        }else{
            //this is not a new note ,View Mode
            setNoteProperties();
        }

    }

    private void initializing_views(){

        note_edit_title = findViewById(R.id.note_edit_title);
        note_tv_title = findViewById(R.id.note_textview_title);
        mBackContainer = findViewById(R.id.back_arrow_container);
        mCheckContainer = findViewById(R.id.check_arrow_container);
        mCheck = findViewById(R.id.tool_bar_check);
        mBack = findViewById(R.id.tool_bar_backArrow);
        mNoteRepository = new NoteRepository(this);

    }

    private void setListeners(){

        meditNoteBinding.linedEditnote.setOnTouchListener(this);
        mgestureDetector = new GestureDetector(this,this);
        mCheck.setOnClickListener(this);
        note_tv_title.setOnClickListener(this);
        mBack.setOnClickListener(this);
        note_edit_title.addTextChangedListener(this);
    }

    private boolean getIncommingNote(){

        if(getIntent().hasExtra("selected_note")){
            mInitialNote = getIntent().getParcelableExtra("selected_note");

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimestramp(mInitialNote.getTimestramp());
            mFinalNote.setId(mInitialNote.getId());

            mMode = EDIT_MODE_ENABLED;

            Log.d(TAG, "getIncommingNote: "+mInitialNote.toString());
            isNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        isNewNote=true;
        return true;
    }

    private void setNoteProperties(){
        note_edit_title.setText(mInitialNote.getTitle());
        note_tv_title.setText(mInitialNote.getTitle());
        meditNoteBinding.linedEditnote.setText(mInitialNote.getContent());

        mMode = EDIT_MODE_DISABLED;
        disableClickInteraction();
        hideSoftKeyBoard(this);
    }

    private void setNewNoteProperties(){
        note_edit_title.setText("Note");
        note_tv_title.setText("Note");

        mFinalNote = new Note();
        mInitialNote = new Note();
        mInitialNote.setTitle("Note");
        mFinalNote.setTitle("Note");

    }

    private void enableEditMode(){

        mBackContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);
        note_edit_title.setVisibility(View.VISIBLE);
        note_tv_title.setVisibility(View.GONE);

        mMode = EDIT_MODE_ENABLED;
        enableClickInteraction();
    }

    private void disableEditMode(){

        mBackContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);
        note_edit_title.setVisibility(View.GONE);
        note_tv_title.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_DISABLED;
        disableClickInteraction();

        String temp = meditNoteBinding.linedEditnote.getText().toString();
        temp = temp.replace("\n","");
        temp = temp.replace(" ","");
        if(temp.length()>0){
            mFinalNote.setTitle(note_edit_title.getText().toString());
            mFinalNote.setContent(meditNoteBinding.linedEditnote.getText().toString());
            String timestramp = Utility.getCurrentTimeStramp();
            mFinalNote.setTimestramp(timestramp);

            if(!mFinalNote.getContent().equals(mInitialNote.getContent()) ||
                    !mFinalNote.getTitle().equals(mInitialNote.getContent())){
                saveChanges();
            }
        }



    }

    private void saveChanges(){
        if(isNewNote){
            saveNewNote();
        }else{
            //update the note here
            updateNote();
        }
    }

    private void saveNewNote() {
        mNoteRepository.insertNote(mFinalNote);
    }

    private void updateNote(){
        mNoteRepository.updateNote(mFinalNote);
    }

    private void enableClickInteraction(){
        meditNoteBinding.linedEditnote.setKeyListener(new EditText(this).getKeyListener());
        meditNoteBinding.linedEditnote.requestFocus();
        meditNoteBinding.linedEditnote.setFocusable(true);
        meditNoteBinding.linedEditnote.setFocusableInTouchMode(true);
        meditNoteBinding.linedEditnote.setCursorVisible(true);
    }

    private void disableClickInteraction() {
        meditNoteBinding.linedEditnote.setKeyListener(null);
        meditNoteBinding.linedEditnote.clearFocus();
        meditNoteBinding.linedEditnote.setFocusable(false);
        meditNoteBinding.linedEditnote.setFocusableInTouchMode(false);
        meditNoteBinding.linedEditnote.setCursorVisible(false);
    }

    private void hideSoftKeyBoard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if(view==null){
            view = new View(activity);
        }
        view.clearFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        return mgestureDetector.onTouchEvent(motionEvent);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tool_bar_check: {
                hideSoftKeyBoard(this);
                disableEditMode();
                break;
            }

            case R.id.note_textview_title:{
                enableEditMode();
                note_edit_title.requestFocus();
                note_edit_title.setSelection(note_edit_title.length());
                break;
            }

            case R.id.tool_bar_backArrow:{
                finish();
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {

        if(mMode == EDIT_MODE_ENABLED){
            onClick(mCheck);
        }else if(mMode == EDIT_MODE_DISABLED){
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode",mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        note_tv_title.setText(s.toString());

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
