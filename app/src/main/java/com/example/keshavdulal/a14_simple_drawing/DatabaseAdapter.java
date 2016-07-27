package com.example.keshavdulal.a14_simple_drawing;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseAdapter {

    DatabaseHelper databaseHelper;


    public DatabaseAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "awajDatabase.db";
        private static final String TABLE_NAME = "TESTTABLE";
        private static final String UID = "_id";
        private static final String NAME = "Name";
        private static final int DATABASE_VERSION = 1;
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " VARCHAR(255))";
        private Context context;
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                sqLiteDatabase.execSQL(DROP_TABLE);
                onCreate(sqLiteDatabase);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
