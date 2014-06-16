package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.NilaiMentah;

/**
 * Created by Keda87 on 6/17/2014.
 */
public class ActivityNilaiDetail extends Activity {

    private TextView tvJudulNilai, tvSemester, tvSks, tvKelas, tvKodeDosen;
    private TextView lSemester, lSks, lKelas, lKodeDosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_detail);

        //instance label widget
        this.lSemester = (TextView) findViewById(R.id.tvDLsem);
        this.lSks = (TextView) findViewById(R.id.tvDLsks);
        this.lKelas = (TextView) findViewById(R.id.tvDLkls);
        this.lKodeDosen = (TextView) findViewById(R.id.tvDLkddsn);

        //instance value widget
        this.tvJudulNilai = (TextView) findViewById(R.id.tvDmatkul);
        this.tvSemester = (TextView) findViewById(R.id.tvDsem);
        this.tvSks = (TextView) findViewById(R.id.tvDsks);
        this.tvKelas = (TextView) findViewById(R.id.tvDkls);
        this.tvKodeDosen = (TextView) findViewById(R.id.tvDkddsn);

        //set typeface
        this.tvJudulNilai.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvSemester.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvSks.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvKelas.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvKodeDosen.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lSemester.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lSks.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lKelas.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lKodeDosen.setTypeface(TassUtilities.getFontFace(this, 0));

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //get object from intent
        NilaiMentah mentah = getIntent().getParcelableExtra("currentDetailNilai");

        //set value
        this.tvJudulNilai.setText(mentah.mataKuliah);
        this.tvSemester.setText(": " + mentah.semester);
        this.tvSks.setText(": " + mentah.sks);
        this.tvKelas.setText(": " + mentah.kelas);
        this.tvKodeDosen.setText(": " + mentah.kodeDosen);


        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.detail_nilai));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
