<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Create new</title>
</head>
<body>
    <h2>Add grade</h2>
    <p style="color: crimson; font-size: 14pt">${requestScope.message}</p>
    <form method="post" action="create">
        <label>
            <select name="selectedSubject">
                <c:forEach items="${requestScope.subjects}" var="subject">
                    <option ${requestScope.selectedSubjectId eq subject.id ? 'selected' : ''} value="${subject.id}">${subject}</option>
                </c:forEach>
            </select>
                <input name="date" type="date" value="${requestScope.date}" class="form-control" />
                <input name="mark" type="number" value="${requestScope.mark}" class="form-control" /> <br/><br/>
                <input name="add" type="submit" class="btn btn-success" value="Submit" />

        </label>

    </form>
</body>
</html>
