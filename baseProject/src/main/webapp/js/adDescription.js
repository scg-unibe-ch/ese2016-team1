$(document).ready(function() {
	
	var buttons = $("#visitList table tr button");
	
	//Makes the enquiry-button inactive after the user applied to a visit
	$(buttons).click(function() {
		var buttonText = $(this).attr("class");
		
		if (buttonText == 'thinInactiveButton') {
			return;
		}
		
		$("#content").children().animate({opacity: 0.4}, 300, function(){
			$("#confirmationDialog").css("display", "block");
			$("#confirmationDialog").css("opacity", "1");
		});
		
		var id = $(this).attr("data-id");
		
		$("#confirmationDialogSend").attr("data-id", id);
	});
	
	function reset(){
		$("#confirmationDialogSend").removeAttr("data-id");
		
		$("#confirmationDialog").css("display", "none");
		$("#confirmationDialog").css("opacity", "0");
		$("#content").children().animate({opacity: 1}, 300);
	}
	
	$("#confirmationDialogSend").click(function (){
		var id = $(this).attr("data-id");
		
		$.get("/profile/enquiries/sendEnquiryForVisit?id=" + id);
		
		
		var enquiryButton = $("#visitList table tr button[data-id='" + id + "']");
		$(enquiryButton).addClass('thinInactiveButton').removeClass('thinButton');
		$(enquiryButton).html('Enquiry sent');
		
		reset();
	});
	
	
	$("#confirmationDialogCancel").click(function (){
		reset();
	}); 
		
});

function drawMap(title, latLon) {
	console.log(title);
	var mymap = L.map('map').setView(latLon, 10);

    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
        '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="http://mapbox.com">Mapbox</a>',
        id: 'mapbox.streets'
    }).addTo(mymap);

    L.marker(latLon).addTo(mymap)
            .bindPopup(title).openPopup();


    var popup = L.popup();

    function onMapClick(e) {
        popup
                .setLatLng(e.latlng)
                .setContent("You clicked the map at " + e.latlng.toString())
                .openOn(mymap);
    }

    mymap.on('click', onMapClick);
}
