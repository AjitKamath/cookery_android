package com.cookery.utils;

/**
 * Created by ajit on 6/1/15.
 */
public final class Constants {
    /*server properties*/
    private static final String SLASH = "/";
    private static final String COLON = ":";
    private static final String SERVER_PROTOCOL = "http";
    private static final String SERVER_PORT = "";

    private static final String SERVER_CODE_ANYWHERE_ADDRESS_AJIT = "www.finappl.com";
    private static final String SERVER_CODE_ANYWHERE_ADDRESS_VISHAL = "cookery_php-dial2vishal53897.codeanyapp.com";
    private static final String SERVER_CODE_ANYWHERE_ADDRESS_ACTIVE = SERVER_CODE_ANYWHERE_ADDRESS_AJIT;

    private static final String SERVER_CODEANYWHERE_PUBLIC_DIR = "public";

    public static final String SERVER_ADDRESS = SERVER_PROTOCOL+COLON+SLASH+SLASH+SERVER_CODE_ANYWHERE_ADDRESS_ACTIVE+SLASH;
    public static final String SERVER_ADDRESS_PUBLIC = SERVER_ADDRESS+SERVER_CODEANYWHERE_PUBLIC_DIR+SLASH;

    public static final String SERVER_CHARSET = "UTF-8";
    public static final int SERVER_TIMEOUT = 5000;  //in milliseconds

    //PHP
    public static final String PHP_CONTROLLER = "Controller.php";
    public static final String PHP_SERVICE_URL = SERVER_ADDRESS_PUBLIC + PHP_CONTROLLER;

    /*PHP function keys*/
    public static final String PHP_FUNCTION_KEY = "function_key";
    public static final String PHP_FUNCTION_KEY_RECIPE_FETCH = "RECIPE_FETCH";
    public static final String PHP_FUNCTION_KEY_RECIPE_FAVORITE_FETCH = "RECIPE_FAVORITE_FETCH";
    public static final String PHP_FUNCTION_KEY_RECIPE_USER_VIEWED_FETCH = "RECIPE_USER_VIEWED_FETCH";
    public static final String PHP_FUNCTION_KEY_RECIPE_REVIEW_FETCH = "RECIPE_REVIEW_FETCH";
    public static final String PHP_FUNCTION_KEY_INGREDIENT_FETCH = "INGREDIENT_FETCH";
    public static final String PHP_FUNCTION_KEY_RECIPE_SEARCH = "RECIPE_SEARCH";
    public static final String PHP_FUNCTION_KEY_RECIPE_SUBMIT = "RECIPE_SUBMIT";
    public static final String PHP_FUNCTION_KEY_COMMENT_SUBMIT = "COMMENT_SUBMIT";
    public static final String PHP_FUNCTION_KEY_REVIEW_SUBMIT = "REVIEW_SUBMIT";
    public static final String PHP_FUNCTION_KEY_REVIEW_DELETE = "REVIEW_DELETE";
    public static final String PHP_FUNCTION_KEY_COMMENT_DELETE = "COMMENT_DELETE";
    public static final String PHP_FUNCTION_KEY_REVIEW_RECIPE = "REVIEW_RECIPE_FETCH";
    public static final String PHP_FUNCTION_KEY_MYLIST_VIEW = "PHP_FUNCTION_KEY_MYLIST_VIEW";
    public static final String PHP_FUNCTION_KEY_LIKE_SUBMIT = "LIKE_SUBMIT";
    public static final String PHP_FUNCTION_KEY_FAV_SUBMIT = "FAV_SUBMIT";
    public static final String PHP_FUNCTION_KEY_COMMENT_FETCH_ALL = "COMMENT_FETCH_ALL";
    public static final String PHP_FUNCTION_KEY_LIKE_FETCH_USERS = "LIKE_FETCH_USERS";
    public static final String PHP_FUNCTION_KEY_VIEW_FETCH_USERS = "VIEW_FETCH_USERS";
    public static final String PHP_FUNCTION_KEY_RECIPE_USER_FETCH = "RECIPE_USER_FETCH";
    public static final String PHP_FUNCTION_KEY_REVIEW_USER_FETCH = "REVIEW_USER_FETCH";
    public static final String PHP_FUNCTION_KEY_REVIEW_USER_FETCH_ALL = "REVIEW_USER_FETCH_ALL";
    public static final String PHP_FUNCTION_KEY_TIMELINE_USER_FETCH = "TIMELINE_USER_FETCH";
    public static final String PHP_FUNCTION_KEY_STORY_USER_FETCH = "STORY_USER_FETCH";
    public static final String PHP_FUNCTION_KEY_USER_REGISTER = "USER_REGISTER";
    public static final String PHP_FUNCTION_KEY_USER_REGISTER_FIRST_CHECK = "USER_REGISTER_CHECK";
    public static final String PHP_FUNCTION_KEY_USER_LOGIN = "USER_LOGIN";
    public static final String PHP_FUNCTION_KEY_USER_FETCH_SELF = "USER_FETCH_SELF";
    public static final String PHP_FUNCTION_KEY_USER_FETCH_PUBLIC = "USER_FETCH_PUBLIC";
    public static final String PHP_FUNCTION_KEY_USER_FOLLOW_SUBMIT = "USER_FOLLOW_SUBMIT";
    public static final String PHP_FUNCTION_KEY_USER_UPDATE_NAME = "USER_UPDATE_NAME";
    public static final String PHP_FUNCTION_KEY_USER_UPDATE_EMAIL = "USER_UPDATE_EMAIL";
    public static final String PHP_FUNCTION_KEY_USER_UPDATE_PASSWORD = "USER_UPDATE_PASSWORD";
    public static final String PHP_FUNCTION_KEY_USER_UPDATE_PHONE = "USER_UPDATE_PHONE";
    public static final String PHP_FUNCTION_KEY_USER_UPDATE_GENDER = "USER_UPDATE_GENDER";
    public static final String PHP_FUNCTION_KEY_USER_UPDATE_IMAGE = "USER_UPDATE_IMAGE";
    public static final String PHP_FUNCTION_KEY_USER_FOLLOWERS_FETCH = "USER_FOLLOWERS_FETCH";
    public static final String PHP_FUNCTION_KEY_USER_FOLLOWINGS_FETCH = "USER_FOLLOWINGS_FETCH";
    public static final String PHP_FUNCTION_KEY_TIMELINE_SCOPE_MODIFY = "TIMELINE_SCOPE_MODIFY";
    public static final String PHP_FUNCTION_KEY_TIMELINE_DELETE = "TIMELINE_DELETE";
    public static final String PHP_FUNCTION_KEY_USER_SEARCH = "USER_SEARCH";
    public static final String PHP_FUNCTION_KEY_TREND_FETCH = "TREND_FETCH";
    public static final String PHP_FUNCTION_KEY_FETCH_RECIPE_IMAGES = "RECIPE_IMAGES_FETCH";
    public static final String PHP_FUNCTION_KEY_FETCH_MASTER_DATA = "MASTER_DATA_FETCH_ALL";
    public static final String PHP_FUNCTION_KEY_INGREDIENT_NUTRITION_FETCH = "INGREDIENT_NUTRITION_FETCH";
    public static final String PHP_FUNCTION_KEY_USER_BIO_SUBMIT = "USER_BIO_SUBMIT";
    public static final String PHP_FUNCTION_KEY_USER_BIO_DELETE = "USER_BIO_DELETE";
    /*PHP function keys*/

