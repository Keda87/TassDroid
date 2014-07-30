package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
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
import butterknife.OnItemClick;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.activities.ActivityBapDetail;
import co.id.keda87.tassdroid.activities.ShowcaseItemActivity;
import co.id.keda87.tassdroid.adapter.BapListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Bap;
import co.id.keda87.tassdroid.pojos.BapDetail;
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

    @InjectView(R.id.lvBap)
    ListView lvBap;
    @InjectView(R.id.pbBap)
    ProgressBar pbBap;
    @InjectView(R.id.tvBapKosong)
    TextView tvBapKosong;

    private Gson gson;
    private Bap[] baps;
    private SessionManager session;
    private HashMap<String, String> user;
    private ApproveBapTask bapTask;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bap, null);
        ButterKnife.inject(this, v);

        //create instance
        this.gson = new Gson();
        this.session = new SessionManager(v.getContext());
        this.user = this.session.getUserDetails();
        this.bapTask = new ApproveBapTask();
        this.preferences = getActivity().getSharedPreferences("co.id.keda87.tassdroid", getActivity().MODE_PRIVATE);

        //launched showcase at the first time launch
        if (this.preferences.getBoolean("showcase_bap", true)) {
            Intent i = new Intent(v.getContext(), ShowcaseItemActivity.class);
            i.putExtra("pref", "bap");
            startActivity(i);
        }

        this.tvBapKosong.setTypeface(TassUtilities.getFontFace(v.getContext(), 0));
        this.tvBapKosong.setVisibility(View.GONE);
        this.pbBap.setVisibility(View.GONE);
        this.lvBap.addHeaderView(new View(v.getContext()));
        this.lvBap.addFooterView(new View(v.getContext()));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TassUtilities.isConnected(getActivity())) { //check if connection available
            //start asynctask
            this.bapTask = new ApproveBapTask();
            this.bapTask.execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            this.tvBapKosong.setVisibility(View.VISIBLE);
            this.pbBap.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.bapTask != null) {
            if (this.bapTask.getStatus() == AsyncTask.Status.RUNNING) {
                this.bapTask.cancel(true);
            }
        }
        Log.d("FRAGMENT", "Fragment BAP destroyed..");
    }

    @OnItemClick(R.id.lvBap)
    void goToDetailBap(int position) {
        Intent intent = new Intent(getActivity(), ActivityBapDetail.class);
        intent.putExtra("MK", baps[position - 1].kodeMataKuliah);
        intent.putExtra("MATA_KULIAH", baps[position - 1].mataKuliah);
        Log.d("DETAIL", baps[position - 1].mataKuliah);
        startActivity(intent);
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

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ASYNCTASK", "AsyncTask BAP dibatalkan..");
        }
    }
}
