package com.cookery.utils;

/**
 * Created by ajit on 6/1/15.
 */
public final class Constants {
    public static final boolean USE_TEST_DATA = false;

    /*server properties*/
    private static final String SLASH = "/";
    private static final String COLON = ":";
    private static final String SERVER_PROTOCOL = "http";
    private static final String SERVER_PORT = "";
    private static final String SERVER_PROJECT_DIRECTORY = "cookery"+SLASH;
    private static final String SERVER_CODEANYWHERE_IP = "cookery_php-dial2vishal53897.codeanyapp.com"+COLON+SERVER_PORT+SLASH;
    private static final String SERVER_DADDY_IP = "192.168.43.173"+COLON+SERVER_PORT+SLASH+SERVER_PROJECT_DIRECTORY+SLASH;

    private static final String SERVER_IP = SERVER_CODEANYWHERE_IP;

    public static final String SERVER_ADDRESS = SERVER_PROTOCOL+COLON+SLASH+SLASH+SERVER_IP;
    public static final String SERVER_CHARSET = "UTF-8";
    public static final int SERVER_TIMEOUT = 5000;  //in milliseconds

    //PHP
    public static final String PHP_FETCH_ALL_FOOD_TYPES = "fetchfoodtype.php";
    public static final String PHP_FETCH_ALL_CUISINES = "fetchcuisine.php";
    public static final String PHP_FETCH_INGREDIENTS = "autocompleteingredient.php";
    public static final String PHP_FETCH_ALL_TASTES = "fetchtastes.php";
    public static final String PHP_FETCH_ALL_QUANTITIES = "fetchqty.php";
    public static final String PHP_FETCH_TRENDING_RECIPES = "fetchtrendingrecipes.php";
    public static final String PHP_FETCH_MASTER_SEARCH = "mastersearch.php";
    public static final String PHP_FETCH_RECIPE = "fetchrecipedetails.php";
    public static final String PHP_FETCH_FAV_RECIPES = "fetchuserfavrecipes.php";
    public static final String PHP_FETCH_VIEWED_RECIPES = "fetchuserviewedrecipes.php";
    public static final String PHP_FETCH_REVIEWED_RECIPES = "fetchuserreviewedrecipes.php";
    public static final String PHP_FETCH_RECIPE_REVIEW = "fetchrecipereview.php";
    public static final String PHP_FETCH_MY_RECIPES = "fetchuserrecipes.php";
    public static final String PHP_SUBMIT_RECIPE = "addrecipe.php";
    public static final String PHP_SUBMIT_RECIPE_COMMENT = "submitrecipecomment.php"; //NOT READY
    public static final String PHP_SUBMIT_RECIPE_REVIEW = "submitrecipereview.php";
    public static final String PHP_SUBMIT_LIKE = "submitlike.php";


    /*Date Time*/
    public static final String DB_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    /*Date Time*/

    //fragment keys
    public static final int GALLERY_CHOICE = 1111;
    public static final int CAMERA_CHOICE = 2222;

    /*Async Task Purpose Keys*/
    public static final String ASYNC_TASK_GET_ALL_CATEGORY_RECIPES = "ASYNC_TASK_GET_ALL_CATEGORY_RECIPES";
    /*Async Task Purpose Keys*/


    //flag value for affirmative/non affirmative
    public static final String AFFIRMATIVE = "Y";
    public static final String NON_AFFIRMATIVE = "N";

    public static final String UI_FONT = "Roboto-Light.ttf";

    //Bundle Keys, Shared Prefs, Intent, fragment names
    //FRAGMENT NAMES
    public static final String FRAGMENT_ADD_RECIPE = "FRAGMENT_ADD_RECIPE";
    public static final String FRAGMENT_MY_FAVORITES = "FRAGMENT_MY_FAVORITES";
    public static final String FRAGMENT_RECIPE = "FRAGMENT_RECIPE";
    public static final String FRAGMENT_RECIPE_COMMENTS = "FRAGMENT_RECIPE_COMMENTS";
    public static final String FRAGMENT_RECIPE_REVIEW = "FRAGMENT_RECIPE_REVIEW";
    public static final String FRAGMENT_RECIPE_REVIEWS = "FRAGMENT_RECIPE_REVIEWS";
    public static final String FRAGMENT_RECIPE_IMAGES = "FRAGMENT_RECIPE_IMAGES";
    public static final String FRAGMENT_COMMON_SELECTION = "FRAGMENT_COMMON_SELECTION";
    public static final String FRAGMENT_COMMON_WAIT = "FRAGMENT_COMMON_WAIT";
    public static final String FRAGMENT_COMMON_MESSAGE = "FRAGMENT_COMMON_MESSAGE";
    public static final String FRAGMENT_PICK_IMAGE = "FRAGMENT_PICK_IMAGE";
    public static final String FRAGMENT_MY_RECIPE = "FRAGMENT_MY_RECIPE";


    //FRAGMENT OBJECT KEYS
    public static final String LOGGED_IN_USER = "LOGGED_IN_USER";
    public static final String MASTER = "MASTER";
    public static final String GENERIC_OBJECT = "GENERIC_OBJECT";
    public static final String LIST_DATA = "LIST_DATA";
    public static final String SELECTED_ITEM = "SELECTED_ITEM";
    public static final String MY_RCPS = "MY_RCPS";

    public static final String TRENDING_RECIPES = "TRENDING RECIPES";
    public static final String TOP_RECIPES_MONTH = "TOP RECIPES OF THE MONTH";
    public static final String TOP_RECIPES_CHEF = "RECIPES FROM OUR TOP CHEF";
    public static final String MY_RECIPES = "MY RECIPES";

    //check master keys
    public static final String CHECK_MASTER_FOR_CATEGORIES = "CHECK_MASTER_FOR_CATEGORIES";
    public static final String CHECK_MASTER_FOR_TENURES = "CHECK_MASTER_FOR_TENURES";
    public static final String CHECK_MASTER_FOR_ALL = "CHECK_MASTER_FOR_ALL";


    //SHARED PREFS KEYS
    public static final String SHARED_PREF_FILE_NAME = "BMB_PREFS";
    public static final String SHARED_PREF_ACTIVE_USER_ID = "ACTIVE_USER_ID";

    //Messages
    public static final String UN_IDENTIFIED_PARENT_FRAGMENT = "Target Fragment hasn't been set before calling the current fragment";
    public static final String UN_IDENTIFIED_OBJECT_TYPE = "Object Type could not be identified for the object : ";
    public static final String UN_IDENTIFIED_VIEW = "Could not identify the view which has been clicked";
    public static final String EMAIL_NOT_VERIFIED = "EMAIL NOT VERIFIED";
    public static final String VERIFICATION_EMAIL_SENT = " VERIFICATION MAIL SENT";

    //Snacks
    public static final String VERIFY_EMAIL = "VERIFY";
    public static final String OK = "OK";
    public static final String SAVED = "Saved";
    public static final String SOMETHING_WENT_WRONG = "Something Went Wrong";

    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_GALLERY_PHOTO = 2;

    //Discount factor
    public static final Double DISCOUNT_FACTOR = 0.15;
}
