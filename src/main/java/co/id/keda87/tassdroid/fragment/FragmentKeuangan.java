package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.KeuanganListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.StatusKeuangan;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

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
    private HashMap<String, String> userCredential;
    private ProgressBar pbUang;
    private KeuanganTask keuanganTask;
    private TextView tvUangKosong;
    private boolean connected;
    private StatusKeuangan[] keuangans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keuangan, container, false);

        //instance
        this.gson = new Gson();
        this.sessionManager = new SessionManager(getActivity());
        this.userCredential = sessionManager.getUserDetails();
        this.connected = TassUtilities.isConnected(getActivity());
        this.keuangans = new StatusKeuangan[0];

        //instance widget
        this.lvStatusKeuangan = (ListView) view.findViewById(R.id.lvKeuangan);
        this.pbUang = (ProgressBar) view.findViewById(R.id.pbUang);
        this.tvUangKosong = (TextView) view.findViewById(R.id.tvKeuanganKosong);

        this.tvUangKosong.setVisibility(View.GONE);
        this.tvUangKosong.setTypeface(TassUtilities.getFontFace(getActivity(), 0));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (connected) { //check if connection available
            //start asynctask
            this.keuanganTask = new KeuanganTask();
            this.keuanganTask.execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            this.tvUangKosong.setVisibility(View.VISIBLE);
            this.pbUang.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    private class KeuanganTask extends AsyncTask<String, Void, StatusKeuangan[]> {

        @Override
        protected StatusKeuangan[] doInBackground(String... params) {
            //url keuangan
            String urlKeuangan = TassUtilities.uriBuilder(params[0], params[1], "keuangan");
            Log.d("URL API KEUANGAN", urlKeuangan);

            //json to object status keuangan
            try {
                keuangans = gson.fromJson(TassUtilities.doGetJson(urlKeuangan), StatusKeuangan[].class);
            } catch (JsonSyntaxException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvUangKosong.setVisibility(View.VISIBLE);
                        TassUtilities.showToastMessage(getActivity(), R.string.error_time_request, 0);
                    }
                });
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return keuangans;
        }

        @Override
        protected void onPostExecute(StatusKeuangan[] statusKeuangans) {
            super.onPostExecute(statusKeuangans);

            pbUang.setVisibility(View.GONE);

            if (statusKeuangans != null) {
                KeuanganListAdapter adapterNilai = new KeuanganListAdapter(statusKeuangans, getActivity());
                lvStatusKeuangan.setAdapter(adapterNilai);

                if (adapterNilai.getCount() > 0) { //check if nilai list not empty
                    lvStatusKeuangan.setVisibility(View.VISIBLE);
                    Log.d("HASIL NILAI", "Data telah ditampung ke ListView");
                } else { //if listview empty show label desc & hide listview
                    tvUangKosong.setVisibility(View.VISIBLE);
                    lvStatusKeuangan.setVisibility(View.GONE);
                    Log.d("HASIL JADWAL", "Data kosong..");
                }
            } else {
                tvUangKosong.setVisibility(View.VISIBLE); //if an error occured, show empty label
                TassUtilities.showToastMessage(getActivity(), R.string.error_time_request, 0);
                Log.e("KESALAHAN", "nilaiMentahs bernilai null");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbUang.setVisibility(View.VISIBLE);
        }
    }
}
