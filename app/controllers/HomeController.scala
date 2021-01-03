// Importar el repository y el contexto de ejecución global

package controllers
import javax.inject.Singleton
import javax.inject.Inject
import models.MovieRepository
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

//....

// Hay que agregar el repository para que sea inyectado
@Singleton
class HomeController @Inject()(
                                cc: ControllerComponents,
                                movieRepository: MovieRepository
                              ) extends AbstractController(cc) {

  // .....

  /*
    Función de ayuda para crear la tabla si esta aún no existe.
   */
  def index = Action {
    Ok(views.html.index())
  }

  def dbInit() = Action.async { request =>
    movieRepository.dbInit
      .map(_ => Created("Tabla creada"))
      .recover { ex =>
        play.Logger.of("dbInit").debug("Error en dbInit", ex)
        InternalServerError(s"Hubo un error")
      }
  }

}