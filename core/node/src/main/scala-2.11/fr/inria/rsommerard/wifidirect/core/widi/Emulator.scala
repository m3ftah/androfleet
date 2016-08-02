package fr.inria.rsommerard.wifidirect.core.widi

import java.io.{ObjectInputStream, ObjectOutputStream, PrintStream}
import java.net._
import java.util.Calendar

import akka.actor.ActorRef
import fr.inria.rsommerard.wifidirect.core.message._
import org.apache.commons.net.telnet.TelnetClient
import play.api.libs.json.{JsValue, Json}

import scala.sys.process.Process

object Emulator {
  val adbPath = "/android-sdk-linux/platform-tools/adb"

  def isApplicationStarted(packageName: String): Boolean = {
    val isEmulatorStarted: Boolean = Process(s"$adbPath devices").!!.trim.contains("emulator-5554")

    if (Process(s"$adbPath -e shell ps").! != 0)
      return false

    val isApplicationInPS = Process(s"$adbPath -e shell ps").!!.trim.contains(packageName)

    isEmulatorStarted && isApplicationInPS
  }

  def setGPSLocation(lon: Double, lat: Double): Unit = {
    println(s"[${Calendar.getInstance().getTime}] New GPS location " + lon + " " + lat)

    val tn = new TelnetClient
    tn.connect("localhost", 5554)

    val out = new PrintStream(tn.getOutputStream)
    out.println(s"geo fix $lon $lat")
    out.flush()
    out.close()
    tn.disconnect()
  }
}

class Emulator(val weaveIp: String) {
  var neighbors: List[Neighbor] = List()
  var services: List[Service] = List()
  var isDiscoverable = false
  var isConnected = false
  var connectedTo: String = ""
  val serverPort: Int = 54421
  val serverSocket: ServerSocket = new ServerSocket(serverPort)
  val name: String = s"N${weaveIp.replace(".","")}"
  var node: ActorRef = _

  val device: Device = Device(name, weaveIp)
  var devices: List[Device] = List()

  var dnsSdServiceResponse: DnsSdServiceResponse = _
  var dnsSdTxtRecord: DnsSdTxtRecord = _
  var wifiP2pConfig: WifiP2pConfig = _

  def updateNeighbors(nghbrs: List[Neighbor]) = {
    neighbors = nghbrs.filterNot(n => n.weaveIp == weaveIp)
    devices = List()
    neighbors.foreach(n => devices = Device(s"N${n.weaveIp.replace(".", "")}", n.weaveIp) :: devices)
  }

  def updateServices(srvcs: List[Service]) = {
    services = srvcs.filter(s => neighbors.contains(Neighbor(s.srcDevice))).filterNot(s => s.srcDevice == weaveIp)
  }

  def sendStateChangedIntent(): Unit = {
    Process(s"${Emulator.adbPath} -e shell am broadcast -a ${Intent.WIFI_P2P_STATE_CHANGED_ACTION} --ei ${Extra.EXTRA_WIFI_STATE} ${Extra.WIFI_P2P_STATE_ENABLED}").run()
  }

  def sendPeersChangedIntent(): Unit = {
    Process(s"${Emulator.adbPath} -e shell am broadcast -a ${Intent.WIFI_P2P_PEERS_CHANGED_ACTION}").run()
  }

  def sendConnectionChangedIntent(): Unit = {
    Process(s"${Emulator.adbPath} -e shell am broadcast -a ${Intent.WIFI_P2P_CONNECTION_CHANGED_ACTION}").run()
  }

  def sendConnectIntent(isConnect: Boolean, isGroupOwner: Boolean, groupOwnerAddress: String): Unit = {
    if (isConnect) {
      Process(s"${Emulator.adbPath} -e shell am broadcast -a ${Intent.CONNECT} --ez ${Extra.EXTRA_CONNECT_STATE} true --ez ${Extra.EXTRA_GROUP_OWNER} $isGroupOwner --es ${Extra.EXTRA_GROUP_OWNER_ADDRESS} $groupOwnerAddress").run()
      return
    }

    Process(s"${Emulator.adbPath} -e shell am broadcast -a ${Intent.CONNECT} --ez ${Extra.EXTRA_CONNECT_STATE} false").run()
  }

