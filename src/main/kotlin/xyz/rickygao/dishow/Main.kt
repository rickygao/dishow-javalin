package xyz.rickygao.dishow

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import io.javalin.event.EventType
import io.javalin.translator.json.JavalinJacksonPlugin
import org.jetbrains.exposed.dao.EntityNotFoundException
import org.jetbrains.exposed.sql.Database
import xyz.rickygao.dishow.controller.*

fun main(args: Array<String>) {
    Javalin.create()
            .event(EventType.SERVER_STARTING) {
                Database.connect(
                        "jdbc:mysql://localhost:3306/dishow",
                        driver = "com.mysql.cj.jdbc.Driver",
                        user = "dishow",
                        password = "dishowpassword"
                )
                JavalinJacksonPlugin.configure(ObjectMapper().registerKotlinModule())
            }
            .routes {

                path("/") {
                    path("/users/username/:username/password/:password") {
                        get(UserController::getUserByUsernameAndPassword)
                        put(UserController::putUserByUsernameAndPassword)
                    }

                    path("/universities") {
                        get(UniversityController::getAllUniversities)
                        path("/:university-id") {
                            get(UniversityController::getUniversityById)
                        }
                    }

                    path("/canteens/:canteen-id") {
                        get(CanteenController::getCanteenById)
                    }

                    path("/catalogs") {
                        path("/:catalog-id") {
                            get(CatalogController::getCatalogById)
                            path("/comments") {
                                get(CatalogCommentController::getCatalogCommentsByCatalog)
                                post(CatalogCommentController::postCatalogComment, listOf(RealRole.USER))
                            }
                        }
                        path("/comments/:catalog-comment-id") {
                            get(CatalogCommentController::getCatalogCommentById)
                        }
                    }

                    path("/dishes/:dish-id") {
                        get(DishController::getDishById)
                    }
                }
            }
            .accessManager(RealAccessManager)
            .exception(NumberFormatException::class.java, ExceptionController::badRequest)
            .exception(EntityNotFoundException::class.java, ExceptionController::notFound)
            .error(400, ErrorController::badRequest)
            .error(401, ErrorController::unauthorized)
            .error(404, ErrorController::notFound)
            .port(8080).start()
}