package io.legopiraat.ogame.client.response

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.auto._

object CombatReportImplicits {
  implicit val combatReportDecoder: Decoder[CombatReport] = deriveDecoder
}

case class CombatReport(RESULT_DATA: CombatResultData)
case class CombatResultData(generic: GenericCombatReport, attackers: List[Attacker])
case class GenericCombatReport(loot_metal: Long, loot_crystal: Long, loot_deuterium: Long, event_time: String)
case class Attacker(fleet_owner: String)
