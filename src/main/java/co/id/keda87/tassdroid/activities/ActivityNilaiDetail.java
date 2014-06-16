package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;

/**
 * Created by Keda87 on 6/17/2014.
 */
public class ActivityNilaiDetail extends Activity {

    private TextView tvJudulNilai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_detail);

        //instance widget
        this.tvJudulNilai = (TextView) findViewById(R.id.tvDmatkul);

        //set typeface
        this.tvJudulNilai.setTypeface(TassUtilities.getFontFace(this, 0));

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.detail_nilai));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
