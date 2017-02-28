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

  val get(val nbNodes: Int): List[Scenario] = {

    val brutLines = scala.io.Source.fromFile(dataFilePath).mkString

    val splittedLines: List[String] = brutLines.split('\n').filterNot(l => l.isEmpty).toList
    val head: String = splittedLines.head

    val lines: List[String] = splittedLines.filterNot(l => l == head)
    val names: Set[String] = (lines.map(l => l.split(',')(0)).toSet).take(nbNodes)

    var scenarios: List[Scenario] = List()
    for (name <- names) {
      var locations: List[Location] = List()
      val sel = lines.filter(l => l.split(',')(0) == name)
      sel.foreach(s => {
        locations = locations :+ Location(s.split(',')(1).toDouble, s.split(',')(2).toDouble, s.split(',')(3).toInt)
      })


      scenarios = scenarios :+ Scenario(name.split('.')(0), locations)
      println("a name is : " + name)
    }

    scenarios
  }
}
