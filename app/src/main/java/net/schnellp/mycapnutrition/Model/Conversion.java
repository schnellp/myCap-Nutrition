package net.schnellp.mycapnutrition.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class Conversion {
    public static String dayNumberToDate(int day) {
        String dt = "1970-01-01";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch(Exception e) {
            System.out.println(e);
        }

        c.add(Calendar.DATE, day);  // number of days to add
        dt = sdf.format(c.getTime());  // dt is now the new date
        return dt;
    }

    public static int dateToDayNumber(String date) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String epoch = "1970-01-01";

        long diff = -1;

        try {
            Date epochDate = myFormat.parse(epoch);
            Date targetDate = myFormat.parse(date);
            diff = targetDate.getTime() - epochDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static String dayNumberToLongDate(int day) {
        String dt = "Thursday, January 1, 1970";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch(Exception e) {
            System.out.println(e);
        }

        c.add(Calendar.DATE, day);  // number of days to add
        dt = sdf.format(c.getTime());  // dt is now the new date
        return dt;
    }
}
