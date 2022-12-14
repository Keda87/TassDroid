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
import butterknife.ButterKnife;
import butterknife.InjectView;
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

    @InjectView(R.id.lvKeuangan)
    ListView lvStatusKeuangan;
    @InjectView(R.id.pbUang)
    ProgressBar pbUang;
    @InjectView(R.id.tvKeuanganKosong)
    TextView tvUangKosong;

    private Gson gson;
    private SessionManager sessionManager;
    private HashMap<String, String> userCredential;
    private KeuanganTask keuanganTask;
    private StatusKeuangan[] keuangans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keuangan, container, false);
        ButterKnife.inject(this, view);

        //instance
        this.gson = new Gson();
        this.sessionManager = new SessionManager(getActivity());
        this.userCredential = sessionManager.getUserDetails();
        this.keuangans = new StatusKeuangan[0];
        this.keuanganTask = new KeuanganTask();

        this.tvUangKosong.setVisibility(View.GONE);
        this.tvUangKosong.setTypeface(TassUtilities.getFontFace(getActivity(), 0));
        this.lvStatusKeuangan.addHeaderView(new View(getActivity()));
        this.lvStatusKeuangan.addFooterView(new View(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (TassUtilities.isConnected(getActivity())) { //check if connection available
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.keuanganTask != null) {
            if (this.keuanganTask.getStatus() == AsyncTask.Status.RUNNING) {
                this.keuanganTask.cancel(true);
            }
        }
        Log.d("FRAGMENT", "Fragment Keuangan destroyed..");
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
                    Log.d("HASIL KEUANGAN", "Data telah ditampung ke ListView");
                } else { //if listview empty show label desc & hide listview
                    tvUangKosong.setVisibility(View.VISIBLE);
                    lvStatusKeuangan.setVisibility(View.GONE);
                    Log.d("HASIL KEUANGAN", "Data kosong..");
                }
            } else {
                tvUangKosong.setVisibility(View.VISIBLE); //if an error occured, show empty label
                lvStatusKeuangan.setVisibility(View.GONE);
                Log.e("KESALAHAN", "nilaiMentahs bernilai null");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbUang.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ASYNCTASK", "AsyncTask Keuangan dibatalkan..");
        }
    }
}
