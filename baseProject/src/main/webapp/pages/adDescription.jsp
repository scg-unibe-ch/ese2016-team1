<%@page import="ch.unibe.ese.team1.model.Ad"%>
<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<c:import url="template/header.jsp" />

<pre><a href="/">Home</a>   &gt;   Ad Description</pre>

<script src="/js/image_slider.js"></script>
<script src="/js/adDescription.js"></script>

<!-- Include Leaflet -->
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.0.2/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.0.2/dist/leaflet.js"></script>

<script>
	var shownAdvertisementID = "${shownAd.id}";
	var shownAdvertisement = "${shownAd}";
	
	function attachBookmarkClickHandler(){
		$("#bookmarkButton").click(function() {
			
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: false}, function(data) {
				$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 3:
					$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");	
				}
				
				attachBookmarkedClickHandler();
			});
		});
	}
	
	function attachBookmarkedClickHandler(){
		$("#bookmarkedButton").click(function() {
			$.post("/bookmark", {id: shownAdvertisementID, screening: false, bookmarked: true}, function(data) {
				$('#bookmarkedButton').replaceWith($('<a class="right" id="bookmarkButton">' + "Bookmark Ad" + '</a>'));
				switch(data) {
				case 0:
					alert("You must be logged in to bookmark ads.");
					break;
				case 1:
					// Something went wrong with the principal object
					alert("Return value 1. Please contact the WebAdmin.");
					break;
				case 2:
					$('#bookmarkedButton').replaceWith($('<a class="right" id="bookmarkButton">' + "Bookmark Ad" + '</a>'));
					break;
				default:
					alert("Default error. Please contact the WebAdmin.");
					
				}			
				attachBookmarkClickHandler();
			});
		});
	}

	$(document).ready(function() {
		attachBookmarkClickHandler();
		attachBookmarkedClickHandler();
		
		$.post("/bookmark", {id: shownAdvertisementID, screening: true, bookmarked: true}, function(data) {
			if(data == 3) {
				$('#bookmarkButton').replaceWith($('<a class="right" id="bookmarkedButton">' + "Bookmarked" + '</a>'));
				attachBookmarkedClickHandler();
			}
			if(data == 4) {
				$('#shownAdTitle').replaceWith($('<h1>' + "${shownAd.title}" + '</h1>'));
			}
		});
		
		$("#newMsg").click(function(){
			$("#content").children().animate({opacity: 0.4}, 300, function(){
				$("#msgDiv").css("display", "block");
				$("#msgDiv").css("opacity", "1");
			});
		});
		
		$("#messageCancel").click(function(){
			$("#msgDiv").css("display", "none");
			$("#msgDiv").css("opacity", "0");
			$("#content").children().animate({opacity: 1}, 300);
		});
		

		$("#messageSend").click(function (){
			if($("#msgSubject").val() != "" && $("#msgTextarea").val() != ""){
				var subject = $("#msgSubject").val();
				var text = $("#msgTextarea").val();
				var recipientEmail = "${shownAd.user.username}";
				$.post("profile/messages/sendMessage", {subject : subject, text: text, recipientEmail : recipientEmail}, function(){
					$("#msgDiv").css("display", "none");
					$("#msgDiv").css("opacity", "0");
					$("#msgSubject").val("");
					$("#msgTextarea").val("");
					$("#content").children().animate({opacity: 1}, 300);
				})
			}
		});
	});
		
</script>


<!-- format the dates -->
<fmt:formatDate value="${shownAd.moveInDate}" var="formattedMoveInDate"
	type="date" pattern="dd.MM.yyyy" />
<fmt:formatDate value="${shownAd.creationDate}" var="formattedCreationDate"
	type="date" pattern="dd.MM.yyyy" />
<c:choose>
	<c:when test="${empty shownAd.moveOutDate }">
		<c:set var="formattedMoveOutDate" value="unlimited" />
	</c:when>
	<c:otherwise>
		<fmt:formatDate value="${shownAd.moveOutDate}"
			var="formattedMoveOutDate" type="date" pattern="dd.MM.yyyy" />
	</c:otherwise>
