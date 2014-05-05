package co.id.keda87.tassdroid.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.activities.*;
import co.id.keda87.tassdroid.helper.TassUtilities;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 4/27/14
 * Time: 7:25 PM
 */
public class FragmentHome extends Fragment implements View.OnClickListener {

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

        //set on click listener each menu buttons
        this.btBio.setOnClickListener(this);
        this.btJadwal.setOnClickListener(this);
        this.btAbsen.setOnClickListener(this);
        this.btNilai.setOnClickListener(this);
        this.btTugas.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.menuBiodata:
                intent = new Intent(v.getContext(), ActivityBiodata.class);
                Log.d("PILIH MENU", "Menu biodata clicked");
                break;
            case R.id.menuJadwal:
                intent = new Intent(v.getContext(), ActivityJadwal.class);
                Log.d("PILIH MENU", "Menu jadwal clicked");
                break;
            case R.id.menuAbsensi:
                intent = new Intent(v.getContext(), ActivityAbsen.class);
                Log.d("PILIH MENU", "Menu absensi clicked");
                break;
            case R.id.menuNilai:
                intent = new Intent(v.getContext(), ActivityNilai.class);
                Log.d("PILIH MENU", "Menu nilai clicked");
                break;
            case R.id.menuTugas:
                intent = new Intent(v.getContext(), ActivityTugas.class);
                Log.d("PILIH MENU", "Menu tugas clicked");
                break;
            default:
                Log.d("PILIH MENU", "No menu clicked");
        }
        startActivity(intent);
    }
}
