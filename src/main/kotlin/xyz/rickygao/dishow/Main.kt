package xyz.rickygao.dishow

import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import io.javalin.event.EventType
import org.jetbrains.exposed.dao.EntityNotFoundException
import org.jetbrains.exposed.sql.Database
import xyz.rickygao.dishow.controller.*

fun main(args: Array<String>) {
    Javalin.create()
            .event(EventType.SERVER_STARTING) {
                Database.connect(
                        "jdbc:mysql://localhost:3306/dishow",
                        driver = "com.mysql.cj.jdbc.Driver",
                        user = "root",
                        password = "dbpassword"
                )
            }
            .routes {

                path("/") {
                    path("/universities") {
                        get(UniversityController::getAllUniversities)
                        path("/:university-id") {
                            get(UniversityController::getUniversityById)
                            path("/canteens") {
                                get(CanteenController::getCanteensByUniversity)
                                path("/name/:canteen-name") {
                                    get(CanteenController::getCanteensByUniversityAndName)
                                }
                            }
                        }
                        path("/name/:university-name") {
                            get(UniversityController::getUniversitiesByName)
                        }
                    }

                    path("/canteens/:canteen-id") {
                        get(CanteenController::getCanteenById)
                        path("/catalogs") {
                            get(CatalogController::getCatalogsByCanteen)
                            path("/name/:catalog-name") {
                                get(CatalogController::getCatalogsByCanteenAndName)
                            }
                        }
                    }

                    path("/catalogs") {
                        path("/:catalog-id") {
                            get(CatalogController::getCatalogById)
                            path("/dishes") {
                                get(DishController::getDishesByCatalog)
                                path("/name/:dish-name") {
                                    get(DishController::getDishesByCatalogAndName)
                                }
                            }
                            path("/comments") {
                                get(CatalogCommentController::getCatalogCommentsByCatalog)
                                post(CatalogCommentController::postCatalogComment)
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