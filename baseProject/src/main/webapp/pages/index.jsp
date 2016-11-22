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

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to FlatFindr</title>
</head>
<body>

<script src="/js/resultsOnMap.js"></script>

<pre>Home</pre>

<h1>Welcome to FlatFindr!</h1>

<c:choose>
	<c:when test="${empty newest}">
		<h2>No ads placed yet</h2>
	</c:when>
	<c:otherwise>
		<div id="resultsDiv" class="resultsDiv">	
			<h2>Our newest ads: <span class="mapSwitch" onclick="openMap()"><a>Show on Map</a></span></h2>		
			<c:forEach var="ad" items="${newest}">
				<div class="resultAd">
					<div class="resultLeft">
						<a href="<c:url value='/ad?id=${ad.id}' />"><img
							src="${ad.pictures[0].filePath}" /></a>
						<h2>
							<a class="link" href="<c:url value='/ad?id=${ad.id}' />">${ad.title}</a>
						</h2>
						<p>${ad.street}, ${ad.zipcode} ${ad.city}</p>
						<br />
						<p>
							<i>${ad.roomType}</i>
						</p>
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

						<p>Move-in date: ${formattedMoveInDate }</p>
					</div>
				</div>
			</c:forEach>
		</div>
		<div id="resultMapContainer" class="resultsDiv">
			<h2>Our newest ads: <span class="mapSwitch" onclick="closeMap()">Show in List</span></h2>
			<div id="resultMap"></div>
		</div>
		
		<c:forEach var="ad" items="${newest}">
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

<c:import url="template/footer.jsp" /><br />