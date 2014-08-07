package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Biodata;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    @InjectView(R.id.bioStatus)
    TextView bioStatus;
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
    @InjectView(R.id.bioFoto)
    ImageView bioFoto;

    @InjectViews({R.id.bioLstatus, R.id.bioLtempatLahir, R.id.bioLkelamin, R.id.bioLtelp, R.id.bioLortuTelp, R.id.bioLemail,
            R.id.bioLalamat, R.id.bioLsemester, R.id.bioLprimer, R.id.bioLsekunder, R.id.bioLdosenWali, R.id.bioLipk,
            R.id.bioLtanggalLahir})
    List<TextView> labelBio;

    private BiodataTask bioTask;
    private Gson gson;
    private SessionManager session;
    private HashMap<String, String> user;
    private ProgressDialog dialog;
    private Biodata biodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);
        ButterKnife.inject(this);

        //instance
        this.bioTask = new BiodataTask();
        this.gson = new Gson();
        this.session = new SessionManager(this);
        this.user = this.session.getUserDetails();
        this.dialog = new ProgressDialog(this);
        this.biodata = new Biodata();
        this.dialog.setMessage(getResources().getString(R.string.dialog_loading));
        this.dialog.setCancelable(false);

        //set typeface individual
        this.bioNama.setTypeface(TassUtilities.getFontFace(this, 1));
        this.bioNim.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioTempatLahir.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioStatus.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioJenisKelamin.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioPhone.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioOrtuPhone.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioEmail.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioAlamat.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioSemester.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioPrimer.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioSekunder.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioDosenWali.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioIpk.setTypeface(TassUtilities.getFontFace(this, 0));
        this.bioTanggalLahir.setTypeface(TassUtilities.getFontFace(this, 0));

        //set label typeface throught for loop
        for (TextView label : this.labelBio) {
            label.setTypeface(TassUtilities.getFontFace(this, 1));
        }

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
    protected void onResume() {
        super.onResume();
        if (this.biodata.alamat == null) {
            if (TassUtilities.isConnected(this)) {
                this.bioTask = new BiodataTask();
                this.bioTask.execute(
                        this.user.get(SessionManager.KEY_USERNAME),
                        this.user.get(SessionManager.KEY_PASSWORD)
                );
            } else {
                TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
            }
        } else {
            Log.d("RESUME", "Masih ada isinya");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.app_item_refreshbio:
                if (TassUtilities.isConnected(this)) {
                    this.bioTask = new BiodataTask();
                    this.bioTask.execute(
                            this.user.get(SessionManager.KEY_USERNAME),
                            this.user.get(SessionManager.KEY_PASSWORD)
                    );
                } else {
                    TassUtilities.showToastMessage(this, R.string.login_page_alert_no_connection, 0);
                }
                return true;
            case R.id.app_item_editbio:
                if (this.biodata.nama != null && biodata.tempatLahir != null) {
                    Intent intent = new Intent(this, ActivityBiodataEdit.class);
                    intent.putExtra("editNama", bioNama.getText());
                    intent.putExtra("editAsal", bioTempatLahir.getText());
                    intent.putExtra("editTanggal", bioTanggalLahir.getText());
                    startActivity(intent);
                } else {
                    TassUtilities.showToastMessage(this, R.string.error_bind_data, 0);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getLocalizedSex(String sex, String language) {
        String sexResult = null;
        if (language.equalsIgnoreCase("English")) {
            switch (sex) {
                case "L":
                    sexResult = "Male";
                    break;
                case "P":
                    sexResult = "Female";
                    break;
            }
        } else {
            switch (sex) {
                case "L":
                    sexResult = "Laki laki";
                    break;
                case "P":
                    sexResult = "Perempuan";
                    break;
            }
        }
        return sexResult;
    }

    private class BiodataTask extends AsyncTask<String, Void, Biodata> {

        @Override
        protected Biodata doInBackground(String... params) {
            String bioApi = TassUtilities.uriBuilder(params[0], params[1], "biodata");
            try {
                biodata = gson.fromJson(TassUtilities.doGetJson(bioApi).replaceFirst("\\[", "").replaceFirst("\\]", ""), Biodata.class);
                Log.d("JSON", TassUtilities.doGetJson(bioApi).replaceFirst("\\[", "").replaceFirst("\\]", ""));
            } catch (JsonSyntaxException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TassUtilities.showToastMessage(ActivityBiodata.this, R.string.label_kosong, 0);
                    }
                });
                Log.e("KESALAHAN", e.getMessage());
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TassUtilities.showToastMessage(ActivityBiodata.this, R.string.label_kosong, 0);
                    }
                });
                Log.e("KESALAHAN", e.getMessage() == null ? "Error teuing!" : e.getMessage());
            }
            return biodata;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (this.getStatus() == Status.RUNNING) {
                dialog.show();
            }
        }

        @Override
        protected void onPostExecute(Biodata biodata) {
            super.onPostExecute(biodata);

            if (dialog.isShowing() && dialog != null) {
                dialog.dismiss();
            }

            if (biodata != null) {
                bioNama.setText(biodata.nama);
                bioNim.setText(biodata.nim);
                bioTempatLahir.setText(biodata.tempatLahir);
                bioTanggalLahir.setText(biodata.tanggalLahir);
                bioStatus.setText(biodata.status);
                try {
                    bioJenisKelamin.setText(getLocalizedSex(biodata.jenisKelamin, Locale.getDefault().getDisplayLanguage()));
                } catch (Exception e) {
                    Log.e("KESALAHAN", "Sex localized error!");
                }
                bioPhone.setText(biodata.telepon);
                bioOrtuPhone.setText(biodata.teleponOrtu);
                bioEmail.setText(biodata.email);
                bioAlamat.setText(biodata.alamat);
                bioSemester.setText(biodata.semester);
                bioPrimer.setText(biodata.kelasPrimer);
                bioSekunder.setText(biodata.kelasSekunder);
                bioDosenWali.setText(biodata.dosenWali);
                bioIpk.setText(biodata.ipk);

                //load image asynchronously
                Ion.with(bioFoto)
                        .error(R.drawable.default_pic)
                        .load(biodata.fotoMahasiswa);
            } else {
                TassUtilities.showToastMessage(ActivityBiodata.this, R.string.label_kosong, 0);
            }
        }
    }
}
