import sbt._

object Dependencies {

  object Version {
    val cats = "1.6.0"
    val http4s = "0.20.0"
    val scalatest = "3.0.5"
    val logback = "1.2.3"
    val circeVersion = "0.11.1"
    val circeConfigVersion = "0.6.1"
    val mockitoScala = "1.0.6"
    val doobieVersion = "0.6.0"
    val flywayVersion = "5.2.4"
  }

  val catsCore = "org.typelevel" %% "cats-core" % Version.cats
  val circeGeneric = "io.circe" %% "circe-generic" % Version.circeVersion
  val circeConfig = "io.circe" %% "circe-config" % Version.circeConfigVersion
  val http4sServer = "org.http4s" %% "http4s-blaze-server" % Version.http4s
  val http4sClient = "org.http4s" %% "http4s-blaze-client" % Version.http4s
  val http4sCirce = "org.http4s" %% "http4s-circe" % Version.http4s
  val http4sDsl = "org.http4s" %% "http4s-dsl" % Version.http4s
  val logback = "ch.qos.logback" % "logback-classic" % Version.logback
  val doobieCore = "org.tpolecat" %% "doobie-core" % Version.doobieVersion
  val doobiePostgres = "org.tpolecat" %% "doobie-postgres" % Version.doobieVersion
  val doobieHikari = "org.tpolecat" %% "doobie-hikari" % Version.doobieVersion
  val flyway = "org.flywaydb" % "flyway-core" % Version.flywayVersion

  object Testing {
    val scalactic = "org.scalactic" %% "scalactic" % Version.scalatest % Test
    val scalatest = "org.scalatest" %% "scalatest" % Version.scalatest % Test
    val mockitoScala = "org.mockito" %% "mockito-scala" % Version.mockitoScala % Test
    val http4sClient = "org.http4s" %% "http4s-blaze-client" % Version.http4s % Test
  }

  val all = Seq(
    catsCore,
    circeGeneric,
    circeConfig,
    http4sServer,
    http4sClient,
    http4sCirce,
    http4sDsl,
    logback,
    doobieCore,
    doobiePostgres,
    doobieHikari,
    flyway
  )

}

