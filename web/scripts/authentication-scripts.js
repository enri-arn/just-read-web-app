/**
 * check if mail is valid.
 * 
 * @param {String} email to validate.
 * @returns {Boolean} true if email is valid, false otherwise.
 */
function validateEmail(email) {
    if (email.length > 255)
        return false;
    var pattern = new RegExp(/\S+@\S+\.\S+/);
    return pattern.test(email);
}

/**
 * check if password is valid.
 * This regex will enforce these rules:
 * At least one upper case English letter, (?=.*?[A-Z])
 * At least one lower case English letter, (?=.*?[a-z]) 
 * At least one digit, (?=.*?[0-9])
 * At least one special character, (?=.*?[#?!@$%^&*-])
 * Minimum eight in length .{8,} (with the anchors)
 * 
 * @param {String} password
 * @returns {Boolean} true if password is valid, false otherwise.
 */
function validatePassword(password) {
    var pattern = new RegExp(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}$/);
    return pattern.test(password);
}

/**
 * check if name and surname are valid.
 * 
 * @param {String} name or surname of the user.
 * @returns {Boolean} true if name is valid, false otherwise.
 */
function validateName(name) {
    if (name.length > 255)
        return false;
    var pattern = new RegExp(/^[a-zA-Z\\\\s]+/);
    return pattern.test(name);
}

$(document).ready(function () {
    
    /**
     * check the change of mail input and
     * check if is correct.
     */
    $("#email-input").change(function () {
        var mail = $(this).val();
        if (!validateEmail(mail)) {
            $(this).css("border-color", "#FF5050");
        } else {
            $(this).css("border-color", "#50ff50");
        }
    });
    
    /**
     * check the change of mail input and
     * check if is correct.
     */
    $("#email-input-reg").change(function () {
        var mail = $(this).val();
        if (!validateEmail(mail)) {
            $(this).css("border-color", "#FF5050");
        } else {
            $(this).css("border-color", "#50ff50");
        }
    });

    /**
     * check if password input is valid
     * by listening the change.
     */
    $("#password-input").change(function () {
        var pwd = $(this).val();
        console.log(pwd);
        console.log(validatePassword(pwd));
        if (!validatePassword(pwd)) {
            $(this).css("border-color", "#FF5050");
        } else {
            $(this).css("border-color", "#50ff50");
        }
    });
    
    /**
     * check if password input is valid
     * by listening the change.
     */
    $("#password-input-reg1").change(function () {
        var pwd = $(this).val();
        if (!validatePassword(pwd)) {
            $(this).css("border-color", "#FF5050");
        } else {
            $(this).css("border-color", "#50ff50");
        }
    });
    
    /**
     * check if password input is valid
     * by listening the change.
     */
    $("#password-input-reg2").change(function () {
        var pwd = $(this).val();
        var pwd1 = $("#password-input-reg1").val();
        if (!validatePassword(pwd) && pwd !== pwd1) {
            $(this).css("border-color", "#FF5050");
        } else {
            $(this).css("border-color", "#50ff50");
        }
    });

    /**
     * check the change of name input and
     * check if is correct.
     */
    $("#name-input-reg").change(function () {
        var name = $(this).val();
        if (!validateName(name)) {
            $(this).css("border-color", "#FF5050");
        } else {
            $(this).css("border-color", "#50ff50");
        }
    });

    /**
     * check the change of surname input and
     * check if is correct.
     */
    $("#surname-input-reg").change(function () {
        var surname = $(this).val();
        if (!validateName(surname)) {
            //do something
        } else {
            //do something else
        }
    });

    /**
     * go to home page when logo is pressed.
     */
    $("#home-link-btn").click(function () {
        window.location.replace("/JustRead/app/");
    });

    /**
     * check if the input has any value (if we've typed into it)
     */
    $('input').blur(function () {
        if ($(this).val())
            $(this).addClass('used');
        else
            $(this).removeClass('used');
    });

    /**
     * send to servlet email and password of user.
     * Servlet responde with true or false.
     * If true user will be send to home, else
     * an error message appear.
     */
    $("#login-btn").click(function () {
        var emailInput = $("#email-input").val();
        var passwordInput = $("#password-input").val();
        if (validateEmail(emailInput) && validatePassword(passwordInput)) {
            $.post("/JustRead/api/login",
                    {
                        email: emailInput,
                        password: passwordInput
                    },
                    function (data, status) {
                        console.log("data = " + data + " status = " + status);
                        if (status === "success" && data === "true") {
                            var snakbar = showSnakBar("Welcome " + emailInput + " , in 2 seconds you will be redirect to the homepage");
                            $(snakbar).appendTo('body');
                            $(snakbar).delay(1800).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                        window.location.replace("/JustRead/app/");
                                    }, 2000);
                        } else {
                            var snakbar = showSnakBar("Operation unsuccesful, we invite you to try again");
                            $(snakbar).appendTo('body');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                        }
                    });
        }
        return false;
    });

    /**
     * send to servlet user's data.
     * Servlet responde with true or false.
     * If true user will be send to home, else
     * an error message appear.
     */
    $("#register-btn").click(function () {
        var emailInput = $("#email-input-reg").val();
        var passwordInput = $("#password-input-reg1").val();
        var passwordInput1 = $("#password-input-reg2").val();
        var nameInput = $("#name-input-reg").val();
        var surnameInput = $("#surname-input-reg").val();
        console.log(validateEmail(emailInput));
        console.log(validatePassword(passwordInput));
        console.log(validateName(nameInput));
        console.log(validateName(surnameInput));
        if (passwordInput === passwordInput1){
            console.log("ok");
        }
        if (validateEmail(emailInput) && validatePassword(passwordInput) && validateName(nameInput) && validateName(surnameInput)) {
            $.post("/JustRead/api/register",
                    {
                        email: emailInput,
                        password: passwordInput,
                        name: nameInput,
                        surname: surnameInput
                    },
                    function (data, status) {
                        console.log("data = " + data + " status = " + status);
                        if (status === "success" && data === "true") {
                            window.location.replace("/JustRead/app/login");
                        } else {
                            alert("register unsuccesful");
                        }
                    });
        }
        return false;
    });
});


