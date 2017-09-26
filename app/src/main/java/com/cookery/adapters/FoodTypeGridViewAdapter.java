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
import com.cookery.models.FoodTypeMO;
import com.cookery.utils.Utility;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;


/**
 * Created by ajit on 8/1/15.
 */
// Inner Class
public class FoodTypeGridViewAdapter extends BaseAdapter {
    private final String CLASS_NAME = this.getClass().getName();
    private final Context mContext;
    private LayoutInflater inflater;
    private final int LAYOUT = R.layout.food_type_gv_item;

    private List<? extends Object> dataList;
    private View.OnClickListener clickListener;

    public FoodTypeGridViewAdapter(Context context, List<? extends Object> dataList, View.OnClickListener clickListener) {
        super();
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList = dataList;
        this.clickListener = clickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;

        if(convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(LAYOUT, null);

            mHolder.food_type_gv_item_ll =  convertView.findViewById(R.id.food_type_gv_item_ll);
            mHolder.food_type_gv_item_iv =  convertView.findViewById(R.id.food_type_gv_item_iv);
            mHolder.food_type_gv_item_tv =  convertView.findViewById(R.id.food_type_gv_item_tv);

            convertView.setTag(LAYOUT, mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag(LAYOUT);
        }

        FoodTypeMO food = (FoodTypeMO) dataList.get(position);

        mHolder.food_type_gv_item_tv.setText(food.getFOOD_TYP_NAME());

        if(food.getIMG() != null){
            Utility.loadImageFromURL(mContext, food.getIMG(), mHolder.food_type_gv_item_iv);
        }

        mHolder.food_type_gv_item_ll.setTag(food);
        mHolder.food_type_gv_item_ll.setOnClickListener(clickListener);

        setFont(mHolder.food_type_gv_item_ll);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private LinearLayout food_type_gv_item_ll;
        private ImageView food_type_gv_item_iv;
        private TextView food_type_gv_item_tv;
    }

    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    private void setFont(ViewGroup group) {
        final Typeface font = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

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
