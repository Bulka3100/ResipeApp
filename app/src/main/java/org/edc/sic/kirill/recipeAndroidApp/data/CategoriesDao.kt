package org.edc.sic.kirill.recipeAndroidApp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.edc.sic.kirill.recipeAndroidApp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<Category>)
}