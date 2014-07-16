package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;

/**
 * Created by Keda87 on 7/8/2014.
 */
public class ActivityGraphAbsen extends Activity {

    @InjectView(R.id.graphContainer)
    LinearLayout containter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_absen);
        ButterKnife.inject(this);

        GraphViewSeries contoh = new GraphViewSeries(new GraphView.GraphViewData[]{
                new GraphView.GraphViewData(5, 78.13),
                new GraphView.GraphViewData(10, 84.85),
                new GraphView.GraphViewData(15, 78.79),
                new GraphView.GraphViewData(20, 93.33),
                new GraphView.GraphViewData(25, 97.06),
                new GraphView.GraphViewData(30, 87.50)
        });

        GraphView graphView = new BarGraphView(this, getResources().getString(R.string.detail_absen_chart));
        graphView.setHorizontalLabels(new String[]{"MI001", "MI002", "MI003", "MI004", "MI005", "MI006"});
        graphView.setScalable(true);
        graphView.setScrollable(true);

        //style
        graphView.getGraphViewStyle().setTextSize(10);

        graphView.addSeries(contoh);
        containter.addView(graphView);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getResources().getString(R.string.detail_absen_chart));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}