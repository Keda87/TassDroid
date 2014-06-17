package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.NilaiListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.NilaiMentah;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private ProgressDialog dialog;
    private NilaiTask nilaiTask;
    private List<NilaiMentah> nilaiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai);

        //instance
        this.lvNilaiMentah = (ListView) findViewById(R.id.lvNilaiMentah);
        this.gson = new Gson();
        this.sessionManager = new SessionManager(this);
        this.userCredential = sessionManager.getUserDetails();
        this.dialog = new ProgressDialog(this);
        this.nilaiList = new ArrayList<>();

        this.dialog.setMessage(getResources().getString(R.string.dialog_loading));
        this.dialog.setCancelable(true);
        this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                nilaiTask.cancel(true);
                dialog.dismiss();
                Log.d("TASK", "AsyncTask nilai mentah telah di cancel");
            }
        });

        this.lvNilaiMentah.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NilaiMentah detailNilai = nilaiList.get(position);

                //intent
                Intent intent = new Intent(ActivityNilai.this, ActivityNilaiDetail.class);
                intent.putExtra("currentDetailNilai", detailNilai);
                startActivity(intent);

                Log.d("LISTVIEW NILAI", "Clicked...");
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

        if (this.nilaiList.isEmpty()) { //check if listview empty
            if (TassUtilities.isConnected(this)) { //check if connection available
                //start asynctask
                this.nilaiTask = new NilaiTask();
                this.nilaiTask.execute(
                        this.userCredential.get(SessionManager.KEY_USERNAME),
                        this.userCredential.get(SessionManager.KEY_PASSWORD)
                );
            } else {
                TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
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
                if (TassUtilities.isConnected(this)) { //check if connection available
                    //start asynctask
                    this.nilaiTask = new NilaiTask();
                    this.nilaiTask.execute(
                            this.userCredential.get(SessionManager.KEY_USERNAME),
                            this.userCredential.get(SessionManager.KEY_PASSWORD)
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

    private class NilaiTask extends AsyncTask<String, Void, List<NilaiMentah>> {

        @Override
        protected List<NilaiMentah> doInBackground(String... params) {
            //url nilai API
            String apiNilai = TassUtilities.uriBuilder(params[0], params[1], "nm");

            //parse json to object
            try {
                NilaiMentah[] nilaiMentah = gson.fromJson(TassUtilities.doGetJson(apiNilai), NilaiMentah[].class);
                nilaiList = Arrays.asList(nilaiMentah);
            } catch (JsonSyntaxException e) {
                TassUtilities.showToastMessage(ActivityNilai.this, R.string.error_time_request, 0);
                Log.e("KESALAHAN JSON", e.getMessage());
            } catch (NullPointerException e) {
                TassUtilities.showToastMessage(ActivityNilai.this, R.string.error_time_request, 0);
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return nilaiList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        protected void onPostExecute(List<NilaiMentah> nilaiMentahs) {
            super.onPostExecute(nilaiMentahs);

            dialog.dismiss();

            if (nilaiMentahs != null) {
                lvNilaiMentah.setAdapter(new NilaiListAdapter(ActivityNilai.this, nilaiMentahs));
                Log.d("HASIL NILAI MENTAH", "Data telah ditampung ke ListView");
            } else {
                TassUtilities.showToastMessage(ActivityNilai.this, R.string.error_time_request, 0);
                Log.e("KESALAHAN", "nilaiMentahs bernilai null");
            }
        }
    }

}
