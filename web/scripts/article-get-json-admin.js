/* global parseFloat */

var urlactiveborrowing = "/JustRead/api/admin/active";
var urlexpiredborrowing = "/JustRead/api/admin/expired";
var urlarchiveborrowing = "/JustRead/api/admin/archive";
var urladminuser = "/JustRead/api/admin/user";
var urlnewbook = "/JustRead/api/admin/newbook";
var urlallusers = "/JustRead/api/admin/allusers";
var urlallbooks = "/JustRead/api/admin/allbooks";
var urlallauthors = "/JustRead/api/admin/allauthors";
var urlallcategories = "/JustRead/api/admin/allcategories";
var urlpromoteuser = "/JustRead/api/admin/promoteuser";
var urldeletebook = "/JustRead/api/admin/deletebook";
var publishers = [];
var categories = [];
var selectedCategories = [];

function addBook(){
    var isbn = $("#inputISBN").val();
    var title = $("#inputTitle").val();
    var author = $("#inputAuthor").val().split(",");
    if((author[author.length-1]).trim() === ""){
        author.pop();
    }
    var publisher = $( "#publisher-select option:selected" ).text();
    var plotInput = $(".input-personal-rewiew").val();
    var numPages = $("#inputNumber").val();
    var language = $("#listlan option:selected").text();
    var year = $("#inputYear").val();
    var cover = $("#cover").val();
    if(isbn !== "" && title !== "" && author !== null && author.length >= 1 && publisher !== null 
            && selectedCategories !== null && selectedCategories.length >= 1){
        //
        $.post(urlnewbook,
            {
                ISBN : isbn,
                title: title,
                "authors[]": author,
                publisher: publisher,
                language: language,
                "categories[]": selectedCategories,
                year: year,
                numpages: numPages,
                plot: plotInput, 
                cover: cover
            }
            ,function(data, status){
            if(data==="true"){
                var snackbar = showSnakBar("Operation successfully complete!");
                $(snackbar).appendTo("article");
                $(snackbar).delay(4500).fadeOut('slow');
                setTimeout(
                    function (){
                        $(snackbar).remove();
                    }, 5000);
            }else{
                var snackbar = showSnakBar("Operation denied!");
                $(snackbar).appendTo("article");
                $(snackbar).delay(4500).fadeOut('slow');
                setTimeout(
                    function (){
                        $(snackbar).remove();
                    }, 5000);
            }
        });
        //
    }
}

function markAsCategory(name){
    if(selectedCategories.indexOf(name) === -1){
        selectedCategories.push(name);
        var category = "#" + name;
        $(category).css("background-color", "#00e676");
    }else{
        selectedCategories = jQuery.grep(selectedCategories, function(value) {
            return value !== name;
        });
        var category = "#" + name;
        $(category).css("background-color", "#dfdfdf");
    }
    
}

/**
 * Prepare the language list.
 * 
 * @param {type} lan
 * @returns {undefined}
 */
function listsLang(lan){
    //alert(lan);
    var languages = ["Italian","English","French","German","Chinese","Japanese","Piedmontese","Swahili","Spanish"];
    var lists = "<option value='" + lan + "'>" + lan + "</option>";
    for(var i=0; i<languages.length; i++){
        //alert(lists);
        if(languages[i] !== lan){
            lists += "<option value='" + languages[i] + "'>" + languages[i] + "</option>";
        }
    }
    $("#listlan").replaceWith("<select id='listlan' name='language'>"+lists+"</select>");
}

function modifyBook(isbn, title, publisher, language, year, cover, npages, plot, cat, authors){
    categories = [];
    $("article").replaceWith("<article id='article' class='article'> " +
                "<div id='div-modify-book'>" +
                 "<h1 class='stats-title'>Modify book</h1><div class='divider'></div>" +
                "<form id='modify-book-form' action='/JustRead/api/admin/modifybook' method='PUT'>" +
                "<p class='input-newbook' modif>ISBN*:<input type='text' id='inputISBN' name='ISBN' pattern='.{13,13}' value='"+isbn+"' readonly required></p>" +
                "<p class='input-newbook' modif>Title*:<input type='text' id='inputTitle' name='title' value='" + title + "' required></p>" +
                "<p class='input-newbook extra-space-box' id='author-paragraph'>Author*: <input type='text' id='inputAuthor' name='authors[]' value='"+ authors + "' required>" +
                "</p>" +
                "<p class='input-newbook extra-space-box'>" +
                "<input type='file' id='cover' name='cover' accept='.jpg, .jpeg, .png'><label id='chooseFile' class='add-button center-btn' for='file'>Choose a cover to upload</label>" +
                "</p>" +
                "<p class='input-newbook'>Language: " +
                "<select id='listlan' name='language'>" +
                "<script>listsLang('"+language+"');</script>"+
                "</select></p>" +
                "<br><p  class='input-newbook blue' id='publisher-paragraph'>Select a publisher*: " +
                    "<select id='publisher-select' name='publisher' >" +
                    "</select>" +
                    "<br><br>or insert a new one: <input class='blue' type='text' id='inputPublisher' name='publisher'>" +
                "</p>" +
                "<p  class='input-newbook' id='category-paragraph'>Select the category* by clicking on " +
                "<br>" + 
                "</p>" +
                 "<p id='plot-paragraph' class='input-newbook extra-space-box'>Insert the plot of the book here: <textarea id='plot-input' name='plot' class='input-personal-rewiew'>" + plot +"</textarea></p>" +
                "<p  class='input-newbook'>Number of pages: <input type='number' id='inputNumber' value='" + npages +"' name='numpages' size='10' min='0'></p>" +
                "<script>setMaxYear();</script>" +
                "<p  class='input-newbook'>Year of publication:<input type='number' id='inputYear' name='year' min=0 value='"+ year + "'></p>" +
                "<input class='add-button' id='add-book' type='button'  value='Modify book' onclick='addBook()'/>" +
                "</form>" +
                "</div>" + "</article>");
            //---
            $("#chooseFile").click(function(){
                chooseFile();
            });
            //--
            $.getJSON(urlnewbook, function (data) {
                $.each(data, function (k, v) {
                    $.each(v, function (k1, v1) {
                        if (k === 0) {
                            publishers.push("<option value='" + v1.name + "'>" + v1.name + "</option>");
                        } else {
                            categories.push("<div id='" + v1.name + "' class='category-tag-container' onclick='markAsCategory(\"" + v1.name +"\")'>" +
                                                "<img class='tag-image' src='data:image/png;base64," + v1.icon + "' style='background-color:#ffc107;'/><h6 class='title-category'>" + v1.name + "</h6>" +
                                            "</div>");
                        }
                    });
                });
                $(publishers.join("")).appendTo("#publisher-select");
                $("#publisher-select").val(publisher);
                $(categories.join("")).appendTo("#category-paragraph");
                var array = cat.split(", ");
                array.pop();
                $.each(array, function(data){
                    markAsCategory(array[data]);
                });
            });
            //
}

