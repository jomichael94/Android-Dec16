package com.example.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

// James Michael 788456

/* This activity allows the user to view, edit and delete a single memo,
 * and is started when any memo is selected in the main activity.
 */
public class ViewEditDeleteMemo extends AppCompatActivity {
    String memoNumber;
    EditText editSubject;
    EditText editDesc;
    CheckBox updateNormalCB;
    CheckBox updateImportantCB;
    CheckBox updateUrgentCB;
    Button updateBtn;
    Button deleteBtn;
    MemoDatabase db;
    Memo memo;
    int importance;
    final int IMPORTANCE_NORMAL = 1;
    final int IMPORTANCE_IMPORTANT = 2;
    final int IMPORTANCE_URGENT = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_delete_memo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the Intent, and also the Extra which contains a memo id number.
        Intent intent = getIntent();
        memoNumber = intent.getStringExtra(MainActivity.MEMO_NUMBER);

        // Similar to AddNewMemo, define EditTexts and CheckBoxes.
        // Also define new buttons for updating and deleting a memo.
        editSubject = (EditText) findViewById(R.id.editSubject);
        editDesc = (EditText) findViewById(R.id.editDesc);
        updateNormalCB = (CheckBox) findViewById(R.id.updateNormalCB);
        updateImportantCB = (CheckBox) findViewById(R.id.updateImportantCB);
        updateUrgentCB = (CheckBox) findViewById(R.id.updateUrgentCB);
        updateBtn = (Button) findViewById(R.id.updateMemoBtn);
        deleteBtn = (Button) findViewById(R.id.deleteMemoBtn);

        /* Create an instance of the database class, and define a Memo object by calling
         * getSingleMemo() while passing through the memo id received using getStringExtra().
         */
        db = new MemoDatabase(this);
        memo = db.getSingleMemo(memoNumber);

        /* Working with the appropriate memo (based on id), update subject and description (if applicable),
         * and check the appropriate importance CheckBox.
         * This is achieved by calling the accessor methods defined in the Memo class.
         */
        editSubject.setText(memo.getSubject());
        editDesc.setText(memo.getDescription());
        if (memo.getImportance().equals("Normal")){
            updateNormalCB.setChecked(true);
        }
        if (memo.getImportance().equals("Important")){
            updateImportantCB.setChecked(true);
        }
        if (memo.getImportance().equals("Urgent")){
            updateUrgentCB.setChecked(true);
        }

        // Set OnClickListeners for each CheckBox to ensure only one importance level can
        // be checked at a given time.
        updateNormalCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateNormalCB.isChecked()) {
                    Snackbar.make(view, "Importance Level: Normal", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    updateImportantCB.setChecked(false);
                    updateUrgentCB.setChecked(false);
                }
            }
        });

        updateImportantCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateImportantCB.isChecked()) {
                    Snackbar.make(view, "Importance Level: Important", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    updateNormalCB.setChecked(false);
                    updateUrgentCB.setChecked(false);
                }
            }
        });

        updateUrgentCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateUrgentCB.isChecked()) {
                    Snackbar.make(view, "Importance Level: Urgent", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    updateNormalCB.setChecked(false);
                    updateImportantCB.setChecked(false);
                }
            }
        });


        // Also set OnClickListeners for both update and delete buttons.
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validates to ensure a subject has been entered and an importance level chosen.
                if (editSubject.getText().toString().equals("")){
                    Snackbar.make(view, "Please enter a Subject", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (!updateNormalCB.isChecked() && !updateImportantCB.isChecked() && !updateUrgentCB.isChecked()) {
                    Snackbar.make(view, "Please choose an Importance level", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                // If all data is valid, the updateMemo() method is called before the database is closed and activity finishes.
                else {
                    if (updateNormalCB.isChecked()) {
                        importance = IMPORTANCE_NORMAL;
                    }
                    if (updateImportantCB.isChecked()) {
                        importance = IMPORTANCE_IMPORTANT;
                    }
                    if (updateUrgentCB.isChecked()) {
                        importance = IMPORTANCE_URGENT;
                    }
                    db.updateMemo(memoNumber, editSubject.getText().toString(), editDesc.getText().toString(), importance);
                    db.close();
                    finish();
                }
            }
        });

        // If the delete button is clicked, the deleteMemo() method is called, and the database is then closed.
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteMemo(memoNumber);
                db.close();
                finish();
            }
        });


    }

}
