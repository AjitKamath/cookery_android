package com.cookery.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientMO;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;


/**
 * Created by ajit on 8/1/15.
 */
// Inner Class
public class IngredientsGridViewAdapter extends BaseAdapter {
    private final String CLASS_NAME = this.getClass().getName();
    private final Context mContext;
    private LayoutInflater inflater;
    private final int LAYOUT = R.layout.ingredient_quantity;

    private List<IngredientMO> dataList;

    public IngredientsGridViewAdapter(Context context, List<IngredientMO> dataList) {
        super();
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;

        if(convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(LAYOUT, null);

            mHolder.ingredient_quantity_rl =  convertView.findViewById(R.id.ingredient_quantity_rl);
            mHolder.ingredient_quantity_iv =  convertView.findViewById(R.id.ingredient_quantity_iv);
            mHolder.ingredient_quantity_cancel_iv =  convertView.findViewById(R.id.ingredient_quantity_cancel_iv);
            mHolder.ingredient_quantity_ingredient_tv =  convertView.findViewById(R.id.ingredient_quantity_ingredient_tv);
            mHolder.ingredient_quantity_quantity_tv =  convertView.findViewById(R.id.ingredient_quantity_quantity_tv);
            mHolder.ingredient_quantity_measurement_tv =  convertView.findViewById(R.id.ingredient_quantity_measurement_tv);

            convertView.setTag(LAYOUT, mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag(LAYOUT);
        }

        final IngredientMO ingredient = dataList.get(position);

        if(ingredient == null){
            return convertView;
        }

        if(ingredient.getING_ID() == 0){
            mHolder.ingredient_quantity_iv.setImageResource(R.drawable.dot);
        }
        mHolder.ingredient_quantity_ingredient_tv.setText(ingredient.getING_NAME());
        mHolder.ingredient_quantity_quantity_tv.setTag(ingredient.getQUANTITY());
        mHolder.ingredient_quantity_measurement_tv.setTag(ingredient.getMSR_NAME());

        mHolder.ingredient_quantity_cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.remove(ingredient);
                notifyDataSetChanged();
            }
        });

        setFont(mHolder.ingredient_quantity_rl);

        return convertView;
    }

    public void updateDatalist(IngredientMO ingredient){
        dataList.add(ingredient);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private RelativeLayout ingredient_quantity_rl;
        private ImageView ingredient_quantity_iv;
        private ImageView ingredient_quantity_cancel_iv;
        private TextView ingredient_quantity_ingredient_tv;
        private TextView ingredient_quantity_quantity_tv;
        private TextView ingredient_quantity_measurement_tv;
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
