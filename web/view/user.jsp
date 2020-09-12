<%-- 
    Document   : index
    Created on : 30-dic-2017, 15.31.03
    Author     : Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="controller.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title title="title of website">JUST READ</title>
        <link rel="shortcut icon" href="../icons/justread.png" />
        <link rel="stylesheet" href="../styles/main-style.css">
        <link rel="stylesheet" href="../styles/actionbar-style.css">
        <link rel="stylesheet" href="../styles/navigation-drawer-style.css">
        <link rel="stylesheet" href="../styles/category-catalog-style.css">
        <link rel="stylesheet" href="../styles/borrowing-item-style.css">
        <link rel="stylesheet" href="../styles/history-item-style.css">
        <link rel="stylesheet" href="../styles/dialog-style.css">
        <link rel="stylesheet" href="../styles/article-main-style.css">
        <link rel="stylesheet" href="../styles/bookinfo-style.css">
        <link rel="stylesheet" href="../styles/contacts-style.css">
        <link rel="stylesheet" type="text/css" href="../styles/footer.css" media="all"/>
        <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCPEK8nxIK9So7KK_ohfe2qLXmI9_3iN_4"
        async defer></script>
        <script type="text/javascript" src="../scripts/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="../scripts/utils.js"></script>
        <script type="text/javascript" src="../scripts/article-get-json.js"></script>
        <script type="text/javascript" src="../scripts/navigation-drawer.js"></script>

    </head>
    <body>
        <%
            String name = (String) request.getAttribute("user-name");
            String level = (String) request.getAttribute("user-level");
            if (name == null) {
        %>
        <!-- header -->
        <%@include file="/template/actionbar-non-logged.html"%>
        <!-- nav -->
        <%@include file="/template/navigation-drawer-non-logged.html"%>
        <%
        } else if (level.equals("admin")) {
        %>
        <%@include file="/template/actionbar-admin-logged.html"%>
        <!-- nav -->
        <%@include file="/template/navigation-drawer-logged.html"%>
        <%
        } else {
        %>
        <!-- header -->
        <%@include file="/template/actionbar-logged.html"%>
        <!-- nav -->
        <%@include file="/template/navigation-drawer-logged.html"%>
        <%
            }
        %>
        <article id="article" class="article">
        </article>
        <!-- footer -->
        <%@include file="/template/footer.html"%>
    </body>
</html>
