package com.example.nick.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;


/**
 * Created by Nick on 15-Mar-17.
 */

public class DBHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IOU.db";

    public static class dbEntry implements BaseColumns {

        public static final String TABLE_NAME = "IOUs";
        public static final String COLUMN_TITLE = "Person";
        public static final String COLUMN_TOTAL = "Total";
        public static final String COLUMN_PAYED = "Payed";
        public static final String COLUMN_DUE_DATE = "Date";
        public static final String COLUMN_COMPLETED = "Completed";
    }

    private static final String SQL_Create_Table =
            "CREATE TABLE " + dbEntry.TABLE_NAME + " ("+
                    dbEntry._ID + " INTEGER PRIMARY KEY," +
                    dbEntry.COLUMN_TITLE + " TEXT," +
                    dbEntry.COLUMN_TOTAL + " REAL," +
                    dbEntry.COLUMN_PAYED + " REAL," +
                    dbEntry.COLUMN_DUE_DATE + " TEXT," +
                    dbEntry.COLUMN_COMPLETED + " NUMERIC)"  ;

    private static final String SQL_Delete_Table =
            "DROP TABLE IF EXISTS " + dbEntry.TABLE_NAME;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public  void onCreate (SQLiteDatabase db) {

        db.execSQL(SQL_Create_Table);

    }

    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_Delete_Table);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void deleteAll ()
    {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(SQL_Delete_Table);
        onCreate(db);
    }

    public long insertRow (String title, double total, double payed, Date due, boolean complete) {
        SQLiteDatabase db = getWritableDatabase();

        String dueDate = due.toString();
        int completed =0;
        if (complete)
        {   completed=1;    }
        ContentValues values = new ContentValues();
        values.put(dbEntry.COLUMN_TITLE, title);
        values.put(dbEntry.COLUMN_TOTAL, total);
        values.put(dbEntry.COLUMN_PAYED, payed);
        values.put(dbEntry.COLUMN_DUE_DATE, dueDate);
        values.put(dbEntry.COLUMN_COMPLETED, completed);


        return db.insert(dbEntry.TABLE_NAME, null, values);
    }


    public boolean deleteRow (long ID)
    {
        SQLiteDatabase db = getReadableDatabase();
        if (rowExists( ID)) {

            String selection = dbEntry._ID + " Like ?";
            String[] selectionArgs = {Long.toString(ID)};
            int i= db.delete(dbEntry.TABLE_NAME, selection, selectionArgs);
            if (i==1) {
                return true;
            }
        }
        return false;
    }

    public String[] getRowByID (long ID)
    {
        SQLiteDatabase db = getReadableDatabase();
        if (rowExists( ID)) {
            String[] projection = {
                    dbEntry._ID,
                    dbEntry.COLUMN_TITLE,
                    dbEntry.COLUMN_TOTAL,
                    dbEntry.COLUMN_PAYED,
                    dbEntry.COLUMN_DUE_DATE,
                    dbEntry.COLUMN_COMPLETED
            };

            String selection = dbEntry._ID + " = ?";
            String[] selectionArgs = {Long.toString(ID)};
            String sortOrder = dbEntry._ID + " DESC";

            Cursor cur = db.query(
                    dbEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );

            String [] values = new String[6];

            while (cur.moveToNext()) {
                values [0] = cur.getString(cur.getColumnIndexOrThrow(dbEntry._ID ));
                values [1] = cur.getString(cur.getColumnIndexOrThrow(dbEntry.COLUMN_TITLE ));
                values [2] = cur.getString(cur.getColumnIndexOrThrow(dbEntry.COLUMN_TOTAL ));
                values [3] = cur.getString(cur.getColumnIndexOrThrow(dbEntry.COLUMN_PAYED ));
                values [4] = cur.getString(cur.getColumnIndexOrThrow(dbEntry.COLUMN_DUE_DATE ));
                values [5] = cur.getString(cur.getColumnIndexOrThrow(dbEntry.COLUMN_COMPLETED ));
            }
            cur.close();

            return values;
        }
        return null;
    }

   public boolean editRow (long ID, String title, double total, double payed, Date due, boolean complete)
   {
       SQLiteDatabase db = getWritableDatabase();

       String dueDate = due.toString();
       String where =  dbEntry._ID + " = " + Long.toString(ID);

       int completed =0;
       if (complete)
       {   completed=1;    }
       ContentValues values = new ContentValues();
       values.put(dbEntry.COLUMN_TITLE, title);
       values.put(dbEntry.COLUMN_TOTAL, total);
       values.put(dbEntry.COLUMN_PAYED, payed);
       values.put(dbEntry.COLUMN_DUE_DATE, dueDate);
       values.put(dbEntry.COLUMN_COMPLETED, completed);

       db.update(dbEntry.TABLE_NAME, values, where, null);

       return true;
   }

    public long getIDByIOU(String title, double total, double payed, Date due, boolean completed)
    {
        SQLiteDatabase db = getReadableDatabase();
        long ID;
        int comp =0;
        if (completed)
        {comp = 1;}
        String query =  "SELECT " + dbEntry._ID + " FROM " + dbEntry.TABLE_NAME +
                        " WHERE " + dbEntry.COLUMN_TITLE + " = \"" + title +
                        "\" AND " + dbEntry.COLUMN_TOTAL + " = " + total +
                        " AND " + dbEntry.COLUMN_PAYED + " = " + payed +
                        " AND " + dbEntry.COLUMN_DUE_DATE + " = \"" + due.toString() +
                        "\" AND " + dbEntry.COLUMN_COMPLETED + " = " +   comp + ";";

        Cursor cur = db.rawQuery(query,null);

        if (cur != null && cur.moveToFirst()) {
            ID = cur.getLong(0);
            return ID;
        }
        Log.d("ERROR FINDING ROW", "Could not find row:\n" + title + " " +
                total + " " + payed + " " + due.toString() + " " + completed);

        return -1;
    }


   public long getLastRowID()
   {
       SQLiteDatabase db = getReadableDatabase();
       long ID ;
       String query =   "select " + dbEntry._ID + " from " + dbEntry.TABLE_NAME +
                        " ORDER BY " + dbEntry._ID + " DESC LIMIT 1";
       Cursor cur = db.rawQuery(query,null);

       if (cur != null && cur.moveToFirst()) {
           ID = cur.getLong(0);
           return ID;

       }
        return -1;
   }

    public long [] getAllIDs ()
    {
        SQLiteDatabase db = getReadableDatabase();
        String IDString = "";

        Cursor cur = db.rawQuery("select * from " + dbEntry.TABLE_NAME,null);

        if (cur.moveToFirst()) {
            while (cur.isAfterLast() == false) {
                String id =   cur.getString(cur.getColumnIndex(dbEntry._ID)) ;
                IDString+= id;
                if (!cur.isLast())
                {
                    IDString +=",";
                }


                cur.moveToNext();
            }
        }

        cur.close();

        String [] temp = IDString.split(",");

        long [] IDs = new long[temp.length];

        for (int i=0; i<temp.length; i++)
        {
            IDs[i] = Long.parseLong(temp[i]);
        }

        return IDs;
    }

    private boolean rowExists (long ID)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + dbEntry.TABLE_NAME +
                " WHERE " + dbEntry._ID + " = " + ID ;
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() <=0)
        {
            c.close();
            return false;
        }
        c.close();
        return true;
    }


}
