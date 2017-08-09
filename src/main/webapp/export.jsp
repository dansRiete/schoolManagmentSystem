<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Aleks
  Date: 08.08.2017
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="language" value="${sessionScope.locale_language}"/>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="text" />
<html lang="${language}">
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
            <h3><fmt:message key="action.download_json"/></h3>
            <br/>
            <form action="export" method="post" enctype="multipart/form-data">
                <input type="submit" value="<fmt:message key="action.download"/>"/>
            </form>
        </div>
    </div>
</div>
<script src="<c:url value="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
</body>
</html>
