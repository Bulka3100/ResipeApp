package org.edc.sic.kirill.recipeAndroidApp.data

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.edc.sic.kirill.recipeAndroidApp.model.Ingredient

class Converters {
    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientsList(value: String): List<Ingredient> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromMethodList(method: List<String>): String {
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun toMethodList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}
