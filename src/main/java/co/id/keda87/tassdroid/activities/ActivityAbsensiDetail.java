package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import co.id.keda87.tassdroid.R;
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

    private Gson gson;
    private HashMap<String, String> user;
    private SessionManager session;
    private String apiUrl;
    private AbsensiDetail[] details;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_detail);

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
        Log.d("DETAIL ABSEN API", this.apiUrl);
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

                    }
                });
            }
            return details;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(AbsensiDetail[] absensiDetails) {
            super.onPostExecute(absensiDetails);
        }
    }
}