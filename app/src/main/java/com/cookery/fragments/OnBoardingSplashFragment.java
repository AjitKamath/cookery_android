package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.OnBoardingSplashViewPagerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vishal on 24/03/18.
 */
public class OnBoardingSplashFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    //components
    @InjectView(R.id.on_boarding_splash_ll)
    LinearLayout on_boarding_splash_ll;

    @InjectView(R.id.dots_ll)
    LinearLayout dots_ll;

    @InjectView(R.id.on_boarding_splash_vp)
    ViewPager on_boarding_splash_vp;

    @InjectView(R.id.prev_btn)
    Button prev_btn;

    @InjectView(R.id.next_btn)
    Button next_btn;


    private OnBoardingSplashViewPagerAdapter onBoardingSplashViewPagerAdapter;
    private TextView[] mDots;
    private int currentpage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding_splash, container);
        ButterKnife.inject(this, view);

        setupOnBoardingSplash();

        return view;
    }

    private void setupOnBoardingSplash() {

    onBoardingSplashViewPagerAdapter = new OnBoardingSplashViewPagerAdapter(mContext);
    on_boarding_splash_vp.setAdapter(onBoardingSplashViewPagerAdapter);

    addDotsIndicator(0);

    on_boarding_splash_vp.addOnPageChangeListener(viewListener);

    next_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(currentpage == mDots.length-1){
             dismiss();
            }else {
                on_boarding_splash_vp.setCurrentItem(currentpage + 1);
            }
        }
    });



    prev_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            on_boarding_splash_vp.setCurrentItem(currentpage - 1);
        }
    });

    }


    public void addDotsIndicator(int position){

        mDots = new TextView[6];
        dots_ll.removeAllViews();

        for(int i=0;i<mDots.length;i++){
            mDots[i] = new TextView(mContext);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.gray));
            dots_ll.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }

    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            currentpage = position;

            if(position == 0){

                next_btn.setEnabled(true);
                prev_btn.setEnabled(false);
                prev_btn.setVisibility(View.INVISIBLE);

                next_btn.setText("Next");
                prev_btn.setText("");

            }else if(position == mDots.length-1){

                next_btn.setEnabled(true);
                prev_btn.setEnabled(true);
                prev_btn.setVisibility(View.VISIBLE);

                next_btn.setText("Got it");
                prev_btn.setText("Prev");

            }
            else{
                next_btn.setEnabled(true);
                prev_btn.setEnabled(true);
                prev_btn.setVisibility(View.VISIBLE);

                next_btn.setText("Next");
                prev_btn.setText("Prev");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

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
