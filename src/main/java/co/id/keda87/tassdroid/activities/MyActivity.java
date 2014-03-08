package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Login;
import com.google.gson.Gson;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private TextView lbLogin;
    private EditText inpUser, inpPass;
    private Button btLogin;
    private Gson gson;
    private TassUtilities utility;

    /**
     * Fungsi untuk menampilkan Toast, dibuat fungsi ini karena
     * untuk menghindari pengulangan dalam pembuatan Toast.
     *
     * @param message : Isi pesan yang akan ditampilkan dari values
     * @return toast
     */
    private Toast getToastMessage(int message) {
        return Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //instance gson
        gson = new Gson();
        utility = new TassUtilities();

        //instance widget
        lbLogin = (TextView) findViewById(R.id.lb_login);
        inpUser = (EditText) findViewById(R.id.et_user);
        inpPass = (EditText) findViewById(R.id.et_pass);
        btLogin = (Button) findViewById(R.id.bt_login);

        //instance font
        Typeface tf = Typeface.createFromAsset(getAssets(), TassUtilities.FONT_PATH);

        //set font on widgets
        lbLogin.setTypeface(tf);
        inpUser.setTypeface(tf);
        inpPass.setTypeface(tf);
        btLogin.setTypeface(tf);

    }

    public void doLogin(View view) {
        DoLoginTask login = new DoLoginTask();

        //check if empty
        if (inpUser.getText().toString().trim().length() <= 0 || inpPass.getText().toString().trim().length() <= 0)
            return;

        if (utility.isConnected(getApplicationContext())) {
            //login action
            login.execute(inpUser.getText().toString().trim(), inpPass.getText().toString().trim());

            inpUser.setText("");
            inpPass.setText("");
        } else {
            getToastMessage(R.string.login_page_alert_no_connection).show();
        }

    }

    private class DoLoginTask extends AsyncTask<String, Integer, Login> {
        Login login = null;

        @Override
        protected Login doInBackground(String... params) {
            //get login API
            String urlLoginApi = TassUtilities.uriBuilder(params[0].toString(), params[1].toString(), "login");
            Log.d("URL API LOGIN", urlLoginApi + " user: " + params[0] + ":" + params[1]);

            //parse json to object
            login = gson.fromJson(TassUtilities.getPlainJSON(urlLoginApi), Login.class);

            return login;
        }

        @Override
        protected void onPostExecute(Login login) {
            super.onPostExecute(login);

            if (login.status.equals("BERHASIL")) {
                getToastMessage(R.string.login_page_alert_valid).show();
                Log.d("HASIL LOGIN", "-\n" +
                        "STATUS: " + login.status + "\n" +
                        "KETUA KELAS: " + login.ketuaKelas);
            } else {
                getToastMessage(R.string.login_page_alert_invalid).show();
                Log.d("HASIL LOGIN", "INVALID LOGIN");
            }
        }

    }
}