function validateAuthors(){
    var pattern = new RegExp(/(((\w{1,90}\s\w{1,90}),\s)|(\w{1,90}\s\w{1,90}))+$/);
    $("#inputAuthor").change(function(){
        var author = $("#inputAuthor").val();
        if(pattern.test(author)){
            $(this).css("border", "2px solid green"); 
        } else {
            $(this).css("border", "2px solid red");
        }
    });
}

/**
 * (Re)built the book table.
 */
function createTableBook() {
        setTimeout(
                function (){
                    // do nothing
                }, 4000);
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'>"+
                "<h1>Books</h1>"+
                "<h3>Our collection of tomes, volumes and magazines accessible to anyone.</h3>"+
                "<table id='books-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>publisher</th>" +
                "<th class='tg-yw4l'>year</th>" +
                //"<th class='tg-yw4l'>cover</th>" +
                //"<th class='tg-yw4l'>pages</th>" +
                "<th class='tg-yw4l'>language</th>" +
                "<th class='tg-yw4l'>authors</th>" +
                "<th class='tg-yw4l'>categories</th>" +
                "<th class='tg-yw4l'>modify</th>" +
                "<th class='tg-yw4l'>delete</th>" +
                "</tr></table></div></article>");
        $.getJSON(urlallbooks, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Books</h1>"+
                "<h3>Can a library bookless exist? So it seems.</h3>"+
                "</div></article>");
                return;
            }
            var authors = [];
            var categoriesAll = [];
            var book = [];
            var isbns = [];
            var titles = [];
            var languages = [];
            var plots = [];
            var publishers = [];
            var npages = [];
            var covers = [];
            var years = [];
            $.each(data, function (k, v) {
                $.each(v, function (k1, v1) {
                    if (k === 0) {
                        isbns[k1] = v[k1].isbn;
                        titles[k1] = v[k1].title;
                        languages[k1] = v[k1].language;
                        plots[k1] = v[k1].plot;
                        publishers[k1] = v[k1].publisher.name;
                        npages[k1] = v[k1].pages;
                        covers[k1] = v[k1].cover;
                        years[k1] = v[k1].yearsOfPublication.value;
                        book[k1] = "<tr>" +
                                "<td>" + v[k1].isbn + "</td>" +
                                "<td>" + v[k1].title + "</td>" +
                                "<td>" + v[k1].publisher.name + "</td>" +
                                "<td>" + v[k1].yearsOfPublication.value + "</td>" +
                                "<td>" + v[k1].language + "</td>";
                    } else if (k === 1) {
                        if (v[k1].length > 0) {
                            authors[k1] = "";
                            $.each(v1, function (k2, v2) {
                                authors[k1] += v2.name + " " + v2.surname + " ";
                            });
                        } else {
                            authors[k1] = "-";
                        }
                        book[k1] += "<td>" + authors[k1] + " ";
                    } else {
                        if (v[k1].length > 0) {
                            categoriesAll[k1] = "";
                            $.each(v1, function (k2, v2) {
                                categoriesAll[k1] += v2.name + " ";
                            });
                        } else {
                            categoriesAll[k1] = "-";
                        }
                        book[k1] += "<td>" + categoriesAll[k1] + "</td><td>" +
                                "<div class='fab-promote' onclick='modifyBook(\"" + isbns[k1] + "\", \"" + titles[k1] + "\", \"" + publishers[k1] + "\", \"" + languages[k1] + "\", \"" + years[k1] + "\", \"" + covers[k1] + "\", \"" + npages[k1] + "\", \"" + plots[k1] + "\", \"" + categoriesAll[k1] + "\", \"" + authors[k1] + "\")'>" +
                                    "<img src='../icons/edit.png' class='fab-promote-icon' alt='modify this book' title='Modify this book'/></div></td>" +
                                "<td><div class='fab-promote' onclick='deleteBook(\"" + isbns[k1] + "\")'>" +
                                    "<img src='../icons/delete.png' class='fab-promote-icon' alt='delete this book' title='Delete this book'/></div></td>" +
                                "</tr>";
                    }
                });
            });
            $(book.join("")).appendTo("#books-table");
        });
    }

