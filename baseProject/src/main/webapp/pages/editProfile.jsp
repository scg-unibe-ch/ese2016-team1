<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:import url="template/header.jsp" />

<script>
	$(document).ready(function() {
		$("#about-me").val("${currentUser.aboutMe}")
		});		
</script>

<script>
function setSubmitTypeUpdate(form)
{
	var submitType = document.getElementById('submitType');
	var creditCard = document.getElementById('field-creditCard');

	submitType.value = "update";
	creditCard.style.color = "white";
	creditCard.value = "nil";
}
</script>

<script>
function setSubmitTypeUpgrade(form)
{
	var submitType = document.getElementById('submitType');
	var userName = document.getElementById('user-name');
	var firstName = document.getElementById('first-name');
	var lastName = document.getElementById('last-name');
	var password = document.getElementById('password');
	
	
	submitType.value = "upgrade";
	userName.style.color = "white";
	firstName.style.color = "white";
	lastName.style.color = "white";
	password.style.color = "white";
	userName.value = "nil";
	firstName.value = "nil";
	lastName.value = "nil";
	password.value = "nil";
}
</script>

<pre><a href="/">Home</a>   &gt;   <a href="/user?id=${currentUser.id}">Public Profile</a>   &gt;   Edit profile</pre>

<h1>Edit your Profile</h1>
<hr />

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />


<c:choose>
	<c:when test="${loggedIn}">
		<a id="profile_picture_editPage"> <c:import
					url="/pages/getUserPicture.jsp" />
		</a>
	</c:when>
	<c:otherwise>
		<a href="/login">Login</a>
	</c:otherwise>
</c:choose>

<form:form method="post" modelAttribute="editProfileForm"
	action="/profile/editProfile" id="editProfileForm" autocomplete="off"
	enctype="multipart/form-data">

<table class="editProfileTable">
	<tr>
		<td class="spacingTable"><label for="user-name">Username:</label><a>&emsp;</a>
		<form:input id="user-name" path="username" value="${currentUser.username}" /></td>
		
	</tr>
	<tr>
		<td class="spacingTable"><label for="first-name">First name:</label><a>&emsp;</a>
		<form:input id="first-name" path="firstName" value="${currentUser.firstName}" /></td>
	</tr>
	<tr>	
		<td class="spacingTable"><label for="last-name">Last name:</label><a>&emsp;</a>
		<form:input id="last-name" path="lastName" value="${currentUser.lastName}" /></td>
	</tr>
	<tr>	
		<td class="spacingTable"><label for="password">Password:</label><a>&emsp;&thinsp;</a>
		<form:input type="password" id="password" path="password" value="${currentUser.password}" /></td>
	</tr>

	<tr>
		<td class="spacingTable"><label for="about-me">About me:</label><a>&emsp;&thinsp;</a><br>
		<form:textarea id="about-me" path="aboutMe" rows="10" cols="100" /></td>
	</tr>
</table>
	<form:input style="display:none" name="submitType" id="submitType" path="submitType" />
	<button type="submit" onClick="setSubmitTypeUpdate(this.form)">Update</button>

<hr class="slim">
<c:choose>
	<c:when test = "${currentUser.premium == false}">
		<h2>Upgrade to Premium</h2>Your Advantages with FlatFinder-Premium:<br />
		<li>Instant alerts messages. (Waiting time for normal user: 1 minute (for testing)).
		<li>Get all informations from your ads on your e-mail address.
		<li>Get priority in the search algorithm for your advertisements.
			<table class="editProfileTable">
				<tr>
					<td><label for="creditCard">Credit Card</label><br />
				<form:input type="text" id="field-creditCard" path="creditCard" /></td>
				</tr>
			</table>
			<button type="submit" onClick="setSubmitTypeUpgrade(this.form)">Upgrade</button>
	</c:when>
	<c:otherwise>
	<h2>You are using FlatFinder-Premium, congratulation!</h2>Your Advantages:<br />
		<li>Instant alert messages. (Waiting time for normal user: 1 minute (for testing)).
		<li>You get all informations from your ads on your e-mail address.
		<li>Your advertisements get priority in the search algorithm.
	<form:input style="display:none" type="text" id="field-creditCard" path="creditCard" />
	</c:otherwise>
</c:choose>
</form:form>

<c:import url="template/footer.jsp" />

