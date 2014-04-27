package co.id.keda87.tassdroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.id.keda87.tassdroid.R;
import co.id.keda87.tassdroid.pojos.SliderItem;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Keda87
 * Date: 4/27/14
 * Time: 6:45 PM
 */
public class SliderListAdapter extends BaseAdapter {

    private Context context;
    private List<SliderItem> sliderItems;

    @Override
    public int getCount() {
        return sliderItems.size();
    }

    @Override
    public Object getItem(int position) {
        return sliderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.slider_list_item, null);
        }

        //instance slider widget
        ImageView sliderIcon = (ImageView) convertView.findViewById(R.id.iconSlider);
        TextView sliderText = (TextView) convertView.findViewById(R.id.textSlider);

        //set widget value
        sliderIcon.setImageResource(sliderItems.get(position).icon);
        sliderText.setText(sliderItems.get(position).title);

        return convertView;
    }
}
