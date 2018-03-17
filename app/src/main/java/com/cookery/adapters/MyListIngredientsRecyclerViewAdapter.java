package com.cookery.adapters;

/**
 * Created by vishal on 25/12/17.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.IngredientMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyListIngredientsRecyclerViewAdapter extends RecyclerView.Adapter<MyListIngredientsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = MyListIngredientsRecyclerViewAdapter.class.getName();
    private Context mContext;

    public List<IngredientMO> ingredients;
    private View.OnClickListener listener;

    public MyListIngredientsRecyclerViewAdapter(Context mContext, List<IngredientMO> ingredients, View.OnClickListener listener) {
        this.mContext = mContext;
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mylist_add_ingredients_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IngredientMO ingredient = ingredients.get(position);

        Utility.loadImageFromURL(mContext, ingredient.getIMG(), holder.mylist_add_ingredients_item_iv);

        holder.mylist_add_ingredients_item_tv.setText(ingredient.getING_NAME().toUpperCase());
        holder.mylist_add_ingredients_item_delete_iv.setTag(ingredient);

        holder.mylist_add_ingredients_item_delete_iv.setOnClickListener(listener);

        /*divider*/
        if (position == ingredients.size() - 1) {
            holder.mylist_add_ingredients_item_divider_view.setVisibility(View.GONE);
        } else {
            holder.mylist_add_ingredients_item_divider_view.setVisibility(View.VISIBLE);
        }
        /*divider*/
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }



    public void addData(IngredientMO ingredient) {
        if(ingredients == null){
            ingredients = new ArrayList<>();
        }

        if(!ingredients.isEmpty()){
            //check if the ingredien is already added, if yes, bring it on top
            for(IngredientMO thisIngredient: ingredients){
                if(thisIngredient.getING_NAME().equalsIgnoreCase(ingredient.getING_NAME())){
                    ingredients.remove(thisIngredient);
                    break;
                }
            }
        }

        ingredients.add(0, ingredient);
        notifyDataSetChanged();
    }

    public void removeData(IngredientMO ingredient) {
        if(ingredients == null){
            ingredients = new ArrayList<>();
        }

        ingredients.remove(ingredient);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mylist_add_ingredients_item_iv;
        public TextView mylist_add_ingredients_item_tv;
        public ImageView mylist_add_ingredients_item_delete_iv;
        public View mylist_add_ingredients_item_divider_view;
        public CheckBox mylist_add_ingredients_item_cb;
        public ViewHolder(View view) {
            super(view);
            mylist_add_ingredients_item_cb = view.findViewById(R.id.mylist_add_ingredients_item_cb);
            mylist_add_ingredients_item_iv = view.findViewById(R.id.mylist_add_ingredients_item_iv);
            mylist_add_ingredients_item_tv = view.findViewById(R.id.mylist_add_ingredients_item_tv);
            mylist_add_ingredients_item_delete_iv = view.findViewById(R.id.mylist_add_ingredients_item_delete_iv);
            mylist_add_ingredients_item_divider_view = view.findViewById(R.id.mylist_add_ingredients_item_divider_view);
        }
    }
}