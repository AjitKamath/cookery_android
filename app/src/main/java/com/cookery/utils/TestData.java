package com.cookery.utils;

import com.cookery.models.CommentMO;
import com.cookery.models.CuisineMO;
import com.cookery.models.FoodTypeMO;
import com.cookery.models.IngredientAkaMO;
import com.cookery.models.IngredientUOMMO;
import com.cookery.models.RecipeMO;
import com.cookery.models.ReviewMO;
import com.cookery.models.TasteMO;
import com.cookery.models.UserMO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class TestData {
    private static List<FoodTypeMO> foodTypes;
    private static List<IngredientAkaMO> ingredients;
    private static List<CuisineMO> cuisines;
    private static List<IngredientUOMMO> quantities;
    private static List<TasteMO> tastes;
    private static List<CommentMO> comments;
    private static UserMO user;
    private static List<ReviewMO> reviews;

    private static List<RecipeMO> recipes; //keep it last

    public static UserMO getUserTestData(){
        user = new UserMO();
        user.setUSER_ID(1);
        user.setNAME("Vishal Varshney");

        return user;
    }

    public static List<RecipeMO> getRecipesTestData(){
        getIngredientsTestData();
        getCommentsTestData();
        getReviewsTestData();

        recipes = new ArrayList<>();
        RecipeMO recipe = null;

        recipe = new RecipeMO();
        recipe.setRCP_NAME("CHICKEN TANDORI");
        recipe.setFoodTypeName("DINNER");
        recipe.setFoodCuisineName("INDIAN");
        recipe.setUserName("Vishal Varhney");
        recipe.setIngredients(ingredients);
        recipe.setReviews(reviews);
        recipe.setComments(comments);
        recipes.add(recipe);

        recipe = new RecipeMO();
        recipe.setRCP_NAME("MUSHROOM NOODLES");
        recipe.setFoodTypeName("BREAKFAST");
        recipe.setFoodCuisineName("CHINESE");
        recipe.setIngredients(ingredients);
        recipe.setComments(comments);
        recipe.setReviews(reviews);
        recipe.setUserName("Ajit Kamath");
        recipes.add(recipe);

        recipe = new RecipeMO();
        recipe.setRCP_NAME("MASALA DOSA");
        recipe.setFoodTypeName("BREAKFAST");
        recipe.setFoodCuisineName("SOUTH INDIAN");
        recipe.setIngredients(ingredients);
        recipe.setComments(comments);
        recipe.setReviews(reviews);
        recipe.setUserName("Ashish Bhargav");
        recipes.add(recipe);

        return recipes;
    }

    public static List<IngredientUOMMO> getQuantitiesTestDate() {
        quantities = new ArrayList<>();
        IngredientUOMMO quantity = null;

        quantity = new IngredientUOMMO();
        quantity.setING_UOM_ID(1);
        quantity.setIS_DEF("Y");
        quantity.setING_UOM_NAME("SPOON");
        quantities.add(quantity);

        quantity = new IngredientUOMMO();
        quantity.setING_UOM_ID(2);
        quantity.setING_UOM_NAME("CUP");
        quantities.add(quantity);

        quantity = new IngredientUOMMO();
        quantity.setING_UOM_ID(3);
        quantity.setING_UOM_NAME("GLASS");
        quantities.add(quantity);

        quantity = new IngredientUOMMO();
        quantity.setING_UOM_ID(4);
        quantity.setING_UOM_NAME("TEA SPOON");
        quantities.add(quantity);

        quantity = new IngredientUOMMO();
        quantity.setING_UOM_ID(5);
        quantity.setING_UOM_NAME("PINCH");
        quantities.add(quantity);

        return quantities;
    }

    public static List<CuisineMO> getCuisinesTestData(){
        cuisines = new ArrayList<>();
        CuisineMO cuisine = null;

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("INDIAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("CHINESE");
        cuisine.setIS_DEF("Y");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("THAI");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("MEXICAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("SOUTH INDIAN");
        cuisines.add(cuisine);

        cuisine = new CuisineMO();
        cuisine.setFOOD_CSN_NAME("NORTH INDIAN");
        cuisines.add(cuisine);

        return cuisines;
    }

    public static List<IngredientAkaMO> getIngredientsTestData(){
        ingredients = new ArrayList<>();
        IngredientAkaMO ingredient = null;

        ingredient = new IngredientAkaMO();
        ingredient.setING_AKA_NAME("MILK");
        ingredient.setING_AKA_ID(1);
        ingredient.setING_UOM_VALUE(1);
        ingredient.setING_UOM_NAME("SPOON");
        ingredients.add(ingredient);

        ingredient = new IngredientAkaMO();
        ingredient.setING_AKA_NAME("SALT");
        ingredient.setING_AKA_ID(2);
        ingredient.setING_UOM_VALUE(3);
        ingredient.setING_UOM_NAME("TABLE SPOON");
        ingredients.add(ingredient);

        ingredient = new IngredientAkaMO();
        ingredient.setING_AKA_NAME("PEPPER");
        ingredient.setING_AKA_ID(3);
        ingredient.setING_UOM_VALUE(5);
        ingredient.setING_UOM_NAME("CUP");
        ingredients.add(ingredient);

        ingredient = new IngredientAkaMO();
        ingredient.setING_AKA_NAME("CHILLY");
        ingredient.setING_AKA_ID(4);
        ingredient.setING_UOM_VALUE(5);
        ingredient.setING_UOM_NAME("BOWL");
        ingredients.add(ingredient);

        ingredient = new IngredientAkaMO();
        ingredient.setING_AKA_NAME("WATER");
        ingredient.setING_AKA_ID(5);
        ingredient.setING_UOM_VALUE(3);
        ingredient.setING_UOM_NAME("PINCH");
        ingredients.add(ingredient);

        return ingredients;
    }

    public static List<FoodTypeMO> getFoodTypesTestData(){
        foodTypes = new ArrayList<>();
        FoodTypeMO foodType = null;

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(1);
        foodType.setFOOD_TYP_NAME("BREAKFAST");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(2);
        foodType.setFOOD_TYP_NAME("LUNCH");
        foodType.setIS_DEF("Y");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(3);
        foodType.setFOOD_TYP_NAME("SNACKS");
        foodType.setIS_DEF("");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(4);
        foodType.setFOOD_TYP_NAME("DINNER");
        foodType.setIS_DEF("");
        foodTypes.add(foodType);

        foodType = new FoodTypeMO();
        foodType.setFOOD_TYP_ID(5);
        foodType.setFOOD_TYP_NAME("DESSERT");
        foodType.setIS_DEF("");
        foodTypes.add(foodType);

        return foodTypes;
    }

    public static List<TasteMO> getTastesTestData(){
        TasteMO taste = null;
        tastes = new ArrayList<>();

        taste = new TasteMO();
        taste.setTST_ID(1);
        taste.setTST_NAME("SWEET");
        tastes.add(taste);

        taste = new TasteMO();
        taste.setTST_ID(1);
        taste.setTST_NAME("SPICE");
        tastes.add(taste);

        return tastes;
    }

    public static List<CommentMO> getCommentsTestData(){
        CommentMO comment = null;
        comments = new ArrayList<>();

        comment = new CommentMO();
        comment.setCOM_ID(1);
        comment.setCOMMENT(getBigStringTestData());
        comments.add(comment);

        comment = new CommentMO();
        comment.setCOM_ID(3);
        comment.setCOMMENT(getBigStringTestData());
        comments.add(comment);

        comment = new CommentMO();
        comment.setCOM_ID(2);
        comment.setCOMMENT(getBigStringTestData());
        comments.add(comment);

        comment = new CommentMO();
        comment.setCOM_ID(9);
        comment.setCOMMENT(getBigStringTestData());
        comments.add(comment);

        return comments;
    }

    public static List<ReviewMO> getReviewsTestData(){
        getUserTestData();

        ReviewMO review = null;
        reviews = new ArrayList<>();

        review = new ReviewMO();
        review.setUSER_ID(user.getUSER_ID());
        review.setRCP_ID(1);
        review.setREVIEW("Food worth dying for.");
        review.setRATING(4);
        reviews.add(review);

        review = new ReviewMO();
        review.setUSER_ID(user.getUSER_ID());
        review.setRCP_ID(2);
        review.setREVIEW("Amazing recipe");
        review.setRATING(3);
        reviews.add(review);

        review = new ReviewMO();
        review.setUSER_ID(user.getUSER_ID());
        review.setRCP_ID(4);
        review.setREVIEW("Simple yet ravishing taste.");
        review.setRATING(4);
        reviews.add(review);

        review = new ReviewMO();
        review.setUSER_ID(user.getUSER_ID());
        review.setRCP_ID(3);
        review.setREVIEW("Have always been a fan of mediterranean food. Loved it.");
        review.setRATING(3);
        reviews.add(review);

        return reviews;
    }

    public static String getBigStringTestData(){
        return "This recipe made my day. This chef is the best !! Would like to try more from him. I follow this chef and he is good lookig too ;) I do not know what to write here. But i am typing because i want to see how a big comment looks like on the screen.";
    }

}
