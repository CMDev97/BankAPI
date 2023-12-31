package it.dcm.bank.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtility {
    private static final String DATE_FORMAT_STRING = "yyyy-MM-dd";


    public static String getDateString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING);
        return localDate.format(formatter);
    }


    public static boolean validRange(LocalDate from, LocalDate to) {
        if (!(from != null && to != null)) return false;
        return from.isBefore(to);
    }

}
