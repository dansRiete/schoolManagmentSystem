<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Aleks
  Date: 31.07.2017
  Time: 15:58
  To change this template use File | Settings | File Templates.
--%>
<link href="<c:url value="/css/navbar.css"/> " rel="stylesheet">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse" >
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href=<c:url value="/list/grades"/>>Grades</a>
        </div>
        <ul class="nav navbar-nav">
            <li ${requestScope.pageTitle eq 'gradesList' ? 'class="active"' : ''}>
                <a href=<c:url value="/list/grades"/>>Grades list</a>
            </li>
            <li ${requestScope.pageTitle eq 'subjectsList' ? 'class="active"' : ''}>
                <a href=<c:url value="/list/subjects"/>>Subjects list</a>
            </li>
            <li style="vertical-align: middle" ${requestScope.pageTitle eq 'createSubject' ? 'class="active"' : ''}>
                <a href="<c:url value="/create/subject"/>">Add subject</a>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <form class="navbar-form right-midle" method="post" action="<c:url value="/logout"/>">
                <button type="submit" value="Log Out" class="btn btn-danger">
                    <span class="glyphicon glyphicon-log-out"></span>
                    Log out
                </button>
            </form>
        </ul>

    </div>

</nav>
