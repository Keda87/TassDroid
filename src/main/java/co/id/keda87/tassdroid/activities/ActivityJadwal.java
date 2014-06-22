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
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.JadwalListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Jadwal;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/3/14
 * Time: 8:46 PM
 */
public class ActivityJadwal extends Activity {

    private ListView lvJadwal;
    private Gson gson;
    private SessionManager sessionManager;
    private HashMap<String, String> userCredential;
    private JadwalKelasTask jadwalTask;
    private TextView tvUnload;
    private ProgressBar pbJadwal;
    private Jadwal[] jadwalKelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);
        Log.d("LOCALE DEFAULT", Locale.getDefault().getDisplayLanguage());

        //instance
        this.lvJadwal = (ListView) findViewById(R.id.lvJadwalKelas);
        this.gson = new Gson();
        this.sessionManager = new SessionManager(this);
        this.userCredential = sessionManager.getUserDetails();
        this.pbJadwal = (ProgressBar) findViewById(R.id.pbJadwal);
        this.tvUnload = (TextView) findViewById(R.id.tvUnload);
        this.jadwalKelas = new Jadwal[0];

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
    protected void onResume() {
        super.onResume();
        boolean connected = TassUtilities.isConnected(this);

        //if the array already filled and connection not available, show the listview
        if (this.jadwalKelas.length > 0 && !connected) {
            this.lvJadwal.setVisibility(View.VISIBLE);
            this.pbJadwal.setVisibility(View.GONE);
            this.tvUnload.setVisibility(View.GONE);
            Log.d("RESUME", "Gak konek dan gak kosong..");
        } else if (this.jadwalKelas.length == 0 && connected) {
            this.jadwalTask = new JadwalKelasTask();
            this.jadwalTask.execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
            Log.d("RESUME", "Konek dan kosong..");
        } else if (!connected && this.jadwalKelas.length > 0) {
            this.tvUnload.setVisibility(View.GONE);
            this.pbJadwal.setVisibility(View.GONE);
            this.lvJadwal.setVisibility(View.VISIBLE);
            Log.d("RESUME", "Gak konek dan gak kosong..");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_item_refresh:
                this.tvUnload.setVisibility(View.GONE);
                boolean konek = TassUtilities.isConnected(this);

                if (konek) {
                    this.jadwalTask = new JadwalKelasTask();
                    this.jadwalTask.execute(
                            this.userCredential.get(SessionManager.KEY_USERNAME),
                            this.userCredential.get(SessionManager.KEY_PASSWORD)
                    );
                    Log.d("REFRESH", "Konek..");

                } else if (!konek && this.jadwalKelas.length > 0) {
                    this.tvUnload.setVisibility(View.GONE);
                    this.pbJadwal.setVisibility(View.GONE);
                    this.lvJadwal.setVisibility(View.VISIBLE);
                    Log.d("REFRESH", "Gak konek dan gak kosong");
                    TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
                } else if (!konek && this.jadwalKelas.length == 0) {
                    this.tvUnload.setVisibility(View.VISIBLE);
                    this.pbJadwal.setVisibility(View.GONE);
                    this.lvJadwal.setVisibility(View.GONE);
                    Log.d("REFRESH", "Gak konek dan kosong");
                    TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
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

                if (adapterJadwal.getCount() > 0) { //check if listview not empty
                    lvJadwal.setVisibility(View.VISIBLE);
                    Log.d("HASIL JADWAL", "Data telah ditampung ke ListView");
                } else { //if listview empty show label desc & hide listview
                    tvUnload.setVisibility(View.VISIBLE);
                    lvJadwal.setVisibility(View.GONE);
                    Log.d("HASIL JADWAL", "Data kosong..");
                }

            } else {
                tvUnload.setVisibility(View.VISIBLE); //if an error occured, show empty label
                Log.e("KESALAHAN", "jadwals bernilai null");
            }
        }
    }
}