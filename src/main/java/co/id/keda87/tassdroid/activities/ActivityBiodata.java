package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Biodata;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/3/14
 * Time: 7:36 PM
 */
public class ActivityBiodata extends Activity {

    @InjectView(R.id.bioNama)
    TextView bioNama;
    @InjectView(R.id.bioNim)
    TextView bioNim;
    @InjectView(R.id.bioTempatLahir)
    TextView bioTempatLahir;
    @InjectView(R.id.bioTanggalLahir)
    TextView bioTanggalLahir;
    @InjectView(R.id.bioJenisKelamin)
    TextView bioJenisKelamin;
    @InjectView(R.id.bioNoTelp)
    TextView bioPhone;
    @InjectView(R.id.bioNoTelpOrtu)
    TextView bioOrtuPhone;
    @InjectView(R.id.bioEmail)
    TextView bioEmail;
    @InjectView(R.id.bioAlamat)
    TextView bioAlamat;
    @InjectView(R.id.bioSemester)
    TextView bioSemester;
    @InjectView(R.id.bioPrimary)
    TextView bioPrimer;
    @InjectView(R.id.bioSekunder)
    TextView bioSekunder;
    @InjectView(R.id.bioDosenWali)
    TextView bioDosenWali;
    @InjectView(R.id.bioIpk)
    TextView bioIpk;

    private BiodataTask bioTask;
    private Gson gson;
    private SessionManager session;
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);
        ButterKnife.inject(this);


        this.bioTask = new BiodataTask();
        this.gson = new Gson();
        this.session = new SessionManager(this);
        this.user = this.session.getUserDetails();

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.mnBio));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TassUtilities.isConnected(this)) {
            this.bioTask = new BiodataTask();
            this.bioTask.execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD)
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
        }
        return super.onOptionsItemSelected(item);
    }

    private class BiodataTask extends AsyncTask<String, Void, Biodata> {

        @Override
        protected Biodata doInBackground(String... params) {
            String bioApi = TassUtilities.uriBuilder(params[0], params[1], "biodata");
            Biodata bio = null;
            try {
                bio = gson.fromJson(TassUtilities.doGetJson(bioApi).replaceFirst("\\[", "").replaceFirst("\\]", ""), Biodata.class);
            } catch (JsonSyntaxException e) {
                Log.e("KESALAHAN", e.getMessage());
            }

            Log.d("JSON", TassUtilities.doGetJson(bioApi).replaceFirst("\\[", "").replaceFirst("\\]", ""));
            return bio;
        }

        @Override
        protected void onPostExecute(Biodata biodata) {
            super.onPostExecute(biodata);
            Log.d("BIODATA", biodata.nama);
            Log.d("BIODATA", biodata.alamat);
        }
    }
}
