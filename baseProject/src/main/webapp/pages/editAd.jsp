<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="template/header.jsp" />

<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>
<link rel="stylesheet" href="https://unpkg.com/flatpickr/dist/flatpickr.min.css">
<script src="https://unpkg.com/flatpickr"></script>

<script src="/js/pictureUploadEditAd.js"></script>

<script src="/js/editAd.js"></script>



<script>
	$(document).ready(function() {		
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
		
		var timepickr = flatpickr("#field-endOfAuction", {
		    enableTime: true
		});
		
		//close flatpickr onmoveout
		$(".flatpickr-calendar").on("mouseleave", function() {
			timepickr.close();
		})
		
		flatpickr("#field-moveInDate", {
		    enableTime: false,
		});
		
		flatpickr("#field-moveOutDate", {
		    enableTime: false
		});
		
		flatpickr("#field-visitDay", {
		    enableTime: false
		});
		
		
		$("#addbutton").click(function() {
			var text = $("#roomFriends").val();
			var alreadyAdded = $("#addedRoommates").html();
			if(validateForm(text)) {
				$.post("/profile/placeAd/validateEmail",{email: text, alreadyIn: alreadyAdded}, function(data) {
					if(validateForm(data)) {
						// length gibt die Anzahl der Elemente im input.roommateInput an. Dieser wird in index geschrieben und iteriert.
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
		
		$(".deleteRoommateButton").click(function()  {
			var userId = $(this).attr("data-user-id");
			var adId = $(this).attr("data-ad-id");
			var row = $(this).parent().parent();
			$.post("/profile/editAd/deleteRoommate", {userId: userId, adId: adId}, function() {
				$(row).animate({opacity: 0}, 300, function() {$(row).remove(); } );
			});
		
		});
	});
	
	function showSettings() {
		
		if (document.getElementById('saleType-Buy').checked) {
			$("#rentalPriceSettings").hide();
			$("#retailPriceSettings").show();
			$("#auctionSettings").hide();
			$("#roommates").hide();
		} else if (document.getElementById('saleType-Rent').checked) {
			$("#rentalPriceSettings").show();
			$("#retailPriceSettings").hide();
			$("#auctionSettings").hide();
			$("#roommates").show();
		} else if (document.getElementById('saleType-Auction').checked) {
			$("#rentalPriceSettings").hide();
			$("#retailPriceSettings").hide();
			$("#auctionSettings").show();
			$("#roommates").hide();
		} 
	}
	
	$(document).ready(function() {
		showSettings();
	});
	
	function cloneRetailPrice(price) {
		$("#buyOutPrice").val(price);
		$("#retailPrice").val(price);
	}
	
	//Handles problems by displaying error-messages.
	function setUpDefaultValues(form)
	{
		//Inputs
		var endOfAuction = document.getElementById('field-endOfAuction');
		var prize = document.getElementById('field-Prize');
		var retailPrize = document.getElementById('retailPrice')
		var currentBidding = document.getElementById('buyOutPrice');
		var squareFootage = document.getElementById('field-SquareFootage');
		
		//Radiobuttons
		var rent = document.getElementById('saleType-Rent');
		var buy = document.getElementById('saleType-Buy');
		var auction = document.getElementById('saleType-Auction');

		if(prize.value == null || prize.value == "")
			prize.value = 0;
		if(retailPrize.value == null || retailPrize.value == "")
			retailPrize.value = 0;
		if(currentBidding.value == null || currentBidding.value == "")
			currentBidding.value = 0;
		if(squareFootage.value == null || squareFootage.value == "")
			squareFootage.value = 0;
		
		if(rent.checked == true) {
			endOfAuction.value = "2000-01-01 12:00";
			retailPrize.value = 0;
			currentBidding.value = 0;
		}
		
		if(buy.checked == true) {
			endOfAuction.value = "2000-01-01 12:00";
			prize.value = 0;
			currentBidding.value = 0;
		}
		
		if(auction.checked == true) {
			prize.value = 0;
		}
	}
</script>

<!-- format the dates -->
<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="yyyy-MM-dd" />
<fmt:formatDate value="${ad.moveOutDate}" var="formattedMoveOutDate"
	type="date" pattern="yyyy-MM-dd" />
<fmt:formatDate value="${ad.endOfAuction}" var="formattedEndOfAuctionDate"
	type="date" pattern="yyyy-MM-dd HH:mm" />	

<fmt:formatDate value="${placeFormAd.moveInDate}" var="formattedMoveInDate2"
	type="date" pattern="yyyy-MM-dd" />
<fmt:formatDate value="${placeFormAd.moveOutDate}" var="formattedMoveOutDate2"
	type="date" pattern="yyyy-MM-dd" />
<fmt:formatDate value="${placeFormAd.endOfAuction}" var="formattedEndOfAuctionDate2"
	type="date" pattern="yyyy-MM-dd HH:mm" />	

	
<pre><a href="/">Home</a>   &gt;   <a href="/profile/myRooms">My Rooms</a>   &gt;   <a href="/ad?id=${ad.id}">Ad Description</a>   &gt;   Edit Ad</pre>


<h1>Edit Ad</h1>
<hr />

<form:form method="post" modelAttribute="placeAdForm"
	action="/profile/editAd" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data"
	novalidate="novalidate">

<input type="hidden" name="adId" value="${ad.id }" />

	<fieldset>
		<legend>Change General info</legend>
		<table class="placeAdTable">
			<tr>
				<td><label for="field-title">Ad Title</label></td>
				<td><label for="type-room">Type:</label></td>
			</tr>

			<tr>
			
			<c:choose>
				<c:when test="${ad.altId != 0}">
				
				<td><form:input id="field-title" path="title" value="${ad.title}" />				
					<form:errors path="title" cssClass="validationErrorText" /></td>
				<td>
				

				
					<c:choose>
						<c:when test="${ad.roomType == 'Room'}">
							<form:radiobutton id="type-room" path="roomType" value="Room" checked="checked" />Room 
							<form:radiobutton id="type-studio" path="roomType" value="Studio" />Studio 
							<form:radiobutton id="type-studio" path="roomType" value="Flat" />Flat 
							<form:radiobutton id="type-studio" path="roomType" value="House" />House
						</c:when>
						<c:when test="${ad.roomType == 'Studio'}">
							<form:radiobutton id="type-room" path="roomType" value="Room" />Room 
							<form:radiobutton id="type-studio" path="roomType" value="Studio" checked="checked" />Studio 
							<form:radiobutton id="type-studio" path="roomType" value="Flat" />Flat 
							<form:radiobutton id="type-studio" path="roomType" value="House" />House
						</c:when>
						<c:when test="${ad.roomType == 'Flat'}">
							<form:radiobutton id="type-room" path="roomType" value="Room" />Room 
							<form:radiobutton id="type-studio" path="roomType" value="Studio" />Studio 
							<form:radiobutton id="type-studio" path="roomType" value="Flat" checked="checked" />Flat 
							<form:radiobutton id="type-studio" path="roomType" value="House" />House
						</c:when>
						<c:otherwise>
							<form:radiobutton id="type-room" path="roomType" value="Room" />Room 
							<form:radiobutton id="type-studio" path="roomType" value="Studio" />Studio 
							<form:radiobutton id="type-studio" path="roomType" value="Flat" />Flat 
							<form:radiobutton id="type-studio" path="roomType" value="House" checked="checked" />House
						</c:otherwise>
					</c:choose>
					
					
					
				</c:when>
				<c:otherwise>
				
				<td><form:input id="field-title" path="title" value="${placeAdForm.title}" />				
					<form:errors path="title" cssClass="validationErrorText" /></td>
				<td>
				
					<c:choose>
						<c:when test="${placeAdForm.roomType == 'Room'}">
							<form:radiobutton id="type-room" path="roomType" value="Room" checked="checked" />Room 
							<form:radiobutton id="type-studio" path="roomType" value="Studio" />Studio 
							<form:radiobutton id="type-studio" path="roomType" value="Flat" />Flat 
							<form:radiobutton id="type-studio" path="roomType" value="House" />House
						</c:when>
						<c:when test="${placeAdForm.roomType == 'Studio'}">
							<form:radiobutton id="type-room" path="roomType" value="Room" />Room 
							<form:radiobutton id="type-studio" path="roomType" value="Studio" checked="checked" />Studio 
							<form:radiobutton id="type-studio" path="roomType" value="Flat" />Flat 
							<form:radiobutton id="type-studio" path="roomType" value="House" />House
						</c:when>
						<c:when test="${placeAdForm.roomType == 'Flat'}">
							<form:radiobutton id="type-room" path="roomType" value="Room" />Room 
							<form:radiobutton id="type-studio" path="roomType" value="Studio" />Studio 
							<form:radiobutton id="type-studio" path="roomType" value="Flat" checked="checked" />Flat 
							<form:radiobutton id="type-studio" path="roomType" value="House" />House
						</c:when>
						<c:otherwise>
							<form:radiobutton id="type-room" path="roomType" value="Room" />Room 
							<form:radiobutton id="type-studio" path="roomType" value="Studio" />Studio 
							<form:radiobutton id="type-studio" path="roomType" value="Flat" />Flat 
							<form:radiobutton id="type-studio" path="roomType" value="House" checked="checked" />House
						</c:otherwise>
					</c:choose>
				</c:otherwise>	
				
				</c:choose>
					
					
					
			</tr>

			<tr>
				<td><label for="field-street">Street</label></td>
				<td><label for="field-city">City / Zip code</label></td>
			</tr>

			<tr>
			
			
		<c:choose>
		<c:when test="${ad.altId != 0}">
			
				<td><form:input id="field-street" path="street"	value="${ad.street}" />
					<form:errors path="street" cssClass="validationErrorText" /></td>
				<td>
					<form:input id="field-city" path="city" value="${ad.zipcode} - ${ad.city}" />
					<form:errors path="city" cssClass="validationErrorText" />
				</td>
		
		</c:when>
		<c:otherwise>
				<td><form:input id="field-street" path="street"
						value="${placeAdForm.getStreet()}" />
					<form:errors path="street" cssClass="validationErrorText" /></td>
				<td>
					<form:input id="field-city" path="city" value="${placeAdForm.getCity()}" />
					<form:errors path="city" cssClass="validationErrorText" />
				</td>
		</c:otherwise>		
		</c:choose>		
				
				
			</tr>

			<tr>
				<td><label for="moveInDate">Move-in date</label></td>
				<td><label for="moveOutDate">Move-out date (optional)</label></td>
			</tr>
			<tr>
			
			<c:choose>
				<c:when test="${ad.altId != 0}">
			
				<td>
					<form:input type="text" id="field-moveInDate"
						path="moveInDate" value="${formattedMoveInDate }" placeholder="Choose a date"/>
					<form:errors path="moveInDate" cssClass="validationErrorText" />
				</td>
				<td>
					<form:input type="text" id="field-moveOutDate"
						path="moveOutDate" value="${formattedMoveOutDate }" placeholder="Choose a date"/>
				</td>
				
				</c:when>
				<c:otherwise>
				
				<td>
					<form:input type="text" id="field-moveInDate"
						path="moveInDate" value="${formattedMoveInDate2 }" placeholder="Choose a date"/>
					<form:errors path="moveInDate" cssClass="validationErrorText" />
				</td>
				<td>
					<form:input type="text" id="field-moveOutDate"
						path="moveOutDate" value="${formattedMoveOutDate2 }" placeholder="Choose a date"/>
				</td>
				
				</c:otherwise>
			</c:choose>	
				
				
			</tr>
			<tr>
				<td><label for="field-SquareFootage">Square Meters</label></td>
				<td><label for="field-SaleType">Sale Type</label>
			</tr>
			<tr>
			
			<c:choose>
				<c:when test="${ad.altId != 0}">
			
			
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage" value="${ad.squareFootage}" placeholder="Prize per month" step="5" /> <form:errors
						path="squareFootage" cssClass="validationErrorText" /></td>
						
				<c:choose>
					<c:when test="${ad.saleType == 'Rent'}">
						<td><form:radiobutton id="saleType-Rent" path="saleType" value="Rent" checked="checked" onclick="showSettings(this.value)"/>Rent
				 		<form:radiobutton id="saleType-Buy"	path="saleType" value="Buy" onclick="showSettings(this.value)"/>Sell
				 		<form:radiobutton id="saleType-Auction"	path="saleType" value="Auction" onclick="showSettings(this.value)"/>Auction
					</c:when>
					<c:when test="${ad.saleType == 'Buy'}">
						<td><form:radiobutton id="saleType-Rent" path="saleType" value="Rent" onclick="showSettings(this.value)"/>Rent
				 		<form:radiobutton id="saleType-Buy"	path="saleType" value="Buy" checked="checked" onclick="showSettings(this.value)"/>Sell
				 		<form:radiobutton id="saleType-Auction"	path="saleType" value="Auction" onclick="showSettings(this.value)"/>Auction
					</c:when>
					<c:when test="${ad.saleType == 'Auction'}">
						<td><form:radiobutton id="saleType-Rent" path="saleType" value="Rent" onclick="showSettings(this.value)"/>Rent
				 		<form:radiobutton id="saleType-Buy"	path="saleType" value="Buy" onclick="showSettings(this.value)"/>Sell
				 		<form:radiobutton id="saleType-Auction"	path="saleType" value="Auction" checked="checked" onclick="showSettings(this.value)"/>Auction
					</c:when>
				</c:choose>
				
				
				</c:when>
				<c:otherwise>
				
				
				<td><form:input id="field-SquareFootage" type="number"
						path="squareFootage" value="${placeAdForm.squareFootage}" placeholder="Prize per month" step="5" /> <form:errors
						path="squareFootage" cssClass="validationErrorText" /></td>
						
				<c:choose>
					<c:when test="${placeAdForm.saleType == 'Rent'}">
						<td><form:radiobutton id="saleType-Rent" path="saleType" value="Rent" checked="checked" onclick="showSettings(this.value)"/>Rent
				 		<form:radiobutton id="saleType-Buy"	path="saleType" value="Buy" onclick="showSettings(this.value)"/>Sell
				 		<form:radiobutton id="saleType-Auction"	path="saleType" value="Auction" onclick="showSettings(this.value)"/>Auction
					</c:when>
					<c:when test="${placeAdForm.saleType == 'Buy'}">
						<td><form:radiobutton id="saleType-Rent" path="saleType" value="Rent" onclick="showSettings(this.value)"/>Rent
				 		<form:radiobutton id="saleType-Buy"	path="saleType" value="Buy" checked="checked" onclick="showSettings(this.value)"/>Sell
				 		<form:radiobutton id="saleType-Auction"	path="saleType" value="Auction" onclick="showSettings(this.value)"/>Auction
					</c:when>
					<c:when test="${placeAdForm.saleType == 'Auction'}">
						<td><form:radiobutton id="saleType-Rent" path="saleType" value="Rent" onclick="showSettings(this.value)"/>Rent
				 		<form:radiobutton id="saleType-Buy"	path="saleType" value="Buy" onclick="showSettings(this.value)"/>Sell
				 		<form:radiobutton id="saleType-Auction"	path="saleType" value="Auction" checked="checked" onclick="showSettings(this.value)"/>Auction
					</c:when>
				</c:choose>
				
				</c:otherwise>
			</c:choose>	
				
			</tr>
		</table>
	</fieldset>

<fieldset id="rentalPriceSettings">
		
		<legend>Rental Price</legend> 
		<table class="placeAdTable">
			<tr>
				<td><label for="field-Prize">Prize per month</label></td>
			</tr>
			<tr>
				<td>
				
				<c:choose>
				<c:when test="${ad.altId != 0}">
				
				
					<form:input id="field-Prize" type="number" path="prize" value="${ad.prizePerMonth}"
						placeholder="Prize per month" step="50"/>
						<form:errors path="prize" cssClass="validationErrorText" />
				
				</c:when>
				<c:otherwise>
				<form:input id="field-Prize" type="number" path="prize" value="${placeAdForm.prize}"
						placeholder="Prize per month" step="50"/>
						<form:errors path="prize" cssClass="validationErrorText" />
				
				</c:otherwise>
			</c:choose>	
				
				
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
			
			<c:choose>
				<c:when test="${ad.altId != 0}">
				
				
				<td><form:input id="retailPrice" type="number" path="retailPrice" value="${ad.retailPrice}"
						placeholder="Retail Price" step="50" onchange="cloneRetailPrice(this.value)"/> <form:errors
						path="retailPrice" cssClass="validationErrorText" /></td>
			
				</c:when>
				<c:otherwise>
				<td><form:input id="retailPrice" type="number" path="retailPrice" value="${placeAdForm.retailPrice}"
						placeholder="Retail Price" step="50" onchange="cloneRetailPrice(this.value)"/> <form:errors
						path="retailPrice" cssClass="validationErrorText" /></td>
				</c:otherwise>
			</c:choose>	
			
			
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
			
			
			<c:choose>
				<c:when test="${ad.altId != 0}">
				
			
				<td><form:input id="currentBidding" type="number" path="currentBidding" value="${ad.currentBidding}"
						placeholder="Start Bidding" step="50" /> <form:errors
						path="currentBidding" cssClass="validationErrorText" /></td>
				<td><form:input type="text" class="flatpickr" id="field-endOfAuction" placeholder="Choose a date" value="${formattedEndOfAuctionDate}"
						path="endOfAuction" />
						<form:errors path="endOfAuction" cssClass="validationErrorText" /></td>
			
			
				</c:when>
				<c:otherwise>
			
				<td><form:input id="currentBidding" type="number" path="currentBidding" value="${placeAdForm.currentBidding}"
						placeholder="Start Bidding" step="50" /> <form:errors
						path="currentBidding" cssClass="validationErrorText" /></td>
				<td><form:input type="text" class="flatpickr" id="field-endOfAuction" placeholder="Choose a date" value="${formattedEndOfAuctionDate2}"
						path="endOfAuction" />
						<form:errors path="endOfAuction" cssClass="validationErrorText" /></td>
			
				</c:otherwise>
			</c:choose>	
			
			
			
			
			
			</tr>
			<tr>
				<td><label for="buyOutPrice">Buy Out Price</label></td>
			</tr>
			<tr>
			
			<c:choose>
				<c:when test="${ad.altId != 0}">
				
			
				<td><form:input id="buyOutPrice" type="number" path="retailPrice"
						placeholder="Buy Out Price" step="50" value="${ad.retailPrice }"
						onchange="cloneRetailPrice(this.value)"/> <form:errors
						path="retailPrice" cssClass="validationErrorText" /></td>
			
				</c:when>
				<c:otherwise>
			
			
				<td><form:input id="buyOutPrice" type="number" path="retailPrice"
						placeholder="Buy Out Price" step="50" value="${ad.retailPrice }"
						onchange="cloneRetailPrice(this.value)"/> <form:errors
						path="retailPrice" cssClass="validationErrorText" /></td>
			
				</c:otherwise>
			</c:choose>	
			</tr>

		</table>
	</fieldset>


	<br />
	<fieldset>
		<legend>Change Room Description</legend>

		<table class="placeAdTable">
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.smokers}">
							<form:checkbox id="field-smoker" path="smokers" checked="checked" /><label>Smoking
							inside allowed</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-smoker" path="smokers" /><label>Smoking
							inside allowed</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.animals}">
							<form:checkbox id="field-animals" path="animals"  checked="checked" /><label>Animals
						allowed</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-animals" path="animals" /><label>Animals
						allowed</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.garden}">
							<form:checkbox id="field-garden" path="garden" checked="checked" /><label>Garden
							(co-use)</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-garden" path="garden" /><label>Garden
							(co-use)</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.balcony}">
							<form:checkbox id="field-balcony" path="balcony"  checked="checked" /><label>Balcony
						or Patio</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-balcony" path="balcony" /><label>Balcony
						or Patio</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.cellar}">
							<form:checkbox id="field-cellar" path="cellar" checked="checked" /><label>Cellar
						or Attic</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-cellar" path="cellar" /><label>Cellar
						or Atticd</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.furnished}">
							<form:checkbox id="field-furnished" path="furnished"  checked="checked" /><label>Furnished
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-furnished" path="furnished" /><label>Furnished</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.washingMachine}">
							<form:checkbox id="field-washingMachine" path="washingMachine" checked="checked" /><label>Washing Machine
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-washingMachine" path="washingMachine" /><label>Washing Machine
							</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.dishwasher}">
							<form:checkbox id="field-dishwasher" path="dishwasher"  checked="checked" /><label>Dishwasher
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-dishwasher" path="dishwasher" /><label>Dishwasher</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.cable}">
							<form:checkbox id="field-cable" path="cable" checked="checked" /><label>Cable TV</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-cable" path="cable" /><label>Cable TV</label>
						</c:otherwise>
					</c:choose>
				</td>
				
				<td>
					<c:choose>
						<c:when test="${ad.garage}">
							<form:checkbox id="field-garage" path="garage"  checked="checked" /><label>Garage
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-garage" path="garage" /><label>Garage</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${ad.internet}">
							<form:checkbox id="field-internet" path="internet"  checked="checked" /><label>WiFi available
							</label>
						</c:when>
						<c:otherwise>
							<form:checkbox id="field-internet" path="internet" /><label>WiFi available</label>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>

		</table>
		<br />
		<form:textarea path="roomDescription" rows="10" cols="100" value="${ad.roomDescription}" />
		<form:errors path="roomDescription" cssClass="validationErrorText" />
	</fieldset>


	<br />
	<fieldset id = "roommates">
		<legend>Change roommates</legend>
		
		<h3>Add new roommates</h3>
		<br />
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
				<td><p id="addedRoommates" path="addedRoommates">Newly added roommates: </p></td>
			</tr>
		</table>


		<p>Edit the description of the roommates:</p>
		<br />
		<form:textarea path="roommates" rows="10" cols="100"
			placeholder="Roommates" />
		<form:errors path="roommates" cssClass="validationErrorText" />
		<hr />
		<h3>Delete existing roommates</h3>
		<br />
		<table class="styledTable">
					<tr>
						<th>Username</th>
						<th>Delete</th>
					</tr>
					
					<c:choose>
					<c:when test="${ad.altId != 0}">
				
			
					
					
					<c:forEach var="user" items="${ad.registeredRoommates}">
							<tr>
								<td>${user.username}</td>
								<td><button type="button" data-user-id="${user.id}" data-ad-id="${ad.id}" class="deleteRoommateButton">Delete</button></td>
							</tr>
							<tr>
					</c:forEach>
					
					
					
					</c:when>
					<c:otherwise>
					
						<c:forEach var="user" items="${roomies}">
								<tr>
									<td>${user.username}</td>
									<td><button type="button" data-user-id="${user.id}" data-ad-id="${ad.id}" class="deleteRoommateButton">Delete</button></td>
								</tr>
								<tr>
						</c:forEach>
					
					</c:otherwise>
					</c:choose>
					
					
					
					
		</table>
	</fieldset>

	<br />
	<fieldset>
		<legend>Change preferences</legend>
		<form:textarea path="preferences" rows="5" cols="100"
			value="${ad.preferences}" ></form:textarea>
	</fieldset>

	
	<fieldset>
		<legend>Add visiting times</legend>
		
		<table>
			<tr>
				<td>
					<input type="text" id="field-visitDay" placeholder="Choose a date"/>
					
					<select id="startHour">
 					<% 
 						for(int i = 0; i < 24; i++){
 							String hour = String.format("%02d", i);
							out.print("<option value=\"" + hour + "\">" + hour + "</option>");
 						}
 					%>
					</select>
					
					<select id="startMinutes">
 					<% 
 						for(int i = 0; i < 60; i++){
 							String minute = String.format("%02d", i);
							out.print("<option value=\"" + minute + "\">" + minute + "</option>");
 						}
 					%>
					</select>
					
					<span>to&thinsp; </span>
					
					<select id="endHour">
 					<% 
 						for(int i = 0; i < 24; i++){
 							String hour = String.format("%02d", i);
							out.print("<option value=\"" + hour + "\">" + hour + "</option>");
 						}
 					%>
					</select>
					
					<select id="endMinutes">
 					<% 
 						for(int i = 0; i < 60; i++){
 							String minute = String.format("%02d", i);
							out.print("<option value=\"" + minute + "\">" + minute + "</option>");
 						}
 					%>
					</select>
			

					<div id="addVisitButton" class="smallPlusButton">+</div>
					
					<div id="addedVisits"></div>
				</td>
				
			</tr>
			
		</table>
		
		
		
		<table>
		<h3>My Visits</h3>
		

		<c:forEach var="tempVisit" items="${visis}">
			<tr>
				<td>${tempVisit}</td>
			</tr>
		</c:forEach>
		

		
		</table>
		
		
		
		<br>
	</fieldset>


	<br />

	<fieldset>
		<legend>Change pictures</legend>
		<h3>Delete existing pictures</h3>
		<br />
		<div>
		
		
			<c:choose>
			<c:when test="${ad.altId != 0}">
		
		
			<c:forEach items="${ad.pictures }" var="picture">
				<div class="pictureThumbnail">
					<div>
					<img src="${picture.filePath}" />
					</div>
					<button type="button" data-ad-id="${ad.id }" data-picture-id="${picture.id }">Delete</button>
				</div>
			</c:forEach>
			
			</c:when>
			<c:otherwise>
			
		
			<c:forEach items="${pics }" var="picture">
				<div class="pictureThumbnail">
					<div>
					<img src="${picture.filePath}" />
					</div>
					<button type="button" data-ad-id="${ad.id }" data-picture-id="${picture.id }">Delete</button>
				</div>
			</c:forEach>
			
			</c:otherwise>
			</c:choose>
			
			
		</div>
		<p class="clearBoth"></p>
		<br /><br />
		<hr />
		<h3>Add new pictures</h3>
		<br />
		<label for="field-pictures">Pictures</label> <input
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

	<div>
		<button type="submit" onClick="setUpDefaultValues(this.form)">Submit</button>
		<a href="<c:url value='/ad?id=${ad.id}' />"> 
			<button type="button">Cancel</button>
		</a>
	</div>

</form:form>


<c:import url="template/footer.jsp" />

