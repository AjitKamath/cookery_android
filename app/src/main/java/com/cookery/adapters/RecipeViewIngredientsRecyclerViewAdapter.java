package com.cookery.adapters;

/**
 * Created by ajit on 13/9/17.
 */


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.fragments.AddMyListFragment;
import com.cookery.interfaces.LongClickListner;
import com.cookery.models.IngredientAkaMO;
import com.cookery.models.MessageMO;
import com.cookery.models.MyListMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE;

public class RecipeViewIngredientsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeViewIngredientsRecyclerViewAdapter.ViewHolder> {

    private static final String CLASS_NAME = RecipeViewIngredientsRecyclerViewAdapter.class.getName();
    private Context mContext;

    public List<IngredientAkaMO> ingredients;
    private View.OnClickListener listener;
    private String ing_aka_name;
    private int ing_aka_id;
    public List<MyListMO> mylists;
    private FragmentManager manager;

    public RecipeViewIngredientsRecyclerViewAdapter(Context mContext, FragmentManager manager, List<IngredientAkaMO> ingredients, List<MyListMO> mylists, View.OnClickListener listener) {
        this.mContext = mContext;
        this.ingredients = ingredients;
        this.listener = listener;
        this.manager = manager;
        this.mylists = mylists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_view_ingredients_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final IngredientAkaMO ingredient = ingredients.get(position);

        if(ingredient.getImages() != null && !ingredient.getImages().isEmpty()){
            Utility.loadImageFromURL(mContext, ingredient.getImages().get(0).getING_IMG(), holder.recipe_view_ingredients_item_iv);
        }

        holder.recipe_view_ingredients_item_tv.setText(ingredient.getING_AKA_NAME().toUpperCase());
        holder.recipe_view_ingredients_category_item_tv.setText(ingredient.getIngredientCategoryName());
        holder.recipe_view_ingredients_item_qty_tv.setText(String.valueOf(ingredient.getING_UOM_VALUE()));
        holder.recipe_view_ingredients_item_qty_type_tv.setText(ingredient.getING_UOM_NAME().toUpperCase());

        /*divider*/
        if(position == ingredients.size()-1){
            holder.recipe_view_ingredients_item_divider_view.setVisibility(View.GONE);
        }
        else{
            holder.recipe_view_ingredients_item_divider_view.setVisibility(View.VISIBLE);
        }
        /*divider*/

        holder.setLongClickListner(new LongClickListner() {
            @Override
            public void onItemLongClick(int pos) {
                ing_aka_name = ingredients.get(pos).getING_AKA_NAME();
                ing_aka_id = ingredients.get(pos).getING_AKA_ID();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    public void addData(IngredientAkaMO ingredient) {
        if(ingredients == null){
            ingredients = new ArrayList<>();
        }

        if(!ingredients.isEmpty()){
            //check if the ingredien is already added, if yes, bring it on top
            for(IngredientAkaMO thisIngredient: ingredients){
                if(thisIngredient.getING_AKA_NAME().equalsIgnoreCase(ingredient.getING_AKA_NAME())){
                    ingredients.remove(thisIngredient);
                    break;
                }
            }
        }

        ingredients.add(0, ingredient);
        notifyDataSetChanged();
    }

    public void removeData(IngredientAkaMO ingredient) {
        if(ingredients == null){
            ingredients = new ArrayList<>();
        }

        ingredients.remove(ingredient);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnCreateContextMenuListener, View.OnLongClickListener  {
        public CircleImageView recipe_view_ingredients_item_iv;
        public TextView recipe_view_ingredients_item_tv;
        public TextView recipe_view_ingredients_category_item_tv;
        public TextView recipe_view_ingredients_item_qty_tv;
        public TextView recipe_view_ingredients_item_qty_type_tv;
        public View recipe_view_ingredients_item_divider_view;

        LongClickListner longClickListner;

        public ViewHolder(View view) {
            super(view);
            recipe_view_ingredients_item_iv = view.findViewById(R.id.recipe_view_ingredients_item_iv);
            recipe_view_ingredients_item_tv = view.findViewById(R.id.recipe_view_ingredients_item_tv);
            recipe_view_ingredients_category_item_tv = view.findViewById(R.id.recipe_view_ingredients_category_item_tv);
            recipe_view_ingredients_item_qty_tv = view.findViewById(R.id.recipe_view_ingredients_item_qty_tv);
            recipe_view_ingredients_item_qty_type_tv = view.findViewById(R.id.recipe_view_ingredients_item_qty_type_tv);
            recipe_view_ingredients_item_divider_view = view.findViewById(R.id.recipe_view_ingredients_item_divider_view);
            view.setOnCreateContextMenuListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Choose Action");
            MyListMO obj = new MyListMO();
            obj.setLIST_NAME("Add to New List");
            mylists.set(0,obj);
            for(int i=0; i<mylists.size(); i++)
            {
                contextMenu.add(0, i, 0, mylists.get(i).getLIST_NAME());
                contextMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int menuindex = menuItem.getItemId();

                        if(menuindex == 0)
                        {
                            // Add to new List
                            AddMyListFragment.openAddListFragmentFromRecipe(manager,ing_aka_id,ing_aka_name);
                        }

                        else
                        {
                            // Add to existing list
                            addIngredientInListFromRecipe(ing_aka_id,mylists.get(menuindex).getLIST_ID());
                        }
                       return true;
                    }
                });
            }
        }

        public void addIngredientInListFromRecipe(int ingid, int listid)
        {
            if(ingid != 0 && listid !=0)
            {
                MyListMO myListMOobj = new MyListMO();
                myListMOobj.setING_AKA_ID(ingid);
                myListMOobj.setLIST_ID(listid);
                new AsyncTaskerAddIngredientToMyList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myListMOobj);
            }
            else {
                Log.e(CLASS_NAME, "Error ! listid or ingid is null");
            }
        }

        class AsyncTaskerAddIngredientToMyList extends AsyncTask<Object, Void, Object> {
            Fragment frag = null;
            @Override
            protected void onPreExecute() {
                frag = Utility.showWaitDialog(manager, "Adding ingredient to your List ..");
            }

            @Override
            protected Object doInBackground(Object... objects) {
                MyListMO listdata = (MyListMO) objects[0];
                return InternetUtility.saveListFromRecipe(listdata);
            }

            @Override
            protected void onPostExecute(Object object) {

                MessageMO message = (MessageMO) object;
                message.setPurpose("ADD_LIST");

                Utility.closeWaitDialog(manager, frag);

                Fragment currentFrag = manager.findFragmentByTag(FRAGMENT_RECIPE);
                Utility.showMessageDialog(manager, currentFrag, message);

                if(message.isError()){
                    Log.e(CLASS_NAME, "Error : "+message.getErr_message());
                }
                else{
                    Log.i(CLASS_NAME, message.getErr_message());
                    //dismiss();
                }
            }
        }

        public void setLongClickListner(LongClickListner lc)
        {
            this.longClickListner = lc;
        }

        @Override
        public boolean onLongClick(View v)
        {
            this.longClickListner.onItemLongClick(getLayoutPosition());
            return false;
        }

    }


}