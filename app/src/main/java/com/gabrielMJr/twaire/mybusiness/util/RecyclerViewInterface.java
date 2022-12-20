package com.gabrielMJr.twaire.mybusiness.util;

import android.view.View;


// This interface was reused on main activity, choose product activity and add cart activity
public interface RecyclerViewInterface
 {
    // On Item view, used to get index of clicked card view
    public abstract void onItemClick(int position);
    
    // On long click, used to check product options menu
    public abstract void onLongClick(int position, View view);
}
