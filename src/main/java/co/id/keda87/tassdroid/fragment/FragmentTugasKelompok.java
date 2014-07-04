package co.id.keda87.tassdroid.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import co.id.keda87.tassdroid.adapter.TugasKelompokAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.TugasKelompok;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created by Keda87 on 6/20/2014.
 */
public class FragmentTugasKelompok extends Fragment {

    @InjectView(R.id.lvTugasKelompok) ListView lvkelompok;
    @InjectView(R.id.pbKelompok) ProgressBar pbKel;
    @InjectView(R.id.tvKelKosong) TextView tvKelKosong;

    private TugasKelompok[] kelompoks;
    private HashMap<String, String> user;
    private SessionManager session;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tugas_kelompok, null);
        ButterKnife.inject(this, v);

        //create instance
        this.gson = new Gson();
        this.kelompoks = new TugasKelompok[0];
        this.session = new SessionManager(v.getContext());
        this.user = session.getUserDetails();

        this.tvKelKosong.setTypeface(TassUtilities.getFontFace(v.getContext(), 0));
        this.pbKel.setVisibility(View.GONE);
        this.tvKelKosong.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TassUtilities.isConnected(getActivity())) {
            new TugasKelompokTask().execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD));
        } else {
            this.tvKelKosong.setVisibility(View.VISIBLE);
            this.pbKel.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    private class TugasKelompokTask extends AsyncTask<String, Void, TugasKelompok[]> {

        @Override
        protected TugasKelompok[] doInBackground(String... params) {
            String kelompokApi = TassUtilities.uriBuilder(params[0], params[1], "tgsk");
            Log.d("API", kelompokApi);

            try {
                kelompoks = gson.fromJson(TassUtilities.doGetJson(kelompokApi), TugasKelompok[].class);
//                kelompoks = gson.fromJson(TassUtilities.doGetJson(params[0]), TugasKelompok[].class);
            } catch (JsonSyntaxException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvKelKosong.setVisibility(View.VISIBLE);
                        }
                    });
                }
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return kelompoks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbKel.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(TugasKelompok[] tugasKelompoks) {
            super.onPostExecute(tugasKelompoks);

            pbKel.setVisibility(View.GONE);

            if (tugasKelompoks != null && getActivity() != null) {
                TugasKelompokAdapter kelompokAdapter = new TugasKelompokAdapter(tugasKelompoks, getActivity());
                lvkelompok.setAdapter(kelompokAdapter);
                Log.d("HASIL TUGAS KELOMPOK", "Data telah ditampung ke listview");
            } else {
                tvKelKosong.setVisibility(View.VISIBLE);
                Log.e("KESALAHAN", "tugasKelompoks bernilai null");
            }
        }
    }
}
