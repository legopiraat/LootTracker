package io.legopiraat.ogame.client

import io.legopiraat.ogame.loottracker.LootTrackerEndpoint.ReportKey

trait OgameUrlBuilder {

  def buildCombatUrl(reportKey: ReportKey, apiKey: String): String = {
    buildUrl(reportKey, apiKey, "combat", "cr")
  }

  def buildEspionageUrl(reportKey: ReportKey, apiKey: String): String = {
    buildUrl(reportKey, apiKey, "spy", "sr")
  }

  def buildRecycleUrl(reportKey: ReportKey, apiKey: String): String = {
    buildUrl(reportKey, apiKey, "recycle", "rr")
  }

  private[this] def buildUrl(reportKey: ReportKey, apiKey: String, reportType: String, reportTypeShorthand: String): String = {
    val reportIdTokens = reportKey.reportKey.split("-").toVector
    val languageCode = reportIdTokens(1)
    val serverCode = reportIdTokens(2)
    val reportIdKey = reportIdTokens(3)

    s"""https://s$serverCode-$languageCode.ogame.gameforge.com/api/v1/$reportType/report?api_key=$apiKey&${reportTypeShorthand}_id=$reportIdKey"""
  }
}
