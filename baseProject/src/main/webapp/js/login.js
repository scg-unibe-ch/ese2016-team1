function onSignIn(googleUser) {
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut();
    var id_token = googleUser.getAuthResponse().id_token;
    var imageUrl = googleUser.getBasicProfile().Paa;
    
    $.ajax({
  	  type: "POST",
  	  url: "/signInWithGoogle",
  	  data: {token: id_token, imageUrl: imageUrl},
    	  success: function() {
    		  window.location.replace("http://localhost:8080/")
    	  }
  	});
}