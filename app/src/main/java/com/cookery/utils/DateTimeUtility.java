package com.cookery.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.cookery.utils.Constants.DB_DATE_TIME;
import static com.cookery.utils.Constants.UI_DATE;

public class DateTimeUtility {
    private static final String CLASS_NAME = DateTimeUtility.class.getName();

    public static Date convertStringToDateTime(String dateTimeStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            return sdf.parse(dateTimeStr);
        } catch (ParseException e) {
            Log.e(CLASS_NAME, "Date Time parse Exception !! Date/Time(" + dateTimeStr + ") Format(" + format + ") : " + e.getMessage());
        }

        return null;
    }

    public static String getSmartDate(String date){
        if(date != null && !date.trim().isEmpty()){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DB_DATE_TIME);
                SimpleDateFormat sdf1 = new SimpleDateFormat(UI_DATE);

                return sdf1.format(sdf.parse(date));
            } catch (ParseException e) {
                Log.e(CLASS_NAME, "Date Time parse Exception !! Date(" + date + ") : " + e.getMessage());
            }
        }

        return "ERROR";
    }

    public static String getSmartDateTime(Date dateTime) {
        SimpleDateFormat sdf = null;

        Calendar now = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);

        int thisYear = now.get(Calendar.YEAR);
        int thisMonth = now.get(Calendar.MONTH);
        int thisDay = now.get(Calendar.DAY_OF_MONTH);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (thisYear == year) {
            if (thisMonth == month) {
                if (thisDay == day) {
                    long milliseconds = now.getTime().getTime() - cal.getTime().getTime();

                    if (milliseconds < 10 * 60 * 60 * 1000) {
                        return getSmartTime(milliseconds);
                    } else {
                        sdf = new SimpleDateFormat("'at' h:mm a");
                    }
                } else {
                    sdf = new SimpleDateFormat("d MMM 'at' h:mm a");
                }
            } else {
                sdf = new SimpleDateFormat("d MMM 'at' h:mm a");
            }
        } else {
            sdf = new SimpleDateFormat("d MMM yyyy 'at' h:mm a");
        }

        return sdf.format(dateTime);
    }

    public static String getCreateOrModifiedTime(String createDtm, String modifiedDtm){
        if(modifiedDtm != null && !modifiedDtm.trim().isEmpty()){
            return DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(modifiedDtm, DB_DATE_TIME));
        }
        else if(createDtm != null && !createDtm.trim().isEmpty()){
            return DateTimeUtility.getSmartDateTime(DateTimeUtility.convertStringToDateTime(createDtm, DB_DATE_TIME));
        }
        else{
            return "ERROR";
        }
    }

    public static String getSmartTime(long millSeconds) {
        long minutes = millSeconds / 3600;

        if (minutes < 24 * 60 * 60) {
            if (minutes < 60) {
                if (minutes <= 5) {
                    return "Just now";
                } else {
                    return (int) minutes + " minutes ago";
                }
            } else {
                int hours = (int) minutes / 60;
                return hours + " hours ago";
            }
        } else {
            int days = (int) minutes / 24;
            return days + " days ago";
        }
    }
}
