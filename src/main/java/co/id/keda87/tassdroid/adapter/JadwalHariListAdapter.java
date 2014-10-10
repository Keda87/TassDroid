package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;

import java.util.*;

/**
 * Created by Keda87 on 10/8/2014.
 */
public class JadwalHariListAdapter extends BaseAdapter {

    private List<String> jadwalHari;
    private LayoutInflater inflater;

    public JadwalHariListAdapter(Context context, HashSet<String> jadwalHari) {
        this.jadwalHari = new ArrayList<>(jadwalHari);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.jadwalHari.size();
    }

    @Override
    public Object getItem(int position) {
        return this.jadwalHari.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HariHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_jadwal_hari, null);
            holder = new HariHolder(convertView);

            holder.hari.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
        } else {
            holder = (HariHolder) convertView.getTag();
        }

        try {
            String hari = Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English") ? TassUtilities.toDayID(jadwalHari.get(position), "en") : TassUtilities.toDayID(jadwalHari.get(position), "id");
            holder.hari.setText(hari);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get current date
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        // if day same as today, change the background color
        if (this.dayToInt(jadwalHari.get(position)) == today) {
            convertView.setBackgroundColor(Color.rgb(252, 196, 73));
        }

        return convertView;
    }

    private int dayToInt(String day) {
        int result = 0;
        switch (day) {
            case "SUN":
                result = 1;
                break;
            case "MON":
                result = 2;
                break;
            case "TUE":
                result = 3;
                break;
            case "WED":
                result = 4;
                break;
            case "THU":
                result = 5;
                break;
            case "FRI":
                result = 6;
                break;
            case "SAT":
                result = 7;
                break;
        }
        return result;
    }

    static class HariHolder {

        @InjectView(R.id.labelHariJadwal)
        TextView hari;

        HariHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
