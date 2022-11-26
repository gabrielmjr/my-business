package com.gabrielMJr.twaire.mybusiness.data_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import com.gabrielMJr.twaire.mybusiness.data_manager.ProductDatabase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ProductDataCenter extends AppCompatActivity
{
    // Context
    private static Context context;

    // Shared preferences attr
    private static SharedPreferences product_id;

    // product preffix
    protected static final String PRODUCT = "product_";

    // Product descriptions
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String AMOUNT = "amount";

    // Shared preferences editor
    private static SharedPreferences.Editor editor;

    // Product database
    private static ProductDatabase product_data_base;

    // Home folder
    public static final String home = "files";

    // Images folder
    public static final String image_dir = "products_pics";

    // pwd = actual directory
    public static String pwd; 
    
    // Constructor
    public ProductDataCenter(Context context)
    {
        this.context = context;
        pwd = context.getApplicationInfo().dataDir;
        product_data_base = new ProductDatabase(context);
    }

    // Creating if not exists local private dir for the app
    public boolean createHome()
    {
        File file = new File(pwd + "/" + home);

        if (!file.exists())
        {
            // Return true if dir was created
            return file.mkdir();
        }
        else
        {
            return false;
        }
    }

    // Folder for the picked images
    public boolean createImgDir()
    {
        String pwd = context.getApplicationInfo().dataDir;
        File file = new File(pwd + "/" + home + "/" + image_dir);

        // If the folder already exists
        if (file.exists())
        {
            return false;
        }

        // If not exists
        else
        {
            file.mkdir();
            return true;
        }
    }


    // add product and return boolean value
    public boolean addProduct(String name, float price, int initial_amount,  BitmapDrawable image)
    {
        // Total index of avaliable products
        int lastIndex = 0;//dbm.getTotalIndex();

        product_id = context.getSharedPreferences(PRODUCT + lastIndex, 0);    
        editor = product_id.edit();

        // Saving the image
        addImage(image, lastIndex);

        // If data center stored the value
        if (product_data_base.addNewProduct(name, price, initial_amount))
        {
            // Total avaliable + 1
            lastIndex++;

            // If database stored return true, else false
            return true;
        }
        else
        {
            return false;
        }
    }

    // Save image method
    public void addImage(BitmapDrawable drawable, int lastIndex)
    {
        // Converting from bitmalDrawable to bitmal
        Bitmap bitmap = drawable.getBitmap();

        // File contains imageDir/imageFile
        File file = new File(pwd + "/" + home + "/" + image_dir, PRODUCT + lastIndex + ".bmp");
        try
        {        
            // Compressing the image
            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

            try
            {
                output.flush();
            }
            catch (IOException e)
            {}

            try
            {
                output.close();
            }
            catch (IOException e)
            {}
        }
        catch (FileNotFoundException e)
        {}
    }

    // Check for product existing using name
    public boolean checkByName(String name)
    {
        for (int i = 0; i < product_data_base.getTotalIndex(); i++)
        {
            // Compare lowercase of the parameter name and stored name of the product
            if (name.toLowerCase().replaceAll("\\s", "").equals(getName(i).toLowerCase().replaceAll("\\s", "")))
            {
                return true;
            }
        }

        // If the code arrive here, the product doesnt exist
        return false;
    }


    // Getting product name using index
    public String getName(int index)
    {
        product_id = context.getSharedPreferences(PRODUCT + index, 0);
        return product_id.getString(NAME, null);
    }

    // Getting product price using index
    public float getPrice(int index)
    {
        product_id = context.getSharedPreferences(PRODUCT + index, 0);
        return product_id.getFloat(PRICE, 0.0f);
    }

    // Getting the product amount using index
    public int getAmount(int index)
    {
        product_id = context.getSharedPreferences(PRODUCT + index, 0);
        return product_id.getInt(AMOUNT, 0);
    }

    // Getting product image using index
    public Uri getImage(int index)
    {
        File file = new File(pwd + "/" + home + "/" + image_dir, PRODUCT + index + ".bmp");

        //InputStream inputStream = new FileInputStream(path);

        //Drawable bmp = Drawable.createFromPath(path);
        return Uri.fromFile(file);

    }


    // Getting total avaliable product on data
    public int getProductsIndex()
    {
        return product_data_base.getTotalIndex();
    }

    // Is data empty method
    public Boolean isDataCenterEmpty()
    {
        return product_data_base.isProductDBEmpty();
    }
    
    // Setting index before initialize product db
    public void setIndex(Context context, int index)
    {
        initializePDB(context, index);
    }
    
    // Initializing product database
    public void initializePDB(Context context, int index)
    {
        product_data_base = new ProductDatabase(context);
    }
}
