var urlactiveborrowing = "/JustRead/api/admin/active";
var urlexpiredborrowing = "/JustRead/api/admin/expired";
var urlarchiveborrowing = "/JustRead/api/admin/archive";
var urladminuser = "/JustRead/api/admin/user";
var urlnewbook = "/JustRead/api/admin/newbook";
var urlallusers = "/JustRead/api/admin/allusers";
var urlallbooks = "/JustRead/api/admin/allbooks";
var urlallauthors = "/JustRead/api/admin/allauthors";
var urlallcategories = "/JustRead/api/admin/allcategories";
var publishers = [];
var categories = [];
var isOpenDialog = false;
var isOpenMenu = false;
var fabMenuOpen = false;

$(document).ready(function () {

    author_field = 1;
    category_select = 1;

    /**
     * add to jQuery put and delete methods using ajax.
     */
    jQuery.each(["put", "delete"], function (i, method) {
        jQuery[ method ] = function (url, data, callback, type) {
            if (jQuery.isFunction(data)) {
                type = type || callback;
                callback = data;
                data = undefined;
            }

            return jQuery.ajax({
                url: url,
                type: method,
                dataType: type,
                data: data,
                success: callback
            });
        };
    });

    /**
     * animate the navigation drawer.
     */
    var $window = $(window);

    function checkWidth() {
        var windowsize = $window.width();
        if (windowsize < 540) {
            $("#navigation-drawer").css("width", "0%;");
        } else {
            $("#navigation-drawer").css("width", "14em;");
        }
    }
    checkWidth();
    $(window).resize(checkWidth);
    
    $("#menu-icon").click(function () {
        $("#navigation-drawer").animate({
            width: "toggle"
        });
        if (!isOpenMenu) {
            isOpenMenu = true;
            $("article").animate({
                width: "98%"
            });
        } else {
            isOpenMenu = false;
            $("article").animate({
                width: "77%"
            });
        }
    });
});

function isEmptyTable(){
    return false;
}

function returnCurrentYear() {
    theYear = new Date().getYear();
    if (theYear < 1900)
        theYear = theYear + 1900;
    return theYear;
}

function setMaxYear() {
    document.getElementById('inputYear').setAttribute('max', returnCurrentYear());
}

function plusAuthorClicked() {
    if (author_field === 5) {
        alert("You have reached the author limit field number: " + author_field + "!");
        return;
    }
    author_field++;
    document.getElementById('author-paragraph').innerHTML += "<p id='author" + author_field + "'>Author*:<input type='text' id='inputAuthor' name='authors[]' required>" +
            "<img id='del" + author_field + "' class='rem-icon del-author-field' src='../icons/delete-circle.svg' alt='remove an author field' title='Remove this author " +
            "field' draggable='false' width='20' height='20' onclick='delAuthorClicked();'></p>";
}

function plusCategoryClicked() {
    if (category_select === 3) {
        alert("You have reached the category limit list number: " + category_select + "!");
        return;
    }
    var uselessCat = [];
    var newlist = categories.slice();
    uselessCat[0] = newlist.indexOf(getSelectedItem(1));
    if (category_select === 2){
        uselessCat[1] = newlist.indexOf(getSelectedItem(2));
        if (uselessCat[1] > -1) {
            delete newlist[uselessCat[1]];
        }
    }
    if (uselessCat[0] > -1) {
        delete newlist[uselessCat[0]];
    }
    category_select++;
    document.getElementById('category-paragraph').innerHTML += "<p id='category" + category_select + "'>Category*: <select id='cat" + category_select + "' name='categories[]'></select>" +
            "<img id='del" + category_select + "' class='rem-icon del-category-field' src='../icons/delete-circle.svg' alt='remove a category list' title='Remove this category list' " +
            "draggable='false' width='20' height='20' onclick='delCategoryClicked();'></p>";
    $(newlist.join("")).appendTo("#cat" + category_select);
}

function getSelectedItem(list){
    try{
        return "<option value='" + $("#cat" + list + " option:selected").text() + "'>" + $("#cat" + list + " option:selected").text() + "</option>";
    }catch(e){
        document.getElementById('category-paragraph').innerHTML = "invalid input type";
    }
}

function delCategoryClicked() {
    var field = document.getElementById('category' + category_select);
    field.parentNode.removeChild(field);
    category_select--;
}

