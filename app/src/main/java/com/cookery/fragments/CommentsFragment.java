package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.RecipeCommentsRecyclerViewAdapter;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.CommentMO;
import com.cookery.models.LikesMO;
import com.cookery.models.RecipeImageMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.UserMO;
import com.cookery.utils.AsyncTaskUtility;
import com.cookery.utils.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.FRAGMENT_COMMENTS;
import static com.cookery.utils.Constants.FRAGMENT_COMMENT_DELETE;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.GENERIC_OBJECT2;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

/**
 * Created by ajit on 21/3/16.
 */
public class CommentsFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;
    private FragmentActivity activity;

    //components
    @InjectView(R.id.common_comments_ll)
    LinearLayout common_comments_ll;

    @InjectView(R.id.common_comments_recipe_ll)
    LinearLayout common_comments_recipe_ll;

    @InjectView(R.id.common_comments_recipe_image_iv)
    ImageView common_comments_recipe_image_iv;

    @InjectView(R.id.common_comments_recipe_name_tv)
    TextView common_comments_recipe_name_tv;

    @InjectView(R.id.common_comments_user_ll)
    LinearLayout common_comments_user_ll;

    @InjectView(R.id.common_comments_user_image_iv)
    CircleImageView common_comments_user_image_iv;

    @InjectView(R.id.common_comments_user_name_tv)
    TextView common_comments_user_name_tv;

    @InjectView(R.id.common_comments_srl)
    SwipeRefreshLayout common_comments_srl;

    @InjectView(R.id.common_comments_rv)
    RecyclerView common_comments_rv;

    @InjectView(R.id.common_comments_comment_et)
    EditText common_comments_comment_et;

    @InjectView(R.id.common_comments_comment_iv)
    ImageView common_comments_comment_iv;
    //end of components

    private CommentMO comment;
    private List<CommentMO> comments;
    private Object objectOfInterest;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_comments, container);
        ButterKnife.inject(this, view);

        Dialog d = getDialog();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDataFromBundle();

        setupPage();

        setFont(common_comments_ll);

        return view;
    }

    private void getDataFromBundle() {
        comments = (List<CommentMO>) getArguments().get(GENERIC_OBJECT);
        comment = (CommentMO) getArguments().get(GENERIC_OBJECT2);
        objectOfInterest = getArguments().get(SELECTED_ITEM);
        loggedInUser = (UserMO) getArguments().get(LOGGED_IN_USER);
    }

    private void setupPage() {
        setupImage();
        setupComments();
    }

    private void setupImage() {
        common_comments_recipe_ll.setVisibility(View.GONE);
        common_comments_user_ll.setVisibility(View.GONE);

        if (objectOfInterest instanceof RecipeMO) {
            common_comments_recipe_ll.setVisibility(View.VISIBLE);

            RecipeMO recipe = (RecipeMO) objectOfInterest;
            Utility.loadImageFromURL(mContext, recipe.getImages().get(0).getRCP_IMG(), common_comments_recipe_image_iv);
            common_comments_recipe_name_tv.setText(recipe.getRCP_NAME());
        } else if (objectOfInterest instanceof UserMO) {
            common_comments_user_ll.setVisibility(View.VISIBLE);

            UserMO user = (UserMO) objectOfInterest;
            Utility.loadImageFromURL(mContext, user.getIMG(), common_comments_user_image_iv);
            common_comments_user_name_tv.setText(user.getNAME());
        } else if (objectOfInterest instanceof RecipeImageMO) {
            common_comments_recipe_ll.setVisibility(View.VISIBLE);

            RecipeImageMO image = (RecipeImageMO) objectOfInterest;
            Utility.loadImageFromURL(mContext, image.getRCP_IMG(), common_comments_recipe_image_iv);
            common_comments_recipe_name_tv.setText(image.getRecipeName());
        } else {
            Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + objectOfInterest);
        }
    }

    private void setupComments() {
        final RecipeCommentsRecyclerViewAdapter adapter = new RecipeCommentsRecyclerViewAdapter(mContext, loggedInUser, comments, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.recipe_comments_item_delete_iv) {
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put(SELECTED_ITEM, view.getTag());
                    paramsMap.put(LOGGED_IN_USER, loggedInUser);

                    Utility.showFragment(getFragmentManager(), FRAGMENT_COMMENTS, FRAGMENT_COMMENT_DELETE, new DeleteCommentFragment(), paramsMap);
                } else if (view.getId() == R.id.common_like_view_ll) {
                    CommentMO comment = (CommentMO) view.getTag();

                    Utility.addRemoveLike(view);

                    LikesMO like = new LikesMO();
                    like.setUSER_ID(loggedInUser.getUSER_ID());
                    like.setTYPE("COMMENT");
                    like.setTYPE_ID(comment.getCOM_ID());

                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                            AsyncTaskUtility.Purpose.SUBMIT_LIKE, loggedInUser, 0)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, like);
                } else {
                    Log.e(CLASS_NAME, "Click action not implemented for this view");
                }
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (view.getId() == R.id.common_like_view_ll) {
                    CommentMO comment = (CommentMO) view.getTag();

                    LikesMO like = new LikesMO();
                    like.setUSER_ID(loggedInUser.getUSER_ID());
                    like.setTYPE("COMMENT");
                    like.setTYPE_ID(comment.getCOM_ID());

                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                            AsyncTaskUtility.Purpose.FETCH_USERS, loggedInUser, 0)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "LIKE", like, comment);
                }
                return true;
            }
        });
        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                if(!adapter.stopOnBottomListener){
                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                            AsyncTaskUtility.Purpose.FETCH_COMMENTS, loggedInUser, comments.size())
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectOfInterest, comment);
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        common_comments_rv.setLayoutManager(mLayoutManager);
        common_comments_rv.setItemAnimator(new DefaultItemAnimator());
        common_comments_rv.setAdapter(adapter);

        common_comments_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                        AsyncTaskUtility.Purpose.FETCH_COMMENTS, loggedInUser, 0)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectOfInterest, comment);
            }
        });

        common_comments_comment_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = String.valueOf(common_comments_comment_et.getText());

                if (temp != null && !temp.trim().isEmpty()) {
                    CommentMO comment = new CommentMO();
                    comment.setUSER_ID(loggedInUser.getUSER_ID());
                    comment.setCOMMENT(temp);

                    if (objectOfInterest instanceof RecipeMO) {
                        comment.setTYPE("RECIPE");
                        comment.setTYPE_ID(((RecipeMO) objectOfInterest).getRCP_ID());
                    } else if (objectOfInterest instanceof RecipeImageMO) {
                        comment.setTYPE("RECIPE_IMG");
                        comment.setTYPE_ID(((RecipeImageMO) objectOfInterest).getRCP_IMG_ID());
                    }
                    else if (objectOfInterest instanceof UserMO) {
                        comment.setTYPE("USER");
                        comment.setTYPE_ID(((UserMO) objectOfInterest).getUSER_ID());
                    }
                    else {
                        Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + objectOfInterest);
                        return;
                    }

                    common_comments_comment_et.setText("");
                    Utility.hideSoftKeyboard(getActivity(), common_comments_comment_et);

                    new AsyncTaskUtility(getFragmentManager(), FRAGMENT_COMMENTS,
                            AsyncTaskUtility.Purpose.SUBMIT_COMMENT, loggedInUser, 0)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, comment);
                }
            }
        });
    }

    public void disableOnBottomListener() {
        ((RecipeCommentsRecyclerViewAdapter) common_comments_rv.getAdapter()).stopOnBottomListener = true;
    }

    public void updateComments(List<CommentMO> comments, int index) {
        setupComments();

        ((RecipeCommentsRecyclerViewAdapter) common_comments_rv.getAdapter()).updateComments(comments, index);
        common_comments_srl.setRefreshing(false);

        if(index != 0){
            common_comments_rv.smoothScrollToPosition(comments.size());
        }
    }

    public void deleteComment(CommentMO comment) {
        comments.remove(comment);
        setupComments();
    }

    public void addComment(CommentMO comment) {
        comments.add(comment);
        setupComments();
        common_comments_rv.smoothScrollToPosition(comments.size());
    }

    // Empty constructor required for DialogFragment
    public CommentsFragment() {
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