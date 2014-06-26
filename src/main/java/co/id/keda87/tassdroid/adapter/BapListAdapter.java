package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.helper.TassUtilities;
import co.id.keda87.tassdroid.pojos.Bap;

/**
 * Created by Keda87 on 6/26/2014.
 */
public class BapListAdapter extends BaseAdapter {

    private Bap[] baps;
    private LayoutInflater inflater;

    public BapListAdapter(Context context, Bap[] baps) {
        this.inflater = LayoutInflater.from(context);
        this.baps = baps;
    }

    @Override
    public int getCount() {
        return this.baps.length;
    }

    @Override
    public Object getItem(int position) {
        return this.baps[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewBapHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_bap, null);
            holder = new ViewBapHolder();

            //instance
            holder.namaMatkul = (TextView) convertView.findViewById(R.id.tvMatkulApprove);
            holder.detailApprove = (TextView) convertView.findViewById(R.id.tvKeteranganApprove);

            //set typeface
            holder.namaMatkul.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            holder.detailApprove.setTypeface(TassUtilities.getFontFace(convertView.getContext(), 0));
            convertView.setTag(holder);
        } else {
            holder = (ViewBapHolder) convertView.getTag();
        }

        //applying striped listview
        int colorPosition = position % TassUtilities.colorsStripped.length;
        convertView.setBackgroundColor(TassUtilities.colorsStripped[colorPosition]);

        //set value
        holder.namaMatkul.setText(baps[position].mataKuliah);
        holder.detailApprove.setText(baps[position].jumlahPertemuanApproval + " / " + baps[position].jumlahPertemuan);

        return convertView;
    }

    private static class ViewBapHolder {
        public TextView namaMatkul, detailApprove;
    }
}
