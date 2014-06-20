package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.NilaiMentah;

/**
 * Created by Keda87 on 6/16/2014.
 */
public class NilaiListAdapter extends BaseAdapter {

    private NilaiMentah[] nilaiMentahs;
    private LayoutInflater inflater;

    public NilaiListAdapter(Context context, NilaiMentah[] nilaiMentahs) {
        this.nilaiMentahs = nilaiMentahs;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.nilaiMentahs.length;
    }

    @Override
    public Object getItem(int position) {
        return this.nilaiMentahs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_nilai, null);
            holder = new ViewHolder();

            //instance widget
            holder.indexNilai = (TextView) convertView.findViewById(R.id.tvIndexMatkul);
            holder.judul = (TextView) convertView.findViewById(R.id.tvMatkulHeader);
            holder.keterangan = (TextView) convertView.findViewById(R.id.tvMatkulKeterangan);

            //set typeface
            holder.indexNilai.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 1));
            holder.keterangan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.judul.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //applying striped listview
        int colorPosition = position % TassUtilities.colorsStripped.length;
        convertView.setBackgroundColor(TassUtilities.colorsStripped[colorPosition]);

        //set value
        holder.judul.setText(this.nilaiMentahs[position].mataKuliah);
        holder.keterangan.setText(this.nilaiMentahs[position].keterangan);
        holder.indexNilai.setText(this.nilaiMentahs[position].nilaiIndex);

        return convertView;
    }

    static class ViewHolder {
        TextView judul, keterangan, indexNilai;
    }
}
