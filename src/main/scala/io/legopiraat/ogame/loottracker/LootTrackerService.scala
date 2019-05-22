package io.legopiraat.ogame.loottracker

import java.time.ZonedDateTime

import cats.Monad
import cats.data.{EitherT, OptionT}
import io.legopiraat.ogame.client.OgameClient
import io.legopiraat.ogame.loottracker.LootTrackerEndpoint.{Raid, ReportKey}

import scala.language.higherKinds

object LootTrackerService {

  def apply[F[_]](lootTrackerRepository: LootTrackerRepository[F], ogameClient: OgameClient[F]): LootTrackerService[F] = {
    new LootTrackerService[F](lootTrackerRepository, ogameClient)
  }
}

class LootTrackerService[F[_]](lootTrackerRepository: LootTrackerRepository[F], ogameClient: OgameClient[F]) {

  def trackLoot(reportKey: ReportKey)(implicit m: Monad[F]): EitherT[F, Exception, Raid] = {
    for {
      raid <- EitherT.liftF(ogameClient.getCombatReport(reportKey))
      _ <- EitherT.liftF(lootTrackerRepository.save(raid))
    } yield raid
  }

  def trackRecycleLoot(reportKey: ReportKey)(implicit m: Monad[F]): EitherT[F, Exception, Raid] = {
    for {
      raid <- EitherT.liftF(ogameClient.getRecycleReport(reportKey))
      _ <- EitherT.liftF(lootTrackerRepository.save(raid))
    } yield raid
  }

  def retrieveLootByDate(name: String, startDate: ZonedDateTime, endDate: ZonedDateTime)(implicit m: Monad[F]): OptionT[F, List[Raid]] = {
    OptionT.liftF(lootTrackerRepository.getByNameAndBetweenStartDateAndEndDate(name, startDate, endDate))
  }
}
