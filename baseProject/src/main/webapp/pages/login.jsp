<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<!-- check if user is logged in -->
<security:authorize var="loggedIn" url="/profile" />

<c:import url="template/header.jsp" />

<!--  import google auth -->
<script src="https://apis.google.com/js/platform.js" async defer></script>

<script src="/js/login.js"></script>

<pre>
	<a href="/">Home</a>   &gt;   Login</pre>
	
<div class="card" id="loginPage">
	<h1>Login</h1>
	<br>
	<c:choose>
		<c:when test="${loggedIn}">
			<p>You are already logged in!</p>
		</c:when>
		<c:otherwise>
			<div>
				<div class="g-signin2" data-onsuccess="onSignIn"></div>
			</div>
			<div>
			<c:if test="${!empty param.error}">
				<p id="loginError">Incorrect email or password. Please retry using correct email
					and password.</p>
				<br />
			</c:if>
			<form id="login-form" method="post" action="/j_spring_security_check">
				<label for="field-email">Email:</label> <input name="j_username"
					id="field-email" /> <label for="field-password">Password:</label> <input
					name="j_password" id="field-password" type="password" />
				<button type="submit">Login</button>
			</form>
			</div>
			<br />
			<h2>Test users</h2>
	
			<ul class="test-users">
				<li>Email: <i>ese@unibe.ch</i>, password: <i>ese</i></li>
				<li>Email: <i>jane@doe.com</i>, password: <i>password</i></li>
				<li>Email: <i>user@bern.com</i>, password: <i>password</i></li>
				<li>Email: <i>oprah@winfrey.com</i>, password: <i>password</i></li>
				<li>Email: <i>premiumKing@premium.ch</i>, password: <i>premium</i></li>
			</ul>
			<br />
	
			<h2>Roommates for AdBern</h2>
			<ul class="test-users">
				<li>Email: <i>hans@unibe.ch</i>, password: <i>password</i></li>
				<li>Email: <i>mathilda@unibe.ch</i>, password: <i>password</i></li>
			</ul>
			<br />		
				Or <a class="link" href="<c:url value="/signup" />">sign up</a> as a new user.		
		</c:otherwise>
	</c:choose>
</div>

<c:import url="template/footer.jsp" />