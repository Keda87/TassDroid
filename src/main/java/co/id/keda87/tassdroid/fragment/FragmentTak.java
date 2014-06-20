package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.TakListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.TranskripTak;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.*;

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
    private ProgressDialog dialog;
    private TakTask takTask = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tak, null);

        //instance
        this.gson = new Gson();
        this.sessionManager = new SessionManager(getActivity());
        this.userCredential = sessionManager.getUserDetails();

        //instance widget
        this.tvTotalTak = (TextView) view.findViewById(R.id.tvTotalPoin);
        this.lvTranskripTak = (ListView) view.findViewById(R.id.lvTak);
        this.dialog = new ProgressDialog(getActivity());

        this.tvTotalTak.setTypeface(TassUtilities.getFontFace(getActivity(), 0));
        this.dialog.setMessage(getResources().getString(R.string.dialog_loading));
        this.dialog.setCancelable(true);
        this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                takTask.cancel(true);
                dialog.dismiss();
                Log.d("TASK", "AsyncTask telah di cancel");
            }
        });
        this.lvTranskripTak.setClickable(false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //start asynctask
        if (TassUtilities.isConnected(getActivity())) {
            takTask = new TakTask();
            takTask.execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    private class TakTask extends AsyncTask<String, Void, List<TranskripTak>> {

        @Override
        protected List<TranskripTak> doInBackground(String... params) {
            //url tak
            String apiTak = TassUtilities.uriBuilder(params[0], params[1], "tak");

            //parse json to object
            List<TranskripTak> takList = null;
            try {
                TranskripTak[] tak = gson.fromJson(TassUtilities.doGetJson(apiTak), TranskripTak[].class);
                takList = Arrays.asList(tak);
            } catch (JsonSyntaxException e) {
                dialog.dismiss();
                TassUtilities.showToastMessage(getActivity(), R.string.error_time_request, 0);
                Log.e("KESALAHAN JSON", e.getMessage());
            }
            return takList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        protected void onPostExecute(List<TranskripTak> transkripTaks) {
            super.onPostExecute(transkripTaks);

            //Sorting TAK list by year
            Collections.sort(transkripTaks, new Comparator<TranskripTak>() {
                @Override
                public int compare(TranskripTak tak1, TranskripTak tak2) {
                    return tak1.tahun.compareTo(tak2.tahun);
                }
            });

            if (dialog.isShowing() || dialog != null) {
                dialog.dismiss();
            }

            if (transkripTaks != null) {
                lvTranskripTak.setAdapter(new TakListAdapter(getActivity(), transkripTaks));
                int totalTak = 0;
                for (TranskripTak tak : transkripTaks) {
                    totalTak += Integer.parseInt(tak.poin);
                }
                tvTotalTak.setText(getResources().getString(R.string.f_tak_label_total_poin) + " " + totalTak);
                Log.d("HASIL TAK", "Data telah ditampung ke listview");
            } else {
                TassUtilities.showToastMessage(getActivity(), R.string.error_time_request, 0);
                Log.e("KESALAHAN", "transkripTaks bernilai null");
            }
        }
    }
}
