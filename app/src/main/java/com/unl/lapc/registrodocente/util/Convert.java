package com.unl.lapc.registrodocente.util;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Usuario on 29/07/2016.
 */
public class Convert {

    public static double toDouble(String s){
        try{
            return  Double.parseDouble(s);
        }catch (Exception e){
            return  0;
        }
    }

    public static int toInt(String s){
        try{
            return  Integer.parseInt(s);
        }catch (Exception e){
            return  0;
        }
    }

    public static Date toDate(DatePicker dp){
        GregorianCalendar calendarBeg=new GregorianCalendar(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
        Date fecha=calendarBeg.getTime();
        return  fecha;
    };

    public static String toShortDateString(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return  sd.format(date);
    }

    public static String toYearMonthNameString(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("MMMM yyyy");
        return  sd.format(date);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
