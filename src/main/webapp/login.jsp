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
    <div style="margin-top: 10ch" class="col-md-5 col-md-offset-4">
        <div class="panel panel-primary login-panel">
            <div class="panel-heading">Login</div>
            <div class="panel-body">

                <form action="<c:url value="/login"/>" method="post" class="form-horizontal">
                    <div class="form-group">
                        <label for="userNameField" class="col-sm-2 control-label">Login</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="userNameField" name="username" placeholder="Login">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="passwordField" class="col-sm-2 control-label">Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="passwordField" name="password" placeholder="Password">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit" class="btn btn-default">Sign in</button>
                        </div>
                    </div>
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
