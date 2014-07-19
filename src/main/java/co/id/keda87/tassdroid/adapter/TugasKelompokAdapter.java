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
import co.id.keda87.tassdroid.pojos.TugasKelompok;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Keda87 on 6/22/2014.
 */
public class TugasKelompokAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private TugasKelompok[] kelompoks;

    public TugasKelompokAdapter(TugasKelompok[] kelompoks, Context ctx) {
        this.kelompoks = kelompoks;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.kelompoks.length;
    }

    @Override
    public Object getItem(int position) {
        return this.kelompoks[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NilaiKelompokHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_tugas_kelompok, null);
            holder = new NilaiKelompokHolder();

            //instance widget
            holder.tNamaMk = (TextView) convertView.findViewById(R.id.kelNamaMk);
            holder.tNamaTugas = (TextView) convertView.findViewById(R.id.kelNamaTugas);
            holder.tDeadline = (TextView) convertView.findViewById(R.id.kelPengumpulan);
            holder.tWaktu = (TextView) convertView.findViewById(R.id.kelWaktu);
            holder.tAnggota = (TextView) convertView.findViewById(R.id.kelAnggota);
            holder.tKeterangan = (TextView) convertView.findViewById(R.id.kelKeterangan);
            holder.lNamaTugas = (TextView) convertView.findViewById(R.id.lkNamaTugas);
            holder.lDeadline = (TextView) convertView.findViewById(R.id.lkDeadline);
            holder.lWaktu = (TextView) convertView.findViewById(R.id.lkTime);
            holder.lAnggota = (TextView) convertView.findViewById(R.id.lkMember);
            holder.lKeterangan = (TextView) convertView.findViewById(R.id.lkDescription);

            //set typeface
            holder.tNamaMk.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tNamaTugas.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tDeadline.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tWaktu.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tAnggota.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tKeterangan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.lNamaTugas.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.lDeadline.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.lWaktu.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.lAnggota.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.lKeterangan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));

            convertView.setTag(holder);
        } else {
            holder = (NilaiKelompokHolder) convertView.getTag();
        }

        //set values
        holder.tNamaMk.setText(kelompoks[position].namaMk);
        holder.tNamaTugas.setText(": " + kelompoks[position].namaTugas);

        try {
            SimpleDateFormat sdfold = new SimpleDateFormat("yyyy-MM-d");
            SimpleDateFormat sdfnew = new SimpleDateFormat("d MMMM yyyy", convertView.getResources().getConfiguration().locale);
            holder.tDeadline.setText(": " + sdfnew.format(sdfold.parse(kelompoks[position].tanggalKumpul.substring(0, 10))));
        } catch (ParseException e) {
            Log.e("KESALAHAN", e.getMessage());
        }

        holder.tWaktu.setText(": " + kelompoks[position].tanggalKumpul.substring(11, 16));
        holder.tAnggota.setText(": " + kelompoks[position].nimKelompok.replaceAll("<br>", ", "));
        holder.tKeterangan.setText(": " + kelompoks[position].keterangan);

        return convertView;
    }

    static class NilaiKelompokHolder {
        TextView tNamaMk, tNamaTugas, tDeadline, tWaktu, tAnggota, tKeterangan;
        TextView lNamaTugas, lDeadline, lWaktu, lAnggota, lKeterangan;
    }
}
