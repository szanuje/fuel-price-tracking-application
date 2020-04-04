document.addEventListener("DOMContentLoaded", function (event) {

    let loginButton = $('#menulogin');
    let registerButton = $('#menuregister');

    // handle pop-up modals
    function show(event, modal) {
        let blurDiv = $('#blur-me');
        event.stopImmediatePropagation();
        modal.removeClass('hide');
        blurDiv.addClass('blur');
        blurDiv.addClass('no-pointer');
        $('html').on('click', function (e) {
            hide(e, modal);
        });
    }

    function hide(ev, modal) {
        let blurDiv = $('#blur-me');
        if (!$(ev.target).is(modal) && !$(modal).has(ev.target).length > 0) {
            modal.addClass('hide');
            blurDiv.removeClass('blur');
            blurDiv.removeClass('no-pointer');
            $('html').off();
        }
    }

    function handleModals() {
        let loginModal = $('#login-modal');
        let registerModal = $('#register-modal');

        loginButton.on('click', function (e) {
            show(e, loginModal);
        });

        registerButton.on('click', function (e) {
            show(e, registerModal);
        });
    }

    handleModals();

    // handle warning messages on modals
    function handleModalWarnings() {
        let url = window.location.href;

        if (url.endsWith("?login=false")) {
            loginButton.click();
            document.getElementById('wrong-username-warning').style.display = 'block';
            history.replaceState({}, document.title, url.split('?')[0]);  // replace / with . to keep url
        }

        if (url.endsWith("?login=true")) {
            loginButton.click();
            history.replaceState({}, document.title, url.split('?')[0]);  // replace / with . to keep url
        }

        if (url.endsWith("?register=false")) {
            registerButton.click();
            document.getElementById('user-exists-warning').style.display = 'block';
            history.replaceState({}, document.title, url.split('?')[0]);  // replace / with . to keep url
        }
    }

    handleModalWarnings();

    //handle register process
    function validatePassword(event, pass, confirmPass) {

        if (pass.value !== confirmPass.value) {
            document.getElementById('password-match-warning').style.display = 'block';
            document.getElementById('registerbutton').setAttribute('type', 'button');
            event.preventDefault();
        } else {
            document.getElementById('password-match-warning').style.display = 'none';
            document.getElementById('registerbutton').setAttribute('type', 'submit');
        }
    }

    function handleRegisterProcess() {
        let password_input = document.getElementById('pass');
        let confirm_password_input = document.getElementById('confirm-pass');
        password_input.onkeyup = function (event) {
            validatePassword(event, password_input, confirm_password_input);
        };
        confirm_password_input.onkeyup = function (event) {
            validatePassword(event, password_input, confirm_password_input);
        };
    }

    handleRegisterProcess();


});

