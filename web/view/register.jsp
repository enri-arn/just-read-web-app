<%-- 
    Document   : signup
    Created on : 3-gen-2018, 14.48.59
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JustRead | Register</title>
        <link rel="shortcut icon" href="icons/justread.png" />
        <link rel="stylesheet" href="../styles/register-style.css">
        <script type="text/javascript" src="../scripts/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="../scripts/authentication-scripts.js"></script>
    </head>
    <body>
        <div class="register-box">
            <h1 class="center-box-title">Register</h1>
            <form action="<%=request.getContextPath()%>/api/register" method="post">
                <div class="group">
                    <input id="email-input-reg" class="input_login" type="email" name="email" autocomplete="on" required autofocus/>
                    <span class="highlight"></span>
                    <label>Insert email here</label>
                </div>
                <div class="group">  
                    <input id="password-input-reg1" class="input_login" type="password" name="password" autocomplete="on" required/>
                    <span class="highlight"></span>
                    <label>Insert password here <span class="little-text">*[verify the roule below]</span></label>
                </div>
                <div class="group">  
                    <input id="password-input-reg2" class="input_login" type="password" name="password" autocomplete="on" required/>
                    <span class="highlight"></span>
                    <label>Reinsert password here</label>
                </div>
                <div class="group">  
                    <input id="name-input-reg" class="input_login" type="text" name="name" autocomplete="on" required/>
                    <span class="highlight"></span>
                    <label>Insert name here</label>
                </div>
                <div class="group">  
                    <input id="surname-input-reg" class="input_login" type="text" name="surname" autocomplete="on" required/>
                    <span class="highlight"></span>
                    <label>Insert surname here</label>
                </div>
                <p><input id="register-btn" class="input_login_btn" type="submit" name="action" value="register"/></p>
            </form>
            <p class="center-text">
                or
                <br>
                <br>
                Return to <a class="home-link" id="home-link-btn" class="home-link" href="<%=request.getContextPath()%>/app/"><b>HomePage</b></a>
            </p>
        </div>
        <div class="info-pwd-box">
            <p class="pwd-title">
                [*] Password must contain:
            </p>
            <ul>
                <li>minimun 8 characters</li>
                <li>at least one uppercase letter</li>
                <li>at least one lowercase letter</li>
                <li>at least one number</li>
                <li>at least one special characters</li>
            </ul>
        </div>
    </body>
</html>
