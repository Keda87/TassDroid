package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.SessionManager;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Adiyat Mubarak
 * Date: 3/9/14
 * Time: 2:55 PM
 */
public class MainMenuActivity extends Activity {

    private TextView nama, nim, km;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);

        //instance
        session = new SessionManager(getApplicationContext());

        //instance widget
        nama = (TextView) findViewById(R.id.tvNama);
        nim = (TextView) findViewById(R.id.tvNim);
        km = (TextView) findViewById(R.id.tvKM);

        session.loginCheck();
    }

    @Override
    protected void onStart() {
        super.onStart();

        HashMap<String, String> user = session.getUserDetails();
        nama.setText(user.get(SessionManager.KEY_USERNAME));
        nim.setText(user.get(SessionManager.KEY_PASSWORD));
        km.setText(user.get(SessionManager.KEY_KM));
    }

    public void doLogout(View v) {
        session.destroySession();
    }
}
