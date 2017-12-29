package com.example.memo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by James on 15/11/2016.
 */

// James Michael 788456

/* This class creates and manages the database.
 * To help with this, I referred to the Android Studio databases tutorial at:
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
public class MemoDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "MemoLog.db";
    SQLiteDatabase db;
    ArrayList<Memo> memoList;

    public MemoDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // This creates the table using the the SQL written in the contract class.
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.SQL_CREATE_ENTRIES);
    }

    // Upgrade the database by deleting the table and then creating a new one.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FeedReaderContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /* Returns a single memo of type Memo.
     * This is used when viewing or editing a single memo by filtering the
     * SQL query by memo id (the primary key) to ensure only one memo is returned.
     */
    public Memo getSingleMemo(String memoNumber) {
        db = getReadableDatabase();

        // Define the projection, selection and selection args.
        // This will ultimately result in the query 'select * from memos where id = ?'.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_MEMO_SUBJECT,
                FeedReaderContract.FeedEntry.COLUMN_MEMO_DESC,
                FeedReaderContract.FeedEntry.COLUMN_MEMO_IMPORTANCE
        };
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { memoNumber };

        // A Cursor allows the results of the query to be read by moving through each column.
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        c.moveToFirst();

        // Get the results in each column and pass these as parameters to create a new Memo object.
        String subject = c.getString(1);
        String description = c.getString(2);
        int importance = Integer.parseInt(c.getString(3));
        c.close();
        Memo memo = new Memo(Integer.parseInt(memoNumber), subject, description, importance);

        // Return the memo.
        return memo;

    }

    /* Returns an ArrayList of Memo objects in order to return all memos in the database.
     * This is used when displaying a list of all memos on screen.
     */
    public ArrayList<Memo> getMemos() {
        db = getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_MEMO_SUBJECT,
                FeedReaderContract.FeedEntry.COLUMN_MEMO_DESC,
                FeedReaderContract.FeedEntry.COLUMN_MEMO_IMPORTANCE
        };

        // Sort the results based on importance level in descending order.
        // This allows more urgent memos to be displayed towards the top.
        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_MEMO_IMPORTANCE + " DESC";
        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        c.moveToFirst();
        memoList = new ArrayList();
        int id;
        String subject;
        String description;
        int importance;
        Memo memo;

        // Use the cursor inside a do..while to move through each row as well as each column, while
        // there is a row available.
        do {
            id = Integer.parseInt(c.getString(0));
            subject = c.getString(1);
            description = c.getString(2);
            importance = Integer.parseInt(c.getString(3));
            memo = new Memo(id, subject, description, importance);
            memoList.add(memo);
        } while (c.moveToNext());
        c.close();

        //Return the ArrayList of memos.
        return memoList;
    }

    // Inserts a new memo entry into the database.
    public void addMemoToDB(String subject, String description, int importance) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_MEMO_SUBJECT, subject);
        values.put(FeedReaderContract.FeedEntry.COLUMN_MEMO_DESC, description);
        values.put(FeedReaderContract.FeedEntry.COLUMN_MEMO_IMPORTANCE, importance);
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }

    /* Updates a memo already in the database.
     * Similar to getSingleMemo(), this filters the query based on memo id to ensure the correct
     * memo gets updated.
     */
    public void updateMemo(String memoNumber, String subject, String description, int importance) {
        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_MEMO_SUBJECT, subject);
        values.put(FeedReaderContract.FeedEntry.COLUMN_MEMO_DESC, description);
        values.put(FeedReaderContract.FeedEntry.COLUMN_MEMO_IMPORTANCE, importance);

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { memoNumber };

        int count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }

    /* Deletes a memo from the database.
     * Again, the unique memo id is used to ensure the correct memo is deleted.
     */
    public void deleteMemo(String memoNumber) {
        db = getReadableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { memoNumber };
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

}
