function onSignIn(googleUser) {
	
  var profile = googleUser.getBasicProfile(); 
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut();
  $.ajax({
	  type: "POST",
	  url: "/signInWithGoogle",
	  data: {lastName: profile.wea, firstName: profile.ofa, imageUrl: profile.getImageUrl(), email: profile.getEmail()},
  	  success: function() {
  		  window.location.replace("http://localhost:8080/")
  	  }
	});
  
}