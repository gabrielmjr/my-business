package com.gabrielMJr.twaire.mybusiness.util;

import android.view.View;

public interface AddCartInterface
{
    // On Item view, used to get index of clicked card view
    public abstract void onItemClick(int position);
    
    // On long click, used to check product options menu
    public abstract void onLongClick(int position, View view);
    
    // Notify item incremented
    public abstract void onItemIncremented(int position, int new_amount);
    
    // Notify item decremented
    public abstract void onItemDecremented(int position, int new_amount);
    
}
