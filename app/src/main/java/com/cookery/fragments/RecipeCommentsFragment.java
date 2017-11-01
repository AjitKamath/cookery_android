package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeCommentsRecyclerViewAdapter;
import com.cookery.models.CommentMO;
import com.cookery.models.MessageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE_COMMENTS;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeCommentsFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    //components
    @InjectView(R.id.common_fragment_recipe_comments_rl)
    RelativeLayout common_fragment_recipe_comments_rl;

    @InjectView(R.id.common_fragment_recipe_comments_rv)
    RecyclerView common_fragment_recipe_comments_rv;

    @InjectView(R.id.common_fragment_recipe_comments_comment_et)
    EditText common_fragment_recipe_comments_comment_et;

    @InjectView(R.id.common_fragment_recipe_comments_comment_iv)
    ImageView common_fragment_recipe_comments_comment_iv;
    //end of components

    private RecipeMO recipe;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_fragment_recipe_comments, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();
        getLoggedInUser();

        setupPage();

        setFont(common_fragment_recipe_comments_rl);

        return view;
    }

    private void getLoggedInUser() {
        loggedInUser = Utility.getUserFromUserSecurity(mContext);
    }

    private void getDataFromBundle() {
        recipe = (RecipeMO) getArguments().get(SELECTED_ITEM);
    }

    private void setupPage() {
        setupComments();
    }

    private void setupComments() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);

        common_fragment_recipe_comments_rv.setLayoutManager(mLayoutManager);
        common_fragment_recipe_comments_rv.setItemAnimator(new DefaultItemAnimator());
        common_fragment_recipe_comments_rv.setAdapter(new RecipeCommentsRecyclerViewAdapter(mContext, recipe.getComments(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));

        common_fragment_recipe_comments_comment_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = String.valueOf(common_fragment_recipe_comments_comment_et.getText());

                if(comment != null && !comment.trim().isEmpty()){
                    CommentMO commentObj = new CommentMO();
                    commentObj.setRCP_ID(recipe.getRCP_ID());
                    commentObj.setUSER_ID(loggedInUser.getUser_id());

                    MessageMO message = InternetUtility.submitRecipeComment(commentObj);

                    if(!message.isError()){
                        common_fragment_recipe_comments_comment_et.setText("");

                        //TODO: re fetch the comments for this recipe
                    }
                    else{
                        message.setPurpose("ADD_RECIPE_COMMENT");

                        Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE_COMMENTS);
                        Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
                    }
                }
            }
        });
    }

    // Empty constructor required for DialogFragment
    public RecipeCommentsFragment() {}

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