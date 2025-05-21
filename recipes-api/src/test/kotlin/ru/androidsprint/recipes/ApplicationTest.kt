package org.edc.sic.kirill.recipes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.edc.sic.kirill.recipes.routers.recipes
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testGetOneRecipe() = testApplication {
        application {
            routing {
                recipes()
            }
        }
        client.get("/recipe/1").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                """{"id":1,"title":"Чизбургер","ingredients":[{"quantity":"1","unitOfMeasure":"шт.","description":"Булочка для гамбургера"},{"quantity":"150","unitOfMeasure":"г","description":"Фарш говяжий"},{"quantity":"0.5","unitOfMeasure":"шт.","description":"Лук репчатый"},{"quantity":"1","unitOfMeasure":"лист","description":"Салат айсберг"},{"quantity":"1","unitOfMeasure":"ломтик","description":"Помидор"},{"quantity":"1","unitOfMeasure":"ломтик","description":"Сыр чеддер"},{"quantity":"1","unitOfMeasure":"ст. ложка","description":"Майонез"},{"quantity":"1","unitOfMeasure":"ст. ложка","description":"Кетчуп"},{"quantity":"0.5","unitOfMeasure":"ч. ложки","description":"Горчица"},{"quantity":"1","unitOfMeasure":"г","description":"Соленые огурцы"},{"quantity":"1","unitOfMeasure":"г","description":"Соль"},{"quantity":"0.5","unitOfMeasure":"г","description":"Перец черный молотый"}],"method":["Разогрейте сковороду и обжаривайте фарш до готовности.","Лук нарежьте кольцами и добавьте к фаршу. Обжаривайте вместе до золотистой корки.","Разрежьте булочку на две части и поджарьте их до золотистости.","На нижнюю часть булочки положите лист салата, ломтик помидора и сыр.","Добавьте фарш с луком и огурцами.","Смажьте верхнюю часть булочки майонезом, кетчупом и горчицей.","Положите верхнюю часть булочки на остальные ингредиенты и подавайте."],"imageUrl":"burger-cheeseburger.png"}""",
                bodyAsText()
            )
        }
    }
}
