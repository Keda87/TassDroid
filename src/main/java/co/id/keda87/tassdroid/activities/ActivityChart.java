package co.id.keda87.tassdroid.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.pojos.Absensi;
import co.id.keda87.tassdroid.pojos.Jadwal;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

/**
 * Created by Keda87 on 10/9/2014.
 */
public class ActivityChart extends Activity {

    private List<Absensi> listAbsensi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_chart);

        this.listAbsensi = getIntent().getParcelableArrayListExtra("dataAbsensi");

        XYSeries absensiSeries = new XYSeries("Kehadiran Mahasiswa");

        int counter = 0;
        for (Absensi absen : this.listAbsensi) {
            absensiSeries.add(counter, absen.prosenHadir == null ? 0.0 : Double.parseDouble(absen.prosenHadir));
            counter++;
        }

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(absensiSeries);

        XYSeriesRenderer absensiRenderer = new XYSeriesRenderer();
        absensiRenderer.setColor(Color.rgb(255, 166, 0));
        absensiRenderer.setFillPoints(true);
        absensiRenderer.setLineWidth(2);
        absensiRenderer.setChartValuesSpacing(50);
        absensiRenderer.setDisplayChartValues(false);

        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Chart Kehadiran");
        multiRenderer.setYTitle("Pertemuan");
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setBarSpacing(1.5);
        multiRenderer.setBarWidth(20);
        multiRenderer.setXLabelsPadding(5);
        multiRenderer.setLabelsTextSize(10);

        int hit = 0;
        for(Absensi data : this.listAbsensi) {
            multiRenderer.addXTextLabel(hit, data.kodeMk);
            hit++;
        }

        multiRenderer.addSeriesRenderer(absensiRenderer);

        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.activity_absensi_chart, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        GraphicalView view = ChartFactory.getBarChartView(this, dataset, multiRenderer, BarChart.Type.DEFAULT);
        addContentView(view, params);
    }
}