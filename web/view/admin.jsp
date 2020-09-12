<%-- 
    Document   : admin
    Created on : 14-gen-2018, 17.48.47
    Author     : Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title title="title of website">JUST READ | Admin</title>
        <!-- css -->
        <link rel="shortcut icon" href="../icons/justread.png" />
        <link rel="stylesheet" href="../styles/main-style.css">
        <link rel="stylesheet" href="../styles/actionbar-style.css">
        <link rel="stylesheet" href="../styles/navigation-drawer-style.css">
        <link rel="stylesheet" href="../styles/category-catalog-style.css">
        <link rel="stylesheet" href="../styles/borrowing-item-style.css">
        <link rel="stylesheet" href="../styles/history-item-style.css">
        <link rel="stylesheet" href="../styles/article-main-style.css">
        <link rel="stylesheet" href="../styles/admin.css">
        <link rel="stylesheet" href="../styles/dialog-style.css">
        <link rel="stylesheet" href="../styles/bookinfo-style.css">
        <link rel="stylesheet" type="text/css" href="../styles/footer.css" media="all"/>
        <!-- js -->
        <script type="text/javascript" src="../scripts/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="../scripts/utils.js"></script>
        <script type="text/javascript" src="../scripts/article-get-json-admin.js"></script>
        <script src='https://cdnjs.cloudflare.com/ajax/libs/jquery-circle-progress/1.1.3/circle-progress.min.js'></script>
    </head>
    <body>
        <!-- header -->
        <%@include file="/template/actionbar-admin.html"%>
        <!-- nav -->
        <%@include file="/template/navigation-drawer-admin.html"%>
        <!-- article -->
        <article id="article" class="article">
            <jsp:include page="/api/admin/active" />
        </article>
        <!-- footer -->
        <%@include file="/template/footer.html"%>
    </body>
</html>