</c:choose>


<h1 id="shownAdTitle">${shownAd.title}
	<c:choose>
		<c:when test="${loggedIn}">
			<a class="right" id="bookmarkButton">Bookmark Ad</a>
		</c:when>
	</c:choose>
</h1>


<hr />

<section>
	<c:choose>
		<c:when test="${loggedIn}">
			<c:if test="${loggedInUserEmail == shownAd.user.username }">
				<a href="<c:url value='/profile/editAd?id=${shownAd.id}' />">
					<button type="button">Edit Ad</button>
				</a>
			</c:if>
		</c:when>
	</c:choose>
	<br>
	<br>

	<table id="adDescTable" class="adDescDiv card">
			<tr>
			<c:choose>
				<c:when test="${shownAd.saleType == \"Auction\"}">
					<c:choose>
						<c:when test="${loggedInUserEmail == shownAd.currentBuyer }">
							<tr><td>You are currently the highest Bidder</td></tr>
						</c:when>
					</c:choose>
					<c:choose>
						<c:when test="${shownAd.auctionEnded}">
							<tr>
								<td><h2>Status</h2></td>
								<td>Auction ended already: 
									<c:if test="${shownAd.getAuctionEndTimeBeforeToday()}">
										Auction end date expired
									</c:if>
									<c:if test="${shownAd.getAuctionEndedCurrentBiddingHigherThanRetailPrice()}">
										Buy out price reached
									</c:if>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
								<tr>
									<td><h2>Current Bidding</h2></td>
									<td>${shownAd.currentBidding}&#32;CHF</td>
								</tr>
									<c:choose>
										<c:when test="${loggedIn}">
											<c:if test="${loggedInUserEmail != shownAd.user.username }">
												<tr>
													<form:form name="form1" method="post" modelAttribute="placeAdForm"
														id="biddingForm" action="/makeAuction" autocomplete="off"
														enctype="multipart/form-data">
														<td><h2>Your Bid</h2></td>
														<td><input type="number" name="price" placeholder="Your Bid" 
														step="5" min="${shownAd.nextPossibleBid }" value="${shownAd.nextPossibleBid }"/></td>
																	<a href="<c:url value='/profile/editAd?id=${shownAd.id}' />">
																		<td>
																			<button type="submit" class="thinInactiveButton">Bid</button>
																			<input type="hidden" name="id" value="${shownAd.id}" />
																		</td> 
																	</a>
												</form:form>
											</tr>
										</c:if>
									</c:when>
								</c:choose>
								<c:choose>
									<c:when test="${shownAd.retailPrice > 0 }">
										<tr>
											<td><h2>Buy Out Price</h2></td>
											<td>${shownAd.retailPrice}&#32;CHF</td>
					 						<form:form name="form1" method="post" modelAttribute="placeAdForm"
												action="/makeAuction" autocomplete="off" id="biddingFormBuyOut"
												enctype="multipart/form-data">
												<c:choose>
													<c:when test="${loggedIn}">
														<c:if test="${loggedInUserEmail != shownAd.user.username }">
															<a href="<c:url value='/profile/editAd?id=${shownAd.id}' />">
																<td>
																	<button type="submit" class="thinInactiveButton">Buy Out</button>
																	<input type="hidden" name="id" value="${shownAd.id }" />
																	<input type="hidden" name="price" value="${shownAd.retailPrice}" />
																</td> 
															</a>
														</c:if>
													</c:when>
												</c:choose>
											</form:form> 
										</tr>
									</c:when>
								</c:choose>
								<tr>
									<td><h2>End of Auction</h2></td>
									<td>${shownAd.endOfAuction}</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</c:when>	
					<c:when test="${shownAd.saleType == \"Rent\"}">
						<tr>
							<td><h2>Monthly Rent</h2></td>
							<td>${shownAd.prizePerMonth}&#32;CHF</td>
						</tr>
					</c:when>
					
					<c:when test="${shownAd.saleType == \"Buy\"}">
						<tr>
							<td><h2>Retail Price</h2></td>
							<td>${shownAd.retailPrice}&#32;CHF</td>
						</tr>
					</c:when>
			</c:choose>
		</tr>
		<tr class="tableSpaceBoarder">
			<td><h2>Type</h2></td>
			<td>${shownAd.roomType}</td>
			<td></td>
		</tr>

		<tr>
			<td><h2>Address</h2></td>
			<td>
				<a class="link" href="http://maps.google.com/?q=${shownAd.street}, ${shownAd.zipcode}, ${shownAd.city}">${shownAd.street},
						${shownAd.zipcode} ${shownAd.city}</a>
			</td>
		</tr>

		<tr>
			<td><h2>Available from</h2></td>
			<td>${formattedMoveInDate}</td>
		</tr>

		<tr>
			<td><h2>Move-out Date</h2></td>
			<td>${formattedMoveOutDate}</td>
		</tr>
		<tr>
			<td><h2>Square Meters</h2></td>
			<td>${shownAd.squareFootage}&#32;m²</td>
		</tr>
		<tr>
			<td><h2>Ad created on</h2></td>
			<td>${formattedCreationDate}</td>
		</tr>
	</table>
