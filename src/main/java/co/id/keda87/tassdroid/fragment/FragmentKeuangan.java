package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
public class FragmentKeuangan extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Gson gson;
    private SessionManager sessionManager;
    private ListView lvStatusKeuangan;
    private SwipeRefreshLayout swipeRefresh;
    private HashMap<String, String> userCredential;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keuangan, container, false);

        //instance
        this.gson = new Gson();
        this.sessionManager = new SessionManager(getActivity());
        this.userCredential = sessionManager.getUserDetails();

        //instance widget
        lvStatusKeuangan = (ListView) view.findViewById(R.id.lvKeuangan);
        this.swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        //set swipe listener
        this.swipeRefresh.setOnRefreshListener(this);
        this.swipeRefresh.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //start asynctask
        if (TassUtilities.isConnected(getActivity())) {
            new KeuanganTask().execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    @Override
    public void onRefresh() {
        if (TassUtilities.isConnected(getActivity())) {
            new KeuanganTask().execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            swipeRefresh.setRefreshing(false);
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
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
                lvStatusKeuangan.setAdapter(new KeuanganListAdapter(statusKeuangans, getActivity().getApplicationContext()));
                swipeRefresh.setRefreshing(false);
                Log.d("HASIL KEUANGAN", "Data ditampung di listview");
            } else {
                Toast.makeText(getActivity(), "kesalahan", Toast.LENGTH_SHORT).show();
                Log.d("HASIL KEUANGAN", "error");
            }
        }
    }
}
