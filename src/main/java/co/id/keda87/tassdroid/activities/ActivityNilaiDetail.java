package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.NilaiMentah;

/**
 * Created by Keda87 on 6/17/2014.
 */
public class ActivityNilaiDetail extends Activity {

    @InjectView(R.id.tvDmatkul)
    TextView tvJudulNilai;
    @InjectView(R.id.tvDsem)
    TextView tvSemester;
    @InjectView(R.id.tvDsks)
    TextView tvSks;
    @InjectView(R.id.tvDkls)
    TextView tvKelas;
    @InjectView(R.id.tvDkddsn)
    TextView tvKodeDosen;
    @InjectView(R.id.tvDalpa)
    TextView tvAlpa;
    @InjectView(R.id.tvDdetail)
    TextView tvDetail;
    @InjectView(R.id.tvDkj1)
    TextView tvKj1;
    @InjectView(R.id.tvDkj2)
    TextView tvKj2;
    @InjectView(R.id.tvDkj3)
    TextView tvKj3;
    @InjectView(R.id.tvDtgs)
    TextView tvTgs;
    @InjectView(R.id.tvDprk)
    TextView tvPrak;

    @InjectView(R.id.tvDLsem)
    TextView lSemester;
    @InjectView(R.id.tvDLsks)
    TextView lSks;
    @InjectView(R.id.tvDLkls)
    TextView lKelas;
    @InjectView(R.id.tvDLkddsn)
    TextView lKodeDosen;
    @InjectView(R.id.tvDLalpa)
    TextView lAlpa;
    @InjectView(R.id.tvDLkaj1)
    TextView lKj1;
    @InjectView(R.id.tvDLkaj2)
    TextView lKj2;
    @InjectView(R.id.tvDLkaj3)
    TextView lKj3;
    @InjectView(R.id.tvDLtgs)
    TextView lTgs;
    @InjectView(R.id.tvDLprak)
    TextView lPrak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_detail);
        ButterKnife.inject(this);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
