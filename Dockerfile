FROM openjdk:11-slim

ADD /target/scala-2.12/ogame-loot-tracker.jar .

ENTRYPOINT["java -jar ogame-loot-tracker.jar"]