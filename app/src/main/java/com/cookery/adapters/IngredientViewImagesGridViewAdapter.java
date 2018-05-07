package com.cookery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cookery.R;
import com.cookery.models.IngredientImageMO;
import com.cookery.utils.Utility;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by vishal on 20/12/17.
 */
public class IngredientViewImagesGridViewAdapter extends BaseAdapter {
    private final String CLASS_NAME = this.getClass().getName();
    private final Context mContext;

    private List<IngredientImageMO> images;
    private View.OnClickListener onClickListener;

    private static final int LAYOUT = R.layout.ingredient_view_image_item;

    public IngredientViewImagesGridViewAdapter(Context context, List<IngredientImageMO> images, View.OnClickListener onClickListener) {
        super();
        this.mContext = context;
        this.images = images;
        this.onClickListener = onClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;

        if(convertView == null) {
            mHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(LAYOUT, null);

            mHolder.ingredient_view_image_item_iv = convertView.findViewById(R.id.ingredient_view_image_item_iv);

            convertView.setTag(LAYOUT, mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag(LAYOUT);
        }

        IngredientImageMO ingredientImage = images.get(position);

        Utility.loadImageFromURL(mContext, ingredientImage.getING_IMG(), mHolder.ingredient_view_image_item_iv);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private LinearLayout ingredient_view_image_item_ll;
        private CircleImageView ingredient_view_image_item_iv;
    }

    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
