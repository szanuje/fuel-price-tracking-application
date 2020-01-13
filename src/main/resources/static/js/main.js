document.addEventListener("DOMContentLoaded", function(event) {
    
    
    var menulogin = document.getElementById('menulogin');
    var menuhome = document.getElementById('menuhome');
    var searchbutton = document.getElementById('searchbutton');

    menulogin.addEventListener('click', function(e) {
        document.getElementById('login').style.display='block';
        document.getElementById('box').style.display='none';

    });

    searchbutton.addEventListener('click', function(e) {
        var searchtext = document.getElementById('location').value;
        // document.getElementById('location').value='';
        console.log(searchtext);
        return searchtext;
    });

    searchbutton.addEventListener("keydown", function(e) {
        if(e.keyCode === 13) {
            searchbutton.click();
        }
    });
    

    var loginsection = document.getElementById('login');

    window.onclick = function(event) {
        if (event.target == loginsection) {
            loginsection.style.display = "none";
            document.getElementById('box').style.display='block';

        }
    }

    var loginbutton = document.getElementById('loginbutton');
    var user = document.getElementById('user');
    var pass = document.getElementById('pass');

    loginbutton.addEventListener('click', function(e) {
        // user.value = "";
        // pass.value = "";
    });


});
  
