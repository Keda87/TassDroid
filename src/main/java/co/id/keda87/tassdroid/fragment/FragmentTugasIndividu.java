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

    @InjectView(R.id.lvTugasIndividu)
    ListView lvIndividu;
    @InjectView(R.id.pbTugasIndividu)
    ProgressBar pbIndividu;
    @InjectView(R.id.tvIndivKosong)
    TextView tvKosong;

    private TugasIndividu[] individus;
    private HashMap<String, String> userCredential;
    private SessionManager session;
    private Gson gson;
    private TugasIndividuTask individuTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tugas_individu, null);
        ButterKnife.inject(this, v);

        //create instance
        this.individus = new TugasIndividu[0];
        this.gson = new Gson();
        this.session = new SessionManager(v.getContext());
        this.userCredential = this.session.getUserDetails();
        this.individuTask = new TugasIndividuTask();

        this.tvKosong.setTypeface(TassUtilities.getFontFace(v.getContext(), 0));
        this.tvKosong.setVisibility(View.GONE);
        this.pbIndividu.setVisibility(View.GONE);
        this.lvIndividu.addHeaderView(new View(v.getContext()));
        this.lvIndividu.addFooterView(new View(v.getContext()));

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TassUtilities.isConnected(getActivity())) {
            this.individuTask.execute(
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.individuTask != null) {
            if (this.individuTask.getStatus() == AsyncTask.Status.RUNNING) {
                this.individuTask.cancel(true);
            }
        }
    }

    public final class TugasIndividuTask extends AsyncTask<String, Void, TugasIndividu[]> {

        @Override
        protected TugasIndividu[] doInBackground(String... params) {

            String tugasIndivAPI = TassUtilities.uriBuilder(params[0], params[1], "tgsi");
            Log.d("API", tugasIndivAPI);

            try {
                individus = gson.fromJson(TassUtilities.doGetJson(tugasIndivAPI), TugasIndividu[].class);
//                individus = gson.fromJson(TassUtilities.doGetJson(params[0]), TugasIndividu[].class);
            } catch (JsonSyntaxException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvKosong.setVisibility(View.VISIBLE);
                        }
                    });
                }
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

            if (tugasIndividus != null && getActivity() != null) {
                TugasIndividuAdapter individuAdapter = new TugasIndividuAdapter(tugasIndividus, getActivity());
                lvIndividu.setAdapter(individuAdapter);
                Log.d("HASIL TUGAS INDIVIDU", "Data telah ditampung ke ListView");
            } else {
                tvKosong.setVisibility(View.VISIBLE); //if an error occured, show empty label
                Log.e("KESALAHAN", "tugasIndividus bernilai null");
            }
        }
    }
}
