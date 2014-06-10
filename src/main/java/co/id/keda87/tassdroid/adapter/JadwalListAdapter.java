package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Jadwal;

import java.util.List;
import java.util.Locale;

/**
 * Created by Keda87 on 6/6/2014.
 */
public class JadwalListAdapter extends BaseAdapter {

    private List<Jadwal> jadwals;
    private LayoutInflater inflater;

    public JadwalListAdapter(Context ctx, List<Jadwal> jadwals) {
        this.jadwals = jadwals;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.jadwals.size();
    }

    @Override
    public Object getItem(int position) {
        return this.jadwals.get(position);
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
            holder = new ViewHolder();

            //instance widget
            holder.kode = (TextView) convertView.findViewById(R.id.tvKdMkKdDosen);
            holder.jam = (TextView) convertView.findViewById(R.id.tvHariJam);
            holder.ruang = (TextView) convertView.findViewById(R.id.tvRuang);

            //set typeface
            holder.kode.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.jam.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.ruang.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 1));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //applying striped listview
        int colorPosition = position % TassUtilities.colorsStripped.length;
        convertView.setBackgroundColor(TassUtilities.colorsStripped[colorPosition]);

        //set textview value
        holder.kode.setText(jadwals.get(position).kodeMk + " - " + jadwals.get(position).kodeDosen);
        String hari = Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English") ? jadwals.get(position).hari : TassUtilities.toDayID(jadwals.get(position).hari);
        Log.d("LOCALE DEFAULT", Locale.getDefault().getDisplayLanguage());
        holder.jam.setText(hari + ", " + jadwals.get(position).waktuMulai.substring(0, 5) + " - " + jadwals.get(position).waktuSelesai.substring(0, 5));
        holder.ruang.setText(jadwals.get(position).kodeRuang);

        return convertView;
    }

    static class ViewHolder {
        TextView kode, jam, ruang;
    }
}
