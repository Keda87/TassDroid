package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import co.id.keda87.tassdroid.pojos.GantiPassword;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Keda87 on 6/30/2014.
 */
public class ActivityGantiPassword extends Activity implements TextWatcher {

    @InjectView(R.id.pwdLabel)
    TextView label;
    @InjectView(R.id.pwdBaru)
    EditText newPass;
    @InjectView(R.id.pwdUlang)
    EditText rePass;
    @InjectView(R.id.btPwdGanti)
    Button change;

    private SessionManager session;
    private ProgressDialog dialog;
    private HashMap<String, String> user;
    private Gson gson;
    private GantiPassword gantiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        ButterKnife.inject(this);

        //instance
        this.session = new SessionManager(this);
        this.dialog = new ProgressDialog(this);
        this.user = this.session.getUserDetails();
        this.gson = new Gson();

        this.dialog.setCancelable(false);
        this.dialog.setMessage(getResources().getString(R.string.bio_edit_update_loading));
        this.rePass.addTextChangedListener(this);
        this.change.setEnabled(false);

        //set typeface
        this.label.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 1));
        this.newPass.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 0));
        this.rePass.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 0));
        this.change.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 0));

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.mnPassword));
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

    @OnClick(R.id.btPwdGanti)
    void aksiUpdatePassword() {
        if (!this.newPass.getText().toString().trim().isEmpty() && !this.rePass.getText().toString().trim().isEmpty() &&
                this.newPass.getText().toString().trim().equals(this.rePass.getText().toString().trim())) {
            if (TassUtilities.isConnected(this)) {
                new GantiPasswordTask().execute(
                        this.user.get(SessionManager.KEY_USERNAME),
                        this.user.get(SessionManager.KEY_PASSWORD),
                        this.newPass.getText().toString().trim()
                );
            } else {
                TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
            }
        } else {
            return;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (this.rePass.getText().toString().trim().equals(this.newPass.getText().toString().trim())) {
            this.change.setEnabled(true);
            Log.d("RETYPE", "True");
        } else {
            this.change.setEnabled(false);
            Log.d("RETYPE", "False");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private class GantiPasswordTask extends AsyncTask<String, Void, GantiPassword> {

        @Override
        protected GantiPassword doInBackground(String... params) {
            String gantiPassApi = TassUtilities.uriBuilder(params[0], params[1], "ubahpwd", params[2]);
            try {
                gantiPassword = gson.fromJson(TassUtilities.doGetJson(gantiPassApi).replaceFirst("\\[", "").replaceFirst("\\]", ""), GantiPassword.class);
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        GantiPasswordTask.this.cancel(true);
                        TassUtilities.showToastMessage(ActivityGantiPassword.this, R.string.act_ganti_password_fail, 0);
                    }
                });
            }
            Log.d("URL GANTI PASSWORD", gantiPassApi);

            return gantiPassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected void onPostExecute(GantiPassword ganti) {
            super.onPostExecute(ganti);
            dialog.dismiss();
            if (ganti != null) {
                session.destroySession();
                ActivityGantiPassword.this.finish();
                TassUtilities.showToastMessage(ActivityGantiPassword.this, R.string.act_ganti_password_success, 1);
                Log.d("HASIL GANTI PASSWORD", ganti.hasil == null ? "hasilnya null" : ganti.hasil);
            } else {
                TassUtilities.showToastMessage(ActivityGantiPassword.this, R.string.act_ganti_password_fail, 0);
                Log.e("HASIL GANTI PASSWORD", ganti.hasil == null ? "hasilnya null" : ganti.hasil);
            }
        }
    }
}
