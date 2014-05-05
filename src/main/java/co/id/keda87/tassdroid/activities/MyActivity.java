package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Login;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private TextView lbLogin;
    private EditText inpUser, inpPass;
    private Button btLogin;
    private Gson gson;
    private TassUtilities utility;
    private SessionManager session;

    private String username, password;

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

        //instance
        gson = new Gson();
        utility = new TassUtilities();
        session = new SessionManager(getApplicationContext());

        //instance widget
        lbLogin = (TextView) findViewById(R.id.lb_login);
        inpUser = (EditText) findViewById(R.id.et_user);
        inpPass = (EditText) findViewById(R.id.et_pass);
        btLogin = (Button) findViewById(R.id.bt_login);

        //set font on widgets
        lbLogin.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 0));
        inpUser.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 0));
        inpPass.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 0));
        btLogin.setTypeface(TassUtilities.getFontFace(getApplicationContext(), 0));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (session.isLoggedIn()) {
            Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(i);
        }
    }

    public void doLogin(View view) {
        DoLoginTask login = new DoLoginTask();

        //check if form empty
        if (inpUser.getText().toString().isEmpty() || inpPass.getText().toString().isEmpty())
            return;

        //check connection availability
        if (utility.isConnected(view.getContext())) {

            //login action
            username = inpUser.getText().toString().trim();
            password = inpPass.getText().toString().trim();
            login.execute(username, password);
        } else {
            getToastMessage(R.string.login_page_alert_no_connection).show();
        }
    }

    private class DoLoginTask extends AsyncTask<String, Integer, Login> {
        Login login = null;

        @Override
        protected Login doInBackground(String... params) {
            //get login API
            String urlLoginApi = TassUtilities.uriBuilder(params[0], params[1], "login");
            Log.d("URL API LOGIN", urlLoginApi + " user: " + params[0] + ":" + params[1]);

            //parse json to object
            try {
                login = gson.fromJson(TassUtilities.doGetJson(urlLoginApi), Login.class);
            } catch (JsonSyntaxException e) {
                Log.e("KESALAHAN", "error rentang waktu client dan server");
            }

            return login;
        }

        @Override
        protected void onPostExecute(Login login) {
            super.onPostExecute(login);

            if (login != null) {
                if (login.status.equals("BERHASIL")) {
                    getToastMessage(R.string.login_page_alert_valid).show();
                    Log.d("HASIL LOGIN", "STATUS: " + login.status + "KETUA KELAS: " + login.ketuaKelas);

                    //create session
                    session.createSession(
                            username,
                            password,
                            login.ketuaKelas
                    );
                    Log.d("USER - PASS", username + " - " + password);

                    inpUser.setText("");
                    inpPass.setText("");

                    //move to main_menu activity
                    Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(i);

                } else {
                    getToastMessage(R.string.login_page_alert_invalid).show();
                    Log.d("HASIL LOGIN", "INVALID LOGIN");
                }
            } else {
                getToastMessage(R.string.error_time_request).show();
                Log.e("HASIL LOGIN", "Server error");
            }
        }
    }
}