  def sendThisDeviceChangedIntent(): Unit = {
    Process(s"${Emulator.adbPath} -e shell am broadcast -a ${Intent.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION}").run()
  }

  def start(nd: ActorRef): Unit = {
    node = nd
    node.tell(Hello("Emulator"), ActorRef.noSender)

    new Thread(new Runnable {
      override def run(): Unit = {
        while (true) {
          val socket = serverSocket.accept
          new Thread(new Runnable {
            override def run(): Unit = {
              implicit val oOStream: ObjectOutputStream = new ObjectOutputStream(socket.getOutputStream)
              implicit val oIStream: ObjectInputStream = new ObjectInputStream(socket.getInputStream)
              val message: String  = receive()

              message match {
                case Protocol.HELLO => hello()
                case Protocol.CARTON => carton()
                case Protocol.DISCOVER_PEERS => discoverPeers()
                case Protocol.STOP_DISCOVERY => stopDiscovery()
                case Protocol.CANCEL_CONNECT => cancelConnect()
                case Protocol.CONNECT => connect()
                case Protocol.REQUEST_PEERS => requestPeers()
                case Protocol.DISCOVER_SERVICES => discoverServices()
                case u: Any => unknown(u)
              }

              socket.close()
            }
          }).start()
        }
      }
    }).start()
  }

  def send(message: String)(implicit oOStream: ObjectOutputStream): Unit = {
    oOStream.writeObject(message)
    oOStream.flush()
  }

  def receive()(implicit oIStream: ObjectInputStream): String = {
    oIStream.readObject().toString
  }

  def hello()(implicit oOStream: ObjectOutputStream): Unit = {
    send(Protocol.ACK)

    val isConnect = false
    val isGroupOwner = false
    val groupOwnerAddress: String = ""

    sendStateChangedIntent()
    sendConnectIntent(isConnect, isGroupOwner, groupOwnerAddress)
    sendThisDeviceChangedIntent()
  }

  def carton()(implicit oIStream: ObjectInputStream, oOStream: ObjectOutputStream): Unit = {
    send(Protocol.ACK)
  }

  def discoverPeers()(implicit oIStream: ObjectInputStream, oOStream: ObjectOutputStream): Unit = {
    send(Protocol.ACK)

    isDiscoverable = true

    node.tell(Discoverable(true), ActorRef.noSender)
    node.tell(Request("Neighbors"), ActorRef.noSender)
  }

  def stopDiscovery(): Unit = {
    node.tell(Discoverable(false), ActorRef.noSender)
  }

  def cancelConnect()(implicit oIStream: ObjectInputStream, oOStream: ObjectOutputStream): Unit = {
    send(Protocol.ACK)

    disconnect()

    node.tell(Disconnect(connectedTo), ActorRef.noSender)
  }

  def disconnect(): Unit = {
    if (!isConnected) {
      return
    }

    isConnected = false

    val isConnect = false
    val isGroupOwner = false
    val groupOwnerAddress: String = ""

    sendConnectIntent(isConnect, isGroupOwner, groupOwnerAddress)
  }

  def connect()(implicit oIStream: ObjectInputStream, oOStream: ObjectOutputStream): Unit = {
    send(Protocol.ACK)

    val jsonWifiP2pConfig = Json.parse(receive())
    implicit val wpsInfoFormat = Json.format[WpsInfo]
    implicit val wifiP2pConfigFormat = Json.format[WifiP2pConfig]

    wifiP2pConfig = Json.fromJson[WifiP2pConfig](jsonWifiP2pConfig).get

    if (isConnected) {
      send(Protocol.CARTON)
      return
    }

    val deviceToConnect = neighbors.filter(n => n.weaveIp == wifiP2pConfig.deviceAddress && n.weaveIp != weaveIp)

    if (deviceToConnect.isEmpty) {
      send(Protocol.CARTON)
      return
    }

    send(Protocol.ACK)

    val isConnect = true
    var isGroupOwner = true
    var groupOwnerAddress: String = weaveIp

    if (wifiP2pConfig.groupOwnerIntent < 7) {
      sendConnectIntent(isConnect, isGroupOwner, groupOwnerAddress)
      node.tell(Connect(weaveIp, wifiP2pConfig.deviceAddress, groupOwnerAddress), ActorRef.noSender)
      return
    }

    isGroupOwner = false
    groupOwnerAddress = wifiP2pConfig.deviceAddress

    sendConnectIntent(isConnect, isGroupOwner, groupOwnerAddress)
    node.tell(Connect(weaveIp, wifiP2pConfig.deviceAddress, groupOwnerAddress), ActorRef.noSender)
  }

