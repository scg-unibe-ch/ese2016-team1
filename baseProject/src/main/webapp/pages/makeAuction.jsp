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
		$("#field-moveInDate").datepicker({
			dateFormat : 'dd-mm-yy'
		});
		$("#field-moveOutDate").datepicker({
			dateFormat : 'dd-mm-yy'
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
		
		
		submit;
		
	});
</script>



<!-- format the dates -->
<fmt:formatDate value="${ad.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd-MM-yyyy" />
<fmt:formatDate value="${ad.moveOutDate}" var="formattedMoveOutDate"
	type="date" pattern="dd-MM-yyyy" />
	
<pre><a href="/">Home</a>   &gt;   <a href="/profile/myRooms">My Rooms</a>   &gt;   <a href="/ad?id=${ad.id}">Ad Description</a>   &gt;   Make Auction</pre>


<h1>Make Auction</h1>
<hr />

<form:form name="form1" method="post" modelAttribute="placeAdForm"
	action="/makeAuction" id="placeAdForm" autocomplete="off"
	enctype="multipart/form-data">

<input type="hidden" name="adId" value="${ad.id }" />

	<fieldset>
		<legend>Make Auction</legend>
		<table class="placeAdTable">
			<tr>
				<td><h2>Current Buyer</h2></td>
			</tr>	
				
			<tr>	
				<td><h2>Current Bidding</h2></td>
				<td>
				<form:input id="currentBidding" type="number" path="currentBidding"
						placeholder="Current Bidding" step="50" value="${ad.currentBidding }"/> <form:errors
						path="currentBidding" cssClass="validationErrorText"/>
				</td>
			</tr>	
			
			<tr>	
			
			
			
				<td><h2>Retail Price</h2></td>
				<c:choose>	
					<c:when test="${ad.retailPrice == 0}">
						<td>No Buy Out Possible	</td>
					</c:when>
					<c:otherwise>
						<td>${ad.retailPrice}&#32;CHF 
							
						</td>
						
					</c:otherwise>
				</c:choose>
				
				
				
			</tr>

			<tr>
				<td>
				<c:choose>
				<c:when test="${loggedIn}">
			
				<form:input id="field-buyer" path="currentBuyer"
						value="${loggedInUserEmail}"/>
				</c:when>	
				</c:choose>
				
				<form:input type="hidden" id="field-retailPrice" path="retailPrice" 
						value="${ad.retailPrice}" />	
				<form:input type="hidden" id="field-title" path="title" 
						value="${ad.title}" />
				<form:input type="hidden" id="field-street" path="street"
						value="${ad.street}" />
				<form:input type="hidden" id="type-studio" path="roomType"
						value="${ad.roomType}" />	
				<form:input type="hidden" id="field-city" path="city" value="${ad.zipcode} - ${ad.city}" />
					<form:errors path="city" cssClass="validationErrorText" />		
				<form:input type="hidden" id="field-moveInDate"
						path="moveInDate" value="${formattedMoveInDate }"/>
				<form:input type="hidden" id="field-moveOutDate"
						path="moveOutDate" value="${formattedMoveOutDate }"/>		
				<form:input type="hidden" id="field-Prize" path="prize"
						placeholder="Prize per month" step="50" value="${ad.prizePerMonth }"/> <form:errors
						path="prize" cssClass="validationErrorText" />
				<form:input type="hidden" id="field-SquareFootage" 
						path="squareFootage" placeholder="Prize per month" step="5" 
						value="${ad.squareFootage }"/> <form:errors
						path="squareFootage" cssClass="validationErrorText" />		
				
				<form:input type="hidden" id="field-auctionPossible"
							path="auctionPossible" value="${ad.auctionPossible }"/>
				
				<form:input type="hidden" id="field-smokers"
							path="smokers" value="${ad.smokers }"/>
				<form:input type="hidden" id="field-animals"
							path="animals" value="${ad.animals }"/>		
				<form:input type="hidden" id="field-garden"
							path="garden" value="${ad.garden }"/>
				<form:input type="hidden" id="field-balcony"
							path="balcony" value="${ad.balcony }"/>
				<form:input type="hidden" id="field-cellar"
							path="cellar" value="${ad.cellar }"/>
				<form:input type="hidden" id="field-furnished"
							path="furnished" value="${ad.furnished }"/>
				<form:input type="hidden" id="field-cable"
							path="cable" value="${ad.cable }"/>
				<form:input type="hidden" id="field-garage"
							path="garage" value="${ad.garage }"/>
				<form:input type="hidden" id="field-internet"
							path="internet" value="${ad.internet }"/>			
						
				<form:input type="hidden"  id="field-description" 
							path="roomDescription" value="${ad.roomDescription}" />
				<form:errors path="roomDescription" cssClass="validationErrorText" />	
				<form:input type="hidden"  id="field-preferences" 
							path="preferences" value="${ad.preferences}" />
				<form:errors path="preferences" cssClass="validationErrorText" />
				<form:input type="hidden"  id="field-roommates" 
							path="roommates" value="${ad.roommates}" />
				<form:errors path="roommates" cssClass="validationErrorText" />	
					
						
				<c:forEach var="user" items="${ad.registeredRoommates}">
					
				</c:forEach>		
				<c:forEach items="${ad.pictures }" var="picture">
				<div class="pictureThumbnail">
					</div>
				</c:forEach>		
						
						
						
					

						
						
			
		</table>
	</fieldset>


	
	
	<br />



	<div>
		<button type="submit" onClick="${ad.currentBidding = ad.retailPrice}">Buy Out</button>
		<button type="submit">Place Bidding</button>
		<a href="<c:url value='/ad?id=${ad.id}' />"> 
			<button type="button">Cancel</button>
		</a>
	</div>

</form:form>


<c:import url="template/footer.jsp" />