function plusPublisherClicked() {
    document.getElementById('publisher-paragraph').innerHTML = "<p>Publisher*: <input type='text' id='inputPublisher' name='publisher'></p>" +
            "<img class='rem-icon' src='../icons/arrow-left.svg' alt='remove add new publisher' title='Back to the list' width='20' height='20' onclick='undoPublisherClicked();'>";
}

function delAuthorClicked() {
    var field = document.getElementById('author' + author_field);
    field.parentNode.removeChild(field);
    author_field--;
}

function undoPublisherClicked() {
    document.getElementById('publisher-paragraph').innerHTML = "Publisher*: <select id='publisher-select' name='publisher'></select>" +
            "<img class='add-icon' src='../icons/plus-circle.svg' alt='add new publisher' title='Add a new publisher field' width='20' height='20' onclick='plusPublisherClicked();'>";
    $(publishers.join("")).appendTo("#publisher-select");
}

/**
 * Prepare the form of the user search.
 * 
 * @returns {undefined}
 */
function prepareForm(){
        $('input[submit]').hide();
        var input_type, input_id, input_name;
        $('input[name=input-choice]').on('change', function() {
            $('input[submit]').show(100);
            if (this.value === 'id') {
              input_type = 'number';
              input_id = 'input-user-id';
              input_name = 'user-id';
            }
            else if (this.value === 'name') {
              input_type = 'text';
              input_id = 'input-user-name';
              input_name = 'user-name';
            }
            else if (this.value === 'email') {
              input_type = 'email';
              input_id = 'input-user-email';
              input_name = 'user-email';
            }else{
              document.getElementById('input-paragraph').innerHTML = "<p>Error</p>";
            }
            document.getElementById('input-paragraph').innerHTML = "<p id='input-paragraph'>" + this.value + "<p><input class='search' type='" + input_type + "' id='" + input_id + "' name='" + input_name + "' size='30' min='1' required></p></p>";
        });
  }

/**
 * Do a put to server for increase loan.
 * 
 * @param {type} isbn of the book.
 * @returns {undefined}
 */
function fabIncrease(isbn) {
    if (!isOpenDialog) {
        isOpenDialog = true;
        var dialog = showDialog("Are you sure to increase the loan?", function () {
            var IncreaseBorrowingUrl = "/JustRead/api/borrowing/increase/" + isbn;
            $.put(IncreaseBorrowingUrl,
                    function (data, status) {
                        console.log("data = " + data + " status = " + status);
                        if (status === "success" && data === true) {
                            var snakbar = showSnakBar("Operation succesfully complete");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            $(dialog).fadeOut(400);
                            isOpenDialog = false;
                        } else {
                            var snakbar = showSnakBar("Operation unsuccesful");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            $(dialog).fadeOut(400);
                            isOpenDialog = false;
                            console.log("status:  " + status);
                        }
                    });
        });
        var is_element_input = $(("#defaultbook-box" + isbn)).is("div");
        if (is_element_input) {
            $(dialog).appendTo("#defaultbook-box" + isbn);
        } else {
            $(dialog).appendTo("article");
            $(dialog).css({
                'position': 'fixed',
                'left': '50%',
                'top': '50%',
                'margin-left': function () {
                    return -$(this).outerWidth() / 2
                },
                'margin-top': function () {
                    return -$(this).outerHeight() / 2
                }
            });
        }
    }
}

/**
 * Delete a category.
 * 
 * @param {type} id
 * @returns {undefined}
 */
function deleteCategory(id) {
    alert("ciao");
    var box = "#category-row" + id;
    var dialog = showDialog("Are you sure to delete this category?", function () {
        var url = "/JustRead/api/admin/deletecategory";
        $.delete(url, function (data, status) {
                        alert("ciao");
                        console.log("data = " + data + " status = " + status);
                        if (status === "success" && data === true) {
                            var snakbar = showSnakBar("Operation successfully complete");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(function () {
                                $(snakbar).remove();
                            }, 5000);
                            $(box).hide(400);
                            $(dialog).hide(400);
                        } else {
                            var snakbar = showSnakBar("Operation unsuccesful");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            $(dialog).fadeOut(400);
                            console.log("status:  " + status);
                        }
        });
    });
}

