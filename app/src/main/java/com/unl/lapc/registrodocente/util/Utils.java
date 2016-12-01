package com.unl.lapc.registrodocente.util;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.DatePicker;

import com.unl.lapc.registrodocente.activity.LoginActivity;
import com.unl.lapc.registrodocente.activity.SettingsActivity;

import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase de utilidad para hacer operacioes comunes de conversión, formato, escritura, etc.
 */
public class Utils {

    //private static final String EMAIL = "^[a-zA-Z0-9_-]{2,15}@[a-zA-Z0-9_-]{2,15}.[a-zA-Z]{2,4}(.[a-zA-Z]{2,4})?$";
    private static final String EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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
     * Formato de un double a 2 decimales
     * @param value
     * @return
     */
    public static String toString2(Double value) {
        try{
            return String.format(new Locale("es_EC"), "%.2f", value != null ? value : 0);
        }catch (Exception e)
        {
            return  "0.00";
        }
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
        //File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File sd = Environment.getExternalStorageDirectory();
        if(sd.canWrite()) {
            File sdapp = new File(sd.getAbsolutePath() + "/RegistroDocente/"+folder+"/");
            if (!sdapp.exists()) {
                sdapp.mkdirs();
            }
            return  sdapp;
        }else{
            Log.e("ExternalSotorage", "No escribible external storage: " + sd.getAbsolutePath());
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

    public static boolean checkReportPermisions(Activity activity){
        int storage = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int contacts = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            //REQUEST_ID_MULTIPLE_PERMISSIONS
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }

        return true;
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

    public static String[] getEmailPref(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String email = sharedPref.getString(SettingsActivity.KEY_PREF_SERGURIDAD_EMAIL, "");
        return new String[]{email};
    }

    public static boolean validarEmail(String value){
        if(value != null){
            String v = value.toString();

            if(v.length() > 0){
                //asignamos la expresion
                Pattern p = Pattern.compile(EMAIL);
                //comparamos con nuestro valor
                Matcher m = p.matcher(v);
                //si el correo es correcto devuelve TRUE o de lo contrario FALSE

                if(!m.matches()){
                    return false;
                }
            }
        }

        return true;
    }



}
