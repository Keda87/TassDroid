package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import co.id.keda87.tassdroid.pojos.TugasKelompok;

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
            convertView = this.inflater.inflate(0, null);
            holder = new NilaiKelompokHolder();

            //instance widget

            //set typeface

            convertView.setTag(holder);
        } else {
            holder = (NilaiKelompokHolder) convertView.getTag();
        }
        return null;
    }

    static class NilaiKelompokHolder {

    }
}
