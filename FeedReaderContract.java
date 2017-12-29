package com.example.memo;

import android.provider.BaseColumns;

/**
 * Created by James on 14/11/2016.
 */

// James Michael 788456

/* This class defines the schema that will later be used when creating the database.
 * To help with this, I referred to the Android Studio databases tutorial at:
 * https://developer.android.com/training/basics/data-storage/databases.html
 */
public final class FeedReaderContract {
    private FeedReaderContract() {

    }

    // Define constants for the table name and table columns.
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "memos";
        public static final String COLUMN_MEMO_SUBJECT = "subject";
        public static final String COLUMN_MEMO_DESC = "description";
        public static final String COLUMN_MEMO_IMPORTANCE = "importance";
        
    }

    // Define SQL statements for creating and deleting a database table.
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_MEMO_SUBJECT + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_MEMO_DESC + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_MEMO_IMPORTANCE + TEXT_TYPE + " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


}

