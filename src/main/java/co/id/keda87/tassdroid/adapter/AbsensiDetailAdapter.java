package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.AbsensiDetail;

/**
 * Created by Keda87 on 7/10/2014.
 */
public class AbsensiDetailAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private AbsensiDetail[] absensiDetails;
    private Context context;

    public AbsensiDetailAdapter(Context ctx, AbsensiDetail[] absensiDetails) {
        this.context = ctx;
        this.absensiDetails = absensiDetails;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return this.absensiDetails.length;
    }

    @Override
    public Object getItem(int position) {
        return this.absensiDetails[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbsenDetailViewHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.list_absensi_detail, null);
            holder = new AbsenDetailViewHolder(convertView);

            //set typeface
            holder.labelPertemuan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.nilaiPertemuan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.nilaiStatus.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            convertView.setTag(holder);
        } else {
            holder = (AbsenDetailViewHolder) convertView.getTag();
        }

        //set value
        holder.nilaiPertemuan.setText(this.absensiDetails[position].pertemuan);
        holder.nilaiStatus.setText(this.absensiDetails[position].statusKehadiran);

        return convertView;
    }

    static class AbsenDetailViewHolder {
        @InjectView(R.id.tvLabelDetailAbsenPertemuan)
        TextView labelPertemuan;

        @InjectView(R.id.tvDetailAbsenPertemuan)
        TextView nilaiPertemuan;

        @InjectView(R.id.tvDetailAbsenStatus)
        TextView nilaiStatus;

        AbsenDetailViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
