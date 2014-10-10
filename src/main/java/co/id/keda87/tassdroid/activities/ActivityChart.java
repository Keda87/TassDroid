package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.pojos.Absensi;
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

        // get list from intent
        this.listAbsensi = getIntent().getParcelableArrayListExtra("dataAbsensi");

        // create chart series
        XYSeries absensiSeries = new XYSeries("Kehadiran Mahasiswa");

        // add data from list to chart series over for loop
        int counter = 0;
        for (Absensi absen : this.listAbsensi) {
            absensiSeries.add(counter, absen.prosenHadir == null ? 0.0 : Double.parseDouble(absen.prosenHadir));
            counter++;
        }

        // create dataset
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // add series to dataset
        dataset.addSeries(absensiSeries);

        // create bar chart and set configuration
        XYSeriesRenderer absensiRenderer = new XYSeriesRenderer();
        absensiRenderer.setColor(Color.rgb(255, 166, 0));
        absensiRenderer.setFillPoints(true);
        absensiRenderer.setLineWidth(2);
        absensiRenderer.setChartValuesSpacing(50);
        absensiRenderer.setDisplayChartValues(false);

        // create chart renderer and set configuration
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Chart Kehadiran");
        multiRenderer.setYTitle("Pertemuan");
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setBarSpacing(1.5);
        multiRenderer.setBarWidth(30);
        multiRenderer.setXLabelsPadding(5);
        multiRenderer.setLabelsTextSize(10);

        // add kodeMk to multirenderer to display the xlabel in chart
        int hit = 0;
        for (Absensi data : this.listAbsensi) {
            multiRenderer.addXTextLabel(hit, data.kodeMk);
            hit++;
        }

        // add absensiRenderer chart to our multiRenderer
        multiRenderer.addSeriesRenderer(absensiRenderer);

        // inflate the layout
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.activity_absensi_chart, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // create view from achartengine
        GraphicalView view = ChartFactory.getBarChartView(this, dataset, multiRenderer, BarChart.Type.DEFAULT);

        // add chartview to current content view
        addContentView(view, params);
    }
}