    /*Timeline keys*/
    public static final String TIMELINE_RECIPE_ADD = "RECIPE_ADD";
    public static final String TIMELINE_RECIPE_MODIFY = "RECIPE_MODIFY";
    public static final String TIMELINE_RECIPE_REMOVE = "RECIPE_REMOVE";

    public static final String TIMELINE_LIKE_RECIPE_ADD = "LIKE_RECIPE_ADD";
    public static final String TIMELINE_LIKE_RECIPE_REMOVE = "LIKE_RECIPE_REMOVE";
    public static final String TIMELINE_LIKE_RECIPE_IMG_ADD = "LIKE_RECIPE_IMG_ADD";
    public static final String TIMELINE_LIKE_RECIPE_IMG_REMOVE = "LIKE_RECIPE_IMG_REMOVE";
    public static final String TIMELINE_LIKE_COMMENT_ADD = "LIKE_COMMENT_ADD";
    public static final String TIMELINE_LIKE_COMMENT_REMOVE = "LIKE_COMMENT_REMOVE";
    public static final String TIMELINE_LIKE_REVIEW_ADD = "LIKE_REVIEW_ADD";
    public static final String TIMELINE_LIKE_REVIEW_REMOVE = "LIKE_REVIEW_REMOVE";
    public static final String TIMELINE_LIKE_USER_ADD = "LIKE_USER_ADD";
    public static final String TIMELINE_LIKE_USER_REMOVE = "LIKE_USER_REMOVE";

