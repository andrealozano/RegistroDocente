package com.unl.lapc.registrodocente.util;

import android.os.Environment;
import android.widget.DatePicker;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Clase de utilidad para hacer operacioes comunes de conversión, formato, escritura, etc.
 */
public class Utils {

    /**
     * Convierte una cadena double
     * @param s la cadena
     * @return
     */
    public static double toDouble(String s){
        try{
            return  Double.parseDouble(s);
        }catch (Exception e){
            return  0;
        }
    }

    /**
     * Convierte una cadena a entero
     * @param s la cadena
     * @return
     */
    public static int toInt(String s){
        try{
            return  Integer.parseInt(s);
        }catch (Exception e){
            return  0;
        }
    }

    /**
     * Extrae la fecha de un componente android DatePicker
     * @param dp el componente
     * @return
     */
    public static Date toDate(DatePicker dp){
        GregorianCalendar calendarBeg=new GregorianCalendar(dp.getYear(),dp.getMonth(),dp.getDayOfMonth());
        Date fecha=calendarBeg.getTime();
        return  fecha;
    };

    /**
     * Convierte una fecha a cadena en formato yyyy-MM-dd
     * @param date la fecha
     * @return
     */
    public static String toShortDateString(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return  sd.format(date);
    }

    /**
     * Convierte una fecha a cadena en formato MMMM yyyy (Ejm: Enero 2016)
     * @param date la fecha
     * @return
     */
    public static String toYearMonthNameString(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("MMMM yyyy");
        return  sd.format(date);
    }

    /**
     * Redondea un valor al número de decimales indicado
     * @param value el valor a redondear
     * @param places el número de decimales
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Genera una cadena con la fecha actual del sistema en formato yyyy-MM-dd-HH-mm.
     * Usada para reportes.
     * @return
     */
    public static String currentReportDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        return  dateFormat.format(new Date());
    }

    /**
     * Escribe el listado de parámetros en formato csv.
     * No agrega saldo de línea.
     * @param sb
     * @param prm
     */
    public static void writeCsv(StringBuilder sb, Object... prm){
        for(int i=0; i < prm.length; i++){
            Object p = prm[i];
            sb.append("\"" + p + "\"");
            if(i+1 == prm.length){
                sb.append(";");
            }else{
                sb.append(";");
            }
        }
    }

    /**
     * Escribe el listado de parámetros en formato csv.
     * Agrega salto de línea al final.
     * @param sb
     * @param prm
     */
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

    /**
     * Obtiene el directorio correspondiente a la tarjeta eterna de memoria.
     * @param folder
     * @return
     */
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

    /**
     * Obtiene el la subcarpeta correspondiente a la tarjeta eterna de memoria.
     * @param folder
     * @param fileName
     * @return
     */
    public static File getExternalStorageFile(String folder, String fileName){
        File file = getExternalStorageDirectory(folder);
        if(file != null){
            File fext = new File(file, fileName);
            return  fext;
        }
        return  null;
    }

    /**
     * Escribe la cadena dentro del fichero indicado.
     * @param sb
     * @param file
     */
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
