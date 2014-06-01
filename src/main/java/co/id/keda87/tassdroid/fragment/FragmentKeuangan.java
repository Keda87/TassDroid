package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
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
public class FragmentKeuangan extends Fragment {

    private Gson gson;
    private SessionManager sessionManager;
    private ListView lvStatusKeuangan;
    private HashMap<String, String> userCredential;
    private ProgressDialog dialog;
    private KeuanganTask keuanganTask = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keuangan, container, false);

        //instance
        this.gson = new Gson();
        this.sessionManager = new SessionManager(getActivity());
        this.userCredential = sessionManager.getUserDetails();

        //instance widget
        this.lvStatusKeuangan = (ListView) view.findViewById(R.id.lvKeuangan);
        this.dialog = new ProgressDialog(getActivity());

        this.dialog.setMessage(getResources().getString(R.string.dialog_loading));
        this.dialog.setCancelable(true);
        this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                keuanganTask.cancel(true);
                dialog.dismiss();
                Log.d("TASK", "AsyncTask telah di cancel");
            }
        });
        this.lvStatusKeuangan.setClickable(false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //start asynctask
        if (TassUtilities.isConnected(getActivity())) {
            keuanganTask = new KeuanganTask();
            keuanganTask.execute(
                    this.userCredential.get(SessionManager.KEY_USERNAME),
                    this.userCredential.get(SessionManager.KEY_PASSWORD)
            );
        } else {
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
            if (keuangans == null) {
                keuangans = gson.fromJson(TassUtilities.doGetJson(urlKeuangan), StatusKeuangan[].class);
                Log.d("PARSE", "Hasil parse ternyata masih null, parse lagi");
            }

            List<StatusKeuangan> uang = Arrays.asList(keuangans);
            if (uang == null) {
                uang = Arrays.asList(keuangans);
                Log.d("PARSE", "Hasil convert ternyata masih null, convert lagi");
            }
            return uang;
        }

        @Override
        protected void onPostExecute(List<StatusKeuangan> statusKeuangans) {
            super.onPostExecute(statusKeuangans);

            if (dialog.isShowing() || dialog != null) {
                dialog.dismiss();
            }

            try {
                if (statusKeuangans != null) {
                    lvStatusKeuangan.setAdapter(new KeuanganListAdapter(statusKeuangans, getActivity().getApplicationContext()));
                    Log.d("HASIL KEUANGAN", "Data ditampung di listview");
                } else {
                    Toast.makeText(getActivity(), R.string.error_time_request, Toast.LENGTH_SHORT).show();
                    Log.d("HASIL KEUANGAN", "statuskeuangas bernilai null");
                }
            } catch (Resources.NotFoundException e) {
                TassUtilities.showToastMessage(getActivity(), R.string.error_time_request, 0);
                Log.e("KESALAHAN", e.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }
}
