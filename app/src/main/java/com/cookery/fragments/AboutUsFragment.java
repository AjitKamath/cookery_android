package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookery.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vishal on 23/03/18.
 */
public class AboutUsFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.aboutuscontent)
    TextView aboutuscontent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container);
        ButterKnife.inject(this, view);

        setupAboutUsFragment();

        return view;
    }

    private void setupAboutUsFragment() {

        String content = "We believe in the science of relationship building, within you and with your recipes. And we believe in chefs.\n"
                +"\n"
                +"We believe the heat of the kitchen forges better relationships.\n"
                +"\n"
                +"We know it does.\n"
                +"\n"
                +"we believe in passionate people, No matter their background or years of experience.\n"
                +"\n"
                +"we are giving your recipe a better Place and loving every second of it\n"
                +"\n"
                +"\n"
                +"\n"
                +"We are Designers, thinkers and collaborators\n"
                +"We are Cookery.";

                aboutuscontent.setText(content);
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
}
