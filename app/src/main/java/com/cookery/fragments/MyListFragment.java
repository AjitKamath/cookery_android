package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.AutoCompleteAdapter;
import com.cookery.adapters.MyListIngredientsRecyclerViewAdapter;
import com.cookery.models.IngredientAkaMO;
import com.cookery.models.MessageMO;
import com.cookery.models.MyListMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_MY_LIST;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.INGREDIENT_ID;
import static com.cookery.utils.Constants.INGREDIENT_NAME;
import static com.cookery.utils.Constants.LIST_ID;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_VIEW;

/**
 * Created by vishal on 13/12/17.
 */
public class MyListFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.fragment_my_list_ll)
    LinearLayout fragment_my_list_ll;

    @InjectView(R.id.recipe_add_ingredients_list_act)
    AutoCompleteTextView recipe_add_ingredients_list_act;

    @InjectView(R.id.common_fragment_header_tv)
    TextView common_fragment_header_tv;

    @InjectView(R.id.recipe_add_ingredients_count_tv)
    TextView recipe_add_ingredients_count_tv;

    @InjectView(R.id.recipe_add_ingredients_list_rv)
    RecyclerView recipe_add_ingredients_list_rv;

    @InjectView(R.id.recipe_add_ingredients_iv)
    ImageView recipe_add_ingredients_iv;

    @InjectView(R.id.recipe_add_ingredients_no_items_tv)
    TextView recipe_add_ingredients_no_items_tv;

    @InjectView(R.id.add_my_list_et)
    EditText add_my_list_et;

    @InjectView(R.id.mylist_save_fab)
    FloatingActionButton mylist_save_fab;

    //components

    private MyListMO mylist;
    private UserMO loggedInUser;
    private int listid;
    private int ing_aka_id;
    private String ing_aka_name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_list, container);
        ButterKnife.inject(this, view);

        getDataFromBundle();

        setupMyListFragment();

        setFont(fragment_my_list_ll);

        return view;
    }

    private void getLoggedInUser() {
        loggedInUser = Utility.getUserFromUserSecurity(mContext);
    }

    private void getDataFromBundle() {
        mylist = (MyListMO) getArguments().get(GENERIC_OBJECT);
        if(null != getArguments().get(LIST_ID)) {
            listid = (Integer) getArguments().get(LIST_ID);
        }
        if(null != getArguments().get(INGREDIENT_ID)) {
            ing_aka_id = (Integer) getArguments().get(INGREDIENT_ID);
        }
        if(null != getArguments().get(INGREDIENT_NAME)) {
            ing_aka_name = (String) getArguments().get(INGREDIENT_NAME);
        }
    }

    private void setupMyListViewFragment(final List<MyListMO> mylistobj)
    {
      common_fragment_header_tv.setText(mylistobj.get(0).getLIST_NAME());
        add_my_list_et.setText(mylistobj.get(0).getLIST_NAME());

      /*auto complete*/
        recipe_add_ingredients_list_act.setAdapter(new AutoCompleteAdapter(mContext, R.layout.ingredient_autocomplete_item, "INGREDIENTS"));
        recipe_add_ingredients_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipe_add_ingredients_list_act.getText().length() == 0) {
                    return;
                }
                IngredientAkaMO ingredient = new IngredientAkaMO();
                ingredient.setING_AKA_NAME(String.valueOf(recipe_add_ingredients_list_act.getText()));
                addIngredient(ingredient);

            }
        });
        recipe_add_ingredients_list_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IngredientAkaMO ingredient = new IngredientAkaMO();
                IngredientAkaMO ing = (IngredientAkaMO) view.getTag();
                ingredient.setING_AKA_ID(ing.getING_AKA_ID());
                ingredient.setING_AKA_NAME(ing.getING_AKA_NAME());
                addIngredient(ingredient);
            }
        });
        /*auto complete*/

    /*ingredients list*/
    IngredientAkaMO ingobj;
        for(MyListMO obj:mylistobj)
        {
            ingobj = new IngredientAkaMO();
            ingobj.setING_AKA_ID(obj.getING_AKA_ID());
            ingobj.setING_AKA_NAME(obj.getING_AKA_NAME());

            if(mylist.getListofingredients() == null){
                mylist.setListofingredients(new ArrayList<IngredientAkaMO>());
            }

            mylist.getListofingredients().add(ingobj);
        }


        MyListIngredientsRecyclerViewAdapter adapter = new MyListIngredientsRecyclerViewAdapter(mContext, mylist.getListofingredients(), new View.OnClickListener() {

            @Override
                public void onClick(View view) {
                    if (R.id.mylist_add_ingredients_item_delete_iv == view.getId()) {
                        removeIngredient((IngredientAkaMO) view.getTag());
                    } else {
                        Log.e(CLASS_NAME, UN_IDENTIFIED_VIEW + view);
                    }
                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
            mLayoutManager.scrollToPosition(adapter.getItemCount() - 1);
            recipe_add_ingredients_list_rv.setLayoutManager(mLayoutManager);
            recipe_add_ingredients_list_rv.setItemAnimator(new DefaultItemAnimator());
            recipe_add_ingredients_list_rv.setAdapter(adapter);

        /*ingredients list*/

            updateIngredients();


            mylist_save_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mylist.setLIST_ID(listid);
                    mylist.setLIST_NAME(String.valueOf(add_my_list_et.getText()));
                    if(mylist.getLIST_ID() != 0)
                    {
                        new MyListFragment.AsyncTaskerUpdateMyList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mylist);
                    }
                    // Handling for Adding the selected ingredient from recipe to newly created list
                    else
                    {
                        MyListIngredientsRecyclerViewAdapter adapter = (MyListIngredientsRecyclerViewAdapter) recipe_add_ingredients_list_rv.getAdapter();
                        String listname = String.valueOf(add_my_list_et.getText());
                        if (listname != null && !listname.trim().isEmpty()) {
                            MyListMO mylistObj = new MyListMO();
                            mylistObj.setLIST_NAME(listname);
                            //mylistObj.setUSER_ID(loggedInUser.getUser_id());
                            mylistObj.setUSER_ID(1);
                            mylistObj.setListofingredients(adapter.ingredients);

                            new MyListFragment.AsyncTaskerSubmitMyList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mylistObj);
                        }
                    }

                }
            });
        }

    private void setupMyListFragment() {
        if (listid != 0) {
            new MyListFragment.AsyncTaskerViewMyList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listid);
        }
        // Handling for Adding the selected ingredient from recipe to new list
        else if(ing_aka_id != 0)
            {
                ArrayList<MyListMO> mylistobj = new ArrayList<MyListMO>();
                MyListMO myListMOobj = new MyListMO();
                myListMOobj.setLIST_NAME("");
                myListMOobj.setING_AKA_ID(ing_aka_id);
                myListMOobj.setING_AKA_NAME(ing_aka_name);
                mylistobj.add(myListMOobj);
                setupMyListViewFragment(mylistobj);
            }
        else {

            common_fragment_header_tv.setText("MY LIST");



     /*auto complete*/
            recipe_add_ingredients_list_act.setAdapter(new AutoCompleteAdapter(mContext, R.layout.ingredient_autocomplete_item, "INGREDIENTS"));
            recipe_add_ingredients_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recipe_add_ingredients_list_act.getText().length() == 0) {
                        return;
                    }
                    IngredientAkaMO ingredient = new IngredientAkaMO();
                    ingredient.setING_AKA_NAME(String.valueOf(recipe_add_ingredients_list_act.getText()));
                    addIngredient(ingredient);

                }
            });
            recipe_add_ingredients_list_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    IngredientAkaMO ingredient = new IngredientAkaMO();
                    IngredientAkaMO ing = (IngredientAkaMO) view.getTag();
                    ingredient.setING_AKA_ID(ing.getING_AKA_ID());
                    ingredient.setING_AKA_NAME(ing.getING_AKA_NAME());
                    addIngredient(ingredient);
                }
            });
        /*auto complete*/

    /*ingredients list*/
            MyListIngredientsRecyclerViewAdapter adapter = new MyListIngredientsRecyclerViewAdapter(mContext, new ArrayList<IngredientAkaMO>(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (R.id.mylist_add_ingredients_item_delete_iv == view.getId()) {
                        removeIngredient((IngredientAkaMO) view.getTag());
                    } else if (R.id.recipe_add_ingredients_item_edit_iv == view.getId()) {
                    } else {
                        Log.e(CLASS_NAME, UN_IDENTIFIED_VIEW + view);
                    }
                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mLayoutManager.scrollToPosition(adapter.getItemCount() - 1);
            recipe_add_ingredients_list_rv.setLayoutManager(mLayoutManager);
            recipe_add_ingredients_list_rv.setItemAnimator(new DefaultItemAnimator());
            recipe_add_ingredients_list_rv.setAdapter(adapter);
        /*ingredients list*/

            updateIngredients();


            mylist_save_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String listname = String.valueOf(add_my_list_et.getText());
                    MyListIngredientsRecyclerViewAdapter adapter = (MyListIngredientsRecyclerViewAdapter) recipe_add_ingredients_list_rv.getAdapter();

                    if (listname != null && !listname.trim().isEmpty()) {
                        MyListMO mylistObj = new MyListMO();
                        mylistObj.setLIST_NAME(listname);
                        //mylistObj.setUSER_ID(loggedInUser.getUser_id());
                        mylistObj.setUSER_ID(1);
                        mylistObj.setListofingredients(adapter.ingredients);

                        new MyListFragment.AsyncTaskerSubmitMyList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mylistObj);
                    }
                }
            });
        }
    }


    class AsyncTaskerViewMyList extends AsyncTask<Object, Void, Object> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object... objects) {
            int mylistid = (Integer) objects[0];

            if (mylistid == 0) {
                Log.e(CLASS_NAME, "Error ! mylistid object is null/zero");
                return null;
            }

            return InternetUtility.viewMyList(mylistid);
        }

        @Override
        protected void onPostExecute(Object object) {

            ArrayList<MyListMO> mylistobj = (ArrayList<MyListMO>) object;
            MessageMO message;
            if (mylistobj == null) {
                    message = new MessageMO();

                    message.setError(true);
                    message.setErr_message("Something went wrong !");
                }
                else {
                setupMyListViewFragment(mylistobj);
            }
        }
    }

    class AsyncTaskerUpdateMyList extends AsyncTask<Object, Void, Object> {
        Fragment frag = null;
        @Override
        protected void onPreExecute() {
            frag = Utility.showWaitDialog(getFragmentManager(), "Updating your List ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            MyListMO mylistObj = (MyListMO) objects[0];

            if (mylistObj == null) {
                Log.e(CLASS_NAME, "Error ! mylistObj object is null");
                return null;
            }

            return InternetUtility.updateList(mylistObj);
        }

        @Override
        protected void onPostExecute(Object object) {

            MessageMO message = (MessageMO) object;
            message.setPurpose("ADD_LIST");

            Utility.closeWaitDialog(getFragmentManager(), frag);

            Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_MY_LIST);
            Utility.showMessageDialog(getFragmentManager(), currentFrag, message);

            if(message.isError()){
                Log.e(CLASS_NAME, "Error : "+message.getErr_message());
            }
            else{
                Log.i(CLASS_NAME, message.getErr_message());
                dismiss();
            }
        }
    }


    class AsyncTaskerSubmitMyList extends AsyncTask<Object, Void, Object> {
        Fragment frag = null;
        @Override
        protected void onPreExecute() {
            frag = Utility.showWaitDialog(getFragmentManager(), "Adding your List ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            MyListMO mylistObj = (MyListMO) objects[0];

            if (mylistObj == null) {
                Log.e(CLASS_NAME, "Error ! mylistObj object is null");
                return null;
            }

            return InternetUtility.saveList(mylistObj);
        }

        @Override
        protected void onPostExecute(Object object) {

            MessageMO message = (MessageMO) object;
            message.setPurpose("ADD_LIST");

            Utility.closeWaitDialog(getFragmentManager(), frag);

            Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_MY_LIST);
            Utility.showMessageDialog(getFragmentManager(), currentFrag, message);

            if(message.isError()){
                Log.e(CLASS_NAME, "Error : "+message.getErr_message());
            }
            else{
                Log.i(CLASS_NAME, message.getErr_message());
                dismiss();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
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



    private void updateIngredients(){
        if(mylist.getListofingredients() == null || mylist.getListofingredients().isEmpty()){
            recipe_add_ingredients_no_items_tv.setVisibility(View.VISIBLE);
            recipe_add_ingredients_list_rv.setVisibility(View.GONE);
            recipe_add_ingredients_count_tv.setVisibility(View.GONE);
        }
        else{
            if(mylist.getListofingredients() .size() == 1){
                recipe_add_ingredients_count_tv.setText(mylist.getListofingredients().size()+" ITEM");
            }
            else{
                recipe_add_ingredients_count_tv.setText(mylist.getListofingredients().size()+" ITEMS");
            }

            recipe_add_ingredients_no_items_tv.setVisibility(View.GONE);
            recipe_add_ingredients_list_rv.setVisibility(View.VISIBLE);
            recipe_add_ingredients_count_tv.setVisibility(View.VISIBLE);
        }
    }



    public void addIngredient(IngredientAkaMO ingredient){
        MyListIngredientsRecyclerViewAdapter adapter = (MyListIngredientsRecyclerViewAdapter)recipe_add_ingredients_list_rv.getAdapter();
        adapter.addData(ingredient);
        mylist.setListofingredients(adapter.ingredients);
        recipe_add_ingredients_list_act.setText("");
        updateIngredients();
    }

    public void removeIngredient(IngredientAkaMO ingredient){
        MyListIngredientsRecyclerViewAdapter adapter = (MyListIngredientsRecyclerViewAdapter)recipe_add_ingredients_list_rv.getAdapter();
        adapter.removeData(ingredient);
        mylist.setListofingredients(adapter.ingredients);
        updateIngredients();
    }
}
