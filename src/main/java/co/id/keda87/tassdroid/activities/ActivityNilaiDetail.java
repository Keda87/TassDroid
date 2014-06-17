package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.NilaiMentah;

/**
 * Created by Keda87 on 6/17/2014.
 */
public class ActivityNilaiDetail extends Activity {

    private TextView tvJudulNilai, tvSemester, tvSks, tvKelas, tvKodeDosen, tvAlpa, tvDetail, tvKj1, tvKj2, tvKj3, tvTgs, tvPrak;
    private TextView lSemester, lSks, lKelas, lKodeDosen, lAlpa, lKj1, lKj2, lKj3, lTgs, lPrak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_detail);

        //instance label widget
        this.lSemester = (TextView) findViewById(R.id.tvDLsem);
        this.lSks = (TextView) findViewById(R.id.tvDLsks);
        this.lKelas = (TextView) findViewById(R.id.tvDLkls);
        this.lKodeDosen = (TextView) findViewById(R.id.tvDLkddsn);
        this.lAlpa = (TextView) findViewById(R.id.tvDLalpa);
        this.lKj1 = (TextView) findViewById(R.id.tvDLkaj1);
        this.lKj2 = (TextView) findViewById(R.id.tvDLkaj2);
        this.lKj3 = (TextView) findViewById(R.id.tvDLkaj3);
        this.lTgs = (TextView) findViewById(R.id.tvDLtgs);
        this.lPrak = (TextView) findViewById(R.id.tvDLprak);

        //instance value widget
        this.tvDetail = (TextView) findViewById(R.id.tvDdetail);
        this.tvJudulNilai = (TextView) findViewById(R.id.tvDmatkul);
        this.tvSemester = (TextView) findViewById(R.id.tvDsem);
        this.tvSks = (TextView) findViewById(R.id.tvDsks);
        this.tvKelas = (TextView) findViewById(R.id.tvDkls);
        this.tvKodeDosen = (TextView) findViewById(R.id.tvDkddsn);
        this.tvAlpa = (TextView) findViewById(R.id.tvDalpa);
        this.tvKj1 = (TextView) findViewById(R.id.tvDkj1);
        this.tvKj2 = (TextView) findViewById(R.id.tvDkj2);
        this.tvKj3 = (TextView) findViewById(R.id.tvDkj3);
        this.tvTgs = (TextView) findViewById(R.id.tvDtgs);
        this.tvPrak = (TextView) findViewById(R.id.tvDprk);

        //set typeface
        this.tvJudulNilai.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvDetail.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvSemester.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvSks.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvKelas.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvKodeDosen.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvAlpa.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lSemester.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lSks.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lKelas.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lKodeDosen.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lAlpa.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lKj1.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lKj2.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lKj3.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lTgs.setTypeface(TassUtilities.getFontFace(this, 0));
        this.lPrak.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvDetail.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvAlpa.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvKj1.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvKj2.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvKj3.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvTgs.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvPrak.setTypeface(TassUtilities.getFontFace(this, 0));

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
        this.tvAlpa.setText(": " + mentah.jumlahAlpha);
        this.tvKj1.setText(": " + mentah.kajian1);
        this.tvKj2.setText(": " + mentah.kajian2);
        this.tvKj3.setText(": " + mentah.kajian3);
        this.tvTgs.setText(": " + mentah.tugas);
        this.tvPrak.setText(": " + mentah.praktikum);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.detail_nilai));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
