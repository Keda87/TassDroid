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
import co.id.keda87.tassdroid.adapter.KalenderListAdapter;
import co.id.keda87.tassdroid.helper.SessionManager;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.KalenderAkademik;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 5/5/14
 * Time: 11:12 AM
 */
public class FragmentKalender extends Fragment {

    @InjectView(R.id.lvKalender)
    ListView lvKelender;

    @InjectView(R.id.calKosong)
    TextView kosong;

    @InjectView(R.id.pbCal)
    ProgressBar progres;

    private Gson gson;
    private SessionManager session;
    private HashMap<String, String> user;
    private KalenderAkademik[] kalenders;
    private KalenderTask task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kalender, null);
        ButterKnife.inject(this, view);

        //instance
        this.gson = new Gson();
        this.session = new SessionManager(view.getContext());
        this.user = this.session.getUserDetails();
        this.kalenders = new KalenderAkademik[0];
        this.task = new KalenderTask();

        this.kosong.setTypeface(TassUtilities.getFontFace(view.getContext(), 0));
        this.kosong.setVisibility(View.GONE);
        this.lvKelender.addHeaderView(new View(getActivity()));
        this.lvKelender.addFooterView(new View(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TassUtilities.isConnected(getActivity())) {
            this.task = new KalenderTask();
            this.task.execute(
                    this.user.get(SessionManager.KEY_USERNAME),
                    this.user.get(SessionManager.KEY_PASSWORD)
            );
        } else {
            this.kosong.setVisibility(View.VISIBLE);
            this.progres.setVisibility(View.GONE);
            TassUtilities.showToastMessage(getActivity(), R.string.login_page_alert_no_connection, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.kalenders != null) {
            if (this.task.getStatus() == AsyncTask.Status.RUNNING) {
                this.task.cancel(true);
            }
        }
    }

    private class KalenderTask extends AsyncTask<String, Void, KalenderAkademik[]> {

        @Override
        protected KalenderAkademik[] doInBackground(String... params) {
            String kalenderApi = TassUtilities.uriBuilder(params[0], params[1], "kalenderakademik");
            try {
                kalenders = gson.fromJson(TassUtilities.doGetJson(kalenderApi), KalenderAkademik[].class);
            } catch (JsonSyntaxException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        kosong.setVisibility(View.VISIBLE);
                    }
                });
            }
            return kalenders;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progres.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(KalenderAkademik[] kalenderAkademiks) {
            super.onPostExecute(kalenderAkademiks);

            progres.setVisibility(View.GONE);

            if (kalenderAkademiks != null) {
                //reverse array
                List<KalenderAkademik> list = Arrays.asList(kalenderAkademiks);
                Collections.reverse(list);
                KalenderAkademik[] reversed = (KalenderAkademik[]) list.toArray();

                KalenderListAdapter adapter = new KalenderListAdapter(reversed, getActivity());
                lvKelender.setAdapter(adapter);

                if (adapter.getCount() > 0) {
                    lvKelender.setVisibility(View.VISIBLE);
                    Log.d("HASIL KALENDER", "Data telah ditampung ke ListView");
                } else {
                    kosong.setVisibility(View.VISIBLE);
                    lvKelender.setVisibility(View.GONE);
                    Log.d("HASIL KALENDER", "Data kosong..");
                }
            } else {
                kosong.setVisibility(View.VISIBLE);
                lvKelender.setVisibility(View.GONE);
            }
        }
    }
}
