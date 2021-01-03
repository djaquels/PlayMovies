package models

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc._
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.{Await, ExecutionContext, Future}

class MovieRepository @Inject() (
  protected val dbConfigProvider: DatabaseConfigProvider,
  cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc)
  with HasDatabaseConfigProvider[JdbcProfile]{
  private lazy val movieQuery = TableQuery[MovieTable]
  /**
   * Función de ayuda para crear la tabla si ésta
   * aún no existe en la base de datos.
   * @return
   */
  def dbInit: Future[Unit] = {
    // Definición de la sentencia SQL de creación del schema
    db.run(movieQuery.schema.createIfNotExists)
  }

  def getAll = ???
  def getOne = ???
  def create = ???
  def update = ???
  def delete = ???
}




