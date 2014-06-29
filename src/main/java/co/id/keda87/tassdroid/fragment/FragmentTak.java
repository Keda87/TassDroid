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
import co.id.keda87.tassdroid.adapter.TakListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.TranskripTak;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/5/14
 * Time: 11:13 AM
 */
public class FragmentTak extends Fragment {

    private Gson gson;
    private SessionManager sessionManager;
    private ListView lvTranskripTak;
    private TextView tvTotalTak;
    private HashMap<String, String> userCredential;
    private ProgressBar pbTak;
    private TakTask takTask;
    private TextView tvTakKosong;
    private TranskripTak[] takList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tak, null);

        //instance
        this.gson = new Gson();
        this.sessionManager = new SessionManager(getActivity());
        this.userCredential = sessionManager.getUserDetails();
        this.takList = new TranskripTak[0];
        this.takTask = new TakTask();

        //instance widget
        this.tvTakKosong = (TextView) view.findViewById(R.id.tvTakKosong);
        this.tvTotalTak = (TextView) view.findViewById(R.id.tvTotalPoin);
        this.lvTranskripTak = (ListView) view.findViewById(R.id.lvTak);
        this.pbTak = (ProgressBar) view.findViewById(R.id.pbTak);

        this.tvTakKosong.setVisibility(View.GONE);
        this.tvTakKosong.setTypeface(TassUtilities.getFontFace(getActivity(), 0));
        this.tvTotalTak.setTypeface(TassUtilities.getFontFace(getActivity(), 0));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (TassUtilities.isConnected(getActivity())) { //check if connection available
            //start asynctask
            this.takTask = new TakTask();
            this.takTask.execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            this.tvTakKosong.setVisibility(View.VISIBLE);
            this.pbTak.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.takList != null) {
            if (this.takTask.getStatus() == AsyncTask.Status.RUNNING) {
                this.takTask.cancel(true);
            }
        }
        Log.d("FRAGMENT", "Fragment TAK destroyed..");
    }

    private class TakTask extends AsyncTask<String, Void, TranskripTak[]> {

        @Override
        protected TranskripTak[] doInBackground(String... params) {
            //url tak
            String apiTak = TassUtilities.uriBuilder(params[0], params[1], "tak");

            //json to object status keuangan
            try {
                takList = gson.fromJson(TassUtilities.doGetJson(apiTak), TranskripTak[].class);
            } catch (JsonSyntaxException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTakKosong.setVisibility(View.VISIBLE);
                    }
                });
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return takList;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d("ASYNCTASK", "AsyncTask TAK dibatalkan..");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbTak.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(TranskripTak[] transkripTaks) {
            super.onPostExecute(transkripTaks);

            pbTak.setVisibility(View.GONE);

            if (transkripTaks != null) {
                //Sorting TAK list by year
                Arrays.sort(transkripTaks, TranskripTak.TahunComparator);

                TakListAdapter adapterTak = new TakListAdapter(getActivity(), transkripTaks);
                lvTranskripTak.setAdapter(adapterTak);

                if (adapterTak.getCount() > 0) { //check if nilai list not empty
                    lvTranskripTak.setVisibility(View.VISIBLE);
                    Log.d("HASIL TAK", "Data telah ditampung ke ListView");
                } else { //if listview empty show label desc & hide listview
                    tvTakKosong.setVisibility(View.VISIBLE);
                    lvTranskripTak.setVisibility(View.GONE);
                    Log.d("HASIL TAK", "Data kosong..");
                }

                //acumulate all tak points
                int totalTak = 0;
                for (TranskripTak tak : transkripTaks) {
                    totalTak += Integer.parseInt(tak.poin);
                }
                tvTotalTak.setText(getResources().getString(R.string.f_tak_label_total_poin) + " " + totalTak);
                Log.d("HASIL TAK", "Data telah ditampung ke listview");
            } else {
                tvTakKosong.setVisibility(View.VISIBLE); //if an error occured, show empty label
                Log.e("KESALAHAN", "transkripTaks bernilai null");
            }
        }
    }
}
