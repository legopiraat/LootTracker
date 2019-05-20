package io.legopiraat.ogame.client.response

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.auto._

object EspionageReportImplicits {
  implicit val espionageReportDecoder: Decoder[EspionageReport] = deriveDecoder
}

case class EspionageReport(RESULT_DATA: EspionageResultData)
case class EspionageResultData(generic: GenericEspionageReport, details: DetailsEspionageReport)
case class GenericEspionageReport(defender_name: String,
                                  defender_planet_coordinates: String,
                                  total_ship_count: Long,
                                  total_defense_count: Long,
                                  loot_percentage: Int,
                                  event_time: String)
case class DetailsEspionageReport(resources: Resources, ships: List[Ship], defense: List[Defense])
case class Resources(metal: Long, crystal: Long, deuterium: Long)
case class Defense() //TODO: Fill in with data
case class Ship(ship_type: Int, count: Long) //TODO: CHeck what is in here
