package co.id.keda87.tassdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import co.id.keda87.tassdroid.pojos.TranskripTak;

import java.util.List;

/**
 * Created by Keda87 on 5/28/2014.
 */
public class TakListAdapter extends BaseAdapter {

    private List<TranskripTak> taks;
    private LayoutInflater inflater;

    public TakListAdapter(Context ctx, List<TranskripTak> taks) {
        this.taks = taks;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.taks.size();
    }

    @Override
    public Object getItem(int position) {
        return this.taks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}
