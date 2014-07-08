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
import co.id.keda87.tassdroid.pojos.Absensi;

/**
 * Created by Keda87 on 7/7/2014.
 */
public class AbsensiAdapter extends BaseAdapter {

    private Absensi[] absens;
    private LayoutInflater inflater;

    public AbsensiAdapter(Absensi[] absens, Context context) {
        this.absens = absens;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.absens.length;
    }

    @Override
    public Object getItem(int position) {
        return this.absens[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbsensiViewHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_absensi, null);
            holder = new AbsensiViewHolder(convertView);

            //set typeface
            holder.namaMatkulKode.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.semesterTahun.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.labelPersentage.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.persentage.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            convertView.setTag(holder);
        } else {
            holder = (AbsensiViewHolder) convertView.getTag();
        }

        //applying striped listview
        int colorPosition = position % TassUtilities.colorsStripped.length;
        convertView.setBackgroundColor(TassUtilities.colorsStripped[colorPosition]);

        //set value
        holder.namaMatkulKode.setText(this.absens[position].namaMk + " - " + this.absens[position].kodeMk);
        holder.semesterTahun.setText("Semester " + this.absens[position].semester + " / " + this.absens[position].tahunAjaran);
        holder.persentage.setText(this.absens[position].prosenHadir + " %");
        return convertView;
    }

    static class AbsensiViewHolder {
        @InjectView(R.id.tvAbsenMatkulKodeMk) TextView namaMatkulKode;
        @InjectView(R.id.tvAbsenSemesterTahun) TextView semesterTahun;
        @InjectView(R.id.tvAbsenLabelPersen) TextView labelPersentage;
        @InjectView(R.id.tvAbsenPersentase) TextView persentage;

        AbsensiViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
