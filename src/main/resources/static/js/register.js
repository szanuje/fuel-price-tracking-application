document.addEventListener("DOMContentLoaded", function (event) {

    var password_input = document.getElementById('confirm-password');
    var confirm_password_input = document.getElementById('password');

    function validatePassword() {
        if (password_input.value != confirm_password_input.value) {
            document.getElementById('password-warning').style.display = 'block';
            document.getElementById('registerbutton').setAttribute('type', 'button');
        } else {
            document.getElementById('password-warning').style.display = 'none';
            document.getElementById('registerbutton').setAttribute('type', 'submit');
        }
    }

    password_input.onkeyup = validatePassword;
    confirm_password_input.onkeyup = validatePassword;
});