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
            <a class="navbar-brand" href=<c:url value="/list/grades"/>>Grades</a>
        </div>
        <ul class="nav navbar-nav">
            <li ${requestScope.pageTitle eq 'gradesList' ? 'class="active"' : ''}>
                <a href=<c:url value="/list/grades"/>>Grades list</a>
            </li>
            <li ${requestScope.pageTitle eq 'displaySubjects' ? 'class="active"' : ''}>
                <a href=<c:url value="/list/subjects"/>>Subjects list</a>
            </li>
            <li ${requestScope.pageTitle eq 'createGrade' ? 'class="active"' : ''}>
                <a href="<c:url value="/create/grade"/>">Add grade</a>
            </li>

            <li ${requestScope.pageTitle eq 'createSubject' ? 'class="active"' : ''}>
                <a href="<c:url value="/create/subject"/>">Add subject</a>
            </li>
            <li ${requestScope.pageTitle ne 'gradesList' ? 'class="hidden"' : ''}>

                        <label >




                        </label>

                    <form style ='float: left; padding: 5px;' class="navbar-form" method="post" action="<c:url value="/logout"/>">
                        <input type="submit" id="logoutButton" style="float: right" class="btn btn-danger" value="Log Out"/>
                    </form>
            </li>
        </ul>
    </div>

</nav>
