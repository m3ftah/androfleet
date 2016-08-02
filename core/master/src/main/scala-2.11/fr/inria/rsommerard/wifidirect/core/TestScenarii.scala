package fr.inria.rsommerard.wifidirect.core

import fr.inria.rsommerard.wifidirect.core.message.{Location, Scenario}

object TestScenarii {

  val dataFilePath = "/data/Test.txt"

  val getDefaultScenarii: List[Scenario] = {

    val brutLines = scala.io.Source.fromFile(dataFilePath).mkString

    val splittedLines: List[String] = brutLines.split('\n').filterNot(l => l.isEmpty).toList
    val head: String = splittedLines.head

    val lines: List[String] = splittedLines.filterNot(l => l == head)
    val id: Set[String] = lines.map(l => l.split(' ')(0)).toSet

    var ndx = 1
    var scenarii: List[Scenario] = List()
    for (i <- id) {
      var locations: List[Location] = List()
      val sel = lines.filter(l => l.split(' ')(0) == i)
      sel.foreach(s => {
        locations = locations :+ Location(s.split(' ')(3).toDouble, s.split(' ')(4).toDouble)
      })

      scenarii = scenarii :+ Scenario(locations)
      ndx += 1
    }

    scenarii
  }
}