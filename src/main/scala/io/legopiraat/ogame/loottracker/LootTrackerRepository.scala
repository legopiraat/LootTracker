package io.legopiraat.ogame.loottracker

import java.sql.SQLException

import cats._
import doobie._
import doobie.implicits._
import io.legopiraat.ogame.loottracker.LootTrackerEndpoint.Raid

import scala.language.higherKinds

object LootTrackerRepository {
  def apply[F[_] : Monad](xa: Transactor[F]): LootTrackerRepository[F] = new LootTrackerRepository[F](xa)
}

class LootTrackerRepository[F[_] : Monad](val xa: Transactor[F]) {

  import LootTrackerSql._

  def save(raid: Raid): F[Either[SQLException, Int]] = {
    insert(raid).run.attemptSql.transact(xa)
  }

  def getAll(name: String): F[List[Raid]] = {
    selectAll(name).transact(xa)
  }
}

object LootTrackerSql {

  def insert(raid: Raid): Update0 =
    sql"""
    INSERT INTO RAID (id, timestamp, metal, crystal, deuterium, player_name)
    VALUES (${raid.id}, ${raid.timestamp}, ${raid.metal}, ${raid.crystal}, ${raid.deuterium}, ${raid.playerName})
    """.update

  def selectAll(name: String): ConnectionIO[List[Raid]] =
    sql"""
          SELECT ID, METAL, CRYSTAL, DEUTERIUM, TIMESTAMP, PLAYER_NAME FROM RAID WHERE player_name = $name
      """.query[Raid].to[List]
}
