package com.unl.lapc.registrodocente.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unl.lapc.registrodocente.R;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.unl.lapc.registrodocente.dao.PeriodoDao;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Periodo;
import com.unl.lapc.registrodocente.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class PeriodoChart extends AppCompatActivity {

    static final int PICK_DESTINO_REPORTE_REQUEST = 1;

    private PieChart pieChart;
    private TextView txtPeriodo;
    private Button imgDown;

    private PeriodoDao periodoDao;
    private Periodo periodo;
    private Clase clase;
    private File emailFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodo_chart);

        Bundle bundle = getIntent().getExtras();
        periodo = bundle.getParcelable("periodo");
        clase = bundle.getParcelable("clase");
        periodoDao = new PeriodoDao(this);

        pieChart = (PieChart) findViewById(R.id.pieChart);
        txtPeriodo = (TextView)findViewById(R.id.txtPeriodo);
        txtPeriodo.setText(clase != null ? clase.getNombre() : periodo.getNombre());

        imgDown = (Button)findViewById(R.id.btnExportar);
        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportarCsv();
            }
        });

        initChart();
    }

    private void initChart(){
        Map<String, Integer> lista = periodoDao.estatisticas(periodo, clase);

        /*definimos algunos atributos*/
        pieChart.setHoleRadius(40f);
        //pieChart.setDrawYValues(true);
        //pieChart.setDrawXValues(true);
        pieChart.setRotationEnabled(true);
        pieChart.animateXY(1500, 1500);

		/*creamos una lista para los valores Y*/
        ArrayList<Entry> valsY = new ArrayList<Entry>();
        /*valsY.add(new Entry(5* 100 / 25,0));
        valsY.add(new Entry(20 * 100 / 25,1));*/
        int ix = 0;
        for(String k: lista.keySet()){
            int v = lista.get(k);
            valsY.add(new Entry(v, ix));
            ix++;
        }

 		/*creamos una lista para los valores X*/
        ArrayList<String> valsX = new ArrayList<String>();
        /*valsX.add("Varones");
        valsX.add("Mujeres");*/
        for(String k: lista.keySet()){
            valsX.add(k);
        }

 		/*creamos una lista de colores*/
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.colorPrimaryDark));

 		/*seteamos los valores de Y y los colores*/
        PieDataSet set1 = new PieDataSet(valsY, "Resultados");
        set1.setSliceSpace(3f);
        set1.setColors(colors);

		/*seteamos los valores de X*/
        PieData data = new PieData(valsX, set1);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate();

        /*Ocutar descripcion*/
        pieChart.setDescription("");
        /*Ocultar leyenda*/
        //pieChart.setDrawLegend(false);
    }

    /**
     * Genera el reporte estadístico y lo envia a la memoria o al correo
     */
    private void exportarCsv(){
        //

        new AlertDialog.Builder(this).setTitle("Estadísticas").setItems(R.array.destino_respaldo_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Map<String, Integer> lista = periodoDao.estatisticas(periodo, clase);

                StringBuilder sb= new StringBuilder();
                Utils.writeCsvLine(sb, "ESTADISTICAS: " + clase != null ? clase.getNombre() : periodo.getNombre());


                for(String s : lista.keySet()){
                    Utils.writeCsvLine(sb, s, lista.get(s));
                }

                Utils.checkReportPermisions(PeriodoChart.this);
                emailFile = Utils.getExternalStorageFile("reportes", String.format("Estadisticas_%s_%s.csv", clase != null ? clase.getNombre() : periodo.getNombre(),  Utils.currentReportDate()));
                Utils.writeToFile(sb, emailFile);

                if (which == 0){
                    emailFile = null;
                }else if(emailFile != null){
                    //Envia al correo
                    Uri u1 = Uri.fromFile(emailFile);
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Registro Docente - Estadísticas");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Estadísticas: " + emailFile.getName());
                    sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, Utils.getEmailPref(getApplicationContext()));
                    sendIntent.setType("text/html");
                    startActivityForResult(Intent.createChooser(sendIntent, "Destino reporte"), PICK_DESTINO_REPORTE_REQUEST);
                }
            }
        }).create().show();
    }
}
