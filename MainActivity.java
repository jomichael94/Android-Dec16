package com.example.memo;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


// James Michael 788456

/* This is the main activity. It displays all current memos and also allows the user
 * to add a new memo by clicking a FloatingActionButton.
 */
public class MainActivity extends AppCompatActivity {
    /* Define MEMO_NUMBER which will be used as an extra when creating an Intent in
     * this activity.
     */
    public final static String MEMO_NUMBER = "com.example.memo.MEMO_NUMBER";
    MemoDatabase db;
    ArrayList<Memo> memos;
    TextView memosHeader;
    ViewGroup layout;
    TextView addedMemo;
    FloatingActionButton addMemoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Create an instance of the database, and define a TextView and
         * ViewGroup which reference ids of each in the xml.
         */
        db = new MemoDatabase(this);
        memosHeader = (TextView) findViewById(R.id.memosHeader);
        layout = (ViewGroup) findViewById(R.id.memos_linear);

        /* Define a FloatingActionButton for adding a new memo.
         * Using an OnClickListener, when clicked, an activity is started where the user
         * can add a memo.
         */
        addMemoButton = (FloatingActionButton) findViewById(R.id.fab);
        addMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newMemo = new Intent(MainActivity.this, AddNewMemo.class);
                startActivity(newMemo);
            }
        });

        //Display all memos inside the defined layout.
        displayAllMemos(layout);
    }

    //Override the onResume() method to update the list after a memo has been added/updated/deleted.
    @Override
    public void onResume() {
        super.onResume();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new MemoDatabase(this);
        memosHeader = (TextView) findViewById(R.id.memosHeader);
        layout = (ViewGroup) findViewById(R.id.memos_linear);

        addMemoButton = (FloatingActionButton) findViewById(R.id.fab);
        addMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newMemo = new Intent(MainActivity.this, AddNewMemo.class);
                startActivity(newMemo);
            }
        });
        displayAllMemos(layout);
    }

    public void displayAllMemos(ViewGroup layout) {
        /* The following is carried out inside a try..catch block in order to catch a
         * 'CursorIndexOutOfBoundsException' if there are no memos in the database.
         */
        try {
            // Define an ArrayList of Memo objects by calling a method inside database class.
            memos = db.getMemos();

            /* For every memo inside the ArrayList, a new TextView is dynamically created and is
             * added to the layout.
             */
            for (final Memo m : memos) {
                addedMemo = new TextView(this);

                /* The TextView is coloured based on importance level, with green for normal,
                 * yellow for important, and red for urgent. The text colour is also set to
                 * white. These colours are all taken from the xml.
                 */
                if (m.getImportance().equals("Normal")) {
                    addedMemo.setBackgroundResource(R.color.importanceNormal);
                    addedMemo.setTextColor(getResources().getColor(R.color.white));
                }
                if (m.getImportance().equals("Important")) {
                    addedMemo.setBackgroundResource(R.color.importanceImportant);
                    addedMemo.setTextColor(getResources().getColor(R.color.white));
                }
                if (m.getImportance().equals("Urgent")) {
                    addedMemo.setBackgroundResource(R.color.importanceUrgent);
                    addedMemo.setTextColor(getResources().getColor(R.color.white));
                }
                addedMemo.setTextSize(24);
                addedMemo.setPadding(60, 50, 50, 50);

                // The TextView is updated to show the subject of a memo.
                addedMemo.setText(m.getSubject());

                /* An OnClickListener is set for each TextView. If one is clicked, an Intent to view
                 * the single memo is defined. putExtra() passes the memo number/id to make it
                 * easier for the new activity to identify which memo was selected.
                 */
                addedMemo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent viewMemo = new Intent(MainActivity.this, ViewEditDeleteMemo.class);
                        viewMemo.putExtra(MEMO_NUMBER, Integer.toString(m.getMemoNumber()));
                        startActivity(viewMemo);
                    }
                });

                //The memo is added to the layout.
                layout.addView(addedMemo);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            // If the exception is caught, the header text is updated to inform the user of no memos.
            memosHeader.setText("You Have No Memos");
        }
    }
}