    public static final String TIMELINE_COMMENT_RECIPE_ADD = "COMMENT_RECIPE_ADD";
    public static final String TIMELINE_COMMENT_RECIPE_REMOVE = "COMMENT_RECIPE_REMOVE";
    public static final String TIMELINE_COMMENT_RECIPE_IMG_ADD = "COMMENT_RECIPE_IMG_ADD";
    public static final String TIMELINE_COMMENT_RECIPE_IMG_REMOVE = "COMMENT_RECIPE_IMG_REMOVE";
    public static final String TIMELINE_COMMENT_USER_ADD = "COMMENT_USER_ADD";
    public static final String TIMELINE_COMMENT_USER_REMOVE = "COMMENT_USER_REMOVE";

    public static final String TIMELINE_REVIEW_RECIPE_ADD = "REVIEW_RECIPE_ADD";
    public static final String TIMELINE_REVIEW_RECIPE_REMOVE = "REVIEW_RECIPE_REMOVE";

    public static final String TIMELINE_USER_ADD = "USER_ADD";
    public static final String TIMELINE_USER_PHOTO_MODIFY = "USER_PHOTO_MODIFY";

    public static final String TIMELINE_USER_FOLLOW = "USER_FOLLOW";
    public static final String TIMELINE_USER_UNFOLLOW = "USER_UNFOLLOW";
    /*Timeline keys*/

    /* My List Keys */
    public static final String PHP_FUNCTION_KEY_MYLIST_FETCH = "MYLIST_FETCH";
    /* My List Keys */

    /*Date Time*/
    public static final String DB_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String UI_DATE = "d MMM ''yy";
    /*Date Time*/


    //flag value for affirmative/non affirmative
    public static final String AFFIRMATIVE = "Y";
    public static final String NON_AFFIRMATIVE = "N";

    //responses from php server
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    public static final String UI_FONT = "Quicksand-Regular.otf";

    //Bundle Keys, Shared Prefs, Intent, fragment names
    //FRAGMENT NAMES
    public static final String FRAGMENT_ADD_RECIPE = "FRAGMENT_ADD_RECIPE";
    public static final String FRAGMENT_MY_FAVORITES = "FRAGMENT_MY_FAVORITES";
    public static final String FRAGMENT_RECIPE = "FRAGMENT_RECIPE";
    public static final String FRAGMENT_COMMENTS = "FRAGMENT_COMMENTS";
    public static final String FRAGMENT_COMMENT_DELETE = "FRAGMENT_COMMENT_DELETE";
    public static final String FRAGMENT_RECIPE_REVIEW = "FRAGMENT_RECIPE_REVIEW";
    public static final String FRAGMENT_RECIPE_REVIEWS = "FRAGMENT_RECIPE_REVIEWS";
    public static final String FRAGMENT_IMAGES = "FRAGMENT_IMAGES";
    public static final String FRAGMENT_RECIPE_STEPS = "FRAGMENT_RECIPE_STEPS";
    public static final String FRAGMENT_COMMON_SELECTION = "FRAGMENT_COMMON_SELECTION";
    public static final String FRAGMENT_COMMON_WAIT = "FRAGMENT_COMMON_WAIT";
    public static final String FRAGMENT_COMMON_MESSAGE = "FRAGMENT_COMMON_MESSAGE";
    public static final String FRAGMENT_MY_RECIPE = "FRAGMENT_MY_RECIPE";
    public static final String FRAGMENT_MY_LIST = "FRAGMENT_MY_LIST";
    public static final String FRAGMENT_MY_REVIEWS = "FRAGMENT_MY_REVIEWS";
    public static final String FRAGMENT_PROFILE_VIEW = "FRAGMENT_PROFILE_VIEW";
    public static final String FRAGMENT_PROFILE_VIEW_NAME = "FRAGMENT_PROFILE_VIEW_NAME";
    public static final String FRAGMENT_PROFILE_VIEW_EMAIL = "FRAGMENT_PROFILE_VIEW_EMAIL";
    public static final String FRAGMENT_PROFILE_VIEW_PASSWORD = "FRAGMENT_PROFILE_VIEW_PASSWORD";
    public static final String FRAGMENT_PROFILE_VIEW_PHONE = "FRAGMENT_PROFILE_VIEW_PHONE";
    public static final String FRAGMENT_PROFILE_VIEW_GENDER = "FRAGMENT_PROFILE_VIEW_GENDER";
    public static final String FRAGMENT_PROFILE_VIEW_IMAGE = "FRAGMENT_PROFILE_VIEW_IMAGE";
    public static final String FRAGMENT_LOGIN = "FRAGMENT_LOGIN";
    public static final String FRAGMENT_USER_VIEW = "FRAGMENT_USER_VIEW";
    public static final String FRAGMENT_USERS = "FRAGMENT_USERS";
    public static final String FRAGMENT_TIMELINE_HIDE = "FRAGMENT_TIMELINE_HIDE";
    public static final String FRAGMENT_TIMELINE_DELETE = "FRAGMENT_TIMELINE_DELETE";
    public static final String FRAGMENT_PEOPLE_VIEW = "FRAGMENT_PEOPLE_VIEW";
    public static final String FRAGMENT_ABOUT_US = "FRAGMENT_ABOUT_US";
    public static final String FRAGMENT_NO_INTERNET = "FRAGMENT_NO_INTERNET";
    public static final String FRAGMENT_SOMETHING_WRONG = "FRAGMENT_SOMETHING_WRONG";
    public static final String FRAGMENT_COOKERY_ERROR = "FRAGMENT_COOKERY_ERROR";
    public static final String FRAGMENT_PICK_IMAGE = "FRAGMENT_PICK_IMAGE";
    public static final String FRAGMENT_SHARE_SOCIAL_MEDIA = "FRAGMENT_SHARE_SOCIAL_MEDIA";
    public static final String FRAGMENT_INGREDIENT_NUTRIENTS = "FRAGMENT_INGREDIENT_NUTRIENTS";
    public static final String FRAGMENT_BIOS_VIEW = "FRAGMENT_BIOS_VIEW";


