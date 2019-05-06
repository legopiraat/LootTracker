package io.legopiraat.ogame.client

import cats.effect.ConcurrentEffect
import cats.implicits._
import io.legopiraat.ogame.client.response.CombatReportImplicits._
import io.legopiraat.ogame.client.response.RecycleReportImplicits._
import io.legopiraat.ogame.client.response.{CombatReport, RecycleReport}
import io.legopiraat.ogame.config.ApiKeyConfig
import io.legopiraat.ogame.loottracker.LootTrackerEndpoint.Raid
import org.http4s.Status.Successful
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.client.blaze._

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

object OgameClient {
  def apply[F[_] : ConcurrentEffect](executionContext: ExecutionContext, apiKeyConfig: ApiKeyConfig): OgameClient[F] = new OgameClient[F](executionContext, apiKeyConfig)
}

class OgameClient[F[_] : ConcurrentEffect](executionContext: ExecutionContext, apiKeyConfig: ApiKeyConfig) extends OgameUrlBuilder {

  def getCombatReport(reportKey: String): F[Raid] = {
    val url = buildUrl(reportKey, apiKeyConfig.apiKey)

    for {
      response <- executeRequest(combatReportRequest(url))
      raid = combatReportToRaid(response, reportKey)
    } yield raid
  }

  def getRecycleReport(reportKey: String): F[Raid] = {
    val url = buildUrl(reportKey, apiKeyConfig.apiKey)

    for {
      response <- executeRequest(recycleReportRequest(url))
      raid = recycleReportToRaid(response, reportKey)
    } yield raid
  }

  private[this] def combatReportToRaid(combatReport: CombatReport, reportKey: String): Raid = {
    val generic = combatReport.RESULT_DATA.generic
    val attacker = combatReport.RESULT_DATA.attackers.head.fleet_owner

    Raid(reportKey, generic.loot_metal, generic.loot_crystal, generic.loot_deuterium, generic.event_time, attacker)
  }

  private[this] def recycleReportToRaid(recycleReport: RecycleReport, reportKey: String): Raid = {
    val generic = recycleReport.RESULT_DATA.generic
    Raid(reportKey, generic.metal_retrieved, generic.crystal_retrieved, 0l, generic.event_time, generic.owner_name)
  }

 private[this] def combatReportRequest(url: String): Client[F] => F[CombatReport] = (client: Client[F]) => {
    client.get(url) {
      case Successful(resp) => resp.decodeJson[CombatReport]
    }
  }

  private[this] def recycleReportRequest(url: String): Client[F] => F[RecycleReport] = (client: Client[F]) => {
    client.get(url) {
      case Successful(resp) => resp.decodeJson[RecycleReport]
    }
  }

  private[this] def executeRequest[T](f: Client[F] => F[T])  = {
    BlazeClientBuilder[F](executionContext).resource.use(f)
  }
}
