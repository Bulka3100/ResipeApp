package org.edc.sic.kirill.recipeAndroidApp.model

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : java.io.Serializable
