<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/> " rel="stylesheet">
    <title>Grades Web-Application</title>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <jsp:include page="navbar.jsp"/>
        </div>

        <div class="col-lg-8">
            <table class="table">
                <tbody>
                <tr>
                    <th>Id</th>
                    <th>
                        Date
                    </th>
                    <th>Subject</th>
                    <th>Mark</th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${requestScope.allGrades}" var="currentGrade">
                    <tr>
                        <td><c:out value="${currentGrade.id}"/></td>
                        <td><c:out value="${currentGrade.date}"/></td>
                        <td><c:out value="${currentGrade.subject}"/></td>
                        <td><c:out value="${currentGrade.mark}"/></td>
                        <td>
                            <form action="delete" method="post">
                                <input type="hidden" name="deletedGradeId" value="${currentGrade.id}"/>
                                <input type="submit" value="Delete">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>