package com.cookery.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.MyListMO;

import java.util.List;

import static com.cookery.utils.Constants.UI_FONT;


/**
 * Created by vishal on 20/12/17.
 */
public class MyListGridViewAdapter extends BaseAdapter {
    private final String CLASS_NAME = this.getClass().getName();
    private final Context mContext;
    private LayoutInflater inflater;
    private final int LAYOUT = R.layout.mylist_gv_item;

    private List<? extends Object> dataList;
    private View.OnClickListener clickListener;

    public MyListGridViewAdapter(Context context, List<? extends Object> dataList, View.OnClickListener clickListener) {
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

            mHolder.grid_view_mylist_tv =  convertView.findViewById(R.id.grid_view_mylist_tv);
            mHolder.grid_view_mylist_item_count_tv =  convertView.findViewById(R.id.grid_view_mylist_item_count_tv);
            mHolder.grid_view_mylist_rl =  convertView.findViewById(R.id.grid_view_mylist_rl);

            convertView.setTag(LAYOUT, mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag(LAYOUT);
        }

        MyListMO mylist = (MyListMO) dataList.get(position);

        mHolder.grid_view_mylist_tv.setText(mylist.getLIST_NAME());
        mHolder.grid_view_mylist_rl.setTag(mylist.getLIST_ID());
        if(mylist.getITEM_COUNT() == 1) {
            mHolder.grid_view_mylist_item_count_tv.setText(mylist.getITEM_COUNT() + "ITEM");
        }
        else
        {
            mHolder.grid_view_mylist_item_count_tv.setText(mylist.getITEM_COUNT() + "ITEMS");
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView grid_view_mylist_tv;
        private TextView grid_view_mylist_item_count_tv;
        private RelativeLayout grid_view_mylist_rl;
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
