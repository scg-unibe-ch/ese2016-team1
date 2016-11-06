<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   Alerts</pre>

<script>
function deleteAlert(button) {
	var id = $(button).attr("data-id");
	$.get("/profile/alerts/deleteAlert?id=" + id, function(){
		$("#alertsDiv").load(document.URL + " #alertsDiv");
	});
}
</script>

<script>
function validateType(form)
{
	var room = document.getElementById('room');
	var studio = document.getElementById('studio');
	var flat = document.getElementById('flat');
	var house = document.getElementById('house');
	var neither = document.getElementById('neither');
	var both = document.getElementById('both');
	
	if(!room.checked && !studio.checked && !flat.checked && !house.checked)
		neither.checked = true;
	else 
		neither.checked = false;
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
		
		
		var price = document.getElementById('priceInput');
		var radius = document.getElementById('radiusInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>

<script>
function use(form, self)
{
	parent = $(self).parent().parent();
	
	var city = parent.find(".table_city").text();
	var price = parent.find(".table_price").text();
	price = parseInt(price.substring(0, price.length - 4));
	var radius = parent.find(".table_radius").text();
	radius = parseInt(radius.substring(0, radius.length - 3));
	
	var earliestMoveInDate = parent.find(".table_earliestMoveInDate").text();
	var latestMoveInDate = parent.find(".table_latestMoveInDate").text();
	var earliestMoveOutDate = parent.find(".table_earliestMoveOutDate").text();
	var latestMoveOutDate = parent.find(".table_latestMoveOutDate").text();
	
 	var room = parent.find(".table_roomType").text().indexOf("Room") >= 0;
	var studio = parent.find(".table_roomType").text().indexOf("Studio") >= 0;
	var flat = parent.find(".table_roomType").text().indexOf("Flat") >= 0;
	var house = parent.find(".table_roomType").text().indexOf("House") >= 0;
	var smokers = parent.find(".table_smokers").text().localeCompare("true") == 0;
	var garden = parent.find(".table_garden").text().localeCompare("true") == 0;
	var cellar = parent.find(".table_cellar").text().localeCompare("true") == 0;
	var cable = parent.find(".table_cable").text().localeCompare("true") == 0;
	var internet = parent.find(".table_internet").text().localeCompare("true") == 0;
	var animals = parent.find(".table_animals").text().localeCompare("true") == 0;
	var balcony = parent.find(".table_balcony").text().localeCompare("true") == 0;
	var furnished = parent.find(".table_furnished").text().localeCompare("true") == 0;
	var garage = parent.find(".table_garage").text().localeCompare("true") == 0;
	
	
	document.getElementById("city").value = city;	
	document.getElementById("priceInput").value = price;	
	document.getElementById("radiusInput").value = radius;
	
 	document.getElementById("field-room").checked = room;
	document.getElementById("field-studio").checked = studio;
	document.getElementById("field-flat").checked = flat;
	document.getElementById("field-house").checked = house;

	document.getElementById("city").value = city;	
	document.getElementById("priceInput").value = price;	
	document.getElementById("radiusInput").value = radius;
	
	document.getElementById("field-earliestMoveInDate").value = earliestMoveInDate;	
	document.getElementById("field-latestMoveInDate").value = latestMoveInDate;	
	document.getElementById("field-earliestMoveOutDate").value = earliestMoveOutDate;	
	document.getElementById("field-latestMoveOutDate").value = latestMoveOutDate; 
	document.getElementById("field-smokers").checked = smokers;
	document.getElementById("field-garden").checked = garden;
	document.getElementById("field-cellar").checked = cellar;
	document.getElementById("field-cable").checked = cable;
	document.getElementById("field-internet").checked = internet;
	document.getElementById("field-animals").checked = animals;
	document.getElementById("field-balcony").checked = balcony;
	document.getElementById("field-furnished").checked = furnished;
	document.getElementById("field-garage").checked = garage;
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
		
		var price = document.getElementById('priceInput');
		var radius = document.getElementById('radiusInput');
		
		if(price.value == null || price.value == "" || price.value == "0")
			price.value = "500";
		if(radius.value == null || radius.value == "" || radius.value == "0")
			radius.value = "5";
	});
</script>

<h1>Create and manage alerts</h1>
<hr />

<h2>Create new alert</h2><br />
<form:form method="post" modelAttribute="alertForm" action="/profile/alerts"
	id="alertForm" autocomplete="off">

	<fieldset>
		<form:checkbox name="room" id="field-room" path="room" /><label>Room</label>
		<form:checkbox name="studio" id="field-studio" path="studio" /><label>Studio</label>
		<form:checkbox name="flat" id="field-flat" path="flat" /><label>Flat</label>
		<form:checkbox name="house" id="field-house" path="house" /><label>House</label>
		
		<form:checkbox style="display:none" name="neither" id="neither" path="noRoomNoStudio" />
		<form:errors path="noRoomNoStudio" cssClass="validationErrorText" /><br />
		
		<label for="city">City / zip code:</label>
		<form:input type="text" name="city" id="city" path="city"
			placeholder="e.g. Bern" tabindex="3" />
		<form:errors path="city" cssClass="validationErrorText" />
		
		<label for="radius">Within radius of (max.):</label>
		<form:input id="radiusInput" type="number" path="radius"
			placeholder="e.g. 5" step="5" />
		km
		<form:errors path="radius" cssClass="validationErrorText" />
		<br /> <label for="price">Price (max.):</label>
		<form:input id="priceInput" type="number" path="price"
			placeholder="e.g. 5" step="50" />
		CHF
		<form:errors path="price" cssClass="validationErrorText" />
		<br />
		
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
				<td><form:checkbox id="field-smokers" path="smokers" value="1" /><label>Smoking inside
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

		<button type="submit" tabindex="7" onClick="validateType(this.form)">Subscribe</button>
		<button type="reset" tabindex="8">Cancel</button>
	</fieldset>

</form:form> <br />
<h2>Your active alerts</h2>

<div id="alertsDiv" class="alertsDiv">			
<c:choose>
	<c:when test="${empty alerts}">
		<p>You currently aren't subscribed to any alerts.
	</c:when>
	<c:otherwise>
		<table class="styledTable" id="alerts">
			<thead>
			<tr>
				<th>Type</th>
				<th>City</th>
				<th>Radius</th>
				<th>max. Price</th>
				<th>Earliest move-in</th>
				<th>Latest move-in</th>
				<th>Earliest move-out</th>
				<th>Latest move-out</th>
				<th>Smoking</th>
				<th>Garden</th>
				<th>Cellar or Attic</th>
				<th>Cable TV</th>
				<th>WiFi</th>
				<th>Animals</th>
				<th>Balcony/Patio</th>
				<th>Furnished</th>
				<th>Garage</th>
				<th>Action</th>
			</tr>
			</thead>
		<c:forEach var="alert" items="${alerts}">
			<tr>
				<td class="table_roomType">
					${alert.roomTypeString}
				</td>
				<td class="table_city">${alert.city}</td>
				<td class="table_radius">${alert.radius} km</td>
				<td class="table_price">${alert.price} CHF</td>
				<td class="table_earliestMoveInDate">${alert.earliestMoveInDate}</td>
				<td class="table_latestMoveInDate">${alert.latestMoveInDate}</td>
				<td class="table_earliestMoveOutDate">${alert.earliestMoveOutDate}</td>
				<td class="table_latestMoveOutDate">${alert.latestMoveOutDate}</td>
				<td class="table_smokers">${alert.smokers}</td>
				<td class="table_garden">${alert.garden}</td>
				<td class="table_cellar">${alert.cellar}</td>
				<td class="table_cable">${alert.cable}</td>
				<td class="table_internet">${alert.internet}</td>
				<td class="table_animals">${alert.animals}</td>
				<td class="table_balcony">${alert.balcony}</td>
				<td class="table_furnished">${alert.furnished}</td>
				<td class="table_garage">${alert.garage}</td>
				<td><button class="useButton" data-id="${alert.id}" onClick="use(this.form, this)">Use</button></td>
				<td><button class="deleteButton" data-id="${alert.id}" onClick="deleteAlert(this)">Delete</button></td>
			</tr>
		</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
</div>

<c:import url="template/footer.jsp" />