package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.StatusKeuangan;

import java.util.List;

/**
 * Created by Keda87 on 5/20/2014.
 */
public class KeuanganListAdapter extends BaseAdapter {

    private List<StatusKeuangan> keuangans;
    private LayoutInflater inflater;

    public KeuanganListAdapter(List<StatusKeuangan> keuangans, Context context) {
        this.keuangans = keuangans;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return keuangans.size();
    }

    @Override
    public Object getItem(int position) {
        return keuangans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_keuangan, null);
            holder = new ViewHolder();

            //instance widget via holder
            holder.tvKetNominal = (TextView) convertView.findViewById(R.id.tvKeteranganNominal);
            holder.tvKuitansi = (TextView) convertView.findViewById(R.id.tvKuitansi);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            holder.tvSemesterTahun = (TextView) convertView.findViewById(R.id.tvSemesterTahun);

            //set typeface via holder
            holder.tvKetNominal.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvKuitansi.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvSemesterTahun.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvStatus.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 1));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //applying striped ListView
        int[] colors = new int[]{0x30FF0000, 0x300000FF};
        int colorPosition = position % colors.length;
        convertView.setBackgroundColor(colors[colorPosition]);

        holder.tvSemesterTahun.setText("SEMESTER " + keuangans.get(position).semester + " : " + keuangans.get(position).tahunAjaran);
        holder.tvStatus.setText(keuangans.get(position).status);
        holder.tvKetNominal.setText(keuangans.get(position).namaTarif + " - " + keuangans.get(position).jumlahBayar);
        holder.tvKuitansi.setText("No Kuitansi" + " : " + keuangans.get(position).nomorKuitansi);
        return convertView;
    }

    static class ViewHolder {
        TextView tvSemesterTahun;
        TextView tvKuitansi;
        TextView tvStatus;
        TextView tvKetNominal;
    }
}
