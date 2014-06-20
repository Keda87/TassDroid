package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.NilaiListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.NilaiMentah;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/3/14
 * Time: 8:47 PM
 */
public class ActivityNilai extends Activity {

    private ListView lvNilaiMentah;
    private Gson gson;
    private SessionManager sessionManager;
    private HashMap<String, String> userCredential;
    private ProgressBar pbNilai;
    private NilaiTask nilaiTask;
    private NilaiMentah[] nilaiList;
    private TextView tvNilaiKosong;
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai);

        //instance
        this.lvNilaiMentah = (ListView) findViewById(R.id.lvNilaiMentah);
        this.gson = new Gson();
        this.sessionManager = new SessionManager(this);
        this.userCredential = sessionManager.getUserDetails();
        this.pbNilai = (ProgressBar) findViewById(R.id.pbNilai);
        this.nilaiList = new NilaiMentah[0];
        this.tvNilaiKosong = (TextView) findViewById(R.id.tvNilaiKosong);

        this.tvNilaiKosong.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tvNilaiKosong.setVisibility(View.GONE);
        this.connected = TassUtilities.isConnected(this);

        this.lvNilaiMentah.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NilaiMentah detailNilai = nilaiList[position];

                //intent
                Intent intent = new Intent(ActivityNilai.this, ActivityNilaiDetail.class);
                intent.putExtra("currentDetailNilai", detailNilai);
                startActivity(intent);

                Log.d("LISTVIEW NILAI", detailNilai.mataKuliah + " Clicked...");
            }
        });

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.mnNilaiMentah));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (this.nilaiList.length == 0) { //check if listview empty
            if (connected) { //check if connection available
                //start asynctask
                this.nilaiTask = new NilaiTask();
                this.nilaiTask.execute(
                        this.userCredential.get(SessionManager.KEY_USERNAME),
                        this.userCredential.get(SessionManager.KEY_PASSWORD)
                );
            } else {
                this.tvNilaiKosong.setVisibility(View.VISIBLE);
                this.pbNilai.setVisibility(View.GONE);
                TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if the array already filled and connection not available, show the listview
        if (this.nilaiList.length > 0 && !connected) {
            this.lvNilaiMentah.setVisibility(View.VISIBLE);
            this.pbNilai.setVisibility(View.GONE);
            this.tvNilaiKosong.setVisibility(View.GONE);
            Log.d("RESUME", "Gak konek dan gak kosong..");
        } else if (this.nilaiList.length == 0 && connected) {
            this.nilaiTask = new NilaiTask();
            this.nilaiTask.execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
            Log.d("RESUME", "Konek dan kosong..");
        } else if (!connected && this.nilaiList.length > 0) {
            this.tvNilaiKosong.setVisibility(View.GONE);
            this.pbNilai.setVisibility(View.GONE);
            this.lvNilaiMentah.setVisibility(View.VISIBLE);
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
                if (connected) {
                    this.nilaiTask = new NilaiTask();
                    this.nilaiTask.execute(
                            this.userCredential.get(SessionManager.KEY_USERNAME),
                            this.userCredential.get(SessionManager.KEY_PASSWORD)
                    );
                    Log.d("REFRESH", "Konek..");

                } else if (!connected && this.nilaiList.length > 0) {
                    this.tvNilaiKosong.setVisibility(View.GONE);
                    this.pbNilai.setVisibility(View.GONE);
                    this.lvNilaiMentah.setVisibility(View.VISIBLE);
                    Log.d("REFRESH", "Gak konek dan gak kosong");
                    TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
                } else if (!connected && this.nilaiList.length == 0) {
                    this.tvNilaiKosong.setVisibility(View.VISIBLE);
                    this.pbNilai.setVisibility(View.GONE);
                    this.lvNilaiMentah.setVisibility(View.GONE);
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

    private class NilaiTask extends AsyncTask<String, Void, NilaiMentah[]> {

        @Override
        protected NilaiMentah[] doInBackground(String... params) {
            //url nilai API
            String apiNilai = TassUtilities.uriBuilder(params[0], params[1], "nm");

            //parse json to object
            try {
                nilaiList = gson.fromJson(TassUtilities.doGetJson(apiNilai), NilaiMentah[].class);
            } catch (JsonSyntaxException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvNilaiKosong.setVisibility(View.VISIBLE);
                        TassUtilities.showToastMessage(ActivityNilai.this, R.string.error_time_request, 0);
                    }
                });
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return nilaiList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbNilai.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(NilaiMentah[] nilaiMentahs) {
            super.onPostExecute(nilaiMentahs);

            pbNilai.setVisibility(View.GONE);

            if (nilaiMentahs != null) {
                NilaiListAdapter adapterNilai = new NilaiListAdapter(ActivityNilai.this, nilaiMentahs);
                lvNilaiMentah.setAdapter(adapterNilai);

                if (adapterNilai.getCount() > 0) { //check if nilai list not empty
                    lvNilaiMentah.setVisibility(View.VISIBLE);
                    Log.d("HASIL NILAI", "Data telah ditampung ke ListView");
                } else { //if listview empty show label desc & hide listview
                    tvNilaiKosong.setVisibility(View.VISIBLE);
                    lvNilaiMentah.setVisibility(View.GONE);
                    Log.d("HASIL JADWAL", "Data kosong..");
                }
            } else {
                tvNilaiKosong.setVisibility(View.VISIBLE); //if an error occured, show empty label
                TassUtilities.showToastMessage(ActivityNilai.this, R.string.error_time_request, 0);
                Log.e("KESALAHAN", "nilaiMentahs bernilai null");
            }
        }
    }
}