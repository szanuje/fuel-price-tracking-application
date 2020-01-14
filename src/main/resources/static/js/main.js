document.addEventListener("DOMContentLoaded", function (event) {


    var menulogin = document.getElementById('menulogin');
    var menuhome = document.getElementById('menuhome');
    var searchbutton = document.getElementById('searchbutton');

    // menulogin.addEventListener('click', function(e) {
    //     document.getElementById('login').style.display='block';
    //     console.log('menu login button clicked, opening login modal');
    // });

    searchbutton.addEventListener('click', function (e) {
        var searchtext = document.getElementById('location').value;
        // document.getElementById('location').value='';
    });

    searchbutton.addEventListener("keydown", function (e) {
        if (e.keyCode === 13) {
            var searchtext = document.getElementById('location').value;
            searchbutton.click();
            console.log(searchtext);
        }
    });


    var loginsection = document.getElementById('login');

    // window.onclick = function (event) {
    //     if (event.target === loginsection) {
    //         loginsection.style.display = "none";
    //         console.log('hiding login section');
    //     }
    // }

    var loginbutton = document.getElementById('loginbutton');
    var user = document.getElementById('user');
    var pass = document.getElementById('pass');

    loginbutton.addEventListener('click', function (e) {
        // user.value = "";
        // pass.value = "";
        console.log('loginbutton clicked');
    });

    // if (location.search.indexOf('show_login=true') > 0) {
    //     loginsection.style.display = "block";
    //     console.log('displaying login modal');
    // }


});
  
