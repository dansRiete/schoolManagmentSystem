<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="services.GradesService" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Grade" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="gradesService" class="services.GradesService" scope="session"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Grades Web-Application</title>
    <style type="text/css">
        table, th, td {
            border: 1px solid black;
        }
    </style>
</head>
<body>

<table>
    <tbody>

    <%
        List<Grade> grades = gradesService.getGradesFromDb();
        pageContext.setAttribute("grades", grades);
    %>

    <tr>
        <th>Id</th>
        <th>Date</th>
        <th>Subject</th>
        <th>Mark</th>
    </tr>
    <c:forEach items="${grades}" var="currentGrade">
        <tr>
            <td><c:out value="${currentGrade.id}"/></td>
            <td><c:out value="${currentGrade.date}"/></td>
            <td><c:out value="${currentGrade.subject}"/></td>
            <td><c:out value="${currentGrade.mark}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>