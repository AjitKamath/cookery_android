package com.cookery.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.cookery.R;
import com.cookery.adapters.HomeSearchAutoCompleteAdapter;
import com.cookery.component.DelayAutoCompleteTextView;
import com.cookery.fragments.AddMyListFragment;
import com.cookery.fragments.AddRecipeFragment;
import com.cookery.fragments.FavoriteRecipesFragment;
import com.cookery.fragments.MyRecipesFragment;
import com.cookery.fragments.MyReviewsFragment;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.MyListMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.TasteMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.TestData;
import com.cookery.utils.Utility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_MY_FAVORITES;
import static com.cookery.utils.Constants.FRAGMENT_MY_LIST;
import static com.cookery.utils.Constants.FRAGMENT_MY_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_MY_REVIEWS;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.MASTER;
import static com.cookery.utils.Constants.MY_LISTS_EXISTS;
import static com.cookery.utils.Constants.MY_RECIPES;
import static com.cookery.utils.Constants.MY_REVIEWS;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.TOP_RECIPES_CHEF;
import static com.cookery.utils.Constants.TOP_RECIPES_MONTH;
import static com.cookery.utils.Constants.TRENDING_RECIPES;


public abstract class CommonActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    private static final String CLASS_NAME = CommonActivity.class.getName();
    private Context mContext = this;
    private MasterDataMO masterData;
    public UserMO loggedInUser;
    private Object homeContent[] = new Object[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume(){
        super.onResume();

        setupNavigator();
        setupSearch();
        setupFab();
        verifyLoggedInUser();

        fetchContent();
    }

    private void verifyLoggedInUser() {
        loggedInUser = Utility.getUserFromUserSecurity(mContext);
        if(loggedInUser == null || loggedInUser.getUser_id() == 0){
            //TODO: show login/signup screen
            loggedInUser = TestData.getUserTestData();
            Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, loggedInUser);
            verifyLoggedInUser();
        }
    }

    private void setupSearch() {
        HomeSearchAutoCompleteAdapter adapter = new HomeSearchAutoCompleteAdapter(mContext);
        getCommon_header_search_av().setThreshold(2);
        getCommon_header_search_av().setAutoCompleteDelay(1000);
        getCommon_header_search_av().setAdapter(adapter);

        getCommon_header_search_av().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecipeMO recipe = (RecipeMO) view.getTag();

                if(recipe == null){
                    Log.e(CLASS_NAME, "Selected Recipe object is null");
                    return;
                }

                new AsyncTaskerFetchRecipe().execute(recipe);
            }
        });

        getCommon_header_search_av().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(getCommon_header_search_av().getText());

                if(text.trim().isEmpty()){
                    RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(100);
                    rotate.setInterpolator(new LinearInterpolator());
                    getCommon_header_search_iv().startAnimation(rotate);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = String.valueOf(getCommon_header_search_av().getText());

                if(text.trim().isEmpty()){
                    RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(100);
                    rotate.setInterpolator(new LinearInterpolator());
                    getCommon_header_search_iv().startAnimation(rotate);

                    getCommon_header_search_iv().setVisibility(View.GONE);
                }
                else{
                    getCommon_header_search_iv().setVisibility(View.VISIBLE);
                }
            }
        });

        getCommon_header_search_iv().setVisibility(View.GONE);
        getCommon_header_search_iv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCommon_header_search_av().setText("");

                RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(100);
                rotate.setInterpolator(new LinearInterpolator());
                getCommon_header_search_iv().startAnimation(rotate);
            }
        });
    }

    private void setupToolbar() {
        //toolbar
        //getToolbar().setTitle(getResources().getString(R.string.app_name));
        //setSupportActionBar(getToolbar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupNavigator() {
        //drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, getDrawer_layout(), null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, getDrawer_layout(), getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getDrawer_layout().addDrawerListener(toggle);
        //toggle.syncState();


        getCommon_header_navigation_drawer_iv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDrawer_layout().openDrawer(GravityCompat.START);
            }
        });

        //navigation
        getNav_view().setItemIconTintList(null);
        getNav_view().getMenu().getItem(1).setActionView(R.layout.menu_dot);
        getNav_view().setNavigationItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        /*if(R.id.fab_seek_ll == view.getId()){
        }
        else if(R.id.fab_share_ll == view.getId()){
            showFabToolbar(false);
            //showShareFragment(null);
        }
        else{
            Log.e(CLASS_NAME, "Could not identify the view");
            Utility.showSnacks(getWrapper_home_cl(), "Could not identify the view", OK, Snackbar.LENGTH_INDEFINITE);
        }*/
    }

    private void setupFab() {
        getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(masterData == null){
                    new AsyncTaskerFetchMasterData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "FETCH_AND_SHOW_ADD_RECIPE");
                }
                else{
                    showAddRecipeFragment(masterData);
                }
            }
        });
    }

    private void showAddRecipeFragment(MasterDataMO masterData) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(MASTER, masterData);
        paramsMap.put(GENERIC_OBJECT, new RecipeMO());

        Utility.showFragment(getFragmentManager(), null, FRAGMENT_ADD_RECIPE, new AddRecipeFragment(), paramsMap);

        /*String fragmentNameStr = FRAGMENT_ADD_RECIPE;
        String parentFragmentNameStr = null;

        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        Fragment parentFragment = null;
        if(parentFragmentNameStr != null && !parentFragmentNameStr.trim().isEmpty()){
            parentFragment = manager.findFragmentByTag(parentFragmentNameStr);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(MASTER, masterData);

        AddRecipeFragment fragment = new AddRecipeFragment();
        fragment.setArguments(bundle);

        if (parentFragment != null) {
            fragment.setTargetFragment(parentFragment, 0);
        }

        fragment.show(manager, fragmentNameStr);*/
    }

    private void showFavRecipesFragment(Map<String, List<RecipeMO>> favRecipes) {
        String fragmentNameStr = FRAGMENT_MY_FAVORITES;

        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(GENERIC_OBJECT, (Serializable) favRecipes);

        FavoriteRecipesFragment fragment = new FavoriteRecipesFragment();
        fragment.setArguments(bundle);

        fragment.show(manager, fragmentNameStr);
    }

    private void setupMyRecipesFragment(List<RecipeMO> recipes){

        Bundle bundle = new Bundle();
        bundle.putSerializable(MY_RECIPES, (Serializable) recipes);

        MyRecipesFragment fragment = new MyRecipesFragment();
        fragment.setArguments(bundle);


        String fragmentNameStr = FRAGMENT_MY_RECIPE;
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        fragment.show(manager, fragmentNameStr);
    }

    private void setupMyListFragment(List<MyListMO> mylists){
        Bundle bundle = new Bundle();
        boolean listsexits = false;
        if(mylists.size() == 0 || mylists == null)
        {
            // show no list exists yet
            bundle.putSerializable(MY_LISTS_EXISTS, (Serializable) listsexits);
        }
        else
        {
            listsexits = true;
            bundle.putSerializable(MY_LISTS_EXISTS, (Serializable) listsexits);
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(MASTER, masterData);
        paramsMap.put(GENERIC_OBJECT, mylists);

        for (Map.Entry<String, Object> iterMap : paramsMap.entrySet()) {
            bundle.putSerializable(iterMap.getKey(), (Serializable) iterMap.getValue());
        }

        AddMyListFragment fragment = new AddMyListFragment();
        fragment.setArguments(bundle);
        String fragmentNameStr = FRAGMENT_MY_LIST;
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        fragment.show(manager, fragmentNameStr);
    }

    private void setupMyReviewsFragment(List<RecipeMO> reviews){

        Bundle bundle = new Bundle();
        bundle.putSerializable(MY_REVIEWS, (Serializable) reviews);

        MyReviewsFragment fragment = new MyReviewsFragment();
        fragment.setArguments(bundle);


        String fragmentNameStr = FRAGMENT_MY_REVIEWS;
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(fragmentNameStr);

        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        fragment.show(manager, fragmentNameStr);
    }

    private void fetchContent() {
        fetchHomeContent();
        fetchMasterContent();
    }

    public void fetchHomeContent(){
        new AsyncTaskerHomeContent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void fetchTimelineContent(){
        new AsyncTaskerTimelineContent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void fetchMasterContent(){
        new AsyncTaskerFetchMasterData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "JUST_FETCH");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(R.id.activity_home_drawer_my_favorites == item.getItemId()){
            new AsyncTaskerFetchFavRecipes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else if(R.id.activity_home_drawer_my_recipes == item.getItemId()){
            new AsyncTaskerFetchMyRecipes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else if(R.id.activity_home_drawer_my_reviews == item.getItemId()){
            new AsyncTaskerFetchMyReviews().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else if(R.id.navigation_drawer_list == item.getItemId()){
            new AsyncTaskerFetchMyLists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else{
            Utility.showSnacks(getDrawer_layout(), "NOT IMPLEMENTED YET", OK, Snackbar.LENGTH_LONG);
            return true;
        }

        getDrawer_layout().closeDrawer(GravityCompat.START);
        onResume();
        return true;
    }

    protected abstract DrawerLayout getDrawer_layout();

    protected abstract FloatingActionButton getFab();

    protected abstract NavigationView getNav_view();

    protected abstract ImageView getCommon_header_navigation_drawer_iv();

    protected abstract CoordinatorLayout getWrapper_home_cl();

    protected abstract DelayAutoCompleteTextView getCommon_header_search_av();

    protected abstract ImageView getCommon_header_search_iv();

    protected abstract void setUpTabs(Object array[]);



    class AsyncTaskerFetchMasterData extends AsyncTask<String, Void, Object> {
        String whatToDo;
        Fragment fragment;

        @Override
        protected Object doInBackground(String... objects) {
            whatToDo = String.valueOf(objects[0]);

            MasterDataMO masterData = new MasterDataMO();

            masterData.setFoodTypes((List<FoodTypeMO>) InternetUtility.fetchAllFoodTypes());
            masterData.setCuisines((List<CuisineMO>)InternetUtility.fetchAllCuisines());
            masterData.setQuantities((List<QuantityMO>)InternetUtility.fetchAllQuantities());
            masterData.setTastes((List<TasteMO>)InternetUtility.fetchAllTastes());

            return masterData;
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "loading data ..");
        }

        @Override
        protected void onPostExecute(Object object) {
           MasterDataMO temp = (MasterDataMO) object;

            if(temp.getFoodTypes() != null && !temp.getFoodTypes().isEmpty()) {
                if(temp.getCuisines() != null && !temp.getCuisines().isEmpty()){
                    if(temp.getQuantities() != null && !temp.getQuantities().isEmpty()){
                        if(temp.getTastes() != null && !temp.getTastes().isEmpty()){
                            masterData = temp;

                            Utility.closeWaitDialog(getFragmentManager(), fragment);

                            if("FETCH_AND_SHOW_ADD_RECIPE".equalsIgnoreCase(whatToDo)){
                                showAddRecipeFragment(masterData);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<TimelineMO> fetchTimelines(){
        return InternetUtility.getFetchUserTimeline(loggedInUser.getUser_id(), 0);
    }


    class AsyncTaskerFetchMyLists extends AsyncTask<Void, Void, List<MyListMO>> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "Fetching your Lists ..");
        }

        @Override
        protected List<MyListMO> doInBackground(Void... objects) {
            return InternetUtility.fetchUserList(loggedInUser.getUser_id(), 0);
        }

        @Override
        protected void onPostExecute(List<MyListMO> objectList) {
            List<MyListMO> mylists = objectList;
            setupMyListFragment(mylists);
            Utility.closeWaitDialog(getFragmentManager(), fragment);
        }
    }

    class AsyncTaskerFetchRecipe extends AsyncTask<RecipeMO, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "Fetching Recipe ..");
        }

        @Override
        protected Object doInBackground(RecipeMO... objects) {
            List<RecipeMO> recipes = (List<RecipeMO>) InternetUtility.fetchRecipe(objects[0], loggedInUser.getUser_id());

            if(recipes != null && !recipes.isEmpty()){
                recipes.get(0).setComments(InternetUtility.fetchRecipeComments(loggedInUser, recipes.get(0), 0));
                recipes.get(0).setReviews(InternetUtility.fetchRecipeReviews(loggedInUser, recipes.get(0), 0));
                return recipes.get(0);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            if(object == null){
                return;
            }

            RecipeMO recipe = (RecipeMO) object;

            if(recipe != null){
                Utility.showRecipeFragment(getFragmentManager(), recipe);

                Utility.closeWaitDialog(getFragmentManager(), fragment);
            }
        }
    }

    class AsyncTaskerFetchFavRecipes extends AsyncTask<Void, Void, Object> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "Fetching your favorite Recipes ..");
        }

        @Override
        protected Object doInBackground(Void... objects) {
            Map<String, List<RecipeMO>> favRecipes = new HashMap<>();

            favRecipes.put("FAVORITES", (List<RecipeMO>)InternetUtility.fetchFavRecipes(loggedInUser.getUser_id()));
            favRecipes.put("VIEWED", (List<RecipeMO>)InternetUtility.fetchViewedRecipes(loggedInUser.getUser_id()));
            favRecipes.put("REVIEWED", (List<RecipeMO>)InternetUtility.fetchReviewedRecipes(loggedInUser.getUser_id()));

            return favRecipes;
        }

        @Override
        protected void onPostExecute(Object object) {
            Map<String, List<RecipeMO>> favRecipes = (Map<String, List<RecipeMO>>) object;

            if(favRecipes != null && !favRecipes.isEmpty()){
                showFavRecipesFragment(favRecipes);

                Utility.closeWaitDialog(getFragmentManager(), fragment);
            }
        }
    }

    class AsyncTaskerFetchMyRecipes extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.fetchMyRecipes(loggedInUser.getUser_id());
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "Loading My Recipes ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            List<RecipeMO> myRecipes = (List<RecipeMO>) object;

            if(myRecipes != null || !myRecipes.isEmpty()){
                setupMyRecipesFragment((List<RecipeMO>) object);
                Utility.closeWaitDialog(getFragmentManager(), fragment);
            }
        }
    }

    class AsyncTaskerFetchMyReviews extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.fetchMyReviews(loggedInUser.getUser_id());
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "Loading My Reviews ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            List<RecipeMO> myReviews = (List<RecipeMO>) object;

            if(myReviews != null || !myReviews.isEmpty()){
                setupMyReviewsFragment((List<RecipeMO>) object);
                Utility.closeWaitDialog(getFragmentManager(), fragment);
            }
        }
    }

    class AsyncTaskerHomeContent extends AsyncTask<Object, Void, Object> {
        //private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            //fetch timelines
            homeContent[0] = fetchTimelines();

            //fetch trends
            Map<String, List<RecipeMO>> allCategoriesRecipes = new HashMap<>();
            allCategoriesRecipes.put(TRENDING_RECIPES, InternetUtility.fetchTrendingRecipes());
            allCategoriesRecipes.put(TOP_RECIPES_MONTH, InternetUtility.fetchTrendingRecipes());
            allCategoriesRecipes.put(TOP_RECIPES_CHEF, InternetUtility.fetchTrendingRecipes());

            homeContent[1] = allCategoriesRecipes;

            return homeContent;
        }

        @Override
        protected void onPreExecute() {
            //fragment = Utility.showWaitDialog(getFragmentManager(), "loading recipes ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            if(homeContent != null){
                setUpTabs(homeContent);
            }
        }
    }

    class AsyncTaskerTimelineContent extends AsyncTask<Object, Void, Object> {
        //private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            //fetch timelines
            return fetchTimelines();
        }

        @Override
        protected void onPreExecute() {
            //fragment = Utility.showWaitDialog(getFragmentManager(), "loading recipes ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            List<TimelineMO> timelines = (List<TimelineMO>) object;

            if(timelines != null){
                homeContent[0] = timelines;
                setUpTabs(homeContent);
            }
        }
    }
}
