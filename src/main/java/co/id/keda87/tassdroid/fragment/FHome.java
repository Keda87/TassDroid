package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 4/27/14
 * Time: 7:25 PM
 */
public class FHome extends Fragment {

    private Button btBio, btJadwal, btAbsen, btNilai, btTugas;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //instance widget
        this.btBio = (Button) getView().findViewById(R.id.menuBiodata);
        this.btJadwal = (Button) getView().findViewById(R.id.menuJadwal);
        this.btAbsen = (Button) getView().findViewById(R.id.menuAbsensi);
        this.btNilai = (Button) getView().findViewById(R.id.menuNilai);
        this.btTugas = (Button) getView().findViewById(R.id.menuTugas);

        //set roboto font
        this.context = getView().getContext();
        this.btBio.setTypeface(TassUtilities.getFontFace(this.context, 1));
        this.btJadwal.setTypeface(TassUtilities.getFontFace(this.context, 1));
        this.btAbsen.setTypeface(TassUtilities.getFontFace(this.context, 1));
        this.btNilai.setTypeface(TassUtilities.getFontFace(this.context, 1));
        this.btTugas.setTypeface(TassUtilities.getFontFace(this.context, 1));
    }
}
