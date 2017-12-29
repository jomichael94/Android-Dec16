package com.example.memo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

// James Michael 788456

/* This activity is used to add a new memo.
 * The user enters a subject for the memo and an optional description, along with choosing
 * a level of importance - Normal, Important or Urgent.
 */
public class AddNewMemo extends AppCompatActivity {
    CheckBox normalCB;
    CheckBox importantCB;
    CheckBox urgentCB;
    Button confirmBtn;
    EditText memoSubject;
    EditText memoDescription;
    int importance;
    MemoDatabase memoDB;
    final int IMPORTANCE_NORMAL = 1;
    final int IMPORTANCE_IMPORTANT = 2;
    final int IMPORTANCE_URGENT = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_memo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Define CheckBoxes, EditTexts and a Button programmatically by referencing their ids.
         * These will be used to select importance, enter memo details, and confirm the new memo.
         */
        normalCB = (CheckBox) findViewById(R.id.normalCB);
        importantCB = (CheckBox) findViewById(R.id.importantCB);
        urgentCB = (CheckBox) findViewById(R.id.urgentCB);
        confirmBtn = (Button) findViewById(R.id.confirmMemoBtn);
        memoSubject = (EditText) findViewById(R.id.subjectText);
        memoDescription = (EditText) findViewById(R.id.descText);

        // Create an instance of the memo database used to store memos.
        memoDB = new MemoDatabase(this);

        /* Set an OnClickListener for the CheckBox used for normal importance.
         * If it is checked, a 'Snackbar' notification is displayed to inform the user of which
         * importance level was clicked.
         * If it is checked, it also unchecks the other importance CheckBoxes that may have been
         * selected previously.
         */
        normalCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (normalCB.isChecked()) {
                    Snackbar.make(view, "Importance Level: Normal", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    importantCB.setChecked(false);
                    urgentCB.setChecked(false);
                }
            }
        });

        // Another OnClickListener is used for important level importance.
        importantCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (importantCB.isChecked()) {
                    Snackbar.make(view, "Importance Level: Important", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    normalCB.setChecked(false);
                    urgentCB.setChecked(false);
                }
            }
        });

        // An OnClickListener used for the urgent importance CheckBox.
        urgentCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (urgentCB.isChecked()) {
                    Snackbar.make(view, "Importance Level: Urgent", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    normalCB.setChecked(false);
                    importantCB.setChecked(false);
                }
            }
        });

        // Set an OnClickListener on the confirm Button.
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* This validates to ensure a subject for the memo has been entered, and an
                 * importance level has been chosen.
                 * If one has not been entered, a Snackbar notification is shown to prompt the user.
                 */
                if (memoSubject.getText().toString().equals("")){
                    Snackbar.make(view, "Please enter a Subject", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (!normalCB.isChecked() && !importantCB.isChecked() && !urgentCB.isChecked()) {
                    Snackbar.make(view, "Please choose an Importance level", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                /* If a subject has been entered, and an importance level chosen, the memo is ready to be added
                 * to the database.
                 * The importance level is determined based on the CheckBox that was selected, and is then
                 * passed to addMemoToDB() along with the subject and optional description.
                 */
                else {
                    if (normalCB.isChecked()){
                        importance = IMPORTANCE_NORMAL;
                    }
                    if (importantCB.isChecked()){
                        importance = IMPORTANCE_IMPORTANT;
                    }
                    if (urgentCB.isChecked()){
                        importance = IMPORTANCE_URGENT;
                    }
                    memoDB.addMemoToDB(memoSubject.getText().toString(), memoDescription.getText().toString(), importance);
                    memoDB.close();
                    finish();
                }
            }
            });


    }

}
