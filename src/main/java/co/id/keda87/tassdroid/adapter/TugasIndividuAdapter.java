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
import co.id.keda87.tassdroid.pojos.TugasIndividu;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Keda87 on 6/21/2014.
 */
public class TugasIndividuAdapter extends BaseAdapter {

    private TugasIndividu[] tugasIndividus;
    private LayoutInflater inflater;

    public TugasIndividuAdapter(TugasIndividu[] tugasIndividus, Context ctx) {
        this.tugasIndividus = tugasIndividus;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.tugasIndividus.length;
    }

    @Override
    public Object getItem(int position) {
        return this.tugasIndividus[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NilaiIndividuHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_tugas_individu, null);
            holder = new NilaiIndividuHolder();

            //instance widget
            holder.tvMataKuliah = (TextView) convertView.findViewById(R.id.tvTugasIndNama);
            holder.tvNamaTugas = (TextView) convertView.findViewById(R.id.tvTugasIndiv);
            holder.tvDeadline = (TextView) convertView.findViewById(R.id.tvDeadIndiv);
            holder.tvWaktu = (TextView) convertView.findViewById(R.id.tvTimeIndiv);
            holder.lDeadline = (TextView) convertView.findViewById(R.id.tvTgslbDeadline);
            holder.lNamaTugas = (TextView) convertView.findViewById(R.id.tvTgslbNama);
            holder.lTime = (TextView) convertView.findViewById(R.id.tvTgslbTime);

            //set typeface
            holder.tvMataKuliah.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvNamaTugas.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvDeadline.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tvWaktu.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.lDeadline.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.lNamaTugas.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.lTime.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));

            convertView.setTag(holder);
        } else {
            holder = (NilaiIndividuHolder) convertView.getTag();
        }

        //set values
        holder.tvMataKuliah.setText(tugasIndividus[position].mataKuliah);
        holder.tvNamaTugas.setText(": " + tugasIndividus[position].namaTugas);

        try {
            SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-d");
            SimpleDateFormat sdfnew = new SimpleDateFormat("d MMMM yyyy", convertView.getResources().getConfiguration().locale);
            holder.tvDeadline.setText(": " + sdfnew.format(sdfold.parse(tugasIndividus[position].deadline.substring(0, 10))));
        } catch (ParseException e) {
            Log.e("KESALAHAN", e.getMessage());
        }
        holder.tvWaktu.setText(": " + tugasIndividus[position].deadline.substring(11, 16));

        return convertView;
    }

    static class NilaiIndividuHolder {
        TextView tvMataKuliah, tvNamaTugas, tvDeadline, tvWaktu;
        TextView lTime, lNamaTugas, lDeadline;
    }
}
