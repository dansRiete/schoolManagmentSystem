<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: Aleks
  Date: 31.07.2017
  Time: 17:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/> " rel="stylesheet">
    <link href="<c:url value="/css/createGrade.css"/> " rel="stylesheet">
    <title>Create subject</title>
</head>
<body>
<div class="container">
    <div class="col-lg-12">
        <jsp:include page="navbar.jsp"/>
    </div>
    <div class="col-lg-6">
        <h2>Add subject</h2>
        <p <c:if test="${fn:startsWith(requestScope.message, 'Success')}">style="color: #3e8f3e" </c:if>>
            ${requestScope.message}
        </p>
        <form method="post" class="navbar-form" action="<c:url value="/create/subject"/>">
            <table class="table">
                <tbody>
                <tr>
                    <th>Subject's title</th>
                </tr>
                <tr>
                    <th><input pattern="[^\s]{1,32}" required title="1 to 32 characters without slashes"type="text" class="form-control" name="subjectTitle" value="${requestScope.subjectTitle}"/></th>
                </tr>
                </tbody>
            </table> <br/>

            <input type="submit" value="Create" class="btn btn-success"/>
        </form>
    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>

</html>
