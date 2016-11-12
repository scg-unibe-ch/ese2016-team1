<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:import url="template/header.jsp" />

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>
<link rel="stylesheet" href="https://unpkg.com/flatpickr/dist/flatpickr.min.css">
<script src="https://unpkg.com/flatpickr"></script>



<script src="/js/pictureUpload.js"></script>


<script>
	$(document).ready(function() {
		
		// Go to controller take what you need from user
		// save it to a hidden field
		// iterate through it
		// if there is id == x then make "Bookmark Me" to "bookmarked"
		
		$("#field-city").autocomplete({
			minLength : 2
		});
		$("#field-city").autocomplete({
			source : <c:import url="getzipcodes.jsp" />
		});
		$("#field-city").autocomplete("option", {
			enabled : true,
			autoFocus : true
		});
		$("#field-moveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-moveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		
		flatpickr(".flatpickr", {
		    enableTime: true
		});
		
		$("#field-visitDay").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		

		$("#addbutton").click(function() {
			var text = $("#roomFriends").val();
			var alreadyAdded = $("#addedRoommates").html();
			if(validateForm(text)) {
				$.post("/profile/placeAd/validateEmail",{email: text, alreadyIn: alreadyAdded}, function(data) {
					if(validateForm(data)) {
						var index = $("#roommateCell input.roommateInput").length;
						$("#roommateCell").append("<input class='roommateInput' type='hidden' name='registeredRoommateEmails[" + index + "]' value='" + data + "' />");
						$("#addedRoommates").append(data + "; ");
					} else {
						alert(data);
					}});
			}
			else {
				alert("Please enter an e-mail adress");
			}
			 
			// Validates the input for Email Syntax
			function validateForm(text) {
			    var positionAt = text.indexOf("@");
			    var positionDot = text.lastIndexOf(".");
			    if (positionAt< 1 || positionDot<positionAt+2 || positionDot+2>=text.length) {
			        return false;
			    } else {
			    	return true;
			    }
			}
		});
		
		$("#addVisitButton").click(function() {
			var date = $("#field-visitDay").val();
			if(date == ""){
				return;
			}
			
			var startHour = $("#startHour").val();
			var startMinutes = $("#startMinutes").val();
			var endHour = $("#endHour").val();
			var endMinutes = $("#endMinutes").val();
			
			if (startHour > endHour) {
				alert("Invalid times. The visit can't end before being started.");
				return;
			} else if (startHour == endHour && startMinutes >= endMinutes) {
				alert("Invalid times. The visit can't end before being started.");
				return;
			}
			
			var newVisit = date + ";" + startHour + ":" + startMinutes + 
				";" + endHour + ":" + endMinutes; 
			var newVisitLabel = date + " " + startHour + ":" + startMinutes + 
			" to " + endHour + ":" + endMinutes; 
			
			var index = $("#addedVisits input").length;
			
			var label = "<p>" + newVisitLabel + "</p>";
			var input = "<input type='hidden' value='" + newVisit + "' name='visits[" + index + "]' />";
			
			$("#addedVisits").append(label + input);
		});
	});
	
	function showSettings() {
		
		if (document.getElementById('saleType-Buy').checked) {
			$("#rentalPriceSettings").hide();
			$("#retailPriceSettings").show();
			$("#auctionSettings").hide();
		} else if (document.getElementById('saleType-Rent').checked) {
			$("#rentalPriceSettings").show();
			$("#retailPriceSettings").hide();
			$("#auctionSettings").hide();
		} else if (document.getElementById('saleType-Auction').checked) {
			$("#rentalPriceSettings").hide();
			$("#retailPriceSettings").hide();
			$("#auctionSettings").show();
		} 
	}
	
	$(document).ready(function() {
		showSettings();
	});
	
	function cloneRetailPrice(price) {
		$("#buyOutPrice").val(price);
		$("#retailPrice").val(price);
	}
	
	function validateNonTrivialRules() {
		if (document.getElementById("saleType-Rent").checked && $("#field-Prize").val() == 0) {
			$("#field-Prize_Error").show();
			return false;
		} else if (document.getElementById("saleType-Buy").checked && $("#retailPrice").val() == 0) {
			$("#retailPrice_Error").show();
			return false;
		} else if (document.getElementById("saleType-Auction").checked) {
			var invalid = false;
			if ($("#currentBidding").val() == 0) {
				invalid = true;
				$("#currentBidding_Error").show();
			} else {
				$("#currentBidding_Error").hide();
			}
			if (typeof($("#field-endOfAuction").val()) == 'undefined') {
				invalid = true;
				$("#field-endOfAuction_Error").show();
			} else {
				$("#field-endOfAuction_Error").hide();
			}
			if ($("#buyOutPrice").val() <= $("#currentBidding").val()) {
				invalid = true;
				$("#buyOutPrice_Error").show();
			} else {
				$("#buyOutPrice_Error").hide();
			}
			if (invalid)
				return false;
		}
		return true;
	}
</script>

<pre>
	<a href="/">Home</a>   &gt;   Place ad</pre>

<h1>Place an ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/placeAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data" onsubmit="return validateNonTrivialRules()">

	<fieldset>
		<legend>General info</legend>
		<table class="placeAdTable">
			<c:choose>
				<c:when test="${loggedIn}">
			
				<form:input id="field-buyer" path="currentBuyer"
						value="${loggedInUserEmail}"/>
				</c:when>	
			</c:choose>
			<tr>
				<td><label for="field-title">Ad Title</label></td>
				<td><label for="type-room">Type:</label></td>
			</tr>

			<tr>
				<td><form:input id="field-title" path="title" placeholder="Ad Title" /></td>
				<td><form:radiobutton id="type-room" path="roomType" value="Room" checked="checked" />Room
				 <form:radiobutton id="type-studio"	path="roomType" value="Studio" />Studio
				 <form:radiobutton id="type-flat"	path="roomType" value="Flat" />Flat
				 <form:radiobutton id="type-house"	path="roomType" value="House" />House</td>
				 <form:errors path="roomType" cssClass="validationErrorText" />
			</tr>

			<tr>
				<td><label for="field-street">Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
			</tr>

			<tr>
				<td><form:input id="field-street" path="street"
						placeholder="Street" /></td>
						<form:errors path="street" cssClass="validationErrorText" />
				<td><form:input id="field-city" path="city" placeholder="City" />
					<form:errors path="city" cssClass="validationErrorText" /></td>
			</tr>

			<tr>
				<td><label for="moveInDate">Move-in date</label></td>
				<form:errors path="moveInDate" cssClass="validationErrorText" />
				<td><label for="moveOutDate">Move-out date (optional)</label></td>
				<form:errors path="moveOutDate" cssClass="validationErrorText" />
			</tr>
			<tr>
				<td><form:input type="text" id="field-moveInDate"
						path="moveInDate" /></td>
				<td><form:input type="text" id="field-moveOutDate"
						path="moveOutDate" /></td>
			</tr>

			<tr>
				<td><label for="field-SquareFootage">Square Meters</label></td>
				<td><label for="field-SaleType">Sale Type</label>
			</tr>
			<tr>
				
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage" placeholder="Prize per month" step="5" /> <form:errors
						path="squareFootage" cssClass="validationErrorText" /></td>
				<td><form:radiobutton id="saleType-Rent" path="saleType" value="Rent" checked="checked" onclick="showSettings(this.value)"/>Rent
				 <form:radiobutton id="saleType-Buy"	path="saleType" value="Buy" onclick="showSettings(this.value)"/>Buy
				 <form:radiobutton id="saleType-Auction"	path="saleType" value="Auction" onclick="showSettings(this.value)"/>Auction
			</tr>
		</table>
	</fieldset>
	
	<br />
	<fieldset id="rentalPriceSettings">
		
		<legend>Rental Price</legend> 
		<table class="placeAdTable">
			<tr>
				<td><label for="field-Prize">Prize per month</label></td>
			</tr>
			<tr>
				<td>
					<form:input id="field-Prize" type="number" path="prize"
						placeholder="Prize per month" step="50" value="${ad.prizePerMonth }"/> 
						<span id="field-Prize_Error" class="validationErrorText" style="display:none">Should not be null</span>
				</td>
			</tr>

		</table>
	</fieldset>
	
	<fieldset id="retailPriceSettings" style="display:none">
		
		<legend>Retail Price</legend> 
		<table class="placeAdTable">
			<tr>
				<td><label for="retailPrice">Retail Price</label></td>
			</tr>
			<tr>
				<td><form:input id="retailPrice" type="number" path="retailPrice"
						placeholder="Retail Price" step="50" 
						onchange="cloneRetailPrice(this.value)"/>
						<span id="retailPrice_Error" class="validationErrorText" style="display:none">Should not be null</span> </td>
			</tr>

		</table>
	</fieldset>
	
	<fieldset id="auctionSettings" style="display:none">
		
		<legend>Auction Settings</legend> 
		<table class="placeAdTable">
			<tr>
				<td><label for="currentBidding">Start Bidding</label></td>
				<td><label for="endOfAuction">End Of Auction</label></td>
			</tr>
			<tr>
				<td><form:input id="currentBidding" type="number" path="currentBidding"
						placeholder="Start Bidding" step="50"/><span id="currentBidding_Error" class="validationErrorText" 
						style="display:none">Should not be null</span></td>
				<td><form:input type="text" class="flatpickr" id="field-endOfAuction"
						path="endOfAuction"/><span id="field-endOfAuction_Error" class="validationErrorText" 
						style="display:none">Should not be null</span></td>
			</tr>
			<tr>
				<td><label for="retailPrice">Buy Out Price</label></td>
			</tr>
			<tr>
				<td><form:input id="buyOutPrice" type="number" path="retailPrice"
						placeholder="Buy Out Price" step="50"  onchange="cloneRetailPrice(this.value)"/>
						<span id="buyOutPrice_Error" class="validationErrorText" 
						style="display:none">Must be bigger than start bidding</span></td>
			</tr>

		</table>
	</fieldset>


	<br />
	<fieldset>
		<legend>Room Description</legend>

		<table class="placeAdTable">
			<tr>
				<td><form:checkbox id="field-smoker" path="smokers" value="1" /><label>Animals
						allowed</label></td>
				<td><form:checkbox id="field-animals" path="animals" value="1" /><label>Smoking
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
				<td><form:checkbox id="field-internet" path="internet"
						value="1" /><label>WiFi available</label></td>
			</tr>

		</table>
		<br />
		<form:textarea path="roomDescription" rows="10" cols="100"
			placeholder="Room Description" />
		<form:errors path="roomDescription" cssClass="validationErrorText" />
	</fieldset>

	<br />
	<fieldset>
		<legend>Roommates (optional)</legend>
		<p>If your roommates have an account, simply add them by email.</p>

		<table class="placeAdTable">
			<tr>
				<td><label for="roomFriends">Add by email</label></td>
			</tr>

			<tr>
				<td id="roommateCell"><form:input type="text" id="roomFriends"
						path="roomFriends" placeholder="email" />

					<div id="addbutton" class="smallPlusButton">+</div></td>
			</tr>
			<tr>
				<td><p id="addedRoommates" path="addedRoommates">Added
						roommates:</p></td>
			</tr>
		</table>

		<br />
		<p>If the roommates do not have accounts or you wish to give
			further information, you can add a text in which you describe the
			roommates.</p>
		<br/>
		<form:textarea path="roommates" rows="10" cols="100"
			placeholder="Roommates" />
		<form:errors path="roommates" cssClass="validationErrorText" />
	</fieldset>

	<br />
	<fieldset>
		<legend>Preferences (optional)</legend>
		<form:textarea path="preferences" rows="5" cols="100"
			placeholder="Preferences"></form:textarea>
	</fieldset>

	<fieldset>
		<legend>Pictures (optional)</legend>
		<br /> <label for="field-pictures">Pictures</label> <input
			type="file" id="field-pictures" accept="image/*" multiple="multiple" />
		<table id="uploaded-pictures" class="styledTable">
			<tr>
				<th id="name-column">Uploaded picture</th>
				<th>Size</th>
				<th>Delete</th>
			</tr>
		</table>
		<br>
	</fieldset>

	<fieldset>
		<legend>Visiting times (optional)</legend>

		<table>
			<tr>
				<td><input type="text" id="field-visitDay" /> <select
					id="startHour">
						<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour
											+ "</option>");
								}
						%>
				</select> <select id="startMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select> <span>to&thinsp; </span> <select id="endHour">
						<%
							for (int i = 0; i < 24; i++) {
									String hour = String.format("%02d", i);
									out.print("<option value=\"" + hour + "\">" + hour
											+ "</option>");
								}
						%>
				</select> <select id="endMinutes">
						<%
							for (int i = 0; i < 60; i++) {
									String minute = String.format("%02d", i);
									out.print("<option value=\"" + minute + "\">" + minute
											+ "</option>");
								}
						%>
				</select>



					<div id="addVisitButton" class="smallPlusButton">+</div>

					<div id="addedVisits"></div></td>

			</tr>

		</table>
		<br>
	</fieldset>



	<br />
	<div>
		<button type="submit">Submit</button>
		<a href="/"><button type="button">Cancel</button></a>
	</div>

</form:form>

<c:import url="template/footer.jsp" />
