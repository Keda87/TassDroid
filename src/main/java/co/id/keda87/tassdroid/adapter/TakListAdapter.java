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
import co.id.keda87.tassdroid.pojos.TranskripTak;

/**
 * Created by Keda87 on 5/28/2014.
 */
public class TakListAdapter extends BaseAdapter {

    private TranskripTak[] taks;
    private LayoutInflater inflater;

    public TakListAdapter(Context ctx, TranskripTak[] taks) {
        this.taks = taks;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.taks.length;
    }

    @Override
    public Object getItem(int position) {
        return this.taks[position];
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
            holder = new ViewHolder(convertView);

            //set typeface
            holder.teksBagian.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.teksTahun.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.teksKegiatan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 1));
            holder.teksPoin.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set value
        holder.teksPoin.setText(taks[position].poin);
        holder.teksKegiatan.setText(taks[position].namaJenisKegiatan);
        holder.teksTahun.setText(taks[position].tahun);
        holder.teksBagian.setText(taks[position].namaBagian);
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tvKegiatan) TextView teksKegiatan;
        @InjectView(R.id.tvPoin) TextView teksPoin;
        @InjectView(R.id.tvTahun) TextView teksTahun;
        @InjectView(R.id.tvBagian) TextView teksBagian;

        ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }

}
