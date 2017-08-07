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
        <form method="post" name="addSubject" class="navbar-form" action="<c:url value="/create/subject"/>">
            <table class="table">
                <tbody>
                <tr>
                    <th>Subject's title</th>
                </tr>
                <tr>
                    <th>
                        <div id="mydiv" class="form-group">
                        <label>
                                <input type="text" class="form-control" id="subjectTitle" name="subjectTitle" value="${requestScope.subjectTitle}"/>
                        </label>
                        </div>
                    </th>
                </tr>
                </tbody>
            </table> <br/>

            <input type="submit" value="Create" class="btn btn-success"/>

        </form>
        <button id="butt" name="butt" onclick="myButton()" ></button>
    </div>
</div>

</body>
<script src="<c:url value="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"/>"></script>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.17.0/jquery.validate.min.js"/>"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/js/createSubject.js"/>"></script>

</html>
