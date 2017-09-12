package com.cookery.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.cookery.models.CuisineMO;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;


/**
 * Created by ajit on 8/1/15.
 */
// Inner Class
public class AddRecipePhotosGridViewAdapter extends BaseAdapter {
    private final String CLASS_NAME = this.getClass().getName();
    private final Context mContext;
    private LayoutInflater inflater;
    private final int LAYOUT = R.layout.add_recipe_photos_gv_item;

    public List<String> dataList;
    private View.OnClickListener clickListener;

    public AddRecipePhotosGridViewAdapter(Context context, List<String> dataList, View.OnClickListener clickListener) {
        super();
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList = dataList;
        this.clickListener = clickListener;
    }

    public void updateDatalist(String photoPath){
        dataList.add(photoPath);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;

        if(convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(LAYOUT, null);

            mHolder.add_recipe_photos_gv_item_rl =  convertView.findViewById(R.id.add_recipe_photos_gv_item_rl);
            mHolder.add_recipe_photos_gv_item_iv =  convertView.findViewById(R.id.add_recipe_photos_gv_item_iv);

            convertView.setTag(LAYOUT, mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag(LAYOUT);
        }

        String photoPath = dataList.get(position);

        mHolder.add_recipe_photos_gv_item_iv.setImageBitmap(BitmapFactory.decodeFile(photoPath));

        mHolder.add_recipe_photos_gv_item_rl.setTag(photoPath);
        mHolder.add_recipe_photos_gv_item_rl.setOnClickListener(clickListener);

        setFont(mHolder.add_recipe_photos_gv_item_rl);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private RelativeLayout add_recipe_photos_gv_item_rl;
        private ImageView add_recipe_photos_gv_item_iv;
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
