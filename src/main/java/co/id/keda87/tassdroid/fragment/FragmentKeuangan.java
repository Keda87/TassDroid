package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.KeuanganListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.StatusKeuangan;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/5/14
 * Time: 11:12 AM
 */
public class FragmentKeuangan extends Fragment {

    private Gson gson;
    private SessionManager sessionManager;
    private ListView lvStatusKeuangan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //instance
        this.gson = new Gson();
        this.sessionManager = new SessionManager(getActivity());

        return inflater.inflate(R.layout.fragment_keuangan, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get user credential
        HashMap<String, String> userCredential = sessionManager.getUserDetails();

        //start asynctask
        new KeuanganTask().execute(
                userCredential.get(SessionManager.KEY_USERNAME),
                userCredential.get(SessionManager.KEY_PASSWORD)
        );
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private class KeuanganTask extends AsyncTask<String, Void, List<StatusKeuangan>> {

        @Override
        protected List<StatusKeuangan> doInBackground(String... params) {
            //url keuangan
            String urlKeuangan = TassUtilities.uriBuilder(params[0], params[1], "keuangan");
            Log.d("URL API KEUANGAN", urlKeuangan);

            //json to object status keuangan
            StatusKeuangan[] keuangans = gson.fromJson(TassUtilities.doGetJson(urlKeuangan), StatusKeuangan[].class);
            return Arrays.asList(keuangans);
        }

        @Override
        protected void onPostExecute(List<StatusKeuangan> statusKeuangans) {
            super.onPostExecute(statusKeuangans);

            if (statusKeuangans != null) {
                lvStatusKeuangan = (ListView) getActivity().findViewById(R.id.lvKeuangan);
                lvStatusKeuangan.setAdapter(new KeuanganListAdapter(statusKeuangans, getActivity().getApplicationContext()));
                Log.d("HASIL KEUANGAN", "Data ditampung di listview");
            } else {
                Toast.makeText(getActivity(), "kesalahan", Toast.LENGTH_SHORT).show();
                Log.d("HASIL KEUANGAN", "error");
            }
        }
    }
}
