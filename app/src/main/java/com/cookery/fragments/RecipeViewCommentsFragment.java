package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.cookery.utils.Constants.FRAGMENT_RECIPE_COMMENTS;
import static com.cookery.utils.Constants.FRAGMENT_RECIPE_LIKED_USERS;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;

/**
 * Created by ajit on 21/3/16.
 */
public class RecipeViewCommentsFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    //components
    @InjectView(R.id.recipe_comments_rl)
    RelativeLayout recipe_comments_rl;

    @InjectView(R.id.recipe_comments_rv)
    RecyclerView recipe_comments_rv;

    @InjectView(R.id.recipe_comments_comment_et)
    EditText recipe_comments_comment_et;

    @InjectView(R.id.recipe_comments_comment_iv)
    ImageView recipe_comments_comment_iv;
    //end of components

    private RecipeMO recipe;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_view_comments, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();
        getLoggedInUser();

        setupPage();

        setFont(recipe_comments_rl);

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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recipe_comments_rv.setLayoutManager(mLayoutManager);
        recipe_comments_rv.setItemAnimator(new DefaultItemAnimator());
        recipe_comments_rv.setAdapter(new RecipeCommentsRecyclerViewAdapter(mContext, loggedInUser, recipe.getComments(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.recipe_comments_item_delete_iv){
                    MessageMO message = new MessageMO();
                    message.setErr_message("Comment will be deleted !");
                    message.setPurpose("RECIPE_VIEW_DELETE_COMMENT");
                    message.setError(false);
                    message.setObject(view.getTag());

                    Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE_COMMENTS);
                    Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
                }
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(view.getId() == R.id.recipe_comments_likes_iv){
                    new AsyncFetchLikedUsers().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (CommentMO) view.getTag());
                }
                return true;
            }
        }));

        recipe_comments_comment_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = String.valueOf(recipe_comments_comment_et.getText());

                if(comment != null && !comment.trim().isEmpty()){
                    CommentMO commentObj = new CommentMO();
                    commentObj.setRCP_ID(recipe.getRCP_ID());
                    commentObj.setUSER_ID(loggedInUser.getUSER_ID());
                    commentObj.setCOMMENT(comment);

                    new AsyncTaskerSubmitRecipeComment().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, commentObj);
                }
            }
        });
    }

    public void deleteComment(CommentMO comment){
        if(recipe.isUserReviewed()){
            new AsyncDeleteComment().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, comment);
        }
        else{
            Log.e(CLASS_NAME, "Error ! Recipe.isUserReviewed is expected to be true.");
        }
    }

    // Empty constructor required for DialogFragment
    public RecipeViewCommentsFragment() {}

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

    class AsyncTaskerSubmitRecipeComment extends AsyncTask<Object, Void, Object> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Object... objects) {
            CommentMO comment = (CommentMO) objects[0];

            if(comment == null){
                Log.e(CLASS_NAME, "Error ! Comment object is null");
                return null;
            }

            return InternetUtility.submitRecipeComment(comment);
        }

        @Override
        protected void onPostExecute(Object object) {
            MessageMO message = (MessageMO) object;

            if(message == null || message.isError()){
                if(message == null){
                    message = new MessageMO();

                    message.setError(true);
                    message.setErr_message("Something went wrong !");
                }

                message.setPurpose("RECIPE_VIEW_COMMENT_ADD");

                Fragment currentFrag = getFragmentManager().findFragmentByTag(FRAGMENT_RECIPE_COMMENTS);
                Utility.showMessageDialog(getFragmentManager(), currentFrag, message);
            }
            else{
                new AsyncTaskerSubmitFetchRecipeComments().executeOnExecutor(THREAD_POOL_EXECUTOR);
                recipe_comments_comment_et.setText("");
                /*Utility.showSnacks(recipe_comments_rl, "Comment submitted", OK, Snackbar.LENGTH_SHORT);*/
            }
        }

        class AsyncTaskerSubmitFetchRecipeComments extends AsyncTask<Void, Void, Object> {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Object doInBackground(Void... objects) {
                return InternetUtility.fetchRecipeComments(loggedInUser, recipe, 0);
            }

            @Override
            protected void onPostExecute(Object object) {
                List<CommentMO> comments = (List<CommentMO>) object;
                recipe.setComments(comments);
                setupComments();

                if(getTargetFragment() instanceof RecipeViewFragment){
                    ((RecipeViewFragment)getTargetFragment()).updateRecipeView();
                }
            }
        }
    }

    public class AsyncDeleteComment extends AsyncTask<Object, Void, Object> {
        Fragment frag;

        @Override
        protected void onPreExecute() {
            frag = Utility.showWaitDialog(getFragmentManager(), "Deleting your comment ..");
        }

        @Override
        protected Object doInBackground(Object... objects) {
            CommentMO comment = (CommentMO) objects[0];
            return InternetUtility.deleteRecipeComment(loggedInUser, comment);
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), frag);

            MessageMO message = (MessageMO) object;

            if (message != null && !message.isError()) {
                dismiss();

                if(getTargetFragment() instanceof RecipeViewFragment){
                    ((RecipeViewFragment)getTargetFragment()).updateRecipeView();
                    ((RecipeViewFragment)getTargetFragment()).showCommentDeletedMessage();
                }
            }
            else{
                Log.e(CLASS_NAME, "Error ! Could not delete Review");
            }
        }
    }

    class AsyncFetchLikedUsers extends AsyncTask<CommentMO, Void, Object> {
        private Fragment fragment;
        private CommentMO comment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "fetching users who liked the Comment ..");
        }

        @Override
        protected Object doInBackground(CommentMO... objects) {
            comment = objects[0];
            return InternetUtility.fetchLikedUsers("COMMENT", comment.getCOM_ID(), 0);
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), fragment);

            if(object == null){
                return;
            }

            List<UserMO> users = (List<UserMO>) object;

            if(users != null && !users.isEmpty()){
                Object array[] = new Object[]{"LIKE", users};

                Map<String, Object> params = new HashMap<String, Object>();
                params.put(GENERIC_OBJECT, array);
                params.put(SELECTED_ITEM, comment);
                params.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(getFragmentManager(), FRAGMENT_RECIPE_COMMENTS, FRAGMENT_RECIPE_LIKED_USERS, new UsersFragment(), params);
            }
        }
    }
}