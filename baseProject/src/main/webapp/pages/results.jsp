<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Include Leaflet -->
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.0.2/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.0.2/dist/leaflet.js"></script>

<c:import url="template/header.jsp" />
<pre><a href="/">Home</a>   &gt;   <a href="/searchAd/">Search</a>   &gt;   Results</pre>

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
/*
 * This script takes all the resultAd divs and sorts them by a parameter specified by the user.
 * No arguments need to be passed, since the function simply looks up the dropdown selection.
 */
function sort_div_attribute() {
    //determine sort modus (by which attribute, asc/desc)
    var sortmode = $('#modus').find(":selected").val();   
    
    //only start the process if a modus has been selected
    if(sortmode.length > 0) {
    	var attname;
		
    	//determine which variable we pass to the sort function
		if(sortmode == "price_asc" || sortmode == "price_desc")
			attname = 'data-price';
	    else if(sortmode == "moveIn_asc" || sortmode == "moveIn_desc")	
			attname = 'data-moveIn';
	    else
			attname = 'data-age';
    	
		//copying divs into an array which we're going to sort
	    var divsbucket = new Array();
	    var divslist = $('div.resultAd');
	    var divlength = divslist.length;
	    for (a = 0; a < divlength; a++) {
			divsbucket[a] = new Array();
			divsbucket[a][0] = divslist[a].getAttribute(attname);
			divsbucket[a][1] = divslist[a];
			divslist[a].remove();
	    }
		
	    //sort the array
		divsbucket.sort(function(a, b) {
	    if (a[0] == b[0])
			return 0;
	    else if (a[0] > b[0])
			return 1;
        else
			return -1;
		});

	    //invert sorted array for certain sort options
		if(sortmode == "price_desc" || sortmode == "moveIn_asc" || sortmode == "dateAge_asc")
			divsbucket.reverse();
        
	    //insert sorted divs into document again
		for(a = 0; a < divlength; a++)
        	$("#resultsDiv").append($(divsbucket[a][1]));
	}
}
</script>

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
	});
</script>

<script src="/js/resultsOnMap.js"></script>

<div id="sortByWrapper">
<select id="modus" onchange="sort_div_attribute()">
    <option value="">Sort by:</option>
    <option value="price_asc">Price (ascending)</option>
    <option value="price_desc">Price (descending)</option>
    <option value="moveIn_desc">Move-in date (earliest to latest)</option>
    <option value="moveIn_asc">Move-in date (latest to earliest)</option>
    <option value="dateAge_asc">Date created (youngest to oldest)</option>
    <option value="dateAge_desc">Date created (oldest to youngest)</option>
</select>
</div>


<div id="indexWrapper">

<div id="mapSwitchWrapper">
	<span class="mapSwitch" id="mapSwitchResultsOpen" onclick="openMap()"><a>Show on Map</a></span>
	<span class="mapSwitch" id="mapSwitchResultsClose" onclick="closeMap()" style='display:none'><a>Show in List</a></span>
</div>


<c:choose>
	<c:when test="${empty results}"> 
		<p>No results found!
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="resultsDiv">
					
			<c:forEach var="ad" items="${results}">
				<div class="resultAd card" data-price="${ad.prizePerMonth}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<div class="leftTextRoomDescription">
							<h2>
								<a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
							</h2>
							<h4>${ad.street}, ${ad.zipcode} ${ad.city}</h4>
							<br />
							<h4>
								<i>${ad.roomType}</i>
							</h4>
						</div>
					</div>
					<div class="resultRight">
						<c:choose>
							<c:when test="${ad.saleType == 'Rent'}">
								<h2>Rent: CHF ${ad.prizePerMonth }</h2>
							</c:when>
							<c:when test="${ad.saleType == 'Buy'}">
								<h2>Buy: CHF ${ad.retailPrice }</h2>
							</c:when>
							<c:when test="${ad.saleType == 'Auction'}">
								<h2>Auction: CHF ${ad.currentBidding }</h2>
							</c:when>
						</c:choose>	
						<br /> <br />

						<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
							type="date" pattern="dd.MM.yyyy" />

						<h4>Move-in date: ${formattedMoveInDate }</h4>
					</div>
				</div>
			</c:forEach>
		</div>
		
		<div id="resultMapContainer" class="resultsDiv">
			<div id="resultMap" class="card"></div>
		</div>
		
		<c:forEach var="ad" items="${results}">
			<script type="text/javascript">
				var longitude = "${ad.longitude}";
				var latitude = "${ad.latitude}"
				var title = "${ad.title}"
				var url = '/ad?id=${ad.id}';
				addPoint(title, [parseFloat(latitude), parseFloat(longitude)], url);
			</script>
		</c:forEach>
	</c:otherwise>
