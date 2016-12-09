function onSignIn(googleUser) {
	
  var profile = googleUser.getBasicProfile();
  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
  console.log('Name: ' + profile.getName());
  console.log('Image URL: ' + profile.getImageUrl());
  console.log('Email: ' + profile.getEmail());
  
  $.ajax({
	  type: "POST",
	  url: "/signInWithGoogle",
	  data: {name: profile.getName(), imageUrl: profile.getImageUrl(), email: profile.getEmail()}
	});
  
  
}