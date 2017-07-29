<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
    <tr>
        <th>Id</th>
        <th>Date</th>
        <th>Subject</th>
        <th>Mark</th>
    </tr>
    <c:forEach items="${requestScope.allGrades}" var="currentGrade">
        <tr>
            <td><c:out value="${currentGrade.id}"/></td>
            <td><c:out value="${currentGrade.date}"/></td>
            <td><c:out value="${currentGrade.subject}"/></td>
            <td><c:out value="${currentGrade.mark}"/></td>
            <td>
                <a href ="delete?id=${currentGrade.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<br/>
<a href ="create">Add grade</a>

<form
</body>
</html>