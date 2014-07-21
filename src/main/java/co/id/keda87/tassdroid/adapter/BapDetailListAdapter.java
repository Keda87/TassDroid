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
import co.id.keda87.tassdroid.pojos.BapDetail;

import java.util.Locale;

/**
 * Created by Keda87 on 7/20/2014.
 */
public class BapDetailListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private BapDetail[] bapDetails;

    public BapDetailListAdapter(Context ctx, BapDetail[] detail) {
        this.inflater = LayoutInflater.from(ctx);
        this.bapDetails = detail;
    }

    @Override
    public int getCount() {
        return this.bapDetails.length;
    }

    @Override
    public Object getItem(int position) {
        return this.bapDetails[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BapDetailHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_bap_detail, null);
            holder = new BapDetailHolder(convertView);

            //set typeface
            holder.labelPertemuan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.labelDosen.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.labelHariTanggal.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.labelWaktu.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.dosen.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.pertemuan.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.hariTanggal.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.waktu.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.status.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 1));
            convertView.setTag(holder);
        } else {
            holder = (BapDetailHolder) convertView.getTag();
        }

        //set value
        holder.pertemuan.setText(": " + bapDetails[position].pertemuan);
        holder.dosen.setText(": " + bapDetails[position].dosen);

        //detect current locale on device and format into current language
        String hari = Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English") ? TassUtilities.toDayID(bapDetails[position].hari, "en") : bapDetails[position].hari;

        holder.hariTanggal.setText(": " + hari + ", " + bapDetails[position].tanggal);
        holder.waktu.setText(": " + bapDetails[position].waktuMulai.substring(0, 5) + " - " + bapDetails[position].waktuSelesai.substring(0, 5));
        holder.status.setText(bapDetails[position].statusApproveMk.substring(0, 5));

        //set status background color depend on value
        if (bapDetails[position].statusApproveMk.equalsIgnoreCase("Sudah Approve Ketua Kelas")) {
            holder.status.setBackgroundResource(R.drawable.custom_label_sudah);
        } else if (bapDetails[position].statusApproveMk.equalsIgnoreCase("Belum Approve Ketua Kelas")) {
            holder.status.setBackgroundResource(R.drawable.custom_label_belum);
        }

        return convertView;
    }

    static class BapDetailHolder {
        @InjectView(R.id.apBapLabelPertemuan)
        TextView labelPertemuan;

        @InjectView(R.id.apBapPertemuan)
        TextView pertemuan;

        @InjectView(R.id.apBapLabelDosen)
        TextView labelDosen;

        @InjectView(R.id.apBapLabelHariTanggal)
        TextView labelHariTanggal;

        @InjectView(R.id.apBapLabelWaktu)
        TextView labelWaktu;

        @InjectView(R.id.apBapDosen)
        TextView dosen;

        @InjectView(R.id.apBapHari)
        TextView hariTanggal;

        @InjectView(R.id.apBapWaktu)
        TextView waktu;

        @InjectView(R.id.apBapStatus)
        TextView status;

        BapDetailHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
