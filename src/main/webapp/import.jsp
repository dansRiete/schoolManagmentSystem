<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Aleks
  Date: 08.08.2017
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/> " rel="stylesheet">
    <title>Synchronizatiom</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <jsp:include page="navbar.jsp"/>
        </div>
        <div class="col-lg-6 col-lg-offset-3">
            <h3>Reload from JSON file:</h3>
            Select a file to upload: <br/>
            <form action="import" method="post" enctype="multipart/form-data">
                <input type="file" name="file" size="50"/>
                <br/>
                <input type="submit" value="Reload from file"/>
            </form>
        </div>
    </div>
</div>
<script src="<c:url value="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>
</html>