</c:choose>

<form:form method="post" modelAttribute="searchForm" action="/results"
	id="filterForm" autocomplete="off">

	<div id="filterDiv" class="card">
		<h2>Filter results:</h2>		
		<form:checkbox name="buy" id="buy" path="buy" /><label>Buy</label>
		<form:checkbox name="rent" id="rent" path="rent" /><label>Rent</label>
		<form:errors path="noBuyNoRent" cssClass="validationErrorText" /><br />
		
		<form:checkbox style="display:none" name="neitherBuyRent" id="neitherBuyRent" path="noBuyNoRent" />
		
		<form:checkbox name="room" id="room" path="roomHelper" /><label>Room</label>
		<form:checkbox name="studio" id="studio" path="studioHelper" /><label>Studio</label>
		<form:checkbox name="flat" id="flat" path="flatHelper" /><label>Flat</label>
		<form:checkbox name="house" id="house" path="houseHelper" /><label>House</label>
	
		<form:checkbox style="display:none" name="neither" id="neither" path="noRoomNoStudio" />
		<form:checkbox style="display:none" name="filtered" id="filtered" path="filtered" />
		<form:errors path="noRoomNoStudio" cssClass="validationErrorText" /> <br />
	
		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" /><br />
			
		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> <label for="prize">Price (max.):</label>
		<form:input id="prizeInput" type="number" path="prize"
			placeholder="e.g. 5" step="50" />
		CHF
		<form:errors path="prize" cssClass="validationErrorText" /><br />
		
		<hr class="slim">		
		
		<table style="width: 80%">
			<tr>
				<td><label for="earliestMoveInDate">Earliest move-in date</label></td>
				<td><label for="earliestMoveOutDate">Earliest move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-earliestMoveInDate"
						path="earliestMoveInDate" /></td>
				<td><form:input type="text" id="field-earliestMoveOutDate"
						path="earliestMoveOutDate" /></td>
			</tr>
			<tr>
				<td><label for="latestMoveInDate">Latest move-in date</label></td>
				<td><label for="latestMoveOutDate">Latest move-out date (optional)</label></td>
			</tr>
			<tr>
				<td><form:input type="text" id="field-latestMoveInDate"
						path="latestMoveInDate" /></td>
				<td><form:input type="text" id="field-latestMoveOutDate"
						path="latestMoveOutDate" /></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-smoker" path="smokers" value="1" /><label>Smoking inside
						allowed</label></td>
				<td><form:checkbox id="field-animals" path="animals" value="1" /><label>Animals
						inside allowed</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-garden" path="garden" value="1" /><label>Garden
						(co-use)</label></td>
				<td><form:checkbox id="field-balcony" path="balcony" value="1" /><label>Balcony
						or Patio</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-cellar" path="cellar" value="1" /><label>Cellar
						or Attic</label></td>
				<td><form:checkbox id="field-furnished" path="furnished"
						value="1" /><label>Furnished</label></td>
			</tr>
			<tr>
				<td><form:checkbox id="field-cable" path="cable" value="1" /><label>Cable
						TV</label></td>
				<td><form:checkbox id="field-garage" path="garage" value="1" /><label>Garage</label>
				</td>
			</tr>
			<tr>
				<td><form:checkbox id="field-internet" path="internet" value="1" /><label>WiFi</label></td>
			</tr>
		</table>
			
		
		<button type="submit" onClick="validateType(this.form)">Filter</button>	
		<button type="reset">Cancel</button>
	</div>
</form:form>
</div>

<c:import url="template/footer.jsp" />