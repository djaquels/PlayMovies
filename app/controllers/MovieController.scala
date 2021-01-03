// Importar el repository y el contexto de ejecución global

package controllers

import javax.inject.{Inject, Singleton}
import models.{Movie, MovieRepository}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

//....

// Hay que agregar el repository para que sea inyectado
@Singleton
class MovieController @Inject()(
                                cc: ControllerComponents,
                                movieRepository: MovieRepository
                              ) extends AbstractController(cc) {

        implicit val serializer = Json.format[Movie]
        val logger = play.Logger.of("MovieController")
        def getMovies = Action.async {
          movieRepository.getAll
            .map(movies =>  {
              val j = Json.obj( "data" -> movies, "message" -> "Correct")
              Ok(j)
            }).recover{
            case ex =>
              logger.error("An error ocurred getMovie",ex)
              InternalServerError(s"Error ${ex.getLocalizedMessage}")
          }
        }
        def getMovie(id: String) = Action.async {
          movieRepository.getOne(id)
            .map(movies =>  {
              val j = Json.obj( "data" -> movies, "message" -> "Correct")
              Ok(j)
            }).recover{
            case ex =>
              logger.error("An error ocurred getMovie",ex)
              InternalServerError(s"Error ${ex.getLocalizedMessage}")
          }
        }
        def createMovie = Action.async(parse.json) { request =>
          val validator = request.body.validate[Movie]
          validator.asEither match {
            case Left(error) => Future.successful(BadRequest("JSON Schema incorrect"))
            case Right(movie) => {
              movieRepository.create(movie)
                .map(movies =>  {
                  val j = Json.obj( "data" -> movies, "message" -> "Movie created")
                  Ok(j)
                }).recover{
                case ex =>
                  logger.error("An error ocurred createMovie",ex)
                  InternalServerError(s"Error ${ex.getLocalizedMessage}")
              }
            }
          }

        }
        def updateMovie(id: String) = Action.async(parse.json) { request =>
          val validator = request.body.validate[Movie]
          validator.asEither match {
            case Left(error) => Future.successful(BadRequest("JSON Schema incorrect"))
            case Right(movie) => {
              movieRepository.update(id,movie)
                .map(movies =>  {
                  val j = Json.obj( "data" -> movies, "message" -> "Movie updated")
                  Ok(j)
                }).recover{
                case ex =>
                  logger.error("An error ocurred updateMovie",ex)
                  InternalServerError(s"Error ${ex.getLocalizedMessage}")
              }
            }
          }

        }
        def deleteMovie(id: String) = Action.async {
          movieRepository.delete(id)
            .map(movies =>  {
              val j = Json.obj( "data" -> movies, "message" -> "Movie deleted")
              Ok(j)
            }).recover{
            case ex =>
              logger.error("An error ocurred deleteMovie",ex)
              InternalServerError(s"Error ${ex.getLocalizedMessage}")
          }
        }

}