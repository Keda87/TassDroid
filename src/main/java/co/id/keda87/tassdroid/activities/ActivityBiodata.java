package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Biodata;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/3/14
 * Time: 7:36 PM
 */
public class ActivityBiodata extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);

        //enable up navigation
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //set activity title
        getActionBar().setTitle(getResources().getString(R.string.mnBio));
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
            Log.d("JSON", TassUtilities.doGetJson(""));
            return null;
        }

        @Override
        protected void onPostExecute(Biodata biodata) {
            super.onPostExecute(biodata);
        }
    }
}
