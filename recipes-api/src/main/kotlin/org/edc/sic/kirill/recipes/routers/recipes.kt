package org.edc.sic.kirill.recipes.routers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.edc.sic.kirill.recipes.data.RecipesService

fun Routing.recipes() {

    val recipesService = RecipesService()

    route("/api") {
        get("/recipe") {
            val recipe = call.parameters["id"]?.toIntOrNull()?.let {
                recipesService.getRecipeById(it)
            }
            if (recipe != null) {
                call.respond(HttpStatusCode.OK, recipe)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/recipes") {
            val ids = call.request.queryParameters["ids"]?.split(",")?.map { it.toInt() }?.toSet()
                ?: throw IllegalArgumentException("Invalid ID")

            val recipes = recipesService.getRecipesByIds(ids)
            call.respond(HttpStatusCode.OK, recipes)
        }

        get("/recipe/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val recipe = recipesService.getRecipeById(id)
            if (recipe != null) {
                call.respond(HttpStatusCode.OK, recipe)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/category/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val category = recipesService.getCategoryById(id)
            if (category != null) {
                call.respond(HttpStatusCode.OK, category)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/category/{id}/recipes") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val category = recipesService.getRecipesByCategoryId(id)
            if (category != null) {
                call.respond(HttpStatusCode.OK, category)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/category") {
            call.respond(HttpStatusCode.OK, recipesService.getCategories())
        }
    }
}