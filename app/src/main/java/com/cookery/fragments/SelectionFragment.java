package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.CuisinesGridViewAdapter;
import com.cookery.adapters.FoodTypeGridViewAdapter;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.utils.Utility;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class SelectionFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.common_selection_rl)
    RelativeLayout common_selection_rl;
    
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
        if(objectList == null || objectList.isEmpty()){
            Log.e(CLASS_NAME, "Error ! Object is null");
            return;
        }

        if(objectList.get(0) instanceof FoodTypeMO){
            FoodTypeGridViewAdapter adapter = new FoodTypeGridViewAdapter(mContext, objectList, new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    FoodTypeMO foodType = (FoodTypeMO) view.getTag();

                    if(getTargetFragment() instanceof AddRecipeFragment){
                        dismiss();
                        ((AddRecipeFragment)getTargetFragment()).setFoodType(foodType);
                    }
                    else{
                        Utility.showUnimplemetedActionSnacks(common_selection_rl);
                    }
                }
            });

            common_selection_food_type_gv.setAdapter(adapter);
        }
        else if(objectList.get(0) instanceof CuisineMO){
            CuisinesGridViewAdapter adapter = new CuisinesGridViewAdapter(mContext, objectList, new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    CuisineMO cuisine = (CuisineMO) view.getTag();

                    if(getTargetFragment() instanceof AddRecipeFragment){
                        dismiss();
                        ((AddRecipeFragment)getTargetFragment()).setCuisine(cuisine);
                    }
                    else{
                        Utility.showUnimplemetedActionSnacks(common_selection_rl);
                    }
                }
            });

            common_selection_food_type_gv.setAdapter(adapter);
        }
    }

    @OnClick(R.id.common_selection_close_iv)
    public void onClose(View view){
        dismiss();
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
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = 900;
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
