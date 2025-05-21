package org.edc.sic.kirill.recipeAndroidApp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.edc.sic.kirill.recipeAndroidApp.model.Category
import org.edc.sic.kirill.recipeAndroidApp.model.Recipe

@Database(
    entities = [Category::class, Recipe::class],
    version = 2,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}