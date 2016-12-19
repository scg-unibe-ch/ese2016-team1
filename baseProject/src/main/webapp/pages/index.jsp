<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<!-- Include Leaflet -->
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.0.2/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.0.2/dist/leaflet.js"></script>
<script src="https://d3js.org/d3.v4.min.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to SwissHomes</title>
</head>
<body id="indexBody">

<div id="indexMap"> 
<p id="indexDescription">Click on the map to quick-search alerts in specified radius</p>

</div>

<script>
	$(document).ready(function() {
		$("#city").autocomplete({
			minLength : 2
		});
		$("#city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});		
		
		$("#field-earliestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-latestMoveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-earliestMoveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-latestMoveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
				
		var price = document.getElementById('prizeInput');
		var radius = document.getElementById('radiusInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>

<script>
function validateType(form)
{
	var room = document.getElementById('room');
	var studio = document.getElementById('studio');
	var flat = document.getElementById('flat');
	var house = document.getElementById('house');
	var neither = document.getElementById('neither');
	var buy = document.getElementById('buy');
	var rent = document.getElementById('rent');
	var neitherBuyRent = document.getElementById('neitherBuyRent');
	var filtered = document.getElementById('filtered');
	
	if(!room.checked && !studio.checked && !flat.checked && !house.checked)
		neither.checked = true;
	else
		neither.checked = false;
	
	if(!buy.checked && !rent.checked)
		neitherBuyRent.checked = true;
	else
		neitherBuyRent.checked = false;
	
	filtered.checked = true;
}
</script>

<script>
	//remove padding from content
	$("#content").css("padding", "0")
	$("#content").css("padding-top", "10px")
</script>

<%-- Clone whole searchAd form. This way we have only duplicates in jsp files --%>
<form:form method="post" modelAttribute="searchForm" action="/results"
	id="searchFormIndex" autocomplete="off" style="display:none">
	<fieldset>
		<form:checkbox name="buy" id="buy" path="buy" checked="checked"/><label>Buy</label>
		<form:checkbox name="rent" id="rent" path="rent" checked="checked"/><label>Rent</label>		
				
		<form:checkbox name="room" id="room" path="roomHelper" checked="checked"/><label>Room</label>
		<form:checkbox name="studio" id="studio" path="studioHelper" checked="checked"/><label>Studio</label>
		<form:checkbox name="flat" id="flat" path="flatHelper" checked="checked"/><label>Flat</label>
		<form:checkbox name="house" id="house" path="houseHelper" checked="checked"/><label>House</label>
		
		<form:input type="text" name="city" id="city" path="city"
			value="<<from coordinates>>" tabindex="3" />		

		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		<form:input id="longitude" type="number" path="longitude"/>
		<form:input id="latitude" type="number" path="latitude"/>
		<form:input id="prizeInput" type="number" path="prize"
			value="5000000" step="50" />
					
		<form:input type="text" id="field-earliestMoveInDate" path="earliestMoveInDate" />
		<form:input type="text" id="field-earliestMoveOutDate" path="earliestMoveOutDate" />
		<form:input type="text" id="field-latestMoveInDate" path="latestMoveInDate" />
		<form:input type="text" id="field-latestMoveOutDate" path="latestMoveOutDate" />
		
		<form:checkbox id="field-smoker" path="smokers" value="1" /><label>Smoking inside allowed</label>
		<form:checkbox id="field-animals" path="animals" value="1" /><label>Animals	inside allowed</label>
		<form:checkbox id="field-garden" path="garden" value="1" /><label>Garden (co-use)</label>
		<form:checkbox id="field-balcony" path="balcony" value="1" /><label>Balcony	or Patio</label>
		<form:checkbox id="field-cellar" path="cellar" value="1" /><label>Cellar or Attic</label>
		<form:checkbox id="field-furnished" path="furnished" value="1" /><label>Furnished</label>
		<form:checkbox id="field-cable" path="cable" value="1" /><label>Cable TV</label>
		<form:checkbox id="field-garage" path="garage" value="1" /><label>Garage</label>
		<form:checkbox id="field-internet" path="internet" value="1" /><label>WiFi</label>

	</fieldset>

</form:form>

<script src="/js/index.js"></script>

<c:import url="template/footer.jsp" /><br />