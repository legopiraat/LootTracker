package io.legopiraat.ogame.loottracker

import java.sql.SQLException
import java.time.{LocalDateTime, ZoneOffset, ZonedDateTime}

import cats._
import doobie._
import doobie.implicits._
import io.legopiraat.ogame.loottracker.LootTrackerEndpoint.Raid

import scala.language.{higherKinds, implicitConversions}

object LootTrackerRepository {
  def apply[F[_] : Monad](xa: Transactor[F]): LootTrackerRepository[F] = new LootTrackerRepository[F](xa)
}

class LootTrackerRepository[F[_] : Monad](val xa: Transactor[F]) {

  import LootTrackerSql._

  def save(raid: Raid): F[Either[SQLException, Int]] = {
    insert(raid).run.attemptSql.transact(xa)
  }

  def getByNameAndBetweenStartDateAndEndDate(name: String, startDate: ZonedDateTime, endDate: ZonedDateTime): F[List[Raid]] = {
    selectBetween(name, startDate, endDate).transact(xa)
  }

  implicit def localDateTimeToEpoch(date: LocalDateTime): Long = {
    date.toEpochSecond(ZoneOffset.ofHours(1))
  }
}

object LootTrackerSql {

  def insert(raid: Raid): Update0 =
    sql"""
    INSERT INTO RAID (id, timestamp, metal, crystal, deuterium, player_name)
    VALUES (${raid.id}, ${raid.timestamp}, ${raid.metal}, ${raid.crystal}, ${raid.deuterium}, ${raid.playerName})
    """.update

  def selectBetween(name: String, startDate: ZonedDateTime, endDate: ZonedDateTime): ConnectionIO[List[Raid]] =
    sql"""
          SELECT ID, METAL, CRYSTAL, DEUTERIUM, TIMESTAMP, PLAYER_NAME FROM RAID
          WHERE player_name = $name
          AND TIMESTAMP BETWEEN ${startDate.toString} AND ${endDate.toString}
      """.query[Raid].to[List]
}