/**
 * Do a delete to server for deliver a loan.
 * 
 * @param {type} isbn of the book.
 * @returns {undefined}
 */
function fabDeliver(isbn) {
    if (!isOpenDialog) {
        isOpenDialog = true;
        var box = "#borrowing-box" + isbn;
        var dialog = showDialog("Are you sure to deliver the loan?", function () {
            var IncreaseBorrowingUrl = "/JustRead/api/borrowing/" + isbn;
            $.delete(IncreaseBorrowingUrl,
                    function (data, status) {
                        console.log("data = " + data + " status = " + status);
                        if (status === "success" && data === true) {
                            var snakbar = showSnakBar("Operation succesfully complete");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            $(box).hide(400);
                            isOpenDialog = false;
                            $(dialog).hide(400);
                        } else {
                            var snakbar = showSnakBar("Operation unsuccesful");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            $(dialog).fadeOut(400);
                            isOpenDialog = false;
                            console.log("status:  " + status);
                        }
                    });
        });
        var is_element_input = $(("#defaultbook-box" + isbn)).is("div");
        if (is_element_input) {
            $(dialog).appendTo("#defaultbook-box" + isbn);
        } else {
            $(dialog).appendTo("article");
            $(dialog).css({
                'position': 'fixed',
                'left': '50%',
                'top': '50%',
                'margin-left': function () {
                    return -$(this).outerWidth() / 2
                },
                'margin-top': function () {
                    return -$(this).outerHeight() / 2
                }
            });
        }
    }
}

/**
 * Do a put to server for sending home book.
 * 
 * @param {type} isbn of the book.
 * @returns {undefined}
 */
function fabBook(isbn) {
    if (!isOpenDialog) {
        isOpenDialog = true;
        var dialog = showDialog("Are you sure to loan this book?", function () {
            var addBorrowingUrl = "/JustRead/api/borrowing/book/" + isbn;
            $.put(addBorrowingUrl,
                    function (data, status) {
                        console.log("data = " + data + " status = " + status);
                        if (status === "success" && data === true) {
                            var snakbar = showSnakBar("Operation succesfully complete");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            isOpenDialog = false;
                            $(dialog).hide(400);
                        } else {
                            var snakbar = showSnakBar("Operation unsuccesful");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            isOpenDialog = false;
                            $(dialog).hide(400);
                            console.log("status:  " + status);
                        }
                    });
        });
            $(dialog).appendTo("article");
            $(dialog).css({
                'position': 'fixed',
                'left': '50%',
                'top': '50%',
                'margin-left': function () {
                    return -$(this).outerWidth() / 2
                },
                'margin-top': function () {
                    return -$(this).outerHeight() / 2
                }
            });
    }
}

/**
 * Do a put to server for sending home book.
 * 
 * @param {type} isbn of the book.
 * @returns {undefined}
 */
function fabSendHome(isbn) {
    if (!isOpenDialog) {
        isOpenDialog = true;
        var dialog = showDialog("Are you sure to send home this book?", function () {
            var addBorrowingUrl = "/JustRead/api/borrowing/send/" + isbn;
            $.put(addBorrowingUrl,
                    function (data, status) {
                        console.log("data = " + data + " status = " + status);
                        if (status === "success" && data === true) {
                            var snakbar = showSnakBar("Operation succesfully complete");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            isOpenDialog = false;
                            $(dialog).hide(400);
                        } else {
                            var snakbar = showSnakBar("Operation unsuccesful");
                            $(snakbar).appendTo('article');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(
                                    function ()
                                    {
                                        $(snakbar).remove();
                                    }, 5000);
                            isOpenDialog = false;
                            $(dialog).hide(400);
                            console.log("status:  " + status);
                        }
                    });
        });
            $(dialog).appendTo("article");
            $(dialog).css({
                'position': 'fixed',
                'left': '50%',
                'top': '50%',
                'margin-left': function () {
                    return -$(this).outerWidth() / 2
                },
                'margin-top': function () {
                    return -$(this).outerHeight() / 2
                }
            });
    }
}

/**
 * Create and show a dialog to confirm or reject operation
 * of the user.
 * 
 * @param {type} text to show
 * @param {type} onConfirm function if accept operation.
 * @returns {Element|showDialog.dialog}
 */
function showDialog(text, onConfirm) {
    console.log("dialog creato ");
    var dialog = document.createElement("div");
    dialog.id = "dialog-form";
    dialog.className = "dialog-box";
    dialog.innerHTML = "<p class='title-dialog'>" + text + "</p>";

    var yesBtn = document.createElement("button");
    yesBtn.innerHTML = "YES";
    yesBtn.onclick = onConfirm;
    yesBtn.className = "yes-no-dialog";
    dialog.appendChild(yesBtn);

    var noBtn = document.createElement("button");
    noBtn.innerHTML = "NO";
    noBtn.onclick = function () {
        $("#dialog-form").remove();
        isOpenDialog = false;
    };
    noBtn.className = "yes-no-dialog";
    dialog.appendChild(noBtn);

    return dialog;
}

/**
 * Create and show a snakBar in the bottom right corner
 * to display to user information about operation.
 * 
 * @param {type} text to show.
 * @returns {Element|showSnakBar.snakBar}
 */
function showSnakBar(text) {
    console.log("snak creato ");
    var snakBar = document.createElement("div");
    snakBar.id = "bar-form";
    snakBar.className = "snakbar-box";
    snakBar.innerHTML = "<p class='title-dialog'>" + text + "</p>";

    return snakBar;
}

/**
 * Return the rate of the book by printing on screen
 * the correct star image icon.
 * 
 * @param {Float} rate of the book 
 * @param {Integer} incr depends on path url
 */
function printRate(rate, incr) {
    var printRate = "";
    var src1, src2;
    if (incr === 1) {
        src1 = "'../../icons/star_hover.png'";
        src2 = "'../../icons/star_black.png'";
    } else {
        src1 = "'../icons/star_hover.png'";
        src2 = "'../icons/star_black.png'";
    }
    for (x = 1; x <= rate; x++) {
        printRate += "<span><img class='star-checked' src=" + src1 + "/></span>";
    }
    for (y = rate; y < 5; y++) {
        printRate += "<span><img class='star-non-checked' src=" + src2 + "/></span>";
    }
    return printRate;
}

/**
 * Return the rate of the book by printing on screen
 * the correct star image icon.
 * 
 * @param {Float} rate of the book
 */
function printRateSmall(rate) {
    var printRate = "";
    var src1, src2;
    src1 = "'../../../icons/star_hover.png'";
    src2 = "'../../../icons/star_black.png'";
    for (x = 1; x <= rate; x++) {
        printRate += "<img  class='rating-star-infobox' src=" + src1 + "/>";
    }
    for (y = rate; y < 5; y++) {
        printRate += "<img  class='rating-star-infobox' src=" + src2 + "/>";
    }
    return printRate;
}

/**
 * Hide and do delete to servlet for deleting favourite 
 * form list.
 * 
 * @param key 
 * @param isbn
 */
function deleteFavourite(key, isbn) {
    console.log(isbn);
    var urlfavourite = "/JustRead/api/favourites";
    var fabFav = "#favourite-box" + key;
    var favUrlBook = urlfavourite + "/" + isbn;
    console.log(favUrlBook);
    $.delete(favUrlBook,
            function (data, status) {
                console.log("data = " + data + " status = " + status);
                if (status === "success" && data === true) {
                    $(fabFav).hide(500);
                    var snakbar = showSnakBar("Book deleted to your favourite list");
                    $(snakbar).appendTo('article');
                    $(snakbar).delay(4500).fadeOut('slow');
                    setTimeout(
                            function ()
                            {
                                $(snakbar).remove();
                            }, 5000);
                } else {
                    var snakbar = showSnakBar("Operation not succesful");
                    $(snakbar).appendTo('article');
                    $(snakbar).delay(4500).fadeOut('slow');
                    setTimeout(
                            function ()
                            {
                                $(snakbar).remove();
                            }, 5000);
                    console.log(status);
                }
            });
}

/**
 * Hide and do put to servlet for adding favourite 
 * form list.
 * 
 * @param isbn
 */
function addFavourite(isbn) {
    console.log(isbn);
    var urlfavourite = "/JustRead/api/favourites";
    var favUrlBook = urlfavourite + "/" + isbn;
    console.log(favUrlBook);
    $.put(favUrlBook,
            function (data, status) {
                console.log("data = " + data + " status = " + status);
                if (status === "success" && data === true) {
                    var snakbar = showSnakBar("Book added to your favourite list");
                    $(snakbar).appendTo('article');
                    $(snakbar).delay(4500).fadeOut('slow');
                    setTimeout(
                            function ()
                            {
                                $(snakbar).remove();
                            }, 5000);
                } else {
                    var snakbar = showSnakBar("Operation not succesful");
                    $(snakbar).appendTo('article');
                    $(snakbar).delay(4500).fadeOut('slow');
                    setTimeout(
                            function ()
                            {
                                $(snakbar).remove();
                            }, 5000);
                    console.log(status);
                }
            });
}

function sendReview(isbn) {
    var check;
    if ($('#rating-input-1-5').is(':checked')) {
        check = "5";
    } else if ($('#rating-input-1-4').is(':checked')) {
        check = "4";
    } else if ($('#rating-input-1-3').is(':checked')) {
        check = "3";
    } else if ($('#rating-input-1-2').is(':checked')) {
        check = "2";
    } else if ($('#rating-input-1-1').is(':checked')) {
        check = "1";
    }
    var comment = $("#textarea-review").val();
    var urlReview = "/JustRead/api/catalog/addreview/" + isbn;
    console.log(urlReview);
    $.post(urlReview,
            {
                rate: check,
                review: comment,
                littleHeart: "false"
            },
            function (data, status) {
                console.log("data = " + data + " status = " + status);
                if (status === "success" && data === "true") {
                    $("#send-review").val("modify your review");
                    var snakbar = showSnakBar("Review added");
                    $(snakbar).appendTo('body');
                    $(snakbar).delay(1800).fadeOut('slow');
                    setTimeout(
                            function () {
                                $(snakbar).remove();
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

function initMap() {
    var uluru = {lat: 45.090135, lng: 7.659229};
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 14,
        center: uluru
    });
    var marker = new google.maps.Marker({
        position: uluru,
        map: map
    });
}

/**
 * Create and show a dialog to confirm or reject operation
 * of the user.
 * 
 * @param {String} name
 * @param {String} surname
 * @param {String} email   
 * @returns {Element|showDialog.dialog}
 */
function showProfileDialog(name, surname, email) {
    console.log("dialog profile creato ");
    var dialog = document.createElement("div");
    dialog.id = "dialog-form";
    dialog.className = "dialog-profile-box";
    dialog.innerHTML = "<p class='profile-dialog'> " + name + " " + surname + "<br>" + "<a href='" + email + "'>" + email + "</a></p>";

    var yesBtn = document.createElement("button");
    yesBtn.innerHTML = "LOGOUT";
    yesBtn.id = "logout-btn";
    yesBtn.className = "yes-no-dialog";
    dialog.appendChild(yesBtn);

    var noBtn = document.createElement("button");
    noBtn.innerHTML = "OK";
    noBtn.onclick = function () {
        $("#dialog-form").remove();
    };
    noBtn.className = "yes-no-dialog";
    dialog.appendChild(noBtn);

    return dialog;
}

/**
 * Create and show a dialog to confirm or reject operation
 * of the user.
 * 
 * @param {String} notify
 * @returns {Element|showDialog.dialog}
 */
function showNotificationsDialog(notify) {
    console.log("dialog profile creato ");
    var dialog = document.createElement("div");
    dialog.id = "dialog-form";
    dialog.className = "dialog-profile-box";
    dialog.innerHTML = "<p class='profile-dialog'> " + notify +"</p>";

    var noBtn = document.createElement("button");
    noBtn.innerHTML = "OK";
    noBtn.onclick = function () {
        $("#dialog-form").remove();
    };
    noBtn.className = "yes-no-dialog";
    dialog.appendChild(noBtn);

    return dialog;
}

/**
 * Search
 * 
 * @returns {undefined}
 */
function search(){
    var urlSearch = "/JustRead/api/search?search=" + $("#search-input").val();
    var url = window.location.pathname;
    window.history.pushState( {} , 'bar', '/JustRead/app/search' );
        var search;
        $.getJSON(urlSearch, 
            function(data){
                if(data === null || data.length === 0){
                   search = "<p>No matches found<p>"; 
                   $("article").replaceWith("<article id='article' class='article'></article>");
                    $(search).appendTo("article");
                } else {
                    var books = [];
                    $.each(data, function (key, val) {
                        console.log(data);
                        books.push("<div class='history-box'>" +
                                        "<div class='book-cover-container' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlhistory + "\")'>" +
                                            "<img id='book-cover' class='cover-book-history' alt='book cover' title='book cover' src=' " + val.cover + "'/>" +
                                        "</div>" +
                                        "<div class='book-info'>" +
                                            "<h2 class='title-book-history' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlhistory + "\")'>" + val.title + "</h2>" +
                                            "<h3 class='author-book-history' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlhistory + "\")'>" + val.author + "</h3>" +
                                            "<div class='fab-box-history'>" +
                                                "<div class='fab-book' onclick=fabBook('"+ val.isbn + "')>" +
                                                    "<img class='fab-book-icon' alt='book book' title='book book' src='../icons/library-plus.png'/>" +
                                                "</div>" +
                                                "<div class='fab-send' onclick=fabSendHome('"+ val.isbn + "')>" +
                                                    "<img class='fab-send-icon' alt='send home' title='send home' src='../icons/truck.png'/>" +
                                                "</div>" +
                                            "</div>" +
                                            "<div class='star-ratings'>" +
                                                printRate(val.rate) +
                                            "</div>" +
                                        "</div>" +
                                    "</div>");
                    });
                    $("body").css("background-image", "none");
                    $("#main-actionbar").css("background-color", "#F44336");
                    $("article").replaceWith("<article id='article' class='article'></article>");
                    $(books.join("")).appendTo("article");
                }
            });
}

function modifyProfile(name, surname){
    var profileChange = "<div class='category-container card' style='height:100%'>" +
                    "<h1 class='title-contacts center'>Profile</h1>" +
                    "<div class='divider'></div>" +
                    "<p class='answer'>Given the impossible verification of the address, we invite you to check accurately \n\
                    the accuracy and reliability of the data you have entered.<br>\n\
                    This will allow you to take advantage of our home delivery service in an easy way.<br>\n\
                    Thanks for understanding, Administrators.</p>" +
                    "<form method='post' enctype='multipart/form-data'>" +
                    "<label class='input-image' for='file'>Choose a image to upload: </label>" +
                    "<input id='profile-image-user' type='file' name='file' accept='image/*'>" +
                    "<input id='name-input-prof' class='input-name' type='text' name='name' value='" + name + "'/>" +
                    "<input id='surname-input-prof' class='input-name' type='text' name='name' value='" + surname + "'/>" +
                    "<input id='address-input-prof' class='input-name' type='text' name='name' placeholder='insert address here ...'/>" +
                    "<input id='btn-save-profile' class='modify-profile' value='Save change'/></form>" +
                 "</div>";
    $("article").replaceWith("<article id='article' class='article'></article>");
    $(profileChange).appendTo("article");
    $("#btn-save-profile").click(function(){     
        var nameProf = $("#name-input-prof").val();
        var surnameProf = $("#surname-input-prof").val();
        var addressProf =  $("#address-input-prof").val();
        var imageProfile = $("#profile-image-user").val();
        var urlModifyProfile = "/JustRead/api/user/modify";
        $.post(urlModifyProfile,
                {
                    name: nameProf,
                    surname: surnameProf,
                    address: addressProf
                },
                function(data, status){
                    if (status === "success" && data === true) {
                        var snakbar = showSnakBar("Profile modify correctly");
                        $(snakbar).appendTo('body');
                        $(snakbar).delay(1800).fadeOut('slow');
                        setTimeout(function (){
                            $(snakbar).remove();
                            window.location.replace("/JustRead/app/");
                            }, 2000);
                        } else {
                            var snakbar = showSnakBar("Operation unsuccesful, we invite you to try again");
                            $(snakbar).appendTo('body');
                            $(snakbar).delay(4500).fadeOut('slow');
                            setTimeout(function (){
                                $(snakbar).remove();
                            }, 5000);
                        }
                });
    });
}

function readURL(input) {

  if (input.files && input.files[0]) {
    var reader = new FileReader();

    reader.onload = function(e) {
        console.log("sto facendo laod");
        console.log(e.target.result);
    };

    reader.readAsDataURL(input.files[0]);
  }
}

function chooseFile(){
    $("#cover").trigger('click');
    readURL(this);
}