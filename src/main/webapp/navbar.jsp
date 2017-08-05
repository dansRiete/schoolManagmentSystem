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
                    <form style ='float: left; padding: 5px;' id="filterForm" class="navbar-form" >
                        <label >
                            <select id="subjectsSelect" name="selectedSubjectId" class="form-control">
                                <c:forEach items="${requestScope.allSubjects}" var="subject">
                                    <option
                                            <c:if test="${requestScope.selectedSubject == subject.id}">selected</c:if>
                                            value="${subject.id}">${subject == null ? 'All subjects' : subject.title}
                                    </option>
                                </c:forEach>
                            </select>
                            <input id="selectedDate" name="selectedDate" value="${requestScope.selectedDate}" type="date"  class="form-control"/>
                            <input type="submit" class="btn btn-success" value="Filter"/>
                            <input type="button" id="resetButton" class="btn btn-success" value="Reset"/>
                            <input type="button" id="avgButton" class="btn btn-success"  value="Avg"/>
                        </label>
                    </form>
                    <form style ='float: left; padding: 5px;' class="navbar-form" method="post" action="<c:url value="/logout"/>">
                        <input type="submit" id="logoutButton" class="btn btn-danger" value="Log out"/>
                    </form>
            </li>
        </ul>
    </div>

</nav>
