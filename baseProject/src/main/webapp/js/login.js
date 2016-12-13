function onSignIn(googleUser) {
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut();
    var id_token = googleUser.getAuthResponse().id_token;
    
    $.ajax({
  	  type: "POST",
  	  url: "/signInWithGoogle",
  	  data: {token: id_token},
    	  success: function() {
    		  window.location.replace("http://localhost:8080/")
    	  }
  	});
}