package com.unl.lapc.registrodocente.util;

/**
 * Created by jaja on 20/11/2016.
 */

public class CedulaUtils {
    /**
     * Obtiene el número verificador de la cédula.
     *
     * @param cedula el número de cédula
     * @return el dígito
     */
    private static int obtenerDigitoVerificador(String cedula) {
        int verificador = 0;
        char num;
        String nu = "";
        int n1 = 0;
        int aux = 0;
        int aux1 = 0;
        int ver = 0;
        for (int i = 0; i < 10; i = i + 2) {
            num = cedula.charAt(i);
            nu = String.valueOf(num);
            n1 = Integer.parseInt(nu);
            n1 = n1 * 2;
            while (n1 > 9) {
                n1 = n1 - 9;
            }
            aux = aux + n1;
        }

        for (int j = 1; j < 9; j = j + 2) {
            num = cedula.charAt(j);
            nu = String.valueOf(num);
            n1 = Integer.parseInt(nu);
            aux1 = aux1 + n1;
        }

        verificador = aux + aux1;

        //saca el verificador
        int divid = verificador;
        int divis = divid / 10;
        int res = divid - (10 * divis);
        if (res == 0) {
            ver = res;
        } else {
            ver = 10 - res;
        }
        return ver;
    }

    /**
     * Permite determinar si la cadena ingresada son solo números.
     *
     * @param cadena la cedena a verificar
     * @return true si es cierto
     */
    private static  boolean esNumero(String cadena) {
        boolean b = true;
        for (int i = 0; ((i < cadena.length())&& (b== true)); i++) {
            char cha = cadena.charAt(i);
            b = Character.isDigit(cha);
        }
        return b;
    }

    /**
     * Verifica que el número de cédula sea correcto.
     *
     * @param cedula el número de cédula
     * @return true si es correcta
     */
    public static boolean validar(String cedula) {
        boolean verifica = false;
        boolean esNum = esNumero(cedula);

        if(cedula.length() == 10 && esNum){
            int v = obtenerDigitoVerificador(cedula);
            int dig = Integer.parseInt(String.valueOf(cedula.charAt(9)));
            if (v == dig) {
                verifica = true;
            } else {
                verifica = false;
            }
        }else{
            verifica = false;
        }

        return verifica;
    }
}
