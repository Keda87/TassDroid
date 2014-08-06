package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;

import java.util.HashMap;

/**
 * Created by Keda87 on 7/14/2014.
 */
public class ActivityBiodataEdit extends Activity {

    @InjectView(R.id.bioEditNama)
    EditText nama;
    @InjectView(R.id.bioEditAsal)
    TextView asal;
    @InjectView(R.id.bioEditTanggal)
    TextView tanggal;
    @InjectView(R.id.bioEditButton)
    Button btEditBio;

    private SessionManager session;
    private HashMap<String, String> user;
    private ProgressDialog dialog;

    @OnClick(R.id.bioEditButton)
    void aksiUpdateBiodata() {
        if (!this.nama.getText().toString().trim().isEmpty() && !this.asal.getText().toString().trim().isEmpty() &&
                !this.tanggal.getText().toString().trim().isEmpty()) {
            if (TassUtilities.isConnected(this)) {
                new EditBioTask().execute(
                        this.user.get(SessionManager.KEY_USERNAME),
                        this.user.get(SessionManager.KEY_PASSWORD),
                        this.nama.getText().toString().trim(),
                        this.asal.getText().toString().trim(),
                        this.tanggal.getText().toString().trim()
                );
            } else {
                TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
            }
        } else {
            TassUtilities.showToastMessage(this, R.string.bio_edit_required, 0);
            Log.e("KESALAHAN", "Isi yang bener bro formnya");
            return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata_edit);
        ButterKnife.inject(this);

        //instance
        this.session = new SessionManager(this);
        this.user = this.session.getUserDetails();
        this.dialog = new ProgressDialog(this);

        this.dialog.setCancelable(false);
        this.dialog.setMessage(getResources().getString(R.string.bio_edit_update_loading));
        this.nama.setTypeface(TassUtilities.getFontFace(this, 0));
        this.asal.setTypeface(TassUtilities.getFontFace(this, 0));
        this.tanggal.setTypeface(TassUtilities.getFontFace(this, 0));
        this.btEditBio.setTypeface(TassUtilities.getFontFace(this, 0));

        //populate bio edit form
        this.nama.setText(getIntent().getStringExtra("editNama"));
        this.asal.setText(getIntent().getStringExtra("editAsal"));
        this.tanggal.setText(getIntent().getStringExtra("editTanggal"));

        getActionBar().setTitle(getResources().getString(R.string.bio_edit_header));
        getActionBar().setDisplayHomeAsUpEnabled(true);
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

    private class EditBioTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            //urutan index params
            //0 user, 1 pass, 2 telp, 3 tanggal
            String bioUpdURL = TassUtilities.uriBuilder(params[0], params[1], "biodata", params[2], params[3], params[4]);
            Log.d("URL UPDATE BIO", bioUpdURL);
            try {
                TassUtilities.doGetJson(bioUpdURL);
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("KESALAHAN", "Error saat update biodata, ");
                        TassUtilities.showToastMessage(ActivityBiodataEdit.this, R.string.bio_edit_gagal, 1);
                        EditBioTask.this.cancel(true);
                        dialog.dismiss();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            TassUtilities.showToastMessage(ActivityBiodataEdit.this, R.string.bio_edit_berhasil, 1);
        }
    }
}