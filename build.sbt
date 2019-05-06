import Dependencies._

lazy val root = (project in file("."))
  .settings(
    organization := "io.legopiraat",
    name := "ogame-calculator",
    scalaVersion := "2.12.8",
    version := "0.1",
    libraryDependencies ++= Seq(
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
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4")
  )

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-value-discard" // Warn when non-Unit expression results are unused.
)

