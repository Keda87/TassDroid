package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.JadwalListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Jadwal;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private ProgressDialog dialog;
    private JadwalKelasTask jadwalTask;

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
        this.dialog = new ProgressDialog(this);

        this.dialog.setMessage(getResources().getString(R.string.dialog_loading));
        this.dialog.setCancelable(true);
        this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                jadwalTask.cancel(true);
                dialog.dismiss();
                Log.d("TASK", "AsyncTask jadwal telah di cancel");
            }
        });
        this.lvJadwal.setClickable(false);

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
                if (TassUtilities.isConnected(this)) {
                    this.jadwalTask = new JadwalKelasTask();
                    this.jadwalTask.execute(
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

    private class JadwalKelasTask extends AsyncTask<String, Void, List<Jadwal>> {

        @Override
        protected List<Jadwal> doInBackground(String... params) {
            //url jadwal API
            String apiJadwal = TassUtilities.uriBuilder(params[0], params[1], "jadwal");

            //parse json to object
            List<Jadwal> jadwalList = null;
            try {
                Jadwal[] jadwaKelas = gson.fromJson(TassUtilities.doGetJson(apiJadwal), Jadwal[].class);
                jadwalList = Arrays.asList(jadwaKelas);
            } catch (JsonSyntaxException e) {
                TassUtilities.showToastMessage(ActivityJadwal.this, R.string.error_time_request, 0);
                Log.e("KESALAHAN JSON", e.getMessage());
            } catch (NullPointerException e) {
                TassUtilities.showToastMessage(ActivityJadwal.this, R.string.error_time_request, 0);
                Log.e("KESALAHAN JSON", e.getMessage());
            }

            return jadwalList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        protected void onPostExecute(List<Jadwal> jadwals) {
            super.onPostExecute(jadwals);

            dialog.dismiss();

            if (jadwals != null) {
                lvJadwal.setAdapter(new JadwalListAdapter(ActivityJadwal.this, jadwals));
                Log.d("HASIL JADWAL", "Data telah ditampung ke ListView");
            } else {
                TassUtilities.showToastMessage(ActivityJadwal.this, R.string.error_time_request, 0);
                Log.e("KESALAHAN", "jadwals bernilai null");
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ASYNC", "async dalam keadaan cancel");
        }
    }
}
