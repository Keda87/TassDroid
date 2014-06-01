package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.TranskripTak;

import java.util.List;

/**
 * Created by Keda87 on 5/28/2014.
 */
public class TakListAdapter extends BaseAdapter {

    private List<TranskripTak> taks;
    private LayoutInflater inflater;

    public TakListAdapter(Context ctx, List<TranskripTak> taks) {
        this.taks = taks;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.taks.size();
    }

    @Override
    public Object getItem(int position) {
        return this.taks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_tak, null);
            holder = new ViewHolder();

            //instance widget
            holder.teksBagian = (TextView) convertView.findViewById(R.id.tvBagian);
            holder.teksPoin = (TextView) convertView.findViewById(R.id.tvPoin);
            holder.teksTahun = (TextView) convertView.findViewById(R.id.tvTahun);
            holder.teksKegiatan = (TextView) convertView.findViewById(R.id.tvKegiatan);

            //set typeface
            holder.teksBagian.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.teksTahun.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.teksKegiatan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 1));
            holder.teksPoin.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //applying striped listview
        int colorPosition = position % TassUtilities.colorsStripped.length;
        convertView.setBackgroundColor(TassUtilities.colorsStripped[colorPosition]);

        //set value
        holder.teksPoin.setText(taks.get(position).poin);
        holder.teksKegiatan.setText(taks.get(position).namaJenisKegiatan);
        holder.teksTahun.setText(taks.get(position).tahun);
        holder.teksBagian.setText(taks.get(position).namaBagian);
        return convertView;
    }

    static class ViewHolder {
        TextView teksKegiatan;
        TextView teksPoin;
        TextView teksTahun;
        TextView teksBagian;
    }

}
