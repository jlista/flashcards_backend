package com.flashcards.repository;

import java.util.Date;

public class MongoHelper {
    public static String isoDateFormat(Date date){
        
        return String.format("ISODate('%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tSZ')", date);
    }
}
