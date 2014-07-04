package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Jadwal;

import java.util.Locale;

/**
 * Created by Keda87 on 6/6/2014.
 */
public class JadwalListAdapter extends BaseAdapter {

    private Jadwal[] jadwals;
    private LayoutInflater inflater;

    public JadwalListAdapter(Context ctx, Jadwal[] jadwals) {
        this.jadwals = jadwals;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.jadwals.length;
    }

    @Override
    public Object getItem(int position) {
        return this.jadwals[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_jadwal_kelas, null);
            holder = new ViewHolder(convertView);

            //set typeface
            holder.kode.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.jam.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.ruang.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.dosen.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //applying striped listview
        int colorPosition = position % TassUtilities.colorsStripped.length;
        convertView.setBackgroundColor(TassUtilities.colorsStripped[colorPosition]);

        //set textview value
        holder.kode.setText(jadwals[position].mataKuliah + " - " + jadwals[position].kodeMk);

        //detect current locale on device
        String hari = Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English") ? TassUtilities.toDayID(jadwals[position].hari, "en") : TassUtilities.toDayID(jadwals[position].hari, "id");
        holder.jam.setText(hari + ", " + jadwals[position].waktuMulai.substring(0, 5) + " - " + jadwals[position].waktuSelesai.substring(0, 5));

        holder.ruang.setText(jadwals[position].kodeRuang);
        holder.dosen.setText(jadwals[position].namaDosen + " (" + jadwals[position].kodeDosen + ")");

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tvMatkulKode) TextView kode;
        @InjectView(R.id.tvHariJam) TextView jam;
        @InjectView(R.id.tvRuang) TextView ruang;
        @InjectView(R.id.tvDosen) TextView dosen;

        ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
