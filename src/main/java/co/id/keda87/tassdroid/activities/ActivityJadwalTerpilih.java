package co.id.keda87.tassdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.adapter.JadwalListAdapter;
import co.id.keda87.tassdroid.pojos.Jadwal;

import java.util.List;

/**
 * Created by Keda87 on 10/9/2014.
 */
public class ActivityJadwalTerpilih extends Activity {

    private ListView jadwalLv;
    private Jadwal[] dataJadwal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);

        this.jadwalLv = (ListView) findViewById(R.id.lvJadwalPilih);

        List<Jadwal> jadwalkoe = getIntent().getParcelableArrayListExtra("hariTerpilih");
        dataJadwal = jadwalkoe.toArray(new Jadwal[jadwalkoe.size()]);

        JadwalListAdapter adapter = new JadwalListAdapter(getApplicationContext(), this.dataJadwal);
        this.jadwalLv.setAdapter(adapter);
    }
}