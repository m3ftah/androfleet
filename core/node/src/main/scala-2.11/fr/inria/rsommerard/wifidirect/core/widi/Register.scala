package fr.inria.rsommerard.wifidirect.core.widi

class Register() {

  var emulators: List[Emulator] = List()

  def addEmulator(emulator: Emulator) = {
    emulators ::= emulator
  }

  def removeEmulator(emulator: Emulator) = {
    emulators = emulators diff List(emulator)
  }

  def clear() = {
    emulators = List()
  }
}
