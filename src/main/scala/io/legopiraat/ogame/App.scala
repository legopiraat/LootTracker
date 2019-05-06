package io.legopiraat.ogame

import cats.effect._
import cats.implicits._
import doobie.util.ExecutionContexts
import io.legopiraat.ogame.config.{ApiKeyConfig, DatabaseConfig}
import io.legopiraat.ogame.loottracker.{LootTrackerEndpoint, LootTrackerRepository, LootTrackerService}
import io.legopiraat.ogame.config.DatabaseConfig.dbConfigDecoder
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.{Router, Server}
import io.circe.config.parser
import io.legopiraat.ogame.client.OgameClient

import scala.language.higherKinds

object App extends IOApp {

  private[this] val root: String = "/ogame-tracker"

  override def run(args: List[String]): IO[ExitCode] = createServer().use(_ => IO.never).as(ExitCode.Success)

  def createServer[F[_] : ContextShift : ConcurrentEffect : Timer](): Resource[F, Server[F]] = {
    for {
      dbConfig <- Resource.liftF(parser.decodePathF[F, DatabaseConfig]("loot-tracker"))
      apiKeyConfig <- Resource.liftF(parser.decodePathF[F, ApiKeyConfig]("ogame"))
      connEc <- ExecutionContexts.fixedThreadPool[F](5)
      txnEc <- ExecutionContexts.cachedThreadPool[F]
      dbContext <- DatabaseConfig.dbTransactor(dbConfig, connEc, txnEc)
      ogameClient = OgameClient[F](connEc, apiKeyConfig)
      lootTrackerRepository = LootTrackerRepository[F](dbContext)
      lootTrackerService = LootTrackerService[F](lootTrackerRepository, ogameClient)
      services = LootTrackerEndpoint.endpoints(lootTrackerService)
      httpApp = Router(root -> services).orNotFound
      _ <- Resource.liftF(DatabaseConfig.initializeDb(dbConfig))
      server <- BlazeServerBuilder[F]
        .bindHttp(8585, "0.0.0.0")
        .withHttpApp(httpApp)
        .resource
    } yield server
  }

}
