package com.gabrielMJr.mybusiness.util;

// Custom toast class
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.gabrielMJr.mybusiness.R;

public class CustomToast
{
    // Custom toast attribute
    private static Toast toast;

    // Widget components
    private TextView toast_status;
    private ImageView toast_icon;
    private View custom_toast;
    
    // Toast attributes
    private int drawable;
    private int text;
    private int duration;
    private int background;

    // Context
    private static Context context;

    // Constructor
    public CustomToast(Context context)
    {
        // Passing the context
        this.context = context;
        toast = new Toast(context);
    }

    
    // Set attributes method
    private void set()
    {  
        // Initialize custom toast attributes
        custom_toast = LayoutInflater.from(context).inflate(R.layout.custom_toast_with_icon, null);

        // Initializing
        toast_status = custom_toast.findViewById(R.id.toast_status);
        toast_icon = custom_toast.findViewById(R.id.toast_icon);

        // Set attributes to toast
        setAttributes(drawable, text, duration, background);
    }

    
    // show method
    public void show()
    {
        set();
        toast.show();
    }
    
    
    
    // Setters and getters
    // Set attributes method
    private void setAttributes(int drawable, int text, int duration, int background)
    {
        // Set the attributes to toast
        toast_status.setText(text);
        toast_icon.setImageResource(drawable);
        custom_toast.setBackgroundResource(background);

        // Setting duration into toast
        toast.setDuration(duration);

        // Setting view into Toast
        toast.setView(custom_toast);
    }
   
    
    // For drawable
    public CustomToast setDrawable(int drawable)
    {
        this.drawable = drawable;
        return this;
    }
    
    public int getDrawable()
    {
        return drawable;
    }
    
    
    // For text
    public CustomToast setText(int text)
    {
        this.text = text;
        return this;
    }
    
    public int getText()
    {
        return text;
    }
    
    
    // For duration
    public CustomToast setDuration(int duration)
    {
        this.duration = duration;
        return this;
    }
    
    public int getDuration()
    {
        return duration;
    }
    
    
    // For background
    public CustomToast setBackground(int background)

    {
        this.background = background;
        return this;
    }
    
    public int getBackground()
    {
        return background;
    }
}
