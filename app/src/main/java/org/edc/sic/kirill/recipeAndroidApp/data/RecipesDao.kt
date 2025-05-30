package org.edc.sic.kirill.recipeAndroidApp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.edc.sic.kirill.recipeAndroidApp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM Recipe WHERE isFavorite = 1 ")
    fun getFavoriteRecipes(): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): Recipe?

    @Query("UPDATE recipe SET isFavorite = :isFavorite WHERE id = :recipeId")
    fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE categoryId = :categoryId")
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<Recipe>)
}