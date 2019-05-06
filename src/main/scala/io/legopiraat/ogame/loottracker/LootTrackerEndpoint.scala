package io.legopiraat.ogame.loottracker

import cats.data.OptionT
import cats.effect.Effect
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import io.legopiraat.ogame.loottracker.LootTrackerEndpoint.{Raid, ReportKey}
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

import scala.language.higherKinds

object LootTrackerEndpoint {
  def endpoints[F[_] : Effect](lootTrackerService: LootTrackerService[F]): HttpRoutes[F] = {
    new LootTrackerEndpoint[F](lootTrackerService).endpoints
  }

  case class Raid(id: String, metal: Long, crystal: Long, deuterium: Long, timestamp: String, playerName: String)
  case class ReportKey(reportKey: String)
}

class LootTrackerEndpoint[F[_] : Effect](lootTrackerService: LootTrackerService[F]) extends Http4sDsl[F] {

  implicit val raidDecoder: EntityDecoder[F, Raid] = jsonOf[F, Raid]
  implicit val reportKeyDecoder: EntityDecoder[F, ReportKey] = jsonOf[F, ReportKey]

  def endpoints: HttpRoutes[F] = HttpRoutes.of[F] {
    case req@POST -> Root / "loot" / "tracker" =>
      val result: F[Either[Exception, Raid]] = for {
        reportKey <- req.as[ReportKey]
        result <- lootTrackerService.trackLoot(reportKey).value
      } yield result

      result.flatMap {
        case Right(e) => Ok(e.asJson)
        case Left(e) => BadRequest()
      }

    case GET -> Root / "loot" / "tracker" / name =>
      val result: OptionT[F, List[Raid]] = for {
        result <- lootTrackerService.allLoot(name)
      } yield result

      result.value.flatMap {
        case Some(raids) => Ok(raids.asJson)
        case None => NotFound()
      }
  }
}
