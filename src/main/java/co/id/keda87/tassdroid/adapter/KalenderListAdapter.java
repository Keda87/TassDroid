package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.KalenderAkademik;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Keda87 on 7/11/2014.
 */
public class KalenderListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private KalenderAkademik[] calendars;

    public KalenderListAdapter(KalenderAkademik[] calendars, Context context) {
        this.calendars = calendars;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.calendars.length;
    }

    @Override
    public Object getItem(int position) {
        return this.calendars[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarViewHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_kalender_akademik, null);
            holder = new CalendarViewHolder(convertView);

            //set typeface
            holder.namaKegiatan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 1));
            holder.semesterLabel.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.semesterNilai.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.tahunStartEnd.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            convertView.setTag(holder);
        } else {
            holder = (CalendarViewHolder) convertView.getTag();
        }

        holder.namaKegiatan.setText(this.calendars[position].namaKegiatan);
        holder.semesterNilai.setText(": " + this.calendars[position].semester);

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-d");
        SimpleDateFormat sdf2 = new SimpleDateFormat("d MMMM yyyy", convertView.getResources().getConfiguration().locale);
        try {
            holder.tahunStartEnd.setText(sdf1.format(sdf2.parse(this.calendars[position].tanggalMulai)
                    + " - " + sdf1.format(sdf2.parse(this.calendars[position].tanggalAkhir))));
        } catch (ParseException e) {
            Log.e("PARSE", e.getMessage());
        }
        return convertView;
    }

    static class CalendarViewHolder {

        @InjectView(R.id.calKegiatan)
        TextView namaKegiatan;

        @InjectView(R.id.calTanggal)
        TextView tahunStartEnd;

        @InjectView(R.id.calSemesterLabel)
        TextView semesterLabel;

        @InjectView(R.id.calSemester)
        TextView semesterNilai;

        CalendarViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