    //FRAGMENT OBJECT KEYS
    public static final String LOGGED_IN_USER = "LOGGED_IN_USER";
    public static final String MASTER = "MASTER";
    public static final String GENERIC_OBJECT = "GENERIC_OBJECT";
    public static final String GENERIC_OBJECT2 = "GENERIC_OBJECT2";
    public static final String LIST_DATA = "LIST_DATA";
    public static final String SELECTED_ITEM = "SELECTED_ITEM";

    public static final String MY_LISTS_EXISTS = "MY_LISTS_EXISTS";
    public static final String LIST_ID = "LIST_ID";
    public static final String INGREDIENT_ID = "INGREDIENT_ID";
    public static final String INGREDIENT_NAME = "INGREDIENT_NAME";

    //Messages
    public static final String UN_IDENTIFIED_PARENT_FRAGMENT = "Target Fragment hasn't been set before calling the current fragment";
    public static final String UN_IDENTIFIED_OBJECT_TYPE = "Object Type could not be identified for the object : ";
    public static final String UN_IDENTIFIED_VIEW = "Could not identify the view which has been clicked";

    //Snacks
    public static final String OK = "OK";

    //scopes
    public static final int SCOPE_PUBLIC = 1;
    public static final int SCOPE_FOLLOWERS = 2;
    public static final int SCOPE_SELF = 3;

    //Images Download
    public static final String GALLERY_DIR = "/Cookery";

    // Rate Us
    public final static int DAYS_UNTIL_PROMPT = 7;//Min number of days
    public final static int LAUNCHES_UNTIL_PROMPT = 7;//Min number of launches

    // Social Registration
    public final static String DEFAULT_SOCIAL_KEY = "DEFAULT_SOCIAL_KEY";

    //security keys
    public final static String APP_UNIQUE_ID = "x-app-unique-id";
    public final static String API_KEY_IDENTIFIER = "x-api-key";
    public final static String API_KEY_ANDROID = "AIzaSyAxI2I8Wvt784ExlS_BBHY8uWPakM7XRBo";

    //Trend Keys
    public final static String TRENDS_RECIPES_OF_THE_MONTH = "RECIPES_OF_THE_MONTH";
    public final static String TRENDS_USER_OF_THE_WEEK = "USER_OF_THE_WEEK";
    public final static String TRENDS_RECIPE_OF_THE_DAY = "RECIPE_OF_THE_DAY";

    //Keys
    public final static String SIMPLE_KEY_FOLLOWERS = "FOLLOWERS";
    public final static String SIMPLE_KEY_FOLLOWING = "FOLLOWING";
    public final static String SIMPLE_KEY_FAVORITES = "FAVORITES";
    public final static String SIMPLE_KEY_VIEWED = "VIEWED";
    public final static String SIMPLE_KEY_REVIEWED = "REVIEWED";
    public final static String SIMPLE_KEY_UNIMPLEMENTED = "UNIMPL";

    public final static float DEFAULT_CROP_RATIO = 0.05f;   //must be between 0 to less than 0.5
    public final static int LOUVRE_REQUEST_CODE = 1111;
    public final static int LOUVRE_MAX_IMAGES = 10;
}
