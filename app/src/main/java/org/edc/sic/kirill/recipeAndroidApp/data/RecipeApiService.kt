package org.edc.sic.kirill.recipeAndroidApp.data

import org.edc.sic.kirill.recipeAndroidApp.model.Category
import org.edc.sic.kirill.recipeAndroidApp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {
    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") recipeId: Int): Call<Recipe>

    @GET("category/{id}/recipes")
    fun getRecipesByIds(@Path("id") categoryId: Int): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

}