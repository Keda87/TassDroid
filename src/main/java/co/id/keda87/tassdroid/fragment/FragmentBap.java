package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.pojos.Bap;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/5/14
 * Time: 11:13 AM
 */
public class FragmentBap extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bap, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class ApproveBapTask extends AsyncTask<String, Void, Bap[]> {

        @Override
        protected Bap[] doInBackground(String... params) {
            return new Bap[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bap[] baps) {
            super.onPostExecute(baps);
        }
    }
}
