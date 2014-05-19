package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import co.id.keda87.tassdroid.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //instance
        gson = new Gson();
        sessionManager = new SessionManager(getActivity());

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
                for (StatusKeuangan keuangan : statusKeuangans) {
                    Log.d("HASIL KEUANGAN", keuangan.semester + " " + keuangan.jumlahBayar + " : " + keuangan.namaTarif);
                }
            } else {
                Toast.makeText(getActivity(), "kesalahan", Toast.LENGTH_SHORT).show();
                Log.d("HASIL KEUANGAN", "error");
            }
        }
    }
}
