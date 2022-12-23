package com.gabrielMJr.mybusiness.data_manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class ProductDatabase
{
    // Attributes
    // DataBase components
    private static final String DB_NAME = "products.db";

    // Table name
    private static final String TBN = "actual_products";

    // Primary key && anothers
    private final String COL_1 = "id";
    private final String COL_2 = "name";
    private final String COL_3 = "price";
    private final String COL_4 = "amount";

    // Actual opened database index
    private int index;

    private Context context;

    private SQLiteDatabase db;

    // Constructor
    public ProductDatabase(Context context)
    {
        this.context = context;
        initDb();
        createTable();
    }
    
    // InitializeDb
    private void initDb()
    {
        db = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null); 
    }

    // Create new table
    private void createTable()
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                   + TBN + "("
                   + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + COL_2 + " TEXT, "
                   + COL_3 + " REAL, "
                   + COL_4 + " INTEGER)");
    }

    // Add products
    public Boolean addNewProduct(String name, float price, int amount)
    {
        initDb();
        try
        {
            db.execSQL("INSERT INTO " 
                       + TBN + "("
                       + COL_2 + ","
                       + COL_3 + ","
                       + COL_4 + ") VALUES ('"
                       + name + "', "
                       + price + ", "
                       + amount + ")");
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

        finally
        {
            db.close();
        }
    }

    // Delete product
    public Boolean deleteProduct(int id)
    {
        initDb();
        try
        {           
            // Delete db
            db.execSQL("DELETE FROM " + TBN + " WHERE " + COL_1 + " = " + id);
            return true;
        }

        // Some error happened
        catch (Exception e)
        {
            return false;
        }
    }

    // Check product by name
    public Boolean checkByName(String name)
    {
        return false;
    }

    // setters and getters
    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    // Is database empty
    protected Boolean isProductDBEmpty()
    {
        initDb();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBN, null);

        Boolean isEmpty = true;

        while (cursor.moveToNext())
        {
            isEmpty = false;
            break;
        }

        return isEmpty;
    }

    // Get total availables products
    protected int getTotalIndex()
    {
        initDb();
        Cursor cursor = db.rawQuery("SELECT " + COL_1 + " FROM " + TBN, null);
        return cursor.getCount();
    }

    // Get name from id
    public String getName(int id)
    {
        initDb();
        Cursor cursor = db.rawQuery("SELECT " + COL_2 + " FROM " + TBN + " WHERE " + COL_1 + " = '" + id + "'", null);
        if (cursor.moveToFirst())
        {
            return cursor.getString(0);
        }
        return null;
    }

    // get price from id
    protected float getPrice(int id)
    {
        initDb();
        Cursor cursor = db.rawQuery("SELECT " + COL_3 + " FROM " + TBN + " WHERE " + COL_1 + " = " + id, null);
        if (cursor.moveToFirst())
        {
            return cursor.getFloat(0);
        }
        return 0.0f;
    }

    // Get amount from id
    protected int getAmount(int id)
    {
        initDb();
        Cursor cursor = db.rawQuery("SELECT " + COL_4 + " FROM " + TBN + " WHERE " + COL_1 + " = " + id, null);
        if (cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }
        return -1;
    }

    // Get index from name
    protected int getID(String name)
    {
        initDb();
        Cursor cursor = db.rawQuery("SELECT " + COL_1 + " FROM " + TBN + " WHERE " + COL_2 + " = '" + name + "'", null);
        return cursor.getCount();
    }

    // Get last id int
    protected int getLastID()
    {
        initDb();
        Cursor cursor = db.rawQuery("SELECT " + COL_1 + " FROM " + TBN , null);
        if (cursor.moveToLast())
        {
            return cursor.getInt(0);
        }

        return 0;
    }

    // Get total available ids
    protected ArrayList<Integer> getIDs()
    {
        initDb();
        ArrayList<Integer> ids = new ArrayList<>();

        // Get ids from cursor
        Cursor cursor = db.rawQuery("SELECT " + COL_1 + " FROM " + TBN, null);
        while (cursor.moveToNext())
        {
            ids.add(cursor.getInt(0));
        }

        // Return the value
        return ids;
    }
}
