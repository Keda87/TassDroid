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
import co.id.keda87.tassdroid.adapter.BapListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Bap;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/5/14
 * Time: 11:13 AM
 */
public class FragmentBap extends Fragment {

    private Gson gson;
    private Bap[] baps;
    private ListView lvBap;
    private ProgressBar pbBap;
    private TextView tvBapKosong;
    private SessionManager session;
    private HashMap<String, String> user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bap, null);

        //create instance
        this.lvBap = (ListView) v.findViewById(R.id.lvBap);
        this.pbBap = (ProgressBar) v.findViewById(R.id.pbBap);
        this.tvBapKosong = (TextView) v.findViewById(R.id.tvBapKosong);
        this.gson = new Gson();
        this.session = new SessionManager(v.getContext());
        this.user = this.session.getUserDetails();

        this.tvBapKosong.setTypeface(TassUtilities.getFontFace(v.getContext(), 0));
        this.tvBapKosong.setVisibility(View.GONE);
        this.pbBap.setVisibility(View.GONE);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TassUtilities.isConnected(getActivity())) { //check if connection available
            //start asynctask
            new ApproveBapTask().execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            this.tvBapKosong.setVisibility(View.VISIBLE);
            this.pbBap.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    private class ApproveBapTask extends AsyncTask<String, Void, Bap[]> {

        @Override
        protected Bap[] doInBackground(String... params) {
            String bapApi = TassUtilities.uriBuilder(params[0], params[1], "dftap");
            Log.d("BAP API", bapApi);
            try {
                baps = gson.fromJson(TassUtilities.doGetJson(bapApi), Bap[].class);
            } catch (JsonSyntaxException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvBapKosong.setVisibility(View.VISIBLE);
                    }
                });
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return baps;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbBap.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Bap[] baps) {
            super.onPostExecute(baps);

            pbBap.setVisibility(View.GONE);

            if (baps != null) {
                BapListAdapter bapListAdapter = new BapListAdapter(getActivity(), baps);
                lvBap.setAdapter(bapListAdapter);
                lvBap.setVisibility(View.VISIBLE);
                Log.d("HASIL BAP", "Data telah ditampung ke ListView");
            } else {
                tvBapKosong.setVisibility(View.VISIBLE);
                Log.e("KESALAHAN", "baps bernilai null");
            }
        }
    }
}
