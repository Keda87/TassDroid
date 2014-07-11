package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
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
    @InjectView(R.id.lb_login)
    TextView lbLogin;
    @InjectView(R.id.et_user)
    EditText inpUser;
    @InjectView(R.id.et_pass)
    EditText inpPass;
    @InjectView(R.id.bt_login)
    Button btLogin;

    private Gson gson;
    private SessionManager session;
    private ProgressDialog dialog;

    private String username, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);

        //instance
        gson = new Gson();
        session = new SessionManager(getApplicationContext());

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.login_page_dialog_signin));
        dialog.setCancelable(false);

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
        if (TassUtilities.isConnected(getApplicationContext())) {

            //login action
            username = inpUser.getText().toString().trim();
            password = inpPass.getText().toString().trim();
            login.execute(username, password);
        } else {
            TassUtilities.showToastMessage(getApplicationContext(), R.string.login_page_alert_no_connection, 0);
        }
    }

    private class DoLoginTask extends AsyncTask<String, Void, Login> {
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
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected void onPostExecute(Login login) {
            super.onPostExecute(login);

            if (dialog.isShowing() || dialog != null) {
                dialog.dismiss();
            }

            if (login != null) {
                if (login.status.equals("BERHASIL")) {
                    TassUtilities.showToastMessage(getApplicationContext(), R.string.login_page_alert_valid, 0);
                    Log.d("HASIL LOGIN", "STATUS: " + login.status + "KETUA KELAS: " + login.ketuaKelas);

                    //create session
                    session.createSession(
                            username,
                            password,
                            login.ketuaKelas
                    );

                    inpUser.setText("");
                    inpPass.setText("");

                    //move to main_menu activity
                    Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//closing all activities
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//add new flag to start new activity
                    startActivity(i);

                } else {
                    TassUtilities.showToastMessage(getApplicationContext(), R.string.login_page_alert_invalid, 0);
                    Log.d("HASIL LOGIN", "INVALID LOGIN");
                }
            } else {
                TassUtilities.showToastMessage(getApplicationContext(), R.string.error_time_request, 0);
                Log.e("HASIL LOGIN", "Server error");
            }
        }
    }
}