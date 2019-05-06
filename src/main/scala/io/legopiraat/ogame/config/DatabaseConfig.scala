package io.legopiraat.ogame.config

import cats.effect.{Async, ContextShift, Resource, Sync}
import cats.syntax.functor._
import io.circe.generic.semiauto._
import doobie.hikari.HikariTransactor
import io.circe.Decoder
import org.flywaydb.core.Flyway

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

case class DatabaseConfig(url: String, driver: String, user: String, password: String)

object DatabaseConfig {

  implicit val dbConfigDecoder: Decoder[DatabaseConfig] = deriveDecoder

  def dbTransactor[F[_] : Async : ContextShift](databaseConfig: DatabaseConfig,
                                                connEc: ExecutionContext,
                                                transEc: ExecutionContext): Resource[F, HikariTransactor[F]] = {
    HikariTransactor.newHikariTransactor[F](databaseConfig.driver, databaseConfig.url, databaseConfig.user, databaseConfig.password, connEc, transEc)
  }

  def initializeDb[F[_]](config: DatabaseConfig)(implicit s: Sync[F]): F[Unit] = {
    s.delay {
      Flyway
        .configure()
        .dataSource(config.url, config.user, config.password)
        .load()
        .migrate()
    }.as(())
  }
}
