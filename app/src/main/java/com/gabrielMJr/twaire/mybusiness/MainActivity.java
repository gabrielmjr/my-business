package com.gabrielMJr.twaire.mybusiness;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;
import com.gabrielMJr.twaire.mybusiness.AddNewProductActivity;

public class MainActivity extends AppCompatActivity {

    // Go to add item button
    private static Button add_new_product;
    
    private void initialize()
    {
        add_new_product = findViewById(R.id.add_new_product);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initialize();
        
        add_new_product.setOnClickListener(
            new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(getApplicationContext(),AddNewProductActivity.class)); 
                }
            });
		
    }


}
