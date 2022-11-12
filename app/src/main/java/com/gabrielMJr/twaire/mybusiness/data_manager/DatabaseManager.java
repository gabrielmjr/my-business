package com.gabrielMJr.twaire.mybusiness.data_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper
{
    // Database names
    private static final String DB1_NAME = "data_center_config.db";
    
    // Table names
    private static final String TABLE1_NAME = "total_data";
    
    //Columns names
    private static final String COL_1 = "ID";
    private static final String COL1_2 = "total_avaliable";
    
    private static SQLiteDatabase db;
    private static Boolean isDatabaseEmpty = false;
    
    private static ContentValues CV;
    
    // Contructor
    public DatabaseManager(Context context)
    {
        super(context, DB1_NAME, null, 1);
        db = this.getWritableDatabase();
        CV = new ContentValues();
    }
    
    // Creating database
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                  + TABLE1_NAME
                  + "("
                  + COL_1
                  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                  + COL1_2
                  + " INTEGER)");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE "
                  + TABLE1_NAME);
                  
        onCreate(db);
    }
    
    // Adding total index of data center to db
    protected boolean addTotalIndex(int totalIndex)
    {
        CV.put(COL1_2, totalIndex);
        
        Long result = db.insert(TABLE1_NAME, null, CV);
        
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    protected int getTotalIndex()
    {
        // Getting the database
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE1_NAME, null);
        
        // Return the ladt value if db isnt empty
        int result = 0;
        while (cursor.moveToNext())
        {
           result = cursor.getInt(1);
            setIsDatabaseEmpty(false);
        }
        return result;
    }
    
    // Getting and setting has data on database
    protected Boolean isDataCenterEmpty()
    {
        return isDatabaseEmpty;
    }
    
    private void setIsDatabaseEmpty(Boolean isDatabaseEmpty)
    {
        this.isDatabaseEmpty = isDatabaseEmpty;
    }
}
