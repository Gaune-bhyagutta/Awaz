package com.awaj;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

 class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "awajDatabase.db";
        private static final String DATABASE_PATH = "data/data/com.awaj/databases/";
        private static final String TABLE_NAME = "NOTE_TBL";
        private static final String UID = "_id";
        private static final String NOTE = "note";
        private static final String FREQ = "freq";
        private static final String MIN_FREQ ="min_freq";
        private static final String MAX_FREQ ="max_freq";
        private static final int DATABASE_VERSION = 1;
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE +
                " VARCHAR(255), "+FREQ+" FLOAT, "+MIN_FREQ+" FLOAT, "+MAX_FREQ+" FLOAT)";
        private Context context;
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private SQLiteDatabase myDatabase;
        double freq[]= new double[107];
        double min_freq[]=new double[107];
        double max_freq[]=new double[107];
        String note[] = new String[107];

        DatabaseHelper(Context context) {
            //calling the super class constructor
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        public void createDatabase() throws IOException{
            boolean dbExist = checkDatabase();
            if(dbExist){
                //do nothing, database already exists
            }else{
                //by calling this method an empty dtabase will be created into the default system path of our application so we are gonna
                //be able to overwrite that database with our database
                this.getReadableDatabase();
                try{
                    copyDataBase();
                }catch (IOException e){
                    throw new Error("Error copying database");
                }
            }
        }

        private boolean checkDatabase(){
            SQLiteDatabase checkDB = null;
            try{
                String myPath = DATABASE_PATH + DATABASE_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            }catch (SQLiteException e){
                //database doesn't exist yet
                Log.d("VIVZ", "database doesn't exist");
            }
            if(checkDB != null){
                checkDB.close();
            }
            return checkDB != null ? true : false;
        }
        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         * */

        private void copyDataBase() throws IOException{
            //open our local db as the input stream
            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            //path to the just created empty db
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            //open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //transferring files from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while((length=myInput.read(buffer))>0){
                myOutput.write(buffer,0,length);
            }
            //close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }

        public void openDatabase() throws SQLiteException{
            //open the database
            String myPath = DATABASE_PATH + DATABASE_NAME;
            myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }

        @Override
        public synchronized void close() {
            if (myDatabase!=null){
                myDatabase.close();
            }
            super.close();
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        public void getAllData(){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


            //select id,note,frequency from table
            String[] columns = {UID, NOTE, FREQ,MIN_FREQ,MAX_FREQ };
            Cursor cursor =sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, null);
            int i = 0;
            while (cursor.moveToNext()){
                int index1 = cursor.getColumnIndex(NOTE);
                int index2 = cursor.getColumnIndex(FREQ);
                int index3 = cursor.getColumnIndex(MIN_FREQ);
                int index4 = cursor.getColumnIndex(MAX_FREQ);
                int cid = cursor.getInt(0);
                note[i] = cursor.getString(index1);
                freq[i] = cursor.getFloat(index2);
                min_freq[i] = cursor.getFloat(index3);
                max_freq[i] = cursor.getFloat(index4);
                i++;
            }
        }

       public int matchFreq(double freq){
            int i,j,match=200;
            for (i=0;i<107;i++){
                if(freq<max_freq[i] && freq>min_freq[i]){
                    match = i;
                }
            }
            return match;
        }

 }

