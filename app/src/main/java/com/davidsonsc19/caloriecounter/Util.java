package com.davidsonsc19.caloriecounter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Util {

    private static int currentItemID = -1;
    public static int edittingItemID = -1;
    public static Date usingDate = new Date();

    // getTodaysFilename returns something like "cc-27Dec2017"
    public static String getFilename(Date date) {
        String[] dateFields = date.toString().split(" ");
        return "cc-" + dateFields[2] + dateFields[1] + dateFields[5];
    }

    public static int getNextItemID() {
        return ++currentItemID;
    }

    public static void setCurrentItemID(int num) {
        // This should only be called once on startup
        currentItemID = num;
    }

    public static Date incrementDate(Date date, int increment) {
        long ms = date.getTime();
        return new Date(ms + increment * TimeUnit.DAYS.toMillis(1));
    }

    public static boolean datesMatch(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static String getDateString(Date date) {
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
    }
}
