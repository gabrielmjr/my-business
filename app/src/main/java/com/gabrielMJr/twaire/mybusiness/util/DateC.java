package com.gabrielMJr.twaire.mybusiness.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateC {
    
    // Get date class
    // Date structure
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
    
    // Date class
    private Date date;
    
    // Return the actual date
    public static String getDate()
    {
        //return sdf.format(new Date());
        return "b";
    }
}
