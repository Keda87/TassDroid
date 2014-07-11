package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.AbsensiDetailAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.AbsensiDetail;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created by Keda87 on 7/8/2014.
 */
public class ActivityAbsensiDetail extends Activity {

    @InjectView(R.id.detailHeader)
    TextView header;
    @InjectView(R.id.detailAbsenKelas)
    TextView kelas;
    @InjectView(R.id.detailAbsenSemester)
    TextView semester;
    @InjectView(R.id.detailAbsenTahun)
    TextView tahun;
    @InjectView(R.id.nilaiDetailKelas)
    TextView nilaiKelas;
    @InjectView(R.id.nilaiDetailTahun)
    TextView nilaiTahun;
    @InjectView(R.id.nilaiDetailSemester)
    TextView nilaiSemester;
    @InjectView(R.id.nilaiDosen)
    TextView nilaiDosen;
    @InjectView(R.id.labelDosen)
    TextView labelDosen;
    @InjectView(R.id.pbDetailAbsen)
    ProgressBar progress;
    @InjectView(R.id.detailAbsenKosong)
    TextView kosong;
    @InjectView(R.id.lvDetailAbsen)
    ListView lvDetailAbsen;
    private Gson gson;
    private HashMap<String, String> user;
    private SessionManager session;
    private String apiUrl;
    private AbsensiDetail[] details;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_detail);
        ButterKnife.inject(this);

        //instance
        this.gson = new Gson();
        this.session = new SessionManager(getApplicationContext());
        this.user = this.session.getUserDetails();
        this.details = new AbsensiDetail[0];
        this.apiUrl = TassUtilities.uriBuilder(
                this.user.get(SessionManager.KEY_USERNAME),
                this.user.get(SessionManager.KEY_PASSWORD),
                "absensi",
                getIntent().getStringExtra("kodeMkAbsen")
        );

        this.progress.setVisibility(View.GONE);
        this.kosong.setVisibility(View.GONE);
        this.header.setTypeface(TassUtilities.getFontFace(this, 0));
        this.kelas.setTypeface(TassUtilities.getFontFace(this, 0));
        this.semester.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tahun.setTypeface(TassUtilities.getFontFace(this, 0));
        this.nilaiKelas.setTypeface(TassUtilities.getFontFace(this, 0));
        this.nilaiSemester.setTypeface(TassUtilities.getFontFace(this, 0));
        this.nilaiTahun.setTypeface(TassUtilities.getFontFace(this, 0));
        this.kosong.setTypeface(TassUtilities.getFontFace(this, 0));
        this.nilaiDosen.setTypeface(TassUtilities.getFontFace(this, 0));
        this.labelDosen.setTypeface(TassUtilities.getFontFace(this, 0));

        Log.d("DETAIL ABSEN API", this.apiUrl);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getResources().getString(R.string.detail_absen_title));
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_item_refresh:
                if (TassUtilities.isConnected(this)) {
                    new AbsensiDetailTask().execute(this.apiUrl);
                } else {
                    TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TassUtilities.isConnected(this)) {
            new AbsensiDetailTask().execute(this.apiUrl);
        } else {
            TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
        }
    }

    private class AbsensiDetailTask extends AsyncTask<String, Void, AbsensiDetail[]> {

        @Override
        protected AbsensiDetail[] doInBackground(String... params) {
            try {
                details = gson.fromJson(TassUtilities.doGetJson(params[0]), AbsensiDetail[].class);
            } catch (JsonSyntaxException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        kosong.setVisibility(View.VISIBLE);
                    }
                });
            }
            return details;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(AbsensiDetail[] absensiDetails) {
            super.onPostExecute(absensiDetails);
            progress.setVisibility(View.GONE);
            if (absensiDetails != null) {
                nilaiSemester.setText(": " + absensiDetails[0].semester);
                nilaiKelas.setText(": " + absensiDetails[0].kodeKelas);
                nilaiTahun.setText(": " + absensiDetails[0].tahunAjaran);
                nilaiDosen.setText(": " + absensiDetails[0].namaDosen);
                header.setText(absensiDetails[0].namaMataKuliah);

                AbsensiDetailAdapter adapter = new AbsensiDetailAdapter(ActivityAbsensiDetail.this, absensiDetails);
                lvDetailAbsen.setAdapter(adapter);
                kosong.setVisibility(View.GONE);
            } else {
                kosong.setVisibility(View.VISIBLE);
            }
        }
    }
}