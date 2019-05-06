package io.legopiraat.ogame.config

import io.circe.generic.semiauto._
import io.circe.Decoder

case class ApiKeyConfig(apiKey: String)

object ApiKeyConfig {

  implicit val apiKeyDecoder: Decoder[ApiKeyConfig] = deriveDecoder

}
