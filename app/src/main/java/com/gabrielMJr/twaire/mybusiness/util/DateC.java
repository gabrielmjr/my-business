package com.gabrielMJr.twaire.mybusiness.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateC
 {
    
    // Attributes
    private static DateTimeFormatter formatter;
    private static LocalDateTime date;
    
    // Init method
    private static void init()
    {
        // Initialize the date variables
        formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMATER);
        date = LocalDateTime.now();
    }
     
    // Get date method
    public static String getDate()
    {
        // Initialize the variables method
        init();
        
        // Format date and return
        return date.format(formatter);
    }
}
