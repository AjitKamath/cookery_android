package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.models.MessageMO;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class MessageFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.common_message_message_tv)
    TextView common_message_message_tv;

    @InjectView(R.id.common_message_iv)
    ImageView common_message_iv;

    @InjectView(R.id.common_message_tv)
    TextView common_message_tv;

    @InjectView(R.id.common_message_ok_tv)
    TextView common_message_ok_tv;

    @InjectView(R.id.common_message_cancel_tv)
    TextView common_message_cancel_tv;
    //end of components

    private Object object;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_message, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        return view;
    }

    private void getDataFromBundle() {
        object = getArguments().get(GENERIC_OBJECT);
    }

    private void setupPage() {
        MessageMO mesage = (MessageMO) object;

        hideAllButtons();

        if("ADD_RECIPE".equalsIgnoreCase(mesage.getPurpose())){
            if(mesage.isError()){
                common_message_iv.setImageResource(R.drawable.scared);
                common_message_message_tv.setText("Sorry :(");
            }
            else{
                common_message_message_tv.setText("Awesome !");
                common_message_iv.setImageResource(R.drawable.happy);
            }

            common_message_tv.setText(mesage.getErr_message());

            common_message_ok_tv.setVisibility(View.VISIBLE);
            common_message_ok_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        else if("CLOSE_ADD_RECIPE".equalsIgnoreCase(mesage.getPurpose())){
            common_message_iv.setImageResource(R.drawable.thinking);
            common_message_message_tv.setText("Quit Recipe ?");
            common_message_tv.setText(mesage.getErr_message());

            common_message_ok_tv.setVisibility(View.VISIBLE);
            common_message_cancel_tv.setVisibility(View.VISIBLE);

            common_message_ok_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();

                    ((AddRecipeFragment) getTargetFragment()).dismiss();
                }
            });

            common_message_cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
    }

    private void hideAllButtons() {
        common_message_ok_tv.setVisibility(View.GONE);
        common_message_cancel_tv.setVisibility(View.GONE);
    }

    // Empty constructor required for DialogFragment
    public MessageFragment() {
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
        if (d != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
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

        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(robotoCondensedLightFont);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v);
            }
        }
    }
}
