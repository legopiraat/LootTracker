package io.legopiraat.ogame.client

trait OgameUrlBuilder {

  def buildUrl(reportKey: String, apiKey: String): String = {
    val reportIdTokens = reportKey.split("-").toVector
    val reportTypeShorthand = reportIdTokens(0)
    val languageCode = reportIdTokens(1)
    val serverCode = reportIdTokens(2)
    val reportIdKey = reportIdTokens(3)

    val uriPrefix = s"""https://s$serverCode-$languageCode.ogame.gameforge.com/api/v1/"""
    val uriSuffix = s"""/report?api_key=$apiKey&${reportTypeShorthand}_id=$reportIdKey"""

    reportTypeShorthand match {
      case "cr" => s"${uriPrefix}combat$uriSuffix"
      case "rr" => s"${uriPrefix}recycle$uriSuffix"
      case "sr" => s"${uriPrefix}spy$uriSuffix"
    }
  }
}
