package com.cookery.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.cookery.R;
import com.cookery.adapters.HomeSearchAutoCompleteAdapter;
import com.cookery.component.DelayAutoCompleteTextView;
import com.cookery.fragments.AddMyListFragment;
import com.cookery.fragments.AddRecipeFragment;
import com.cookery.fragments.FavoriteRecipesFragment;
import com.cookery.fragments.LoginFragment;
import com.cookery.fragments.MyRecipesFragment;
import com.cookery.fragments.MyReviewsFragment;
import com.cookery.fragments.PeopleViewFragment;
import com.cookery.fragments.ProfileViewFragment;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.MasterDataMO;
import com.cookery.models.MyListMO;
import com.cookery.models.QuantityMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.TasteMO;
import com.cookery.models.TimelineMO;
import com.cookery.models.UserMO;
import com.cookery.utils.InternetUtility;
import com.cookery.utils.Utility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cookery.utils.Constants.DAYS_UNTIL_PROMPT;
import static com.cookery.utils.Constants.FRAGMENT_ADD_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_LOGIN;
import static com.cookery.utils.Constants.FRAGMENT_MY_FAVORITES;
import static com.cookery.utils.Constants.FRAGMENT_MY_LIST;
import static com.cookery.utils.Constants.FRAGMENT_MY_RECIPE;
import static com.cookery.utils.Constants.FRAGMENT_MY_REVIEWS;
import static com.cookery.utils.Constants.FRAGMENT_PEOPLE_VIEW;
import static com.cookery.utils.Constants.FRAGMENT_PROFILE_VIEW;
import static com.cookery.utils.Constants.GENERIC_OBJECT;
import static com.cookery.utils.Constants.LAUNCHES_UNTIL_PROMPT;
import static com.cookery.utils.Constants.LOGGED_IN_USER;
import static com.cookery.utils.Constants.MASTER;
import static com.cookery.utils.Constants.MY_LISTS_EXISTS;
import static com.cookery.utils.Constants.OK;
import static com.cookery.utils.Constants.TOP_RECIPES_CHEF;
import static com.cookery.utils.Constants.TOP_RECIPES_MONTH;
import static com.cookery.utils.Constants.TRENDING_RECIPES;


