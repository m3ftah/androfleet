package fr.inria.rsommerard.wifidirect.core

import fr.inria.rsommerard.wifidirect.core.message.{Location, Scenario}

object Scenarios {

  val dataFilePath = "/scenarios.txt"

  val getMinTimestamp: Int = {
    val brutLines = scala.io.Source.fromFile(dataFilePath).mkString
    val splittedLines: List[String] = brutLines.split('\n').filterNot(l => l.isEmpty).toList
    val head: String = splittedLines.head
    val lines: List[String] = splittedLines.filterNot(l => l == head)
    val timestamps: List[Int] = lines.map(l => l.split(',')(3).toInt)
    timestamps.min
  }

  val getMaxTimestamp: Int = {
    val brutLines = scala.io.Source.fromFile(dataFilePath).mkString
    val splittedLines: List[String] = brutLines.split('\n').filterNot(l => l.isEmpty).toList
    val head: String = splittedLines.head
    val lines: List[String] = splittedLines.filterNot(l => l == head)
    val timestamps: List[Int] = lines.map(l => l.split(',')(3).toInt)
    timestamps.max
  }

}
