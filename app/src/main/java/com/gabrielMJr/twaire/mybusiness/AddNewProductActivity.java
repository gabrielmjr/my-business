package com.gabrielMJr.twaire.mybusiness;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import com.gabrielMJr.twaire.mybusiness.R;

public class AddNewProductActivity extends AppCompatActivity {
    
    // Attributes
    private static ImageView add_new_product_image;
    private static EditText add_new_product_name;
    private static EditText add_new_product_price;
    
    //private static String path;
    //private static Uri uri;
    
    // Initializing
    private void initialize()
    {
        add_new_product_image = findViewById(R.id.add_new_product_image);
        
        add_new_product_name = findViewById(R.id.add_new_product_name);
        add_new_product_price = findViewById(R.id.add_new_product_price);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        initialize();
        
   
    }
    
}
