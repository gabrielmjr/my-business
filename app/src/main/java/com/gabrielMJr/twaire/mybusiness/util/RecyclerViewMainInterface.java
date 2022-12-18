package com.gabrielMJr.twaire.mybusiness.util;

import android.view.View;

public interface RecyclerViewMainInterface
 {
    // On Item view, used to get index of clicked card view
    public abstract void onItemClick(int position);
    
    // On long click, used to check product options menu
    public abstract void onLongItClick(int position, View view);
}
