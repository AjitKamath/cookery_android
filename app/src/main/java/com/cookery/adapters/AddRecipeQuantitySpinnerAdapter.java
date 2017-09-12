package com.cookery.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.QuantityMO;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 10/9/17.
 */

public class AddRecipeQuantitySpinnerAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private static final int LAYOUT = R.layout.quantity_spinner_item;

    private List<QuantityMO> quantities;

    public AddRecipeQuantitySpinnerAdapter(Context applicationContext, List<QuantityMO> quantities) {
        this.context = applicationContext;
        this.quantities = quantities;
        this.inflter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return quantities.size();
    }

    @Override
    public Object getItem(int i) {
        return quantities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder mHolder;

        if(convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflter.inflate(LAYOUT, null);

            mHolder.quantity_spinner_item_ll =  convertView.findViewById(R.id.quantity_spinner_item_ll);
            mHolder.quantity_spinner_item_tv =  convertView.findViewById(R.id.quantity_spinner_item_tv);

            convertView.setTag(LAYOUT, mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag(LAYOUT);
        }

        mHolder.quantity_spinner_item_tv.setText(String.valueOf(quantities.get(i).getQTY_NAME()));
        mHolder.quantity_spinner_item_ll.setTag(quantities.get(i));

        setFont(mHolder.quantity_spinner_item_ll);

        return convertView;
    }

    public class ViewHolder {
        private LinearLayout quantity_spinner_item_ll;
        private TextView quantity_spinner_item_tv;
    }

    private void setFont(ViewGroup group) {
        final Typeface font = Typeface.createFromAsset(context.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTypeface(font);
            }
            else if(v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}