public abstract class CommonActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String CLASS_NAME = CommonActivity.class.getName();
    private Context mContext = this;
    private MasterDataMO masterData;
    public UserMO loggedInUser;
    private Object homeContent[] = new Object[3];

    private boolean initialLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialLoad = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupSearch();
        setupFab();

        verifyLoggedInUser();
        setupRateUs(mContext);
    }

    private void verifyLoggedInUser() {
        loggedInUser = Utility.getUserFromUserSecurity(mContext);
        if (loggedInUser == null || loggedInUser.getUSER_ID() == 0) {
            Utility.showFragment(getFragmentManager(), null, FRAGMENT_LOGIN, new LoginFragment(), null);
        } else {
            new AsyncTaskerFetchUser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


    public void setupRateUs(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(mContext.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }
        SharedPreferences.Editor editor = prefs.edit();
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);
        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }
        editor.commit();
    }


    public void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {

        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.rate_us), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                // TODO: URL of App
                //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.cookery")));
                //dismiss();
            }
        }).setNegativeButton(getString(R.string.remind_me_later), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                //dialog.dismiss();
            }
        }).setNeutralButton(getString(R.string.no_thanks), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
             //   dialog.dismiss();
            }
        }).setMessage(R.string.rate_msg).setTitle(R.string.rate_title).create();
        dialog.show();
}


    public void updateLoggedInUser() {
        Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, null);
        verifyLoggedInUser();
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

                if (recipe == null) {
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

                if (text.trim().isEmpty()) {
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

                if (text.trim().isEmpty()) {
                    RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(100);
                    rotate.setInterpolator(new LinearInterpolator());
                    getCommon_header_search_iv().startAnimation(rotate);

                    getCommon_header_search_iv().setVisibility(View.GONE);
                } else {
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

    private void setupNavigator() {
        //drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, getDrawer_layout(), null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, getDrawer_layout(), getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getDrawer_layout().addDrawerListener(toggle);
        //toggle.syncState();

        if (loggedInUser.getIMG() != null && !loggedInUser.getIMG().trim().isEmpty()) {
            Utility.loadImageFromURL(mContext, loggedInUser.getIMG(), (ImageView) getNav_view().findViewById(R.id.navigation_header_iv));
        }

        if (loggedInUser.getNAME() != null && !loggedInUser.getNAME().trim().isEmpty()) {
            ((TextView) getNav_view().findViewById(R.id.navigation_header_name_tv)).setText(loggedInUser.getNAME());
        }

        if (loggedInUser.getEMAIL() != null && !loggedInUser.getEMAIL().trim().isEmpty()) {
            ((TextView) getNav_view().findViewById(R.id.navigation_header_email_tv)).setText(loggedInUser.getEMAIL());
        }

        getNav_view().findViewById(R.id.navigation_header_user_details_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTaskerFetchUserDetails().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

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
                if (masterData == null) {
                    new AsyncTaskerFetchMasterData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "FETCH_AND_SHOW_ADD_RECIPE");
                } else {
                    showAddRecipeFragment(masterData);
                }
            }
        });
    }

    private void logout() {
        Utility.writeIntoUserSecurity(mContext, LOGGED_IN_USER, null);
        LoginFragment dd = new LoginFragment();
        dd.signOut();
        Utility.showFragment(getFragmentManager(), null, FRAGMENT_LOGIN, new LoginFragment(), null);
    }

    private void share(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Cookery");
        String sAux = "\nLet me recommend you this application\n\n";
        // TODO: Need to change the below URL with our App after publish on Play Store (below one is just for test)
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.cookery \n\n";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "choose one"));
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

    private void setupMyListFragment(List<MyListMO> mylists) {
        Bundle bundle = new Bundle();
        boolean listsexits = false;
        if (mylists.size() == 0 || mylists == null) {
            // show no list exists yet
            bundle.putSerializable(MY_LISTS_EXISTS, listsexits);
        } else {
            listsexits = true;
            bundle.putSerializable(MY_LISTS_EXISTS, listsexits);
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

    public void fetchHomeContent(){
        new AsyncTaskerHomeContent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void fetchTimelineContent(){
        new AsyncTaskerTimelineContent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void fetchMasterContent(){
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
        else if(R.id.navigation_drawer_share == item.getItemId()){
            share();
        }
        else if(R.id.navigation_drawer_people == item.getItemId()){
            new AsyncTaskerFetchPeople().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else if(R.id.navigation_drawer_logout == item.getItemId()){
                logout();
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
            Utility.closeWaitDialog(getFragmentManager(), fragment);

            MasterDataMO temp = (MasterDataMO) object;

            if(temp.getFoodTypes() != null && !temp.getFoodTypes().isEmpty()) {
                if(temp.getCuisines() != null && !temp.getCuisines().isEmpty()){
                    if(temp.getQuantities() != null && !temp.getQuantities().isEmpty()){
                        if(temp.getTastes() != null && !temp.getTastes().isEmpty()){
                            masterData = temp;

                            if("FETCH_AND_SHOW_ADD_RECIPE".equalsIgnoreCase(whatToDo)){
                                Map<String, Object> paramsMap = new HashMap<>();
                                paramsMap.put(MASTER, masterData);
                                paramsMap.put(GENERIC_OBJECT, new RecipeMO());
                                Utility.showFragment(getFragmentManager(), null, FRAGMENT_ADD_RECIPE, new AddRecipeFragment(), paramsMap);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<TimelineMO> fetchTimelines(){
        if(loggedInUser != null && loggedInUser.getUSER_ID() != 0){
            return InternetUtility.getFetchUserTimeline(loggedInUser.getUSER_ID(), 0);
        }

        return null;
    }

    private List<TimelineMO> fetchStories(){
        if(loggedInUser != null && loggedInUser.getUSER_ID() != 0){
            return InternetUtility.getFetchUserStories(loggedInUser.getUSER_ID(), 0);
        }

        return null;
    }

    class AsyncTaskerFetchMyLists extends AsyncTask<Void, Void, List<MyListMO>> {
        private Fragment fragment;

        @Override
        protected void onPreExecute(){
            fragment = Utility.showWaitDialog(getFragmentManager(), "Fetching your Lists ..");
        }

        @Override
        protected List<MyListMO> doInBackground(Void... objects) {
            //return InternetUtility.fetchUserList(loggedInUser.getUSER_ID(), 0);
            return InternetUtility.fetchUserList(loggedInUser.getUSER_ID());
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
            List<RecipeMO> recipes = (List<RecipeMO>) InternetUtility.fetchRecipe(objects[0], loggedInUser.getUSER_ID());

            if(recipes != null && !recipes.isEmpty()){
                //TODO: avoid setting mylist into recipes object. it should be independent. my list has nothing to do with recipe
                recipes.get(0).setMylists(InternetUtility.fetchUserList(loggedInUser.getUSER_ID()));
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

            favRecipes.put("FAVORITES", (List<RecipeMO>)InternetUtility.fetchFavRecipes(loggedInUser.getUSER_ID()));
            favRecipes.put("VIEWED", (List<RecipeMO>)InternetUtility.fetchViewedRecipes(loggedInUser.getUSER_ID()));
            favRecipes.put("REVIEWED", (List<RecipeMO>)InternetUtility.fetchReviewedRecipes(loggedInUser.getUSER_ID()));

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
            return InternetUtility.fetchMyRecipes(loggedInUser.getUSER_ID(), 0);
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "Loading My Recipes ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), fragment);

            List<RecipeMO> myRecipes = (List<RecipeMO>) object;
            if(myRecipes != null || !myRecipes.isEmpty()){
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, myRecipes);
                paramsMap.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(getFragmentManager(), null, FRAGMENT_MY_RECIPE, new MyRecipesFragment(), paramsMap);
            }
        }
    }

    class AsyncTaskerFetchMyReviews extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.fetchMyReviews(loggedInUser.getUSER_ID(), 0);
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "Loading My Reviews ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), fragment);

            List<ReviewMO> myReviews = (List<ReviewMO>) object;
            if(myReviews != null && !myReviews.isEmpty()){
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(GENERIC_OBJECT, myReviews);
                paramsMap.put(LOGGED_IN_USER, loggedInUser);

                Utility.showFragment(getFragmentManager(), null, FRAGMENT_MY_REVIEWS, new MyReviewsFragment(), paramsMap);
            }
        }
    }

    class AsyncTaskerHomeContent extends AsyncTask<Object, Void, Object> {
        //private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            //fetch stories
            homeContent[0] = fetchStories();

            //fetch trends
            Map<String, List<RecipeMO>> allCategoriesRecipes = new HashMap<>();
            allCategoriesRecipes.put(TRENDING_RECIPES, InternetUtility.fetchTrendingRecipes());
            allCategoriesRecipes.put(TOP_RECIPES_MONTH, InternetUtility.fetchTrendingRecipes());
            allCategoriesRecipes.put(TOP_RECIPES_CHEF, InternetUtility.fetchTrendingRecipes());
            homeContent[1] = allCategoriesRecipes;

            //fetch timelines
            homeContent[2] = fetchTimelines();

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
                homeContent[2] = timelines;
                setUpTabs(homeContent);
            }
        }
    }

    class AsyncTaskerFetchUser extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.fetchUser(loggedInUser.getUSER_ID());
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "fetching your data ..");
        }

        @Override
        protected void onPostExecute(Object object) {
            Utility.closeWaitDialog(getFragmentManager(), fragment);

            List<UserMO> user = (List<UserMO>) object;

            if(user != null && !user.isEmpty()) {
                loggedInUser = user.get(0);
                setupNavigator();

                if(initialLoad){
                    fetchHomeContent();
                    fetchMasterContent();
                    initialLoad = false;
                }
            }
            else{
                Utility.showFragment(getFragmentManager(), null, FRAGMENT_LOGIN, new LoginFragment(), null);
            }
        }
    }

    class AsyncTaskerFetchUserDetails extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            return InternetUtility.fetchUser(loggedInUser.getUSER_ID());
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "fetching profile..");
        }

        @Override
        protected void onPostExecute(Object object) {
            List<UserMO> user = (List<UserMO>) object;

            if(user != null && !user.isEmpty()) {
                loggedInUser = user.get(0);
                Utility.closeWaitDialog(getFragmentManager(), fragment);

                Map<String, Object> params = new HashMap<>();
                params.put(GENERIC_OBJECT, loggedInUser);
                Utility.showFragment(getFragmentManager(), null, FRAGMENT_PROFILE_VIEW, new ProfileViewFragment(), params);
            }
        }
    }

    class AsyncTaskerFetchPeople extends AsyncTask<Object, Void, Object> {
        private Fragment fragment;

        @Override
        protected Object doInBackground(Object... objects) {
            Object[] people = new Object[2];

            people[0] = InternetUtility.fetchUserFollowers(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), 0);
            people[1] = InternetUtility.fetchUserFollowings(loggedInUser.getUSER_ID(), loggedInUser.getUSER_ID(), 0);

            return people;
        }

        @Override
        protected void onPreExecute() {
            fragment = Utility.showWaitDialog(getFragmentManager(), "fetching people who follow you and whom you follow..");
        }

        @Override
        protected void onPostExecute(Object object) {
            Object[] people = (Object[]) object;

            if(people != null && people.length != 0) {
                Utility.closeWaitDialog(getFragmentManager(), fragment);

                Map<String, Object> params = new HashMap<>();
                params.put(GENERIC_OBJECT, people);
                Utility.showFragment(getFragmentManager(), null, FRAGMENT_PEOPLE_VIEW, new PeopleViewFragment(), params);
            }
        }
    }
}
