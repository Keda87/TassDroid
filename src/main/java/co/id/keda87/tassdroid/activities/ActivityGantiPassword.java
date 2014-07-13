package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;

/**
 * Created by Keda87 on 6/30/2014.
 */
public class ActivityGantiPassword extends Activity {

    @InjectView(R.id.pwdLabel) TextView label;
    @InjectView(R.id.pwdBaru) EditText newPass;
    @InjectView(R.id.pwdUlang) EditText rePass;
    @InjectView(R.id.btPwdGanti) Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        ButterKnife.inject(this);

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
}
