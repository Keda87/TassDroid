package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import co.id.keda87.tassdroid.adapter.BapDetailListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.BapDetail;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created by Keda87 on 7/20/2014.
 */
public class ActivityBapDetail extends Activity {

    @InjectView(R.id.lvApBap)
    ListView lvDetailBap;

    @InjectView(R.id.apBapKosong)
    TextView kosong;

    @InjectView(R.id.apBapProgress)
    ProgressBar dialog;

    private BapDetail[] detailBap;
    private BapDetail[] approveBap;
    private Gson gson;
    private HashMap<String, String> user;
    private SessionManager session;
    private BapDetailTask bapDetailTask;
    private String kodeMk;
    private SharedPreferences preferences;
    private PositionHolder holder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bap_detail);
        ButterKnife.inject(this);

        //instance
        this.gson = new Gson();
        this.session = new SessionManager(this);
        this.user = this.session.getUserDetails();
        this.detailBap = new BapDetail[0];
        this.approveBap = new BapDetail[0];
        this.bapDetailTask = new BapDetailTask();
        this.kodeMk = getIntent().getStringExtra("MK");
        this.preferences = getSharedPreferences("co.id.keda87.tassdroid", MODE_PRIVATE);

        //launched showcase at the first time launch
        if (this.preferences.getBoolean("showcase_approve", true)) {
            Intent i = new Intent(this, ShowcaseApproveActivity.class);
            startActivity(i);
        }

        kosong.setTypeface(TassUtilities.getFontFace(this, 0));
        kosong.setVisibility(View.GONE);
        lvDetailBap.addFooterView(new View(this));
        lvDetailBap.addHeaderView(new View(this));

        getActionBar().setTitle(getIntent().getStringExtra("MATA_KULIAH"));
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TassUtilities.isConnected(this)) {
            this.bapDetailTask = new BapDetailTask();
            this.bapDetailTask.execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD),
                    kodeMk
            );
        } else {
            TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.detailBap != null) {
            if (this.bapDetailTask.getStatus() == AsyncTask.Status.RUNNING) {
                this.bapDetailTask.cancel(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_item_refresh:
                if (TassUtilities.isConnected(this)) {
                    kosong.setVisibility(View.GONE);
                    this.bapDetailTask = new BapDetailTask();
                    this.bapDetailTask.execute(
                            this.user.get(SessionManager.KEY_USERNAME),
                            this.user.get(SessionManager.KEY_PASSWORD),
                            getIntent().getStringExtra("MK")
                    );
                } else {
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

    private String showAlert(int position) {
        StringBuilder buff = new StringBuilder(getResources().getString(R.string.bap_detail_aprove_sudah))
                .append(" ").append(position)
                .append(" ").append(getResources().getString(R.string.bap_detail_aprove_sudah_end));
        return buff.toString();
    }

    @OnItemClick(R.id.lvApBap)
    void approveBap(int position) {
        holder = new PositionHolder();
        holder.position = position;

        if (detailBap[position - 1].statusApproveMk.equalsIgnoreCase("Sudah Approve Ketua Kelas")) {
            Toast.makeText(this, this.showAlert(holder.position), Toast.LENGTH_SHORT).show();
        } else {
            if (TassUtilities.isConnected(ActivityBapDetail.this)) {
                new ApproveTask().execute(
                        user.get(SessionManager.KEY_USERNAME),
                        user.get(SessionManager.KEY_PASSWORD),
                        kodeMk,
                        String.valueOf(holder.position)
                );
            } else {
                TassUtilities.showToastMessage(ActivityBapDetail.this, R.string.login_page_alert_no_connection, 0);
            }
        }
    }

    private class BapDetailTask extends AsyncTask<String, Void, BapDetail[]> {

        @Override
        protected BapDetail[] doInBackground(final String... params) {
            try {
                String detailBapApi = TassUtilities.uriBuilder(params[0], params[1], "dftap", params[2]);
                Log.d("BAP DETAIL API", detailBapApi);
                detailBap = gson.fromJson(TassUtilities.doGetJson(detailBapApi), BapDetail[].class);
                Log.d("Mode Async", "Lihat Detail BAP");
            } catch (JsonSyntaxException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TassUtilities.showToastMessage(ActivityBapDetail.this, R.string.bap_detail_gagal_approve, 0);
                        kosong.setVisibility(View.VISIBLE);
                    }
                });
            }
            return detailBap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(BapDetail[] bapDetails) {
            super.onPostExecute(bapDetails);
            dialog.setVisibility(View.GONE);

            if (bapDetails != null) {
                BapDetailListAdapter adapter = new BapDetailListAdapter(ActivityBapDetail.this, bapDetails);
                lvDetailBap.setAdapter(adapter);
                if (adapter.getCount() > 0) {
                    lvDetailBap.setVisibility(View.VISIBLE);
                    kosong.setVisibility(View.GONE);
                } else {
                    kosong.setVisibility(View.VISIBLE);
                    lvDetailBap.setVisibility(View.GONE);
                }
            } else {
                kosong.setVisibility(View.VISIBLE);
                lvDetailBap.setVisibility(View.GONE);
                Log.e("Kesalahan", "Detail BAP bernilai null");
            }
        }
    }

    private class ApproveTask extends AsyncTask<String, Void, BapDetail[]> {

        @Override
        protected BapDetail[] doInBackground(String... params) {
            try {
                String approveApi = TassUtilities.uriBuilder(params[0], params[1], "approve", params[2], params[3]);
                approveBap = gson.fromJson(TassUtilities.doGetJson(approveApi), BapDetail[].class);
                Log.d("BAP APPROVE API POSISI: " + params[3], approveApi);
            } catch (JsonSyntaxException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TassUtilities.showToastMessage(ActivityBapDetail.this, R.string.bap_detail_gagal_approve, 0);
                        return;
                    }
                });
            }
            return approveBap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(BapDetail[] bapDetails) {
            super.onPostExecute(bapDetails);
            dialog.setVisibility(View.GONE);
            if (bapDetails != null) {
                BapDetailListAdapter adapter = new BapDetailListAdapter(ActivityBapDetail.this, bapDetails);
                lvDetailBap.setAdapter(adapter);
                if (adapter.getCount() > 0) {
                    TassUtilities.showToastMessage(ActivityBapDetail.this, R.string.bap_detail_sukses_approve, 0);
                } else {
                    TassUtilities.showToastMessage(ActivityBapDetail.this, R.string.bap_detail_gagal_approve, 0);
                }
            } else {
                TassUtilities.showToastMessage(ActivityBapDetail.this, R.string.bap_detail_gagal_approve, 0);
            }
        }
    }

    private class PositionHolder {
        int position;
    }
}