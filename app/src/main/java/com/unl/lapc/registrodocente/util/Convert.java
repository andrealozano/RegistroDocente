package com.unl.lapc.registrodocente.util;

import android.os.Environment;
import android.widget.DatePicker;

import java.io.File;
import java.io.FileWriter;
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

    public static String currentReportDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        return  dateFormat.format(new Date());
    }

    public static void writeCsvLine(StringBuilder sb, Object... prm){
        for(int i=0; i < prm.length; i++){
            Object p = prm[i];
            sb.append("\"" + p + "\"");
            if(i+1 == prm.length){
                sb.append("\n");
            }else{
                sb.append(";");
            }
        }
    }

    public static File getExternalStorageDirectory(String folder){
        File sd = Environment.getExternalStorageDirectory();
        if(sd.canWrite()) {
            File sdapp = new File(sd.getAbsolutePath() + "/RegistroDocente/"+folder+"/");
            if (!sdapp.exists()) {
                sdapp.mkdirs();
            }
            return  sdapp;
        }
        return null;
    }

    public static File getExternalStorageFile(String folder, String fileName){
        File file = getExternalStorageDirectory(folder);
        if(file != null){
            File fext = new File(file, fileName);
            return  fext;
        }
        return  null;
    }

    public static void writeToFile(StringBuilder sb, File file){
        FileWriter writer = null;
        try{
            writer = new FileWriter(file);
            writer.append(sb.toString());
            writer.flush();
        }catch (Exception e){
        }finally {
            try{writer.close();}catch (Exception ex){}
        }
    }

}
