<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create new</title>
    <link href="<c:url value="/css/bootstrap.min.css"/> " rel="stylesheet">
    <link href="<c:url value="/css/createGrade.css"/> " rel="stylesheet">
</head>
<body>
<div class="container">
        <div class="col-lg-12">
            <jsp:include page="navbar.jsp"/>
        </div>
        <div class="col-lg-6">
            <h2>Add grade</h2>
            <p <c:if test="${fn:startsWith(requestScope.statusMessage, 'Success')}">style="color: #3e8f3e" </c:if> >
                ${requestScope.statusMessage}
            </p>
            <form method="post" class="navbar-form" action="<c:url value="/create/grade"/>">
                <table class="table">
                    <tbody>
                    <tr>
                        <th>Subject</th>
                        <th>Date</th>
                        <th>Mark</th>
                    </tr>
                    <tr>
                    <th>
                        <select class="form-control" name="selectedSubject">
                            <c:forEach items="${requestScope.subjects}" var="subject">
                                <option ${requestScope.selectedSubjectId eq subject.id ? 'selected' : ''}
                                        value="${subject.id}">${subject}
                                </option>
                            </c:forEach>
                        </select>
                    </th>
                    <th>
                        <input name="date" type="date" value="${requestScope.date}" class="form-control"/>
                    </th>
                    <th>
                        <input name="mark" type="number" min="0" value="${requestScope.mark}" class="form-control"/>
                    </th>
                    </tr>
                    </tbody>
                </table>
                <br/><br/>
                <input name="add" type="submit" class="btn btn-success" value="Create"/>

            </form>
        </div>
    </div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>
