<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Aleks
  Date: 03.08.2017
  Time: 16:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Log In</title>
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
</head>
<body>

<div class="container">
<div class="row">
    <div class="col-md-4 col-md-offset-4">
        <div class="panel panel-primary login-panel">
            <div class="panel-heading">Login</div>
            <div class="panel-body">
            <form action="<c:url value="/login"/>" method="post">
                <div class="form-group">
                    <label for="userNameField">Username</label>
                    <input id="userNameField" type="text" name="username">
                </div>
                <div class="form-group">
                    <label for="passwordField">Password</label>
                    <input id="passwordField" type="password" name="password">
                </div>
                <button type="submit" class="btn btn-default">Log In</button>
            </form>
            </div>
        </div>
    </div>
</div>
</div>

<script src="<c:url value="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"/>"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>
</html>
