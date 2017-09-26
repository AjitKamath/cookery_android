package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.AddRecipeQuantitySpinnerAdapter;
import com.cookery.models.IngredientMO;
import com.cookery.models.QuantityMO;
import com.cookery.utils.Utility;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.LIST_DATA;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class AddRecipeIngredientQuantityFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.fragment_ingredient_quantity_ll)
    LinearLayout fragment_ingredient_quantity_ll;

    @InjectView(R.id.fragment_ingredient_quantity_ingredient_iv)
    ImageView fragment_ingredient_quantity_ingredient_iv;

    @InjectView(R.id.fragment_ingredient_quantity_ingredient_tv)
    TextView fragment_ingredient_quantity_ingredient_tv;

    @InjectView(R.id.fragment_ingredient_quantity_quantity_spinner)
    Spinner fragment_ingredient_quantity_quantity_spinner;

    @InjectView(R.id.fragment_ingredient_quantity_quantity_et)
    EditText fragment_ingredient_quantity_quantity_et;

    @InjectView(R.id.fragment_ingredient_quantity_ok_tv)
    TextView fragment_ingredient_quantity_ok_tv;
    //end of components

    private IngredientMO ingredient;
    private List<QuantityMO> quantities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_quantity, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        ingredient = (IngredientMO) getArguments().get(SELECTED_ITEM);
        quantities = (List<QuantityMO>) getArguments().get(LIST_DATA);
    }

    private void setupPage() {
        if(ingredient == null){
            Log.e(CLASS_NAME, "Error ! Ingredient is null");
            return;
        }

        if(ingredient.getIMG() != null){
            Utility.loadImageFromURL(mContext, ingredient.getIMG(), fragment_ingredient_quantity_ingredient_iv);
        }

        fragment_ingredient_quantity_ingredient_tv.setText(ingredient.getING_NAME());

        AddRecipeQuantitySpinnerAdapter adapter = new AddRecipeQuantitySpinnerAdapter(mContext, quantities);
        fragment_ingredient_quantity_quantity_spinner.setAdapter(adapter);

        fragment_ingredient_quantity_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(String.valueOf(fragment_ingredient_quantity_quantity_et.getText()));

                if(quantity == 0){
                    Utility.showSnacks(fragment_ingredient_quantity_ll, "Quantity cannot be 0", OK, Snackbar.LENGTH_SHORT);
                    return;
                }

                dismiss();

                ingredient.setQuantity((QuantityMO) fragment_ingredient_quantity_quantity_spinner.getSelectedView().getTag());
                ingredient.setQTY(quantity);

                ((AddRecipeFragment)getTargetFragment()).addIngredient(ingredient);
            }
        });
    }

    // Empty constructor required for DialogFragment
    public AddRecipeIngredientQuantityFragment() {}

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
            int width = 800;
            int height = 600;
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
