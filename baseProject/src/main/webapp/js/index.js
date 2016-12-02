var RADIUS = 100;
    var clicked = false;
    var hoverCircle = null;
    
	var mymap = L.map('indexMap').setView([46.777, 8.157], 8);

	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw', {
		maxZoom: 18,
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'mapbox.streets'
	}).addTo(mymap);
    
    //set center
    mymap.panTo(new L.LatLng(46.777, 8.157));
    
    // do this to automatically generate svg element
    L.circle([0,0], 0).addTo(mymap);
    
    initializeHoverCircle()
    
    mymap.on('click', onMapClick);
    
    mymap.on('mousemove', function(e) {
    	if (!clicked)
    		moveHoverCircle(e.latlng);
    });
        
    function initializeHoverCircle() {        
        d3.selectAll("#hoverCircle").remove()
        
        hoverCircle = d3.select("#indexMap").select("svg")
            .append("circle")
            .attr("class", "hoverCircle")
    }
    
    function moveHoverCircle(latLng) {
        d3.select(".hoverCircle")
            .attr("cx", mymap.latLngToLayerPoint(latLng).x)
            .attr("cy", mymap.latLngToLayerPoint(latLng).y)
            .attr("r", RADIUS)
    }

	function onMapClick(e) {
        latLng = mymap.latLngToLayerPoint(e.latlng)
        
        d3.selectAll(".searchCircle").remove()
        
        var circle = d3.select("#indexMap").select("svg")
            .append("circle")
            .attr("cx", latLng.x)
            .attr("cy", latLng.y)
            .attr("r", 0)
            .attr("class", "searchCircle")
        
        circle.transition().attr("r", RADIUS)
        
        console.log(mymap.latLngToLayerPoint(e.latlng))
        console.log("radius: " + calculateRadiusPxInKm(e.latlng))
        
        // remove hovercircle and don't move hoverCircle from now
        d3.select(".hoverCircle").remove()
        clicked = true;
        
        setTimeout(function() {
        	$.get("/getResultsFromMap?coordinates=" + [e.latlng.lat, e.latlng.lng] + "&radius=" 
        			+ calculateRadiusPxInKm(e.latlng));
        },500)
	}
    
    function calculateRadiusPxInKm(latlng) {
        var centerLatLng = latlng;
        var pointC = mymap.latLngToContainerPoint(centerLatLng); // convert to containerpoint (pixels)
        var pointX = [pointC.x + 100, pointC.y]; // add one pixel to x

        // convert containerpoints to latlng's
        var latLngC = mymap.containerPointToLatLng(pointC);
        var latLngX = mymap.containerPointToLatLng(pointX);

        var distanceX = latLngC.distanceTo(latLngX); // calculate distance between c and x (latitude)
        return distanceX
    }