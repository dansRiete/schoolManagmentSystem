<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${sessionScope.locale_language}"/>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="text" />

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
                <a href=<c:url value="/list/grades"/>><fmt:message key="navbar.grades_list"/></a>
            </li>
            <li ${requestScope.pageTitle eq 'subjectsList' ? 'class="active"' : ''}>
                <a href=<c:url value="/list/subjects"/>><fmt:message key="navbar.subject_list"/></a>
            </li>
            <li ${requestScope.pageTitle eq 'import' ? 'class="active"' : ''}>
                <a href=<c:url value="/import"/>><fmt:message key="navbar.import"/></a>
            </li>
            <li ${requestScope.pageTitle eq 'export' ? 'class="active"' : ''}>
                <a href=<c:url value="/export"/>><fmt:message key="navbar.export"/></a>
            </li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li>
                <form class="navbar-form right-midle" action="<c:url value="/locale"/>" method="post">
                    <label for="language">
                        <select class="form-control" id="language" name="locale_language" onchange="submit()">
                            <option value="en" <c:if test="${sessionScope.locale_language eq 'en'}">selected</c:if>>Eng</option>
                            <option value="ua" <c:if test="${sessionScope.locale_language eq 'ua'}">selected</c:if>>Ukr</option>
                        </select>
                    </label>
                </form>
            </li>
            <li>
            <form class="navbar-form right-midle" method="post" action="<c:url value="/logout"/>">
                <button type="submit" value="logout" class="btn btn-danger">
                    <span class="glyphicon glyphicon-log-out"></span>
                    <fmt:message key="navbar.logout"/>
                </button>
            </form>
            </li>
        </ul>

    </div>

</nav>
