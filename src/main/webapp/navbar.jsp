<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Aleks
  Date: 31.07.2017
  Time: 15:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href=<c:url value="/display"/>>Grades</a>
        </div>
        <ul class="nav navbar-nav">
            <li ${requestScope.page eq 'display' ? 'class="active"' : ''}>
                <a href=<c:url value="/display"/>>Home</a>
            </li>
            <li ${requestScope.page eq 'createGrade' ? 'class="active"' : ''}>
                <a href="<c:url value="/create/grade"/>">Add grade</a>
            </li>

            <li ${requestScope.page eq 'createSubject' ? 'class="active"' : ''}>
                <a href="<c:url value="/create/subject"/>">Add subject</a>
            </li>
            <li ${requestScope.page ne 'display' ? 'class="hidden"' : ''}>
                <form class="navbar-form" >
                    <select name="selectedSubject" class="form-control">
                        <c:forEach items="${requestScope.allSubjects}" var="subject">
                            <option value="${subject.id}">${subject == null ? 'All' : subject.title}</option>
                        </c:forEach>
                    </select>
                    <input type="submit" class="btn btn-success" value="Filter"/>
                </form>
            </li>
        </ul>
    </div>
</nav>