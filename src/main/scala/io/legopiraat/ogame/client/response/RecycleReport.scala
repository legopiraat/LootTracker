package io.legopiraat.ogame.client.response

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import io.circe.generic.auto._

object RecycleReportImplicits {
  implicit val recycleReportDecoder: Decoder[RecycleReport] = deriveDecoder
}

case class RecycleReport(RESULT_DATA: RecycleResultData)
case class RecycleResultData(generic: GenericRecycleReport)
case class GenericRecycleReport(metal_retrieved: Long, crystal_retrieved: Long, owner_name: String, event_time: String)
