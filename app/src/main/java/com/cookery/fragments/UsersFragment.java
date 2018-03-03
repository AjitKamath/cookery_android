package com.cookery.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.UsersRecyclerViewAdapter;
import com.cookery.interfaces.ItemClickListener;
import com.cookery.interfaces.OnBottomReachedListener;
import com.cookery.models.CommentMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.UserMO;
import com.cookery.utils.DateTimeUtility;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.SELECTED_ITEM;
import static com.cookery.utils.Constants.UI_FONT;
import static com.cookery.utils.Constants.UN_IDENTIFIED_OBJECT_TYPE;

/**
 * Created by vishal on 27/9/17.
 */
public class UsersFragment extends DialogFragment {
    private final String CLASS_NAME = this.getClass().getName();
    private Context mContext;

    /*components*/
    @InjectView(R.id.users_rl)
    LinearLayout users_rl;

    //user
    @InjectView(R.id.users_type_user_ll)
    LinearLayout users_type_user_ll;

    @InjectView(R.id.users_type_user_iv)
    CircleImageView users_type_user_iv;

    @InjectView(R.id.users_type_user_name_tv)
    TextView users_type_user_name_tv;
    //user

    //recipe
    @InjectView(R.id.users_type_recipe_ll)
    LinearLayout users_type_recipe_ll;

    @InjectView(R.id.users_type_recipe_iv)
    ImageView users_type_recipe_iv;

    @InjectView(R.id.users_type_recipe_name_tv)
    TextView users_type_recipe_name_tv;

    @InjectView(R.id.users_type_recipe_type_tv)
    TextView users_type_recipe_type_tv;

    @InjectView(R.id.users_type_recipe_cuisine_tv)
    TextView users_type_recipe_cuisine_tv;
    //recipe

    //comment
    @InjectView(R.id.users_type_comment_cv)
    CardView users_type_comment_cv;

    @InjectView(R.id.users_type_comment_user_iv)
    CircleImageView users_type_comment_user_iv;

    @InjectView(R.id.users_type_comment_user_tv)
    TextView users_type_comment_user_tv;

    @InjectView(R.id.users_type_comment_tv)
    TextView users_type_comment_tv;

    @InjectView(R.id.users_type_comment_likes_iv)
    ImageView users_type_comment_likes_iv;

    @InjectView(R.id.users_type_comment_likes_tv)
    TextView users_type_comment_likes_tv;

    @InjectView(R.id.users_type_comment_date_tv)
    TextView users_type_comment_date_tv;
    //comment

    //review
    @InjectView(R.id.users_type_review_cv)
    CardView users_type_review_cv;

    @InjectView(R.id.users_type_review_user_iv)
    CircleImageView users_type_review_user_iv;

    @InjectView(R.id.users_type_review_user_tv)
    TextView users_type_review_user_tv;

    @InjectView(R.id.users_type_review_review_tv)
    TextView users_type_review_review_tv;

    @InjectView(R.id.users_type_review_likes_iv)
    ImageView users_type_review_likes_iv;

    @InjectView(R.id.users_type_review_likes_tv)
    TextView users_type_review_likes_tv;

    @InjectView(R.id.users_type_review_item_star_1_iv)
    ImageView users_type_review_item_star_1_iv;

    @InjectView(R.id.users_type_review_item_star_2_iv)
    ImageView users_type_review_item_star_2_iv;

    @InjectView(R.id.users_type_review_item_star_3_iv)
    ImageView users_type_review_item_star_3_iv;

    @InjectView(R.id.users_type_review_item_star_4_iv)
    ImageView users_type_review_item_star_4_iv;

    @InjectView(R.id.users_type_review_item_star_5_iv)
    ImageView users_type_review_item_star_5_iv;

    @InjectView(R.id.users_type_review_date_tv)
    TextView users_type_review_date_tv;
    //review

    @InjectView(R.id.users_type_user_rank_tv)
    TextView users_type_user_rank_tv;

    @InjectView(R.id.users_tv)
    TextView users_tv;

    @InjectView(R.id.users_srl)
    SwipeRefreshLayout users_srl;

    @InjectView(R.id.users_rv)
    RecyclerView users_rv;
    /*components*/