</section>

<div id="image-slider">
	<div id="left-arrow">
		<img src="/img/left-arrow.png" />
	</div>
	<div id="images">
		<c:forEach items="${shownAd.pictures}" var="picture">
			<img src="${picture.filePath}" />
		</c:forEach>
	</div>
	<div id="right-arrow">
		<img src="/img/right-arrow.png" />
	</div>
</div>

<hr class="clearBoth" />

<section>
	<div id="descriptionTexts">
		<div class="adDescDiv card">
			<h2>Room Description</h2>
			<p>${shownAd.roomDescription}</p>
		</div>
		<br />

		<div class="adDescDiv card">
			<h2>Roommates</h2>
			<p>${shownAd.roommates}</p>
			<c:forEach var="mate" items="${shownAd.registeredRoommates}">
				<div class="roommate">
				<table id="mate">
					<tr>
						<td>
						<a href="/user?id=${mate.id}">
						<c:choose>
							<c:when test="${mate.picture.filePath != null}">
								<img src="${mate.picture.filePath}">
							</c:when>
							<c:otherwise>
								<img src="/img/avatar.png">
							</c:otherwise>
						</c:choose>
						</a>
						</td>
						<td>${mate.firstName} ${mate.lastName}</td>
						<td>${mate.username}</td>
						<td>
						<c:choose>
							<c:when test="${mate.gender == 'MALE'}">
								male
							</c:when>
							<c:when test="${mate.gender == 'FEMALE'}">
								female
							</c:when>
							<c:otherwise>
								unknown
							</c:otherwise>
						</c:choose></td>
					</tr>
				</table>
			</div>
			</c:forEach>
		</div>
		<br />

		<div class="adDescDiv card">
			<h2>Preferences</h2>
			<p>${shownAd.preferences}</p>
		</div>
		<br />

		<div id="visitList" class="adDescDiv card">
			<h2>Visiting times</h2>
			<table>
				<c:forEach items="${visits }" var="visit">
					<tr>
					
					<c:set var="already" value="false"/>
					<c:forEach items="${visits }" var="visit2">
					
						<c:if test="${visit.id < visit2.id}">
						<c:if test="${visit.startTimestamp == visit2.startTimestamp }">
						<c:if test="${visit.endTimestamp == visit2.endTimestamp }">
					
						<c:set var="already" value="true"/>
	
						</c:if>
						</c:if>
						</c:if>
					
					</c:forEach>
					
					<c:if test="${already == false}">
					
						<td>
							<fmt:formatDate value="${visit.startTimestamp}" pattern="yyyy-MM-dd " />
							&nbsp; from
							<fmt:formatDate value="${visit.startTimestamp}" pattern=" HH:mm " />
							until
							<fmt:formatDate value="${visit.endTimestamp}" pattern=" HH:mm" />
						</td>
						<td><c:choose>
								<c:when test="${loggedIn}">
									<c:if test="${loggedInUserEmail != shownAd.user.username}">
										<button class="thinButton" type="button" data-id="${visit.id}">Send
											enquiry to advertiser</button>
									</c:if>
								</c:when>
								<c:otherwise>
									<a href="/login"><button class="thinInactiveButton" type="button"
										data-id="${visit.id}">Login to send enquiries</button></a>
								</c:otherwise>
							</c:choose></td>
							
						</c:if>	
														
					</tr>
				</c:forEach>
			</table>
		</div>
		
		<div class="adDescDiv card" id="advertiserDiv">
		
			<table id="advertiserTable">
				<tr>
				<td><h2>Advertiser</h2><br /></td>
				</tr>
			
				<tr>
					<td><c:choose>
							<c:when test="${shownAd.user.picture.filePath != null}">
								<img src="${shownAd.user.picture.filePath}">
							</c:when>
							<c:otherwise>
								<img src="/img/avatar.png">
							</c:otherwise>
						</c:choose></td>
					
					<td>${shownAd.user.username}</td>
					
					<td id="advertiserEmail">
					<c:choose>
						<c:when test="${loggedIn}">
							<a href="/user?id=${shownAd.user.id}"><button type="button">Visit profile</button></a>
						</c:when>
						<c:otherwise>
							<a href="/login"><button class="thinInactiveButton" type="button">Login to visit profile</button></a>
						</c:otherwise>
					</c:choose>
			
					<td>
						<form>
							<c:choose>
								<c:when test="${loggedIn}">
									<c:if test="${loggedInUserEmail != shownAd.user.username }">
										<button id="newMsg" type="button">Contact Advertiser</button>
									</c:if>
								</c:when>
								<c:otherwise>
									<a href="/login"><button class="thinInactiveButton" type="button">Login to contact advertiser</button></a>
								</c:otherwise>
							</c:choose>
						</form>
					</td>
				</tr>
			</table>
		</div>

	</div>

	<table id="checkBoxTable" class="adDescDiv card">
		<tr>
			<td><h2>Smoking inside allowed</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.smokers}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Animals allowed</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.animals}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Furnished Room</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.furnished}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<tr>
			<td><h2>WiFi available</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.internet}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Cable TV</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.cable}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Garage</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.garage}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Cellar</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.cellar}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Washing Machine</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.washingMachine}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Dishwasher</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.dishwasher}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Balcony</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.balcony}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

		<tr>
			<td><h2>Garden</h2></td>
			<td>
				<c:choose>
					<c:when test="${shownAd.garden}"><img src="/img/check-mark.png"></c:when>
					<c:otherwise><img src="/img/check-mark-negative.png"></c:otherwise>
				</c:choose>
			</td>
		</tr>

	</table>
	
	<div class="adDescDiv card" id="map">
	</div>
	
	<div class="adDescDiv card" id="noCoordinates">
		<p>No Coordinates can be found with city = "${shownAd.city}"<p>
	</div>
	
	<script>
	
	    var title = "${shownAd.title}";
	    var lon = "${shownAd.longitude}";
	    var lat = "${shownAd.latitude}";
		drawMap(title, [parseFloat(lat),parseFloat(lon)]);
	
	</script>
	
	
</section>

<div class="clearBoth"></div>
<br>


<div id="msgDiv">
<form class="msgForm">
	<h2>Contact the advertiser</h2>
	<br>
	<br>
	<label>Subject: <span>*</span></label>
	<input  class="msgInput" type="text" id="msgSubject" placeholder="Subject" />
	<br><br>
	<label>Message: </label>
	<textarea id="msgTextarea" placeholder="Message" ></textarea>
	<br/>
	<button type="button" id="messageSend">Send</button>
	<button type="button" id="messageCancel">Cancel</button>
	</form>
</div>

<div id="confirmationDialog">
	<form>
	<p>Send enquiry to advertiser?</p>
	<button type="button" id="confirmationDialogSend">Send</button>
	<button type="button" id="confirmationDialogCancel">Cancel</button>
	</form>
</div>


<c:import url="template/footer.jsp" />