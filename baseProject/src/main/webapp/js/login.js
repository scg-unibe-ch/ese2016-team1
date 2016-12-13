function onSignIn(googleUser) {
    var id_token = googleUser.getAuthResponse().id_token;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/tokensignin');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        var response = xhr.responseText.replace(/"/g, '').replace(/'/g, '"');
        var json = JSON.parse(response);
        if (json.status !== 'success') {
            console.log(json.message);
        }
        else {
            $('#field-email').val(json.email);
            $('#field-password').val(json.password);
            $('#login-form').submit();
        } 
        
    };
    xhr.send('token=' + id_token);
}