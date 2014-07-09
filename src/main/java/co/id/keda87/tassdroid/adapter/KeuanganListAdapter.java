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
import co.id.keda87.tassdroid.pojos.StatusKeuangan;

/**
 * Created by Keda87 on 5/20/2014.
 */
public class KeuanganListAdapter extends BaseAdapter {

    private StatusKeuangan[] keuangans;
    private LayoutInflater inflater;
    private Context context;

    public KeuanganListAdapter(StatusKeuangan[] keuangans, Context context) {
        this.keuangans = keuangans;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return keuangans.length;
    }

    @Override
    public Object getItem(int position) {
        return keuangans[position];
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
            holder = new ViewHolder(convertView);

            //set typeface via holder
            holder.tvKetNominal.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvKuitansi.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvSemesterTahun.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvStatus.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 1));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvSemesterTahun.setText(this.context.getResources().getString(R.string.adapter_keuangan_semester) + " " + keuangans[position].semester + " : " + keuangans[position].tahunAjaran);
        holder.tvStatus.setText(keuangans[position].status);
        holder.tvKetNominal.setText(keuangans[position].namaTarif + " - " + TassUtilities.toRupiah(keuangans[position].jumlahBayar));
        holder.tvKuitansi.setText(this.context.getResources().getString(R.string.adapter_keuangan_kuitansi) + " : " + keuangans[position].nomorKuitansi);
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tvSemesterTahun) TextView tvSemesterTahun;
        @InjectView(R.id.tvKuitansi) TextView tvKuitansi;
        @InjectView(R.id.tvStatus) TextView tvStatus;
        @InjectView(R.id.tvKeteranganNominal) TextView tvKetNominal;

        ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
