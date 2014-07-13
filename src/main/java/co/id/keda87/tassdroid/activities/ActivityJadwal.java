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
import co.id.keda87.tassdroid.adapter.JadwalListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Jadwal;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/3/14
 * Time: 8:46 PM
 */
public class ActivityJadwal extends Activity {

    @InjectView(R.id.tvUnload)
    TextView tvUnload;
    @InjectView(R.id.pbJadwal)
    ProgressBar pbJadwal;
    @InjectView(R.id.lvJadwalKelas)
    ListView lvJadwal;
    private Jadwal[] jadwalKelas;
    private Gson gson;
    private SessionManager sessionManager;
    private HashMap<String, String> userCredential;
    private JadwalKelasTask jadwalTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);
        ButterKnife.inject(this);

        //instance
        this.gson = new Gson();
        this.sessionManager = new SessionManager(this);
        this.userCredential = sessionManager.getUserDetails();
        this.jadwalKelas = new Jadwal[0];

        this.lvJadwal.addHeaderView(new View(this));
        this.lvJadwal.addFooterView(new View(this));
        this.tvUnload.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvUnload.setVisibility(View.GONE);

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.mnJadwalKuliah));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //start asynctask
        if (TassUtilities.isConnected(this)) {
            this.jadwalTask = new JadwalKelasTask();
            this.jadwalTask.execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            this.tvUnload.setVisibility(View.VISIBLE);
            this.pbJadwal.setVisibility(View.GONE);
            TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_item_refresh:
                if (TassUtilities.isConnected(this)) { // if connected, sent request
                    this.jadwalTask = new JadwalKelasTask();
                    this.jadwalTask.execute(
                            this.userCredential.get(SessionManager.KEY_USERNAME),
                            this.userCredential.get(SessionManager.KEY_PASSWORD)
                    );
                    Log.d("REFRESH", "Konek..");

                } else { //if not connected
                    this.pbJadwal.setVisibility(View.GONE);
                    this.lvJadwal.setVisibility(View.GONE);
                    this.tvUnload.setVisibility(View.VISIBLE);
                    TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
                    Log.d("REFRESH", "Gak konek..");
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    private class JadwalKelasTask extends AsyncTask<String, Void, Jadwal[]> {

        @Override
        protected Jadwal[] doInBackground(String... params) {
            //url jadwal API
            String apiJadwal = TassUtilities.uriBuilder(params[0], params[1], "jadwal");

//            parse json to object gson
            try {
                jadwalKelas = gson.fromJson(TassUtilities.doGetJson(apiJadwal), Jadwal[].class);
            } catch (JsonSyntaxException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvUnload.setVisibility(View.VISIBLE);
                    }
                });
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return jadwalKelas;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbJadwal.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Jadwal[] jadwals) {
            super.onPostExecute(jadwals);

            pbJadwal.setVisibility(View.GONE);

            if (jadwals != null) {
                JadwalListAdapter adapterJadwal = new JadwalListAdapter(ActivityJadwal.this, jadwals);
                lvJadwal.setAdapter(adapterJadwal);
                if (adapterJadwal.getCount() > 0) {
                    lvJadwal.setVisibility(View.VISIBLE);
                    Log.d("JADWAL", "Data tidak kosong kok ;)");
                } else {
                    tvUnload.setVisibility(View.VISIBLE);
                    lvJadwal.setVisibility(View.GONE);
                    Log.d("JADWAL", "Data kosong.. :(");
                }
            } else {
                Log.e("KESALAHAN", "jadwals bernilai null");
            }
        }
    }
}