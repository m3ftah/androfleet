var config = require('./config');

var URL = config.url;
var markers = [];
var circles = [];
var _map;
var isMapInitialized = false;
var isIntervalSetted = false;

function computeCenter(positions) {
  if (positions.length == 0) {
    return {lat: 0, lng: 0};
  }

  var latitude = 0;
  var longitude = 0;

  for (var i = 0; i < positions.length; i++) {
    var position = positions[i];

    latitude += position.lat;
    longitude += position.lon;
  }

  latitude /= positions.length;
  longitude /= positions.length;

  return {lat: latitude, lng: longitude};
}

function initMap() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function () {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
      var positions = JSON.parse(xhttp.responseText);
      var cntr = computeCenter(positions);

      if ((isMapInitialized === false)) { // || ((cntr.lat !== _map.center.lat()) && (cntr.lng !== _map.center.lng()))) {
        console.log("Drawing map...");

        _map = new google.maps.Map(document.getElementById('map'), {
          zoom: 14,
          center: cntr
        });


        isMapInitialized = true;
      }

      deleteMarkers();
      deleteCircles();
      for (var i = 0; i < positions.length; i++) {
        var position = positions[i];
        addMarker(position);
        addCircle(position);
      }
    }
  };

  xhttp.open('GET', URL + '/locations');
  xhttp.send();

  if (isIntervalSetted === false) {
    // refresh every 30 sec
    setInterval(initMap, 30000);
    isIntervalSetted = true;
  }
}

function clearCircles() {
    for (var i = 0; i < circles.length; i++) {
      circles[i].setMap(null);
    }
}

function clearMarkers() {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
}

function deleteMarkers() {
  clearMarkers();
  markers = [];
}

function deleteCircles() {
    clearCircles();
    circles = [];
}

function addCircle(pos) {
    var circle = new google.maps.Circle({
      strokeColor: '#ff8080',
      strokeOpacity: 0.8,
      strokeWeight: 2,
      fillColor: '#ff8080',
      fillOpacity: 0.35,
      center: {lat: pos.lat, lng: pos.lon},
      radius: 200,
      map: _map
    });

    circles.push(circle);
}

function addMarker(pos) {
  var marker = new google.maps.Circle({
    strokeColor: '#ff8080',
    strokeOpacity: 1,
    fillColor: '#ff8080',
    fillOpacity: 1,
    radius: 5,
    center: {lat: pos.lat, lng: pos.lon},
    map: _map
  });

  markers.push(marker);
}