    private List<UserMO> usersList;
    private String purpose;
    private Object objectOfInterest;
    private UserMO loggedInUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.users, container);
        ButterKnife.inject(this, view);

        getDataFromBundle();

        setupPage();

        setFont(users_rl);

        return view;
    }

    private void getDataFromBundle() {
        Object array[] = (Object[])getArguments().get(GENERIC_OBJECT);
        purpose = String.valueOf(array[0]);
        usersList = (List<UserMO>) array[1];
        objectOfInterest = getArguments().get(SELECTED_ITEM);
        loggedInUser = (UserMO) getArguments().get(LOGGED_IN_USER);
    }

    private void hideAllTypeViews(){
        List<ViewGroup> views = new ArrayList<>();
        views.add(users_type_user_ll);
        views.add(users_type_recipe_ll);
        views.add(users_type_comment_cv);
        views.add(users_type_review_cv);

        for(ViewGroup view : views){
            view.setVisibility(View.GONE);
        }
    }

    private void setupPage() {
        hideAllTypeViews();

        if(objectOfInterest instanceof RecipeMO){
            setupSelectedRecipe((RecipeMO) objectOfInterest);

            if("LIKE".equalsIgnoreCase(purpose)){
                users_tv.setText(((RecipeMO) objectOfInterest).getLikesCount()+" LIKED THE RECIPE "+((RecipeMO)objectOfInterest).getRCP_NAME());
            }
            else if("VIEW".equalsIgnoreCase(purpose)){
                users_tv.setText(((RecipeMO) objectOfInterest).getViewsCount()+" VIEWED THE RECIPE "+((RecipeMO)objectOfInterest).getRCP_NAME());
            }
            else{
                Log.e(CLASS_NAME, "Unimplemented purpose("+purpose+") for a recipe");
            }
        }
        else if(objectOfInterest instanceof CommentMO){
            setupSelectedComment((CommentMO) objectOfInterest);

            if("LIKE".equalsIgnoreCase(purpose)){
                CommentMO comment = (CommentMO)objectOfInterest;
                users_tv.setText(((CommentMO) objectOfInterest).getLikesCount()+" LIKED "+Utility.getUserNameOrYour(comment.getUserName(), comment.getUSER_ID(), loggedInUser.getUSER_ID())+" COMMENT");
            }
            else{
                Log.e(CLASS_NAME, "Unimplemented purpose("+purpose+") for a comment");
            }
        }
        else if(objectOfInterest instanceof ReviewMO){
            setupSelectedReview((ReviewMO) objectOfInterest);

            if("LIKE".equalsIgnoreCase(purpose)){
                ReviewMO review = (ReviewMO) objectOfInterest;
                users_tv.setText(((RecipeMO) objectOfInterest).getLikesCount()+" LIKED "+Utility.getUserNameOrYour(review.getUserName(), review.getUSER_ID(), loggedInUser.getUSER_ID())+" REVIEW");
            }
            else{
                Log.e(CLASS_NAME, "Unimplemented purpose("+purpose+") for a review");
            }
        }
        else if(objectOfInterest instanceof UserMO){
            UserMO user = (UserMO) objectOfInterest;
            setupSelectedUser((UserMO)objectOfInterest);

            if("FOLLOWERS".equalsIgnoreCase(purpose)){
                users_tv.setText(((UserMO) objectOfInterest).getFollowersCount()+" PEOPLE FOLLOW "+Utility.getUserNameOrYou(user.getNAME(), user.getUSER_ID(), loggedInUser.getUSER_ID()));
            }
            else if("FOLLOWINGS".equalsIgnoreCase(purpose)){
                users_tv.setText(((UserMO) objectOfInterest).getFollowingCount()+" PEOPLE ARE FOLLOWED BY "+Utility.getUserNameOrYou(user.getNAME(), user.getUSER_ID(), loggedInUser.getUSER_ID()));
            }
            else{
                Log.e(CLASS_NAME, "Unimplemented purpose("+purpose+") for a user");
            }
        }
        else{
            Log.e(CLASS_NAME, "Unimplemented Object of interest : "+objectOfInterest);
            return;
        }

        if(usersList != null){
            final UsersRecyclerViewAdapter adapter = new UsersRecyclerViewAdapter(mContext, usersList, purpose, new ItemClickListener() {
                @Override
                public void onItemClick(Object item) {

                }
            });
            adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position) {
                    new AsyncFetchUsers(adapter.getItemCount()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            users_rv.setLayoutManager(mLayoutManager);
            users_rv.setItemAnimator(new DefaultItemAnimator());
            users_rv.setAdapter(adapter);

            users_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new AsyncFetchUsers(0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
        }
    }

    private void updateUsers(int index, List<UserMO> users){
        ((UsersRecyclerViewAdapter)users_rv.getAdapter()).updateUsers(index, users);
        users_srl.setRefreshing(false);
    }

    private void setupSelectedReview(ReviewMO review) {
        users_type_review_cv.setVisibility(View.VISIBLE);

        Utility.loadImageFromURL(mContext, review.getUserImage(), users_type_review_user_iv);
        users_type_review_user_tv.setText(review.getUserName());
        users_type_review_review_tv.setText(review.getREVIEW());
        users_type_review_likes_iv.setImageResource(Utility.getLikeImageId(review.isUserLiked()));
        users_type_review_likes_tv.setText(Utility.getSmartNumber(review.getLikedUsers() == null ? 0 : review.getLikedUsers().size()));
        users_type_review_date_tv.setText(DateTimeUtility.getCreateOrModifiedTime(review.getCREATE_DTM(), review.getMOD_DTM()));

        selectStars(review.getRATING());
    }

    private void selectStars(int count) {
        List<ImageView> stars = new ArrayList<>();
        stars.add(users_type_review_item_star_1_iv);
        stars.add(users_type_review_item_star_2_iv);
        stars.add(users_type_review_item_star_3_iv);
        stars.add(users_type_review_item_star_4_iv);
        stars.add(users_type_review_item_star_5_iv);

        for(int i=0; i<stars.size(); i++){
            if(i < count){
                stars.get(i).setImageResource(R.drawable.star);
            }
            else{
                stars.get(i).setImageResource(R.drawable.star_unselected);
            }
        }
    }

    private void setupSelectedComment(CommentMO comment) {
        users_type_comment_cv.setVisibility(View.VISIBLE);

        Utility.loadImageFromURL(mContext, comment.getUserImage(), users_type_comment_user_iv);
        users_type_comment_user_tv.setText(comment.getUserName());
        users_type_comment_tv.setText(comment.getCOMMENT());
        users_type_comment_likes_iv.setBackgroundResource(Utility.getLikeImageId(comment.isUserLiked()));
        users_type_comment_likes_tv.setText(Utility.getSmartNumber(comment.getLikedUsers() == null ? 0 : comment.getLikedUsers().size()));
        users_type_comment_date_tv.setText(DateTimeUtility.getSmartDate(comment.getCREATE_DTM()));
    }

    private void setupSelectedUser(UserMO user) {
        users_type_user_ll.setVisibility(View.VISIBLE);

        Utility.loadImageFromURL(mContext, user.getIMG(), users_type_user_iv);
        users_type_user_name_tv.setText(user.getNAME());
        users_type_user_rank_tv.setText(user.getCurrentRank());
    }

    public void setupSelectedRecipe(RecipeMO recipe) {
        users_type_recipe_ll.setVisibility(View.VISIBLE);

        if(recipe.getImages() != null && !recipe.getImages().isEmpty()){
            Utility.loadImageFromURL(mContext, recipe.getImages().get(0), users_type_recipe_iv);
        }

        users_type_recipe_name_tv.setText(recipe.getRCP_NAME());
        users_type_recipe_type_tv.setText(recipe.getFoodTypeName());
        users_type_recipe_cuisine_tv.setText(recipe.getFoodCuisineName());
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

    class AsyncFetchUsers extends AsyncTask<Void, Void, Object> {
        private int index;

        public AsyncFetchUsers(int index){
            this.index = index;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected Object doInBackground(Void... objects) {
            if ("LIKE".equalsIgnoreCase(purpose)) {
                if(objectOfInterest instanceof RecipeMO){
                    return InternetUtility.fetchLikedUsers("RECIPE", ((RecipeMO)objectOfInterest).getRCP_ID(), index);
                }
                else if(objectOfInterest instanceof CommentMO){
                    return InternetUtility.fetchLikedUsers("COMMENT", ((CommentMO)objectOfInterest).getCOM_ID(), index);
                }
                else if (objectOfInterest instanceof ReviewMO){
                    return InternetUtility.fetchLikedUsers("REVIEW", ((ReviewMO)objectOfInterest).getREV_ID(), index);
                }
                else{
                    Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE+objectOfInterest);
                }
            } else if ("VIEW".equalsIgnoreCase(purpose)) {
                return InternetUtility.fetchViewedUsers((RecipeMO)objectOfInterest, index);
            }
            else if ("FOLLOWERS".equalsIgnoreCase(purpose)) {
                return InternetUtility.fetchUserFollowers(((UserMO)objectOfInterest).getUSER_ID(), loggedInUser.getUSER_ID(), index);
            }
            else if ("FOLLOWINGS".equalsIgnoreCase(purpose)) {
                return InternetUtility.fetchUserFollowings(((UserMO)objectOfInterest).getUSER_ID(), loggedInUser.getUSER_ID(), index);
            }
            else {
                Log.e(CLASS_NAME, UN_IDENTIFIED_OBJECT_TYPE + purpose);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            if (object == null) {
                return;
            }

            List<UserMO> users = (List<UserMO>) object;

            if (users != null) {
                updateUsers(index, users);
            }
        }
    }
}
