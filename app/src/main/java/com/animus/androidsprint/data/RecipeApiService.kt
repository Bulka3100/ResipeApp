package com.animus.androidsprint.data

import com.animus.androidsprint.model.Category
import com.animus.androidsprint.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") recipeId: Int): Call<Recipe>

    @GET("category/{id}/recipes")
    fun getRecipesByIds(@Path("id") categoryId: Int): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

}