var mymap = null;
var points = [];

function openMap() {
	$("#resultsDiv").hide();
	$("#resultMapContainer").show();
	$("#mapSwitchResultsOpen").hide();
	$("#mapSwitchResultsClose").show();
	$("#modus").hide();
	if (mymap == null)
		initializeMap();	
}

function closeMap() {
	$("#resultsDiv").show();
	$("#resultMapContainer").hide();
	$("#mapSwitchResultsOpen").show();
	$("#mapSwitchResultsClose").hide();
	$("#modus").show(); 
}

function initializeMap() {
	
	mymap = L.map('resultMap').setView([0,0], 8);

    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
        '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="http://mapbox.com">Mapbox</a>',
        id: 'mapbox.streets'
    }).addTo(mymap);
        
    for (var i = 0; i < points.length; i++) {
        var popupText = points[i].title + "<br><a href='"+points[i].url+"'>Öffnen</a>";
	    L.marker(points[i].latLon).addTo(mymap)
		.bindPopup(popupText).openPopup();
    }
    
    mymap.panTo(new L.LatLng(46.777, 8.157));

}

function addPoint(title, latLon, url) {
	
	points.push({
		"title": title,
		"latLon": latLon,
		"url": url
	});
}