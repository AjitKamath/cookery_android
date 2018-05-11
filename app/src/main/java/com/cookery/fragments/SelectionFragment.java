package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.CuisinesGridViewAdapter;
import com.cookery.adapters.FoodTypeGridViewAdapter;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

/**
 * Created by ajit on 21/3/16.
 */
public class SelectionFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.common_selection_rl)
    RelativeLayout common_selection_rl;

    @InjectView(R.id.common_selection_search_et)
    EditText common_selection_search_et;

    @InjectView(R.id.common_selection_search_iv)
    ImageView common_selection_search_iv;
    
    @InjectView(R.id.common_selection_food_type_gv)
    GridView common_selection_food_type_gv;
    //end of components

    private List<Object> objectList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_selection, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        objectList = (List<Object>) getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        common_selection_search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = String.valueOf(s).trim().toUpperCase();

                if(query.isEmpty()){
                    common_selection_search_iv.setVisibility(View.GONE);
                    filterList(objectList);
                }
                else{
                    common_selection_search_iv.setVisibility(View.VISIBLE);

                    List<Object> tempList = new ArrayList<>();
                    for(Object item : objectList){
                        if(item instanceof FoodTypeMO){
                            FoodTypeMO foodType = (FoodTypeMO) item;
                            if(foodType.getFOOD_TYP_NAME().toUpperCase().contains(query)){
                                tempList.add(item);
                            }
                        }
                        else if(item instanceof CuisineMO){
                            CuisineMO cuisine = (CuisineMO) item;
                            if(cuisine.getFOOD_CSN_NAME().toUpperCase().contains(query)){
                                tempList.add(item);
                            }
                        }
                        else{
                            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+item);
                        }
                    }

                    filterList(tempList);
                }
            }
        });
        common_selection_search_iv.setVisibility(View.GONE);


        if(objectList == null || objectList.isEmpty()){
            Log.e(CLASS_NAME, "Error ! Object is null");
            return;
        }

        filterList(objectList);
    }

    private void filterList(List<? extends Object> list){
        if(list != null && !list.isEmpty()){
            if(list.get(0) instanceof FoodTypeMO){
                FoodTypeGridViewAdapter adapter = new FoodTypeGridViewAdapter(mContext, list, new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        FoodTypeMO foodType = (FoodTypeMO) view.getTag();

                        if(getTargetFragment() instanceof RecipeAddFragment){
                            dismiss();
                            ((RecipeAddFragment)getTargetFragment()).setFoodType(foodType);
                        }
                        else{
                            Utility.showUnimplemetedActionSnacks(common_selection_rl);
                        }
                    }
                });

                common_selection_food_type_gv.setAdapter(adapter);
            }
            else if(list.get(0) instanceof CuisineMO){
                CuisinesGridViewAdapter adapter = new CuisinesGridViewAdapter(mContext, list, new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        CuisineMO cuisine = (CuisineMO) view.getTag();

                        if(getTargetFragment() instanceof RecipeAddFragment){
                            dismiss();
                            ((RecipeAddFragment)getTargetFragment()).setCuisine(cuisine);
                        }
                        else{
                            Utility.showUnimplemetedActionSnacks(common_selection_rl);
                        }
                    }
                });

                common_selection_food_type_gv.setAdapter(adapter);
            }
            else{
                Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+list.get(0));
            }
        }
        else{
            common_selection_food_type_gv.setAdapter(null);
        }
    }



    // Empty constructor required for DialogFragment
    public SelectionFragment() {}

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
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
        }
    }

    //method iterates over each component in the activity and when it finds a text view..sets its font
    public void setFont(ViewGroup group) {
        //set font for all the text view
        final Typeface robotoCondensedLightFont = Typeface.createFromAsset(mContext.getAssets(), UI_FONT);

        int count = group.getChildCount();
        View v;

        for(int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if(v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            }
            else if(v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}
