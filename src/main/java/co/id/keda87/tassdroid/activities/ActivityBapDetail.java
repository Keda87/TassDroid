package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
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
    private Gson gson;
    private HashMap<String, String> user;
    private SessionManager session;
    private BapDetailTask bapDetailTask;

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
        this.bapDetailTask = new BapDetailTask();

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
            this.bapDetailTask.execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD),
                    getIntent().getStringExtra("MK")
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
        }
        return super.onOptionsItemSelected(item);
    }

    private class BapDetailTask extends AsyncTask<String, Void, BapDetail[]> {

        @Override
        protected BapDetail[] doInBackground(String... params) {
            String detailBapApi = TassUtilities.uriBuilder(params[0], params[1], "dftap", params[2]);
            Log.d("BAP DETAIL API", detailBapApi);

            try {
                detailBap = gson.fromJson(TassUtilities.doGetJson(detailBapApi), BapDetail[].class);
            } catch (JsonSyntaxException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                } else {
                    kosong.setVisibility(View.VISIBLE);
                    lvDetailBap.setVisibility(View.GONE);
                }
            } else {
                kosong.setVisibility(View.VISIBLE);
                lvDetailBap.setVisibility(View.GONE);
            }
        }
    }
}