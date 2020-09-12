var urlfavourite = "/JustRead/api/favourites";
var urlborrowing = "/JustRead/api/borrowing";
var urlhistory = "/JustRead/api/history";
var urlcontacts = "/JustRead/api/contacts";

$("document").ready(function () {
    window.history.pushState('JustRead/app/catalog', 'Title', 'catalog');
    $.getJSON("/JustRead/api/catalog", function (data) {
        var items = [];
        $.each(data, function (key, val) {
            var url = "/JustRead/api/catalog/" + val.name;
            items.push("<div class='category-container'>\n\
                            <div class='category-title' title='clcik to expand genre' onclick='showCategory(\"" + val.name +"\")'>\n\
                                <img src='data:image/png;base64," + val.icon + "' class='icon-genre'/><li id='category-name'>" + val.name + "<span class='show-more'><img src='../icons/chevron-down.svg' alt='show more' title='show more' class='down-arrow'/></span></li>" +
                    "</div>" +
                    "<div class='divider'></div>" +
                    "<div id='book-container" + key + "' class='book-container'></div>" +
                    "</div>");
            $.getJSON(url, function (data) {
                var books = [];
                $.each(data, function (key, val) {
                    books.push("<div id='" + key + "' class='book-item' onclick='showBookInfo(\"" + val.isbn +"\", \"" + url + "\")'>" +
                            "<div class='cover-book-box'>" +
                            "<img class='cover-book' alt='book cover' title='click to show more info' src='" + val.cover + "'/>" +
                            "</div>" +
                            "<div class='title-book-box'><h2 class='title-book'>" + val.title + "</h2></div>" +
                            "</div>");
                });
                var id = "#book-container" + key;
                $(books.join("")).appendTo(id);
            });
        });

        $("<ul/>", {
            "class": "category-list",
            html: items.join("")
        }).appendTo("article");
    });
    
    //get favourites
    $("#favourites-link").click(function () {
        window.history.pushState('JustRead/app/catalog', 'Title', 'favourites');
        $.post(urlfavourite, function (data) {
            var books = [];
            $.each(data, function (key, val) {
                books.push("<div id='favourite-box" + key + "' class='history-box' >" +
                                "<div class='book-cover-container' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlfavourite + "\")'>" +
                                    "<img id='book-cover' class='cover-book-history' alt='book cover' title='book cover' src=' " + val.cover + "'/>" +
                                "</div>" +
                                "<div class='book-info'>" +
                                    "<h2 class='title-book-history' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlfavourite + "\")'>" + val.title + "</h2>" +
                                    "<h3 class='author-book-history' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlfavourite + "\")'>" + val.author + "</h3>" +
                                    "<div class='fab-box-favourite'>" +
                                        "<div id='fab-fav" + key + "' class='fab-book' onclick=deleteFavourite(" + key + ",\"" + val.isbn +"\")>" +
                                            "<img class='fab-book-icon' alt='book book' title='book book' src='../icons/heart.png'/>" +
                                        "</div>" +
                                    "</div>" +
                                    "<div class='star-ratings'>" +
                                        printRate(val.rate) +
                                    "</div>" +
                                "</div>" +
                            "</div>");
            });
            $("article").replaceWith("<article id='article' class='article'></article>");
            $(books.join("")).appendTo("article");
        });
    });
    //
    
    //get borrowing
    $("#borrowing-link").click(function () {
        window.history.pushState('JustRead/app/catalog', 'Title', 'borrowing');
        $.getJSON(urlborrowing, function  (data) {
            var borrowing = [];
            $.each(data, function (key, val) {
                borrowing.push("<div id='borrowing-box" + val.isbn + "' class='borrowing-box' >" +
                                "<div class='book-cover-container' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlborrowing + "\")'>" +
                                    "<img id='book-cover' class='cover-book-borrowing' alt='book cover' title='book cover' src=' " + val.cover + "'/>" +
                                "</div>" +
                                "<div class='book-info'>" +
                                    "<h2 class='title-book-borrowing' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlborrowing + "\")'>" + val.title + "</h2>" +
                                    "<h3 class='author-book-borrowing' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlborrowing + "\")'>" + val.author + "</h3>" +
                                    "<div class='start-end-date'>" + val.dates + "</div>" +
                                    "<div class='progress-bar-container'>" +
                                        "<div class='current-progress' style='width:" + val.progress+ "%;'></div>" +
                                    "</div>"+
                                    "<div class='fab-box'>" +
                                        "<div class='fab-increase' onclick=fabIncrease('"+ val.isbn + "')>" +
                                            "<img class='fab-increase-icon' alt='increase' title='increase' src='../icons/repeat.png'/>" +
                                        "</div>" +
                                        "<div class='fab-deliver' onclick=fabDeliver('"+ val.isbn + "')>" +
                                            "<img class='fab-deliver-icon' alt='deliver' title='deliver' src='../icons/calendar-check.png'/>" +
                                        "</div>" +
                                    "</div>" +
                                "</div>" +
                            "</div>");
            });
            $("article").replaceWith("<article id='article' class='article'></article>");
            $(borrowing.join("")).appendTo("article");
        });
    });
    //
    
    //get history
    $("#history-link").click(function () {
        window.history.pushState('JustRead/app/catalog', 'Title', 'history');
        $.getJSON(urlhistory, function (data) {
            var books = [];
            $.each(data, function (key, val) {
                books.push("<div id='defaultbook-box" + val.isbn + "' class='history-box'>" +
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
            $("article").replaceWith("<article id='article' class='article'></article>");
            $(books.join("")).appendTo("article");
        });
    });
    //
    
    //get contact 
    $("#contacts-link").click(function () {
        window.history.pushState('JustRead/app/catalog', 'Title', 'contacts');
        $.getJSON(urlcontacts, function (data) {
            var calendar = "<table class='calendar'>";
            for(i = 0; i < data.length; i++){
                if(i%2 === 0){
                    calendar += "<tr class='personal-tr'><td class='personal-td'> " + data[i] + "</td>";
                } else {
                    calendar += "<th class='personal-th'> " + data[i] + "</th></tr>";
                }
            }
            calendar += "</table>";
            var contact =   "<div class='category-container card'>" +
                                "<h1 class='title-contacts'>General</h1>" +
                                "<div class='divider'></div>" +
                                "<div id='map' class='map'></div>" +
                                "<table class='table-info'>" +
                                    "<tr class='tr-info'><th class='tr-info'>Library Name:  </th>" +
                                    "<td class='tr-info red'>Just Read</td></tr>" +
                                    "<tr class='tr-info'><th class='tr-info'>Street:  </th>" +
                                    "<td class='tr-info red'><a href='http://maps.google.com/?q=1200 Via Pessinetto 12, Torino, 10149'>Via Pessinetto 18, TO</a></td></tr>" +
                                    "<tr class='tr-info'><th class='tr-info'>Phone:  </th>" +
                                    "<td class='tr-info red'><a href='tel:+39 017183451'>017183451</a></td></tr>" +
                                    "<tr class='tr-info'><th class='tr-info'>Email:  </th>" +
                                    "<td class='tr-info red'><a href='mailto:justread.jr@flashmail.com'>justread.jr@flashmail.com</a></td></tr>" +
                                "</table>" +
                            "</div>" +
                            "<div class='category-container card'> " +
                                "<h1 class='title-contacts'>Buisness hour</h1>" +
                                "<div class='divider'></div>" +
                                calendar +
                            "</div>";
            $("article").replaceWith("<article id='article' class='article'></article>");
            $(contact).appendTo("article");
            initMap();
        });
    });
    //
    //get faq
    $("#faq-link").click(function () {
        window.history.pushState('JustRead/app/catalog', 'Title', 'faq');
        var faq = "<div class='category-container card' style='height:100%'>" +
                    "<h1 class='title-contacts'>Frequently Assisted Question</h1>" +
                        "<div class='divider'></div>" +
                    "<h2 class='question'>How loans work?</h2>" +
                    "<p class='answer'>JustRead Library uses the follwing rules:<br>"+
                    "When you borrow a book on default you have 30 days to read it. If you were be able to read in time you just have to deliver whenever you want; if you weren't be able to read in time you can " +
                    "increase your loan for 3 times. The possibility to increase the current borrowing will be allowded after 15 days after start date of the borowing.<br>"+
                    "If you don't deliver within the maximum terms established you will not be able to make any loans.<br>"+
                    "<span class='nb'>Always check the notifications section or the borrowing section to check the status of your loans!</span></p>"+
                    "<h2 class='question'>How can i book a book?</h2>" +
                    "<p class='answer'>You can choose two way:<br> - Click on the floating button with plus icon on book info window;<br>"
                    +" - Click on the floating button, also with plus icon, that appear when you expand a category (or in history window)</p>"+
                    "<h2 class='question'>How can i send book home?</h2>" +
                    "<p class='answer'>The first thing you have to do is check if you have the correct address (or check if you have insert address on your profile page).<br>"
                    +" Than you can choose two way:<br> - Click on the floating button with van icon on book info window;<br>"
                    +" - Click on the floating button, also with van icon, that appear when you expand a category (or in histroy window)</p>"+
                    "<h2 class='question'>What about privacy?</h2>" +
                    "<p class='answer'>Regarding privacy every users can't visualize all the information about others. For each users all sensitive data are encrypted with a symmetric key algorithm.<br>"+
                    "The only information you can view is profile image of users that release a review of a book.</p>"+
                    "<h2 class='question'>It is possible to use the application without being registered?</h2>" +
                    "<p class='answer'>No, it isn't. This because application is designed also for users that want to improve your experience with us. The application is an extensione of the website that allow users to improve their experience.</p>"+
                "</div>";
        $("article").replaceWith("<article id='article' class='article'></article>");
        $(faq).appendTo("article");
    });
    //
    //get about
    $("#about-link").click(function () {
        window.history.pushState('JustRead/app/catalog', 'Title', 'about');
        var about = "<div class='category-container card' style='height:100%'>" +
                    "<h1 class='title-contacts center'>What about us?</h1>" +
                        "<div class='divider'></div>" +
                    "<h2 class='question  center'>Arnaudo Enrico</h2>" +
                    "<p class='answer  center'><i>\"Declare varaibles, not war\"</i><br><br>I have been programming for 5 years and I have been using different programming languages including Java, C, Python, Javascript." +
                    "This is my first web application and I believe it will not be the last one. (at least I hope)</p>" +
                    "<h2 class='question  center'>Giraudo Paolo</h2>" +
                    "<p class='answer  center'><i>\"You don't have to be great to start, but you have to start to be great\"</i><br><br>I've been programing since I was 15, using different higj-level programing languages like Java, C, C++ and javascript</p>" +
                    "<h2 class='question  center'>Impeduglia Alessia</h2>" +
                    "<p class='answer  center'><i>\"Learn for being a person, the world needs you\"</i><br><br>My first step in programming were in University. In 5 year I've learned Java, C, JavaScript and Python. This is my first web application.</p>" +
                "</div>";
        $("article").replaceWith("<article id='article' class='article'></article>");
        $(about).appendTo("article");
    });
    //
    //get app
    $("#application-link").click(function () {
        window.history.pushState('JustRead/app/catalog', 'Title', 'application');
        var about = "<div class='category-container card' style='height:100%'>" +
                    "<h1 class='title-contacts center'>Application</h1>" +
                        "<div class='divider'></div>" +
                    "<h2 class='question center'>Stay reader</h2>" +
                    "<p class='answer center'>Find out in the cataloge your ideal book and loan it whenever, wherever</p>" +
                    "<img class='intro' src='../icons/intro1.png' title='intro image' alt='intro image' draggable='false'/>" +
                    "<h2 class='question center'>Double the time …</h2>" +
                    "<p class='answer center'>Enjoy your reading, renewe your loan</p>" +
                    "<img class='intro' src='../icons/intro2.png' title='intro image' alt='intro image' draggable='false'/>" +
                    "<h2 class='question center'>… But stay alert</h2>" +
                    "<p class='answer center'>Verify your remaining time in your personal section</p>" +
                    "<img class='intro' src='../icons/intro3.png' title='intro image' alt='intro image' draggable='false'/>" +
                    "<h2 class='question center'>Did you enjoy your books? Love it</h2>" +
                    "<p class='answer center'>Save your best readings in your favourite list</p>" +
                    "<img class='intro' src='../icons/intro4.png' title='intro image' alt='intro image' draggable='false'/>" +
                "</div>";
        $("article").replaceWith("<article id='article' class='article'></article>");
        $(about).appendTo("article");
    });
    //
    //get profile
    $("#profile-link").click(function () {
        window.history.pushState('JustRead/app/catalog', 'Title', 'profile');
        $.getJSON("/JustRead/api/user/info", 
            function(data){
                    var profile = "<div class='category-container card' style='height:100%'>" +
                                    "<h1 class='title-contacts center'>Profile</h1>" +
                                        "<div class='divider'></div>" +
                                        "<img src='../icons/profile.png' alt='profile image' class='user-profile-image'/>" +
                                        "<h2 class='name-surname'>" + data.name + "  " + data.surname + "</h2>" +
                                        "<h3 class='user-email'>" + data.email + "</h3>" +
                                        "<h3 class='info-extra'>" + data.address + "</h3>" +
                                        "<button class='modify-profile' onclick='modifyProfile(\"" + data.name + "\", \"" + data.surname + "\")'>Modify profile</button> " +
                                    "</div>";
                                    $("article").replaceWith("<article id='article' class='article'></article>");
                                    $(profile).appendTo("article"); 
            });
    });
    //
    
    $("#profile-link-box").click(function(){
        $.getJSON("/JustRead/api/user/info", 
        function(data){
            if(data !== null){
                var dialog = showProfileDialog(data.name, data.surname, data.email);
                $("body").append(dialog);
                    $("#logout-btn").click(function () {
                    console.log("parte la logout");
                    $.delete("/JustRead/api/logout",
                            function (data) {
                                if (data === 'true') {
                                    $("#dialog-form").remove();
                                    console.log("data ok");
                                    window.location.replace("/JustRead/");
                                } else {
                                    alert("operation unsuccesful");
                                }
                });
    });
            } else {
                alert("operation unsuccesfully");
            }
        });
        
    });
    
    $("#notification-link-box").click(function(){
        $.getJSON("/JustRead/api/user/notifications", 
            function(data){
                if(data !== null){
                   var dialog = showNotificationsDialog(data);
                   $("body").append(dialog);
                }
            });
    });
    
    //search
    $("#search-icon").click(function(){
        search();
    });
    $('#search-input').bind("enterKey",function(e){
        search();
    });
    $('#search-input').keyup(function(e){
        if(e.keyCode === 13){
            $(this).trigger("enterKey");
        }
    });
    //
    
});

function showCategory(name){
    $("#navigation-drawer").css("width", "0%");
    $("#navigation-drawer").css("visibility", "hidden");
    var newPathUrl = "/JustRead/app/catalog/" + name;
    $("body").css("background-image", "none");
    $("#main-actionbar").css("background-color", "#F44336");
    window.history.pushState(window.location.pathname, 'Title', newPathUrl);
    var urlCategory = "/JustRead/api/catalog/" + name;
    $.getJSON(urlCategory, function (data) {
            var books = [];
            $.each(data, function (key, val) {
                books.push("<div id='defaultbook-box" + val.isbn + "' class='history-box'>" +
                                "<div class='book-cover-container' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlCategory + "\")'>" +
                                    "<img id='book-cover' class='cover-book-history' alt='book cover' title='book cover' src=' " + val.cover + "'/>" +
                                "</div>" +
                                "<div class='book-info'>" +
                                    "<h2 class='title-book-history' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlCategory + "\")' >" + val.title + "</h2>" +
                                    "<h3 class='author-book-history' onclick='showBookInfo(\"" + val.isbn +"\", \"" + urlCategory + "\")' >" + val.author + "</h3>" +
                                    "<div class='fab-box-history'>" +
                                        "<div class='fab-book' onclick=fabBook('"+ val.isbn + "')>" +
                                            "<img class='fab-book-icon' alt='book book' title='book book' src='../../icons/library-plus.png'/>" +
                                        "</div>" +
                                        "<div class='fab-send' onclick=fabSendHome('"+ val.isbn + "')>" +
                                            "<img class='fab-send-icon' alt='send home' title='send home' src='../../icons/truck.png'/>" +
                                        "</div>" +
                                    "</div>" +
                                    "<div class='star-ratings'>" +
                                        printRate(val.rate, 1) +
                                    "</div>" +
                                "</div>" +
                            "</div>");
            });
            $("article").replaceWith("<article id='article' class='article-exp'></article>");
            $(books.join("")).appendTo("article");
        });
    $("#menu-icon").attr("src","../../icons/arrow-left.svg");
    $("#menu-icon").click(function(){
        window.history.go(-1);
        $("article").animate({
                width: "77%"
        });
        window.location.replace("/JustRead/app/");
        $("#menu-icon").attr("src","../icons/menu.svg");
    });
}

/**
 * Show all the information about the selected book.
 * 
 * @param {String} isbn of the book
 * @param {String} path of the url
 * @returns {undefined}
 */
function showBookInfo(isbn, path){
    $("#navigation-drawer").css("width", "0%");
    $("#navigation-drawer").css("visibility", "hidden");
    $("#menu-icon").click(function(){
        window.history.go(-1);
        window.location.replace("/JustRead/app/");
        $("#menu-icon").attr("src","../../../icons/menu.svg");
    });
    var regex = new RegExp(/\/JustRead\/api\/catalog\/\w{3,15}$/);
    console.log("path = " + path + " match = " + path.match(regex));
    if(path.match(regex)){
        var newPathInfo = "/JustRead/app/catalog" + path.slice(21) + "/" + isbn; 
    } else {
        console.log(path.slice(14));
        var newPathInfo = "/JustRead/app/" + path.slice(14) + "/book/" + isbn;
    }
    var urlBookInfo = path + "/" + isbn;
    window.history.pushState("/JustRead/app/catalog", 'Title', newPathInfo);
    $("#menu-icon").attr("src","../../../icons/arrow-left-white.png");
    $.getJSON(urlBookInfo, function (data) {
        console.log(data);
        var coverUrl =  data.cover;
        var bookIsbn = data.isbn;
        var avaiable = data.isAvaiable;
        var bookinfo =  "<div class='info-box'>" +
                            "<h2 class='bookinfo-title'>" + data.title + "</h2>" +
                            "<h3 class='bookinfo-author'>" + data.author + "</h3>" +
                            "<div class='divider'></div>" +
                            "<div class='bookinfo-rating-box'>" +
                                "<div class='bookinfo-rating'>" +
                                    "<h3 class='rate-text'>" + Number(data.rate).toFixed(1) + "</h3>" +
                                    printRateSmall(data.rate) +
                                    "<h6 class='number-voters'>" + data.numVoters + "</h6>" +
                                    "<img class='account-num-voters' src='../../../icons/account.svg'/>" +
                                "</div>" +
                                "<div class='rating-container'>" +
                                    "<div class='five-star-box' style='width:" + data.fiveStar + "0%'></div>" +
                                    "<div class='four-star-box' style='width:" + data.fourStar + "0%'></div>" +
                                    "<div class='three-star-box' style='width:" + data.threeStar + "0%'></div>" +
                                    "<div class='two-star-box' style='width:" + data.twoStar + "0%'></div>" +
                                    "<div class='one-star-box' style='width:" + data.oneStar + "0%'></div>" +
                                "</div>" +
                                "<div class='general-info-box'>" +
                                    "<table class='table-info-bookinfo'>" +
                                        "<tr><th class='general-info-title'>Publisher</th><th class='general-info-title'>Pages</th></tr>" +
                                        "<tr><td>" + data.publisher + "</td><td>" + data.pages + "</td></tr>" +
                                        "<tr><th class='general-info-title'>ISBN</th><th class='general-info-title'>Language</th></tr>" +
                                        "<tr><td>" + data.isbn + "</td><td>" + data.language + "</td></tr>" +
                                    "</table>" +
                                "</div>" +
                            "</div>" +
                        "</div>" +
                        "<div class='plot-box'>" +
                            "<h2 class='plot-title'>Plot</h2>" + 
                            "<div class='divider'></div>" +
                            "<div class='plot-text'><p>" + data.plot + "</p></div>" +
                        "</div>" +
                        "<div class='category-box'>" +
                            "<h2 class='plot-title'>Categories</h2>" + 
                            "<div class='divider'></div>";
                    $.post(urlBookInfo,
                        {
                            info: "categories"
                        },
                    function (data, status) {
                        $.each(data, function(key, val){
                            bookinfo += "<div class='category-tag-container' onclick='showCategory(\"" + val.name +"\")'>" +
                                            "<img class='tag-image' src='data:image/png;base64," + val.icon + "' style='background-color:#AA00FF;'/><h6 class='title-category'>" + val.name + "</h6>" +
                                        "</div>";
                        });
                        bookinfo += "<h2 class='plot-title personal-reviews-title'>Your review</h2>" +
                                    "<div class='divider'></div>" +
                                    "<h6 class='title-review-insert'>Insert your rate here</h6>" +
                                        "<span class='rating-insert'>" +
                                            "<input type='radio' class='rating-input' id='rating-input-1-5' name='rating-input-1'><label for='rating-input-1-5' class='rating-star'></label>" +
                                            "<input type='radio' class='rating-input' id='rating-input-1-4' name='rating-input-1'><label for='rating-input-1-4' class='rating-star'></label>" +
                                            "<input type='radio' class='rating-input' id='rating-input-1-3' name='rating-input-1'><label for='rating-input-1-3' class='rating-star'></label>" +
                                            "<input type='radio' class='rating-input' id='rating-input-1-2' name='rating-input-1'><label for='rating-input-1-2' class='rating-star'></label>" +
                                            "<input type='radio' class='rating-input' id='rating-input-1-1' name='rating-input-1'><label for='rating-input-1-1' class='rating-star'></label>" +
                                        "</span>" +
                                        "<textarea id='textarea-review' class='input-personal-review' rows='5' cols='50'></textarea>" + 
                                        "<input id='send-review' class='send-review-btn' type='submit' name='action' value='send review' onclick='sendReview(\"" + bookIsbn +"\")'/>" +
                                    "</div>" +
                                "</div>";
                            bookinfo += "<div class='review-box'>" +
                                    "<h2 class='plot-title reviews-title'>Reviews</h2>" +
                                    "<div class='divider'></div>" +
                                    "<div class='reviews-users'>";
                            $.post(urlBookInfo,
                            {
                                info: "reviews"
                            },
                            function (data, status) {
                                $.each(data, function(key, val){
                                    console.log(val);
                                    bookinfo += "<div class='reviews-users-box'>" +
                                                    "<img class='profile-image' src='../../../icons/profile.png'/>" +
                                                    "<p class='box-review'>" + val.comment + "</p>" +
                                                "</div>";
                                });
                            if(avaiable){
                                bookinfo += "</div></div>" + 
                                        "<div class='fixed-action-btn'>" +
                                            "<a class='fab-book-open' onclick=fabBook('" + bookIsbn + "')><span class='title-fab fab1'>Borrow book</span><img class='large material-icons fab-icon-open' src='../../../icons/library-plus.png'/></a>" +
                                            "<a class='fab-send-open' onclick=fabSendHome('" + bookIsbn + "')><span class='title-fab fab2'>Send home</span><img class='large material-icons fab-icon-open' src='../../../icons/truck.png'/></a>" +
                                            "<a class='fab-fav-open' onclick=addFavourite('" + bookIsbn + "')><span class='title-fab fab3'>Add to favourite</span><img class='large material-icons fab-icon-open' src='../../../icons/heart.png'/></a>" +
                                        "</div>";
                            } else {
                                bookinfo += "</div></div>" + 
                                        "<div class='fixed-action-btn'>" +
                                            "<a class='fab-book-open' onclick=fabIncrease('"+ bookIsbn + "')><span class='title-fab fab1'>Increase loan</span><img class='large material-icons fab-icon-open' src='../../../icons/repeat.png'/></a>" +
                                            "<a class='fab-send-open' onclick=fabDeliver('" + bookIsbn + "')><span class='title-fab fab2'>Deliver loan</span><img class='large material-icons fab-icon-open' src='../../../icons/calendar-check.png'/></a>" +
                                            "<a class='fab-fav-open' onclick=addFavourite('" + bookIsbn + "')><span class='title-fab fab3'>Add to favourite</span><img class='large material-icons fab-icon-open' src='../../../icons/heart.png'/></a>" +
                                        "</div>";
                            }

                            $("article").replaceWith("<article id='article' class='article'></article>");
                            console.log("article rimpiazzato !!");
                            $("#article").css("width", "90%");
                            $("#article").css("margin-right", "5%");
                            $("body").css("background-image", "url(\"" + coverUrl +"\")");
                            $("body").css("background-size", "cover");
                            $("body").css("background-attachment", "fixed");
                            $("body").css("background-position", "center");
                            $("#main-actionbar").css("background-color", "rgba(0,0,0,0.4)");
                            $(bookinfo).appendTo("article");
                        });
                    });
    });
}


