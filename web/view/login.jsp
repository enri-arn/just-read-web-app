<%-- 
    Document   : signin
    Created on : 3-gen-2018, 14.27.29
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JustRead | Login</title>
        <link rel="shortcut icon" href="icons/justread.png" />
        <link rel="stylesheet" href="../styles/login-style.css">
        <link rel="stylesheet" href="../styles/dialog-style.css">
        <script type="text/javascript" src="../scripts/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="../scripts/utils.js"></script>
        <script type="text/javascript" src="../scripts/authentication-scripts.js"></script>
    </head>
    <body>
        <div class="log-in-box">
            <h1 class="center-box-title">Login</h1>
            <form action="<%=request.getContextPath()%>/api/login" method="post">
                <div class="group">
                    <input id="email-input" class="input_login" type="email" name="email" required autofocus/>
                    <span class="highlight"></span>
                    <label>Insert email here</label>
                </div>
                <div class="group">  
                    <input id="password-input" class="input_login" type="password" name="password" required/>
                    <span class="highlight"></span>
                    <label>Insert password here</label>
                </div>
                <p><input id="login-btn" class="input_login_btn" type="submit" name="action" value="login"/></p>    
            </form>
            <p class="create-new">
                Don't have an account yet? 
                <a class="register-link" class="ext-link" href="<%=request.getContextPath()%>/app/register"><b>Register</b></a>
            </p>
            <p class="center-text">
                or
                <br>
                <br>
                Return to <a class="home-link" id="home-link-btn" class="home-link" href="<%=request.getContextPath()%>/app/"><b>HomePage</b></a>
            </p>
        </div>
    </body>
</html>