  def connectExt(weaveIpFrom: String, groupOwnerIp: String): Unit = {
    val isConnect = true
    val isGroupOwner =
      weaveIp == groupOwnerIp

    if (isConnected) {
      return
    }

    isConnected = true
    connectedTo = weaveIpFrom

    sendConnectIntent(isConnect, isGroupOwner, groupOwnerIp)
  }

  def requestPeers()(implicit oOStream: ObjectOutputStream): Unit = {
    send(Protocol.ACK)

    implicit val deviceFormat = Json.format[Device]

    val json = Json.toJson(devices)
    println(json)
    send(json.toString())
  }

  def discoverServices()(implicit oIStream: ObjectInputStream, oOStream: ObjectOutputStream): Unit = {
    send(Protocol.ACK)

    isDiscoverable = true
    node.tell(Discoverable(true), ActorRef.noSender)
    node.tell(Request("Neighbors"), ActorRef.noSender)

    // Receive DnsSdServiceResponse from emulator
    var str = receive()
    val jsonDnsSdServiceResponse = Json.parse(str)
    implicit val deviceFormat = Json.format[Device]
    implicit val dnsSdServiceResponseFormat = Json.format[DnsSdServiceResponse]
    dnsSdServiceResponse = Json.fromJson[DnsSdServiceResponse](jsonDnsSdServiceResponse).get
    dnsSdServiceResponse.srcDevice = device

    send(Protocol.ACK)

    // Receive DnsSdTxtRecord from emulator
    str = receive()
    val jsonDnsSdTxtRecord = Json.parse(str)
    implicit val dnsSdTxtRecordFormat = Json.format[DnsSdTxtRecord]
    dnsSdTxtRecord = Json.fromJson[DnsSdTxtRecord](jsonDnsSdTxtRecord).get
    dnsSdTxtRecord.srcDevice = device

    send(Protocol.ACK)

    node.tell(Service(dnsSdServiceResponse.instanceName, dnsSdServiceResponse.registrationType, dnsSdTxtRecord.fullDomainName, dnsSdTxtRecord.txtRecordMap, dnsSdServiceResponse.srcDevice.deviceAddress), ActorRef.noSender)

    // Send DnsSdServiceResponse to emulator
    var dnsSdServiceResponses: List[DnsSdServiceResponse] = List()
    var dnsSdTxtRecords: List[DnsSdTxtRecord] = List()

    for (s <- services) {
      val srcDevice = Device(s"N${s.srcDevice.replace(".", "")}", s.srcDevice)
      dnsSdServiceResponses = DnsSdServiceResponse(s.instanceName, s.registrationType, srcDevice) :: dnsSdServiceResponses
      dnsSdTxtRecords = DnsSdTxtRecord(s.fullDomainName, s.txtRecordMap, srcDevice) :: dnsSdTxtRecords
    }

    val jsonDnsSdServiceResponses: JsValue = Json.toJson(dnsSdServiceResponses)
    println(jsonDnsSdServiceResponses.toString())
    send(jsonDnsSdServiceResponses.toString())

    var ack = receive()
    if (Protocol.ACK != ack) {
      println(s"[${Calendar.getInstance().getTime}] Error when sending dnsSdServiceResponses")

      return
    }

    // Send DnsSdTxtRecord to emulator
    val jsonDnsSdTxtRecords = Json.toJson(dnsSdTxtRecords)
    send(jsonDnsSdTxtRecords.toString())

    ack = receive()
    if (Protocol.ACK != ack) {
      println(s"[${Calendar.getInstance().getTime}] Error when sending dnsSdTxtRecords")
    }
  }

  def unknown(u: Any): Unit = {
    println(s"[${Calendar.getInstance().getTime}] Unknown: $u")
  }
}