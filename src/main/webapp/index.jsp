<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Grades Web-Application</title>
    <style type="text/css">
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2
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
        <th>Action</th>
    </tr>
    <c:forEach items="${requestScope.allGrades}" var="currentGrade">
        <tr>
            <td><c:out value="${currentGrade.id}"/></td>
            <td><c:out value="${currentGrade.date}"/></td>
            <td><c:out value="${currentGrade.subject}"/></td>
            <td><c:out value="${currentGrade.mark}"/></td>
            <td>
                <a href ="delete?deletedSubjectId=${currentGrade.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<br/>
<a href ="create">Add grade</a>

<p>Select by subject</p>
<form method="get" action="display">
    <select name="selectedSubject">
        <c:forEach items="${requestScope.allSubjects}" var="subject">
            <option value="${subject.id}">${subject == null ? 'All' : subject.title}</option>
        </c:forEach>
    </select>
    <input name="fetch" type="submit" class="btn btn-success" value="Filter" />
</form>
</body>
</html>