package io.legopiraat.ogame

object FunctionReactorCalculator extends App {

  def energyOutput(reactorLevel: Long, energyTechLevel: Long): Long = {
    Math.pow(30 * reactorLevel * (1.05 + energyTechLevel * 0.01), reactorLevel).toLong
  }

  println(energyOutput(1, 1))
  println(energyOutput(1, 2))
  println(energyOutput(1, 3))
  println(energyOutput(1, 4))
  println(energyOutput(1, 5))
  println(energyOutput(1, 6))
  println(energyOutput(1, 7))
  println(energyOutput(1, 8))
  println(energyOutput(1, 9))
  println(energyOutput(1, 10))

}
