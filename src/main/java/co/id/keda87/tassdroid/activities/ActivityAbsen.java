package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.AbsensiAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Absensi;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/3/14
 * Time: 8:47 PM
 */
public class ActivityAbsen extends Activity {

    @InjectView(R.id.lvAbsensi)
    ListView lvAbsen;

    @InjectView(R.id.tvAbsenKosong)
    TextView tvKosong;

    @InjectView(R.id.pbAbsensi)
    ProgressBar pbAbsen;

    private Absensi[] absensi;
    private Gson gson;
    private SessionManager session;
    private HashMap<String, String> user;
    private AbsenTask absenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);
        ButterKnife.inject(this);

        //instance
        this.gson = new Gson();
        this.session = new SessionManager(getApplicationContext());
        this.absenTask = new AbsenTask();
        this.user = this.session.getUserDetails();

        this.tvKosong.setVisibility(View.GONE);
        this.tvKosong.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 0));

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.mnAbsen));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TassUtilities.isConnected(getApplicationContext())) {
            this.absenTask = new AbsenTask();
            this.absenTask.execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            tvKosong.setVisibility(View.VISIBLE);
            pbAbsen.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getApplicationContext(), R.string.login_page_alert_no_connection, 0);
        }
    }

    @OnItemClick(R.id.lvAbsensi)
    void onAbsenItemSelected(int position) {
        Toast.makeText(this, absensi[position].kodeMk, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TassUtilities.isConnected(getApplicationContext())) {
            this.absenTask = new AbsenTask();
            this.absenTask.execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD)
            );
            Log.d("RESUME", "konek..");
        } else {
            lvAbsen.setVisibility(View.GONE);
            pbAbsen.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getApplicationContext(), R.string.login_page_alert_no_connection, 0);
            Log.d("RESUME", "gak konek..");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_item_refresh:
                if (TassUtilities.isConnected(getApplicationContext())) {
                    this.absenTask = new AbsenTask();
                    this.absenTask.execute(
                            this.user.get(SessionManager.KEY_USERNAME),
                            this.user.get(SessionManager.KEY_PASSWORD)
                    );
                    Log.d("RESUME", "konek..");
                } else {
                    lvAbsen.setVisibility(View.GONE);
                    pbAbsen.setVisibility(View.GONE);
                    tvKosong.setVisibility(View.VISIBLE);
                    TassUtilities.showToastMessage(getApplicationContext(), R.string.login_page_alert_no_connection, 0);
                    Log.d("RESUME", "gak konek..");
                }
                return true;
            case R.id.app_item_chart:
                startActivity(new Intent(this, ActivityGraphAbsen.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh_chart, menu);
        return true;
    }

    private class AbsenTask extends AsyncTask<String, Void, Absensi[]> {

        @Override
        protected Absensi[] doInBackground(String... params) {
            String absenApi = TassUtilities.uriBuilder(params[0], params[1], "absensi");
            try {
                absensi = gson.fromJson(TassUtilities.doGetJson(absenApi), Absensi[].class);
            } catch (JsonSyntaxException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvKosong.setVisibility(View.VISIBLE);
                    }
                });
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return absensi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbAbsen.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Absensi[] absensis) {
            super.onPostExecute(absensis);

            pbAbsen.setVisibility(View.GONE);
            if (absensis != null) {
                AbsensiAdapter adapterAbsen = new AbsensiAdapter(absensis, getApplicationContext());
                lvAbsen.setAdapter(adapterAbsen);

                if (adapterAbsen.getCount() > 0) {
                    lvAbsen.setVisibility(View.VISIBLE);
                    tvKosong.setVisibility(View.GONE);
                    Log.d("HASIL ABSENSI", "Data absensi sudah di tampung ke listview");
                } else {
                    tvKosong.setVisibility(View.VISIBLE);
                    lvAbsen.setVisibility(View.GONE);
                    Log.d("HASIL ABSENSI", "Data absensi kosong");
                }
            } else {
                tvKosong.setVisibility(View.VISIBLE);
                lvAbsen.setVisibility(View.GONE);
                Log.e("KESALAHAN", "absensis bernilai null");
            }
        }
    }
}