/**
 * Delete the selected row from the catalog.
 * 
 * @param {type} isbn
 * @returns {undefined}
 */
function deleteBook(isbn){
    //
    var dialog = showDialog("Are you sure to delete the book?", function () {
            var urldelete = urldeletebook + "/" + isbn;
            $.delete(urldelete, function(data, status){
                if(status === 'success' && data===true){
                    var snackbar = showSnakBar("Operation successfully complete!");
                    $(snackbar).appendTo("article");
                    $(snackbar).delay(3500).fadeOut('slow');
                    setTimeout(
                        function (){
                            $(snackbar).remove();
                            createTableBook();
                        }, 4000);
                        $(dialog).fadeOut(400);
                }else{
                    var snackbar = showSnakBar("Operation denied!");
                    $(snackbar).appendTo("article");
                    $(snackbar).delay(4500).fadeOut('slow');
                    setTimeout(
                        function (){
                            $(snackbar).remove();
                        }, 5000);
                        $(dialog).fadeOut(400);
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
    //
}

/**
 * Change the role of the selected user level.
 * 
 * @param {type} id
 * @param {type} src
 * @returns {undefined}
 */
function changeRole(id, src){
    var val;
    var newsrc;
    if (src === 'http://localhost:8080/JustRead/icons/0.svg'){
        val = 1;
        newsrc = "../icons/1.svg";
    }
    else{
        val = 0;
        newsrc = "../icons/0.svg";
    }
    document.getElementById(id).setAttribute("src", newsrc);
    
    var urlpromote = urlpromoteuser + "/" + id;
    $.put(urlpromote, function(data, status){
        if(status === 'success' && data==='true'){
            var snackbar = showSnakBar("Operation successfully complete!");
            $(snackbar).appendTo("article");
            $(snackbar).delay(4500).fadeOut('slow');
            setTimeout(
                function (){
                    $(snackbar).remove();
                }, 5000);
        }
    });
       
}

/**
 * Return the remaining days to finishing the reading.
 * 
 * @param {type} enddate
 * @returns {Number}
 */
function remainingDays(enddate) {
    if (!enddate instanceof Date) {
        throw {name: 'DateFormatException', message: 'Date format exception!'};
    }
    var currentdate = new Date();
    var oneDay = 24*60*60*1000; // hours*minutes*seconds*milliseconds
    return Math.round(Math.abs((enddate.getTime() - currentdate.getTime())/(oneDay)));
}

// var for the circle progress bar
var progressBarOptions = {
	startAngle: -1.55,
	size: 250,
        value: 0.75,
        //fill: {color: '#ff0000'}
        fill: {
            gradient: ["#00e676", "#009b50"]
        }
};

/**
 * Set the percentage of the progress and creates it.
 * 
 * @param {type} a
 * @returns {undefined}
 */
function setRound(a) {
    for (var k = 0; k < a.length; k++) {
        $(".circle").circleProgress(progressBarOptions).on('circle-animation-progress', function(event, progress, stepValue) {});
        $("#circle-"+k).circleProgress({
                startAngle: progressBarOptions.startAngle,
                value : a[k],
                size: progressBarOptions.size,
                fill: progressBarOptions.fill
        });
    }
}

$("document").ready(function () {
    
    window.history.pushState("JustRead/app/admin", "Title", "admin");
    author_field = 1;
    category_select = 1;
    
    $.getJSON("/JustRead/api/admin", function (data) {
        var stats = [];//<html>
        var value = [];//%
        var i = 0;//#stat
        $("article").append("<div id='main-div-stats'><h1 class='stats-title'>Stats</h1><div class='divider'></div></div>");
        $.each(data, function (key, val) {
            stats.push("<div class='main-div-stat float-stat" + i + "'>"+
                        "<div class='div-stats circle" + i + "' id='circle-" + i + "'>"+
                            "<strong></strong>"+
                             "<p id='twodec' class='p-stat'>" + parseFloat(val.value).toFixed(2) + " %</p>"+
                                "<p class='p-stat'>" + val.description + "</p>"+
                        "</div>" +
                        "</div>");
            value[i] = val.value/100;
            i++;
        });
        $(stats.join("")).appendTo("#main-div-stats");
        setRound(value);
    });

    $("#new-book-link").click(function () {
        categories = [];
        publishers = [];
        $("article").replaceWith("<article id='article' class='article'> "+
                "<div id='div-new-book'>" +
                "<h1 class='stats-title'>Insert new book</h1><div class='divider'></div>" +
                "<form id='new-book-form' action='/JustRead/api/admin/newbook' method='POST'>" +
                "<p class='input-newbook'>ISBN*:<input type='text' id='inputISBN' name='ISBN' pattern='.{13,13}' required></p>" +
                "<p class='input-newbook'>Title*:<input type='text' id='inputTitle' name='title' required></p>" +
                "<p  class='input-newbook extra-space-box' id='author-paragraph'>Insert author or authors of the book separated by comma: <input type='text' id='inputAuthor' placeholder='John Green, Luigi Pirandello, ...' name='authors[]' required>" +
                "</p>" +
                "<p class='input-newbook extra-space-box'>" +
                "<input type='file' id='cover' name='cover' accept='.jpg, .jpeg, .png'><label class='add-button center-btn' for='file'>Choose a cover to upload</label>" +
                "</p>" +
                "<p  class='input-newbook'>Language: " +
                "<select id='language' name='language'>" +
                "<option value='Italian'>Italian</option>" +
                "<option value='English'>English</option>" +
                "<option value='French'>French</option>" +
                "<option value='German'>German</option>" +
                "<option value='Chinese'>Chinese</option>" +
                "<option value='Japanese'>Japanese</option>" +
                "<option value='Piedmontese'>Piedmontese</option>" +
                "<option value='Swahili'>Swahili</option>" +
                "<option value='Spanish'>Spanish</option>" +
                "</select>" +
                "</p>" +
                "<br><p  class='input-newbook blue' id='publisher-paragraph'>Select a publisher*: " +
                    "<select id='publisher-select' name='publisher'>" +
                    "</select>" +
                    "<br><br>or insert a new one: <input class='blue' type='text' id='inputPublisher' name='publisher'>" +
                "</p>" +
                "<p  class='input-newbook' id='category-paragraph'>Select the category* by clicking on " +
                "<br>" + 
                "</p>" +
                "<p id='plot-paragraph' class='input-newbook extra-space-box'>Insert the plot of the book here: <textarea id='plot-input' name='plot' class='input-personal-rewiew'></textarea></p>" +
                "<p  class='input-newbook'>Number of pages: <input type='number' id='inputNumber' name='numpages' size='10' min='0'></p>" +
                "<script>setMaxYear();</script>" +
                "<p  class='input-newbook'>Year of publication:<input type='number' id='inputYear' name='year' min=0></p>" +
                "<input class='add-button' id='add-book' type='button'  value='Add to the library' onclick='addBook()'/>" +
                "</form>" +
                "</div>" + "</article>");
        $.getJSON(urlnewbook, function (data) {
            $.each(data, function (k, v) {
                $.each(v, function (k1, v1) {
                    if (k === 0) {
                        publishers.push("<option value='" + v1.name + "'>" + v1.name + "</option>");
                    } else {
                        categories.push("<div id='" + v1.name + "' class='category-tag-container' onclick='markAsCategory(\"" + v1.name +"\")'>" +
                                            "<img class='tag-image' src='data:image/png;base64," + v1.icon + "' style='background-color:#ffc107;'/><h6 class='title-category'>" + v1.name + "</h6>" +
                                        "</div>");
                    }
                });
            });
            $(publishers.join("")).appendTo("#publisher-select");
            $(categories.join("")).appendTo("#category-paragraph");
        });
        //
    });

    function searchArchivedBorrowing(){
        var urlSearchArchived = "/JustRead/api/search/archived?search=" + $("#search-input-archived").val();
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'>"+
                "<h1 class='stats-title'>Archived borrowing</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>The table contains the history of the successful loans.</h3>"+
                "<table id='archive-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>user</th>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>start date</th>" +
                "<th class='tg-yw4l'>end date</th>" +
                "<th class='tg-yw4l'>#renewals</th>" +
                "</tr></table></div></article>");
        $.getJSON(urlSearchArchived, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Archive borrowing</h1>"+
                "<h3>No matches found...</h3>"+
                "</div></article>");
                return;
            }
            var archivedborrowing = [];
            $.each(data, function (key, val) {
                archivedborrowing.push("<tr>" +
                        "<td><a href='mailto:" + val.user.email +"' title='Click to send email to user'>" + val.user.name + " " + val.user.surname + "</a></td>" +
                        "<td>" + val.book.isbn + "</td>" +
                        "<td>" + val.book.title + "</td>" +
                        "<td>" + val.startDate + "</td>" +
                        "<td>" + val.endDate + "</td>" +
                        "<td>" + val.renewal + "</td>" +
                        "</tr>");
            });
            $(archivedborrowing.join("")).appendTo("#archive-table");
        });
    }
    $("#archive-link").click(function () {
        
        $('#search-input-expired').css("display", "none");
        $('#search-icon-expired').css("display", "none");
        $('#search-input-archived').css("display", "");
        $('#search-icon-archived').css("display", "");
        $('#search-input-user').css("display", "none");
        $('#search-icon-user').css("display", "none");
        $('#search-input-books').css("display", "none");
        $('#search-icon-books').css("display", "none");
        $('#search-input-active').css("display", "none");
        $('#search-icon-active').css("display", "none");
        
        $('#search-icon-archived').click(function(){
            if($(this).val() !== ''){
                searchArchivedBorrowing();
                $(this).val('');
            }
        });
        $('#search-input-archived').bind("enterKey",function(e){
            if($(this).val() !== ''){
                searchArchivedBorrowing();
                $(this).val('');
            }
        });
        $('#search-input-archived').keyup(function(e){
            if(e.keyCode === 13){
                $(this).trigger("enterKey");
            }
        });
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'>"+
                "<h1 class='stats-title'>Archived borrowing</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>The table contains the history of the successful loans.</h3>"+
                "<table id='archive-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>user</th>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>start date</th>" +
                "<th class='tg-yw4l'>end date</th>" +
                "<th class='tg-yw4l'>#renewals</th>" +
                "</tr></table></div></article>");
        $.getJSON(urlarchiveborrowing, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Archive borrowing</h1>"+
                "<h3>If you can read this the library isn't working...</h3>"+
                "</div></article>");
                return;
            }
            var archivedborrowing = [];
            $.each(data, function (key, val) {
                archivedborrowing.push("<tr>" +
                        "<td><a href='mailto:" + val.user.email +"' title='Click to send email to user'>" + val.user.name + " " + val.user.surname + "</a></td>" +
                        "<td>" + val.book.isbn + "</td>" +
                        "<td>" + val.book.title + "</td>" +
                        "<td>" + val.startDate + "</td>" +
                        "<td>" + val.endDate + "</td>" +
                        "<td>" + val.renewal + "</td>" +
                        "</tr>");
            });
            $(archivedborrowing.join("")).appendTo("#archive-table");
        });
    });

    function searchActiveBorrowing (){
        //-----------------------------------------------------------------------
        var urlSearchActive = "/JustRead/api/search/active?search=" + $("#search-input-active").val();
        $.getJSON(urlSearchActive, function (data) {
            var activeborrowing = [];
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Active borrowing</h1>"+
                "<h3>No matches found...</h3>"+
                "</div></article>");
                return;
            }
            $.each(data, function (key, val) {
                try {
                    activeborrowing.push("<tr>" +
                            "<td><a href='mailto:" + val.user.email +"' title='Click to send email to user'>" + val.user.name + " " + val.user.surname + "</a></td>" +
                            "<td>" + val.book.isbn + "</td>" +
                            "<td>" + val.book.title + "</td>" +
                            "<td>" + val.startDate + "</td>" +
                            "<td>" + val.endDate + "</td>" +
                            "<td>" + remainingDays(new Date(val.endDate)) + "</td>" +
                            "<td>" + val.renewal + "</td>" +
                            "</tr>");
                } catch (e) {
                    alert("errore");
                    return e.name + e.message;
                }
            });
            $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1 class='stats-title'>Active borrowing</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>The table contains all current loans.</h3>"+
                "<table id='active-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>user</th>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>start date</th>" +
                "<th class='tg-yw4l'>end date</th>" +
                "<th class='tg-yw4l'>remaining days</th>" +
                "<th class='tg-yw4l'>#renewals</th>" +
                "</tr></table></div></article>");
            $(activeborrowing.join("")).appendTo("#active-table");
        });
        //-----------------------------------------------------------------------
    }
    $("#active-link").click(function () {
        window.history.pushState("JustRead/admin/", "Title", "active");
        
        $('#search-input-expired').css("display", "none");
        $('#search-icon-expired').css("display", "none");
        $('#search-input-archived').css("display", "none");
        $('#search-icon-archived').css("display", "none");
        $('#search-input-user').css("display", "none");
        $('#search-icon-user').css("display", "none");
        $('#search-input-books').css("display", "none");
        $('#search-icon-books').css("display", "none");
        $('#search-input-active').css("display", "");
        $('#search-icon-active').css("display", "");
        
        $("#search-icon-active").click(function(){
            if($(this).val() !== ''){
                searchActiveBorrowing();
                $(this).val('');
            }
        });
        $('#search-input-active').bind("enterKey",function(e){
            if($(this).val() !== ''){
                searchActiveBorrowing();
                $(this).val('');
            }
        });
        $('#search-input-active').keyup(function(e){
            if(e.keyCode === 13){
                $(this).trigger("enterKey");
            }
        });
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1 class='stats-title'>Active borrowing</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>The table contains all current loans.</h3>"+
                "<table id='active-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>user</th>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>start date</th>" +
                "<th class='tg-yw4l'>end date</th>" +
                "<th class='tg-yw4l'>remaining days</th>" +
                "<th class='tg-yw4l'>#renewals</th>" +
                "</tr></table></div></article>");
        $.getJSON(urlactiveborrowing, function (data) {
            var activeborrowing = [];
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Active borrowing</h1>"+
                "<h3>Unfortunatly, no one is reading a book.</h3>"+
                "</div></article>");
                return;
            }
            $.each(data, function (key, val) {
                try {
                    activeborrowing.push("<tr>" +
                            "<td><a href='mailto:" + val.user.email +"' title='Click to send email to user'>" + val.user.name + " " + val.user.surname + "</a></td>" +
                            "<td>" + val.book.isbn + "</td>" +
                            "<td>" + val.book.title + "</td>" +
                            "<td>" + val.startDate + "</td>" +
                            "<td>" + val.endDate + "</td>" +
                            "<td>" + remainingDays(new Date(val.endDate)) + "</td>" +
                            "<td>" + val.renewal + "</td>" +
                            "</tr>");
                } catch (e) {
                    alert("errore");
                    return e.name + e.message;
                }
            });
            $(activeborrowing.join("")).appendTo("#active-table");
        });
    });

    function searchExpiredBorrowing (){
        //-----------------------------------------------------------------------
        var urlSearchExpired = "/JustRead/api/search/expired?search=" + $("#search-input-expired").val();
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1 class='stats-title'>Expired borrowing</h1><div class='divider'></div>"+
                "<h3>The table lists the undelivered books. To contact the reader by email click on his name in the first column.</h3>"+
                "<table id='expired-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>user</th>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>start date</th>" +
                "<th class='tg-yw4l'>end date</th>" +
                "<th class='tg-yw4l'>surplus days</th>" +
                "<th class='tg-yw4l'>#renewals</th>" +
                "</tr></table></div></article>");
        $.getJSON(urlSearchExpired, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1 class='stats-title'>Expired borrowing</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>No matches found...</h3>"+
                "</div></article>");
                return;
            }
            var expiredborrowing = [];
            $.each(data, function (key, val) {
                expiredborrowing.push("<tr>" +
                        "<td><a href='mailto:" + val.user.email + "?subject=EXPIRED LOAN!&body=Dear " + val.user.name + ", your loan has expired, please return the book as soon as possible! The direction' "+
                        "title='Click to contact via email'>" + val.user.name + " " + val.user.surname + "</a></td>" +
                        "<td>" + val.book.isbn + "</td>" +
                        "<td>" + val.book.title + "</td>" +
                        "<td>" + val.startDate + "</td>" +
                        "<td>" + val.endDate + "</td>" +
                        "<td>" + remainingDays(new Date(val.endDate)) + "</td>" +
                        "<td>" + val.renewal + "</td>" +
                        "</tr>");
            });
            $(expiredborrowing.join("")).appendTo("#expired-table");
        });
        //-----------------------------------------------------------------------
    }
    $("#expired-link").click(function () {
        window.history.pushState("JustRead/app/admin/", "Title", "expired");
        
        $('#search-input-expired').css("display", "");
        $('#search-icon-expired').css("display", "");
        $('#search-input-archived').css("display", "none");
        $('#search-icon-archived').css("display", "none");
        $('#search-input-user').css("display", "none");
        $('#search-icon-user').css("display", "none");
        $('#search-input-books').css("display", "none");
        $('#search-icon-books').css("display", "none");
        $('#search-input-active').css("display", "none");
        $('#search-icon-active').css("display", "none");
        
        $('#search-icon-expired').click(function(){
            if($(this).val() !== ''){
                searchExpiredBorrowing();
                $(this).val('');
            }
        });
         $('#search-input-expired').bind("enterKey",function(e){
            if($(this).val() !== ''){
                searchExpiredBorrowing();
                $(this).val('');
            }
        });
         $('#search-input-expired').keyup(function(e){
            if(e.keyCode === 13){
                $(this).trigger("enterKey");
            }
        });
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1 class='stats-title'>Expired borrowing</h1><div class='divider'></div>"+
                "<h3>The table lists the undelivered books. To contact the reader by email click on his name in the first column.</h3>"+
                "<table id='expired-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>user</th>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>start date</th>" +
                "<th class='tg-yw4l'>end date</th>" +
                "<th class='tg-yw4l'>surplus days</th>" +
                "<th class='tg-yw4l'>#renewals</th>" +
                "</tr></table></div></article>");
        $.getJSON(urlexpiredborrowing, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1 class='stats-title'>Expired borrowing</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>They're all good guys.</h3>"+
                "</div></article>");
                return;
            }
            var expiredborrowing = [];
            $.each(data, function (key, val) {
                expiredborrowing.push("<tr>" +
                        "<td><a href='mailto:" + val.user.email + "?subject=EXPIRED LOAN!&body=Dear " + val.user.name + ", your loan has expired, please return the book as soon as possible! The direction' "+
                        "title='Click to contact via email'>" + val.user.name + " " + val.user.surname + "</a></td>" +
                        "<td>" + val.book.isbn + "</td>" +
                        "<td>" + val.book.title + "</td>" +
                        "<td>" + val.startDate + "</td>" +
                        "<td>" + val.endDate + "</td>" +
                        "<td>" + remainingDays(new Date(val.endDate)) + "</td>" +
                        "<td>" + val.renewal + "</td>" +
                        "</tr>");
            });
            $(expiredborrowing.join("")).appendTo("#expired-table");
        });
    });

    function searchUser(){
        var searchUSer = "/JustRead/api/search/user?search=" + $("#search-input-user").val();
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'>"+
                "<h1 class='stats-title'>Users</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>List of registered users. Click an empty star shape to promote a user.</h3>"+
                "<table id='users-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>email</th>" +
                "<th class='tg-yw4l'>name</th>" +
                "<th class='tg-yw4l'>surname</th>" +
                "<th class='tg-yw4l'>address</th>" +
                "<th class='tg-yw4l'>role</th>" +
                "</tr></table></div></article>");
        $.getJSON(searchUSer, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Users</h1>"+
                "<h3>If you aren't a cracker you'll never see this message.</h3>"+
                "</div></article>");
                return;
            }
            var users = [];
            $.each(data, function (key, val) {
                var address = val.address;
                if (address === null || address === 'null')
                    address = 'unavailable';
                users.push("<tr>" +
                        "<td><a href='mailto:" + val.email + "'>" + val.email + "</td>" +
                        "<td><a href='mailto:" + val.email + "'>" + val.name + "</a></td>" +
                        "<td><a href='mailto:" + val.email + "'>" + val.surname + "</td>" +
                        "<td><a href='mailto:" + val.email + "'>" + address + "</td>" +
                        "<td><div class='fab-promote'><img class='fab-promote-icon' id='" + val.email + "' src='../icons/" + val.level + ".png' alt='Promote/declass user' title='Promote/declass user' width='20' height='20' onclick='changeRole(this.id, this.src)'></div></td>" +
                        "</tr>");
            });
            $(users.join("")).appendTo("#users-table");
        });
    }
    $("#all-users-link").click(function () {
        
        $('#search-input-expired').css("display", "none");
        $('#search-icon-expired').css("display", "none");
        $('#search-input-archived').css("display", "none");
        $('#search-icon-archived').css("display", "none");
        $('#search-input-user').css("display", "");
        $('#search-icon-user').css("display", "");
        $('#search-input-books').css("display", "none");
        $('#search-icon-books').css("display", "none");
        $('#search-input-active').css("display", "none");
        $('#search-icon-active').css("display", "none");
        
        $('#search-icon-user').click(function(){
            if($(this).val() !== ''){
                searchUser();
                $(this).val('');
            }
        });
         $('#search-input-user').bind("enterKey",function(e){
            if($(this).val() !== ''){
                searchUser();
                $(this).val('');
            }
        });
         $('#search-input-user').keyup(function(e){
            if(e.keyCode === 13){
                $(this).trigger("enterKey");
            }
        });
        
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'>"+
                "<h1 class='stats-title'>Users</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>List of registered users. Click an empty star shape to promote a user.</h3>"+
                "<table id='users-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>email</th>" +
                "<th class='tg-yw4l'>name</th>" +
                "<th class='tg-yw4l'>surname</th>" +
                "<th class='tg-yw4l'>address</th>" +
                "<th class='tg-yw4l'>role</th>" +
                "</tr></table></div></article>");
        $.getJSON(urlallusers, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Users</h1>"+
                "<h3>If you aren't a cracker you'll never see this message.</h3>"+
                "</div></article>");
                return;
            }
            var users = [];
            $.each(data, function (key, val) {
                var address = val.address;
                if (address === null || address === 'null')
                    address = 'unavailable';
                users.push("<tr>" +
                        "<td><a href='mailto:" + val.email + "'>" + val.email + "</td>" +
                        "<td><a href='mailto:" + val.email + "'>" + val.name + "</a></td>" +
                        "<td><a href='mailto:" + val.email + "'>" + val.surname + "</td>" +
                        "<td><a href='mailto:" + val.email + "'>" + address + "</td>" +
                        "<td><div class='fab-promote'><img class='fab-promote-icon' id='" + val.email + "' src='../icons/" + val.level + ".png' alt='Promote/declass user' title='Promote/declass user' width='20' height='20' onclick='changeRole(this.id, this.src)'></div></td>" +
                        "</tr>");
            });
            $(users.join("")).appendTo("#users-table");
        });
    });

    function searchBook(){
        var searchBooks = "/JustRead/api/search/book?search=" + $("#search-input-books").val();
        console.log(searchBooks);
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'>"+
                "<h1 class='stats-title'>Books</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>Our collection of tomes, volumes and magazines accessible to anyone.</h3>"+
                "<table id='books-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>publisher</th>" +
                "<th class='tg-yw4l'>year</th>" +
                "<th class='tg-yw4l'>language</th>" +
                "<th class='tg-yw4l'>authors</th>" +
                "<th class='tg-yw4l'>categories</th>" +
                "<th class='tg-yw4l'>modify</th>" +
                "<th class='tg-yw4l'>delete</th>" +
                "</tr></table></div></article>");
        $.getJSON(searchBooks, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Books</h1>"+
                "<h3>No found matches</h3>"+
                "</div></article>");
                return;
            }
            var authors = [];
            var categories1 = [];
            var book = [];
            var isbns = [];
            var titles = [];
            var languages = [];
            var plots = [];
            var publishers = [];
            var npages = [];
            var covers = [];
            var years = [];
            $.each(data, function (k, v) {
                $.each(v, function (k1, v1) {
                    if (k === 0) {
                        isbns[k1] = v[k1].isbn;
                        titles[k1] = v[k1].title;
                        languages[k1] = v[k1].language;
                        plots[k1] = v[k1].plot;
                        publishers[k1] = v[k1].publisher.name;
                        npages[k1] = v[k1].pages;
                        covers[k1] = v[k1].cover;
                        years[k1] = v[k1].yearsOfPublication.value;
                        book[k1] = "<tr>" +
                                "<td>" + v[k1].isbn + "</td>" +
                                "<td>" + v[k1].title + "</td>" +
                                "<td>" + v[k1].publisher.name + "</td>" +
                                "<td>" + v[k1].yearsOfPublication.value + "</td>" +
                                "<td>" + v[k1].language + "</td>";
                    } else if (k === 1) {
                        if (v[k1].length > 0) {
                            authors[k1] = "";
                            $.each(v1, function (k2, v2) {
                                authors[k1] += v2.name + " " + v2.surname + ", ";
                            });
                        } else {
                            authors[k1] = "-";
                        }
                        book[k1] += "<td>" + authors[k1] + "</td>";
                    } else {
                        if (v[k1].length > 0) {
                            categories1[k1] = "";
                            $.each(v1, function (k2, v2) {
                                categories1[k1] += v2.name + ", ";
                            });
                        } else {
                            categories1[k1] = "-";
                        }
                        book[k1] += "<td>" + categories1[k1] + "</td><td>" +
                                "<div class='fab-promote' onclick='modifyBook(\"" + isbns[k1] + "\", \"" + titles[k1] + "\", \"" + publishers[k1] + "\", \"" + languages[k1] + "\", \"" + years[k1] + "\", \"" + covers[k1] + "\", \"" + npages[k1] + "\", \"" + plots[k1] + "\", \"" + categories1[k1] + "\", \"" + authors[k1] + "\")'>" +
                                    "<img src='../icons/edit.png' class='fab-promote-icon edit-icon' alt='modify this book' title='Modify this book'/></div></td>" +
                                "<td><div class='fab-promote' onclick='deleteBook(\"" + isbns[k1] + "\")'>" +
                                    "<img src='../icons/delete.png' class='fab-promote-icon delete-icon' alt='delete this book' title='Delete this book'/></div></td>" +
                                "</tr>";
                    }
                });
            });
            $(book.join("")).appendTo("#books-table");
            $(".delete-icon").mouseenter(function(){
                $(this).attr("src", "../icons/delete-empty.png");
            });
            $(".delete-icon").mouseleave(function(){
                $(this).attr("src", "../icons/delete.png");
            });
        });
    }
    $("#all-books-link").click(function (){
        
        $('#search-input-expired').css("display", "none");
        $('#search-icon-expired').css("display", "none");
        $('#search-input-archived').css("display", "none");
        $('#search-icon-archived').css("display", "none");
        $('#search-input-user').css("display", "none");
        $('#search-icon-user').css("display", "none");
        $('#search-input-books').css("display", "");
        $('#search-icon-books').css("display", "");
        $('#search-input-active').css("display", "none");
        $('#search-icon-active').css("display", "none");
        
        $('#search-icon-books').click(function(){
            if($(this).val() !== ''){
                searchBook();
                $(this).val('');
            }
        });
         $('#search-input-books').bind("enterKey",function(e){
            if($(this).val() !== ''){
                searchBook();
                $(this).val('');
            }
        });
         $('#search-input-books').keyup(function(e){
            if(e.keyCode === 13){
                $(this).trigger("enterKey");
            }
        });
        
        $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'>"+
                "<h1 class='stats-title'>Books</h1><div class='divider'></div>"+
                "<h3 class='subtitle'>Our collection of tomes, volumes and magazines accessible to anyone.</h3>"+
                "<table id='books-table' class='tg unselectable'><tr>" +
                "<th class='tg-yw4l'>ISBN</th>" +
                "<th class='tg-yw4l'>title</th>" +
                "<th class='tg-yw4l'>publisher</th>" +
                "<th class='tg-yw4l'>year</th>" +
                "<th class='tg-yw4l'>language</th>" +
                "<th class='tg-yw4l'>authors</th>" +
                "<th class='tg-yw4l'>categories</th>" +
                "<th class='tg-yw4l'>modify</th>" +
                "<th class='tg-yw4l'>delete</th>" +
                "</tr></table></div></article>");
        $.getJSON(urlallbooks, function (data) {
            if(data.length < 1){
                $("article").replaceWith("<article id='article' class='article'><div id='main-div-admin'><h1>Books</h1>"+
                "<h3>Can a library bookless exist? So it seems.</h3>"+
                "</div></article>");
                return;
            }
            var authors = [];
            var categories1 = [];
            var book = [];
            var isbns = [];
            var titles = [];
            var languages = [];
            var plots = [];
            var publishers = [];
            var npages = [];
            var covers = [];
            var years = [];
            $.each(data, function (k, v) {
                $.each(v, function (k1, v1) {
                    if (k === 0) {
                        isbns[k1] = v[k1].isbn;
                        titles[k1] = v[k1].title;
                        languages[k1] = v[k1].language;
                        plots[k1] = v[k1].plot;
                        publishers[k1] = v[k1].publisher.name;
                        npages[k1] = v[k1].pages;
                        covers[k1] = v[k1].cover;
                        years[k1] = v[k1].yearsOfPublication.value;
                        book[k1] = "<tr>" +
                                "<td>" + v[k1].isbn + "</td>" +
                                "<td>" + v[k1].title + "</td>" +
                                "<td>" + v[k1].publisher.name + "</td>" +
                                "<td>" + v[k1].yearsOfPublication.value + "</td>" +
                                "<td>" + v[k1].language + "</td>";
                    } else if (k === 1) {
                        if (v[k1].length > 0) {
                            authors[k1] = "";
                            $.each(v1, function (k2, v2) {
                                authors[k1] += v2.name + " " + v2.surname + ", ";
                            });
                        } else {
                            authors[k1] = "-";
                        }
                        book[k1] += "<td>" + authors[k1] + "</td>";
                    } else {
                        if (v[k1].length > 0) {
                            categories1[k1] = "";
                            $.each(v1, function (k2, v2) {
                                categories1[k1] += v2.name + ", ";
                            });
                        } else {
                            categories1[k1] = "-";
                        }
                        book[k1] += "<td>" + categories1[k1] + "</td><td>" +
                                "<div class='fab-promote' onclick='modifyBook(\"" + isbns[k1] + "\", \"" + titles[k1] + "\", \"" + publishers[k1] + "\", \"" + languages[k1] + "\", \"" + years[k1] + "\", \"" + covers[k1] + "\", \"" + npages[k1] + "\", \"" + plots[k1] + "\", \"" + categories1[k1] + "\", \"" + authors[k1] + "\")'>" +
                                    "<img src='../icons/edit.png' class='fab-promote-icon edit-icon' alt='modify this book' title='Modify this book'/></div></td>" +
                                "<td><div class='fab-promote' onclick='deleteBook(\"" + isbns[k1] + "\")'>" +
                                    "<img src='../icons/delete.png' class='fab-promote-icon delete-icon' alt='delete this book' title='Delete this book'/></div></td>" +
                                "</tr>";
                    }
                });
            });
            $(book.join("")).appendTo("#books-table");
            $(".delete-icon").mouseenter(function(){
                $(this).attr("src", "../icons/delete-empty.png");
            });
            $(".delete-icon").mouseleave(function(){
                $(this).attr("src", "../icons/delete.png");
            });
        });
        
    });

});