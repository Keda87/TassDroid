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
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.TugasIndividuAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.TugasIndividu;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created by Keda87 on 6/20/2014.
 */
public class FragmentTugasIndividu extends Fragment {

    private ListView lvIndividu;
    private ProgressBar pbIndividu;
    private TextView tvKosong;
    private TugasIndividu[] individus;
    private HashMap<String, String> userCredential;
    private SessionManager session;
    private Gson gson;
    private boolean connected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tugas_individu, null);

        //create instance
        this.lvIndividu = (ListView) v.findViewById(R.id.lvTugasIndividu);
        this.tvKosong = (TextView) v.findViewById(R.id.tvIndivKosong);
        this.pbIndividu = (ProgressBar) v.findViewById(R.id.pbTugasIndividu);
        this.individus = new TugasIndividu[0];
        this.gson = new Gson();
        this.connected = TassUtilities.isConnected(v.getContext());
        this.session = new SessionManager(v.getContext());
        this.userCredential = this.session.getUserDetails();

        this.tvKosong.setTypeface(TassUtilities.getFontFace(v.getContext(), 0));
        this.tvKosong.setVisibility(View.GONE);
        this.pbIndividu.setVisibility(View.GONE);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (connected) {
            new TugasIndividuTask().execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD));
//            new TugasIndividuTask()
//                    .execute("http://tass.telkomuniversity.ac.id/telkomuniversity.php/api?key=f1f573f355427effd0c048e522fd2a8a&nim=6302100285&type=tgsi&a=2&hsl=9e9884a325493711ce8d119f9110e9c4");
        } else {
            this.tvKosong.setVisibility(View.VISIBLE);
            this.pbIndividu.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    public final class TugasIndividuTask extends AsyncTask<String, Void, TugasIndividu[]> {
        @Override
        protected TugasIndividu[] doInBackground(String... params) {

            String tugasIndivAPI = TassUtilities.uriBuilder(params[0], params[1], "tgsi");
            Log.d("API", "alamat API tugas individu");

            try {
                individus = gson.fromJson(TassUtilities.doGetJson(tugasIndivAPI), TugasIndividu[].class);
//                individus = gson.fromJson(TassUtilities.doGetJson(params[0]), TugasIndividu[].class);
            } catch (JsonSyntaxException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvKosong.setVisibility(View.VISIBLE);
                        TassUtilities.showToastMessage(getActivity(), R.string.error_time_request, 0);
                    }
                });
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return individus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbIndividu.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(TugasIndividu[] tugasIndividus) {
            super.onPostExecute(tugasIndividus);

            pbIndividu.setVisibility(View.GONE);

            if (tugasIndividus != null) {
                TugasIndividuAdapter individuAdapter = new TugasIndividuAdapter(tugasIndividus, getActivity());
                lvIndividu.setAdapter(individuAdapter);
                Log.d("HASIL TUGAS INDIVIDU", "Data telah ditampung ke ListView");
            } else {
                tvKosong.setVisibility(View.VISIBLE); //if an error occured, show empty label
                TassUtilities.showToastMessage(getActivity(), R.string.error_time_request, 0);
                Log.e("KESALAHAN", "tugasIndividus bernilai null");
            }
        }
    }
}
