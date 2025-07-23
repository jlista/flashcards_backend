package com.flashcards;

import java.util.Date;

public class CardHelper {

    public static String getMongoDateFormat(Date date) {
        return String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tSZ", date);
    }

    public static String getIsoDateFormat(Date date) {
        return String.format("ISODate('%s')", CardHelper.getMongoDateFormat(date));
    }
}
