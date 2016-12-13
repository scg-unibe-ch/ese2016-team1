<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
	});

</script>

<pre><a href="/">Home</a>   &gt;   My Rooms</pre>
		<div id="resultsDiv" class="resultsDiv card cardPadding">
		<h1>My Advertisements</h1>
			<c:choose>
			<c:when test="${empty ownAdvertisements}">
				<p>You have not advertised anything yet.</p>
			</c:when>
			<c:otherwise>
			<c:forEach var="ad" items="${ownAdvertisements}">
				<div class="resultAd" data-price="${ad.prizePerMonth}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<div class="leftTextRoomDescription">
						<h2>
							<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
						</h2>
						<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<p>
							<i>${ad.roomType}</i>
						</p>
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
						<p>Move-in date: ${ad.moveInDate }</p>
					</div>
				</div>
			</c:forEach>
		</c:otherwise>
		</c:choose>
			<br /> <br />
		</div>		
		
		<div id="resultsDiv" class="resultsDiv card cardPadding">
		<h1>My Bookmarks</h1>
		<c:choose>
			<c:when test="${empty bookmarkedAdvertisements}">
				<p>You have not bookmarked anything yet.</p><br /><br />
			</c:when>
			<c:otherwise>
			<c:forEach var="ad" items="${bookmarkedAdvertisements}">
				<div class="resultAd" data-price="${ad.prizePerMonth}" 
								data-moveIn="${ad.moveInDate}" data-age="${ad.moveInDate}">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<div class="leftTextRoomDescription">
						<h2>
							<a href="<c:url value='/ad?id=${ad.id}' />">${ad.title }</a>
						</h2>
						<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<p>
							<i>${ad.roomType}</i>
						</p>
						</div>
					</div>
					<div class="resultRight">
						<h2>CHF ${ad.prizePerMonth }</h2>
						<br /> <br />
						<p>Move-in date: ${ad.moveInDate }</p>
					</div>
				</div>
			</c:forEach>
			</c:otherwise>
		</c:choose>
			
		</div>		

<c:import url="template/footer.jsp" />