<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<%--<c:set var="language" value="${sessionScope.locale_language}"/>--%>
<fmt:setLocale value="${sessionScope.locale_language}" />
<fmt:setBundle basename="text" />
<html lang="${sessionScope.locale_language}">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/> " rel="stylesheet">
    <link href="<c:url value="/css/add-grades-list.css"/> " rel="stylesheet">
    <script type="text/javascript">
        var serverSelectedSubjectId = '${requestScope.selectedSubjectId}';
        var serverSelectedDate = '${requestScope.selectedDate}';
    </script>
    <title>Grades Web-Application</title>
</head>

<body onload="checkFilterConditions()">

<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 id="modal-title-text" class="modal-title"></h4>
            </div>
            <div class="modal-body">
                <p id="modal-body-text"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="action.close"/></button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="modal fade" id="modalAddGrade" tabindex="-1" role="dialog">
<div class="modal-dialog" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <h4 id="modalAddGrade-title-text" class="modal-title"><fmt:message key="action.add_grade"/></h4>
        </div>

        <form method="post" id="addNewGradeForm" name="addNewGradeForm" class="navbar-form" action="<c:url value="/create/grade"/>">

        <div class="modal-body">

            <p style="color: #ac2925; font-weight: bold" id="statusMessage"></p>
            <table class="table">
                <tbody>
                <tr>
                    <th><fmt:message key="entity.subject"/></th>
                    <th><fmt:message key="entity.date"/></th>
                    <th><fmt:message key="entity.mark"/></th>
                </tr>
                <tr>
                    <th>
                        <select class="form-control" id="modalSelectedSubjectId" name="modalSelectedSubjectId">
                            <c:forEach items="${requestScope.subjects}" var="subject">
                                <option ${subject eq null ? 'hidden' : ''} value="${subject.id}">${subject}</option>
                            </c:forEach>
                        </select>
                    </th>
                    <th>
                        <input name="date" id="date" type="date" value="${requestScope.date}" class="form-control"/>
                        <span class="help-block"></span>
                    </th>
                    <th>
                        <input name="mark" id="mark" type="number" min="0" value="${requestScope.mark}" class="form-control"/>
                    </th>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="modal-footer">
            <button type="button" class="btn btn-default" onclick="addGrade(this.form)"><fmt:message key="action.add_grade"/></button>
        </div>
        </form>
    </div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <jsp:include page="navbar.jsp"/>
        </div>

        <div class="col-lg-10 col-lg-offset-1">
            <table class="table">
                <tbody>
                <tr>
                    <form id="filterForm" class="navbar-form" >
                    <th>
                        <fmt:message key="entity.date"/>
                        <input id="selectedDate" onchange="checkFilterConditions()"
                               placeholder="<fmt:message key="entity.all_dates"/>"
                               name="selectedDate" value="${requestScope.selectedDate}"
                               type="date"
                               class="form-control"/>
                    </th>
                    <th>
                        <fmt:message key="entity.subject"/>
                        <select id="subjectsSelectId" onchange="checkFilterConditions()" name="selectedSubjectId" class="form-control">
                        <c:forEach items="${requestScope.subjects}" var="subject">
                            <option <c:if test="${requestScope.selectedSubjectId == subject.id}">selected</c:if> value="${subject.id}">
                                <c:choose>
                                    <c:when test="${subject == null}">
                                        <fmt:message key="entity.all_subjects"/>
                                    </c:when>
                                    <c:otherwise>
                                        ${subject.title}
                                    </c:otherwise>
                                </c:choose>
                            </option>
                        </c:forEach>
                        </select>
                    </th>
                    <th><fmt:message key="entity.mark"/></th>
                    <th>
                        <fmt:message key="action.filter"/><br/>
                        <input type="submit" id="filterBtn" class="btn btn-default" value="<fmt:message key="action.filter"/>"/>
                        <input type="button" id="resetBtn" <c:if test="${requestScope.selectedSubjectId != 0 || requestScope.selectedDate != null}">disabled</c:if>id="resetButton" class="btn btn-default" onclick="resetFilter()" value="<fmt:message key="action.reset"/>"/>
                    </th>
                    </form>
                </tr>
                <c:forEach items="${requestScope.allGrades}" var="currentGrade">
                    <tr>
                        <td ><c:out value="${currentGrade.date}"/></td>
                        <td style="margin-bottom: 0"><c:out value="${currentGrade.subject}"/></td>
                        <td style="margin-bottom: 0"><c:out value="${currentGrade.mark}"/></td>
                        <td style="margin-bottom: 0">
                            <form action="<c:url value="/deleteGrade"/>" style="margin-bottom: 0" method="post">
                                <input type="hidden" name="deletedGradeId" value="${currentGrade.id}"/>
                                <input type="submit" class="btn btn-default" value="<fmt:message key="action.delete"/>">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <nav class="text-center">
                <ul class="pagination">
                    <c:forEach items="${requestScope.paginatorDisplayedPages}" var="page">
                        <li
                                <c:if test="${page < 0 || page > requestScope.availablePagesNumber - 1}">class="hidden"</c:if>
                                <c:if test="${requestScope.pageIndex == page}">class="active"</c:if>
                        >
                            <a href="<c:url value="/list/grades?page=${page}"/>">${page + 1}</a>
                        </li>
                    </c:forEach>
                </ul>
            </nav>

            <hr>
            <input type="button" id="avgButton" class="btn btn-primary footer-buttons-class" onclick="submitAverage()" value="<fmt:message key="action.get_average"/>"/>
            <input type="button" id="addNewGradeButton" class="btn btn-primary footer-buttons-class" onclick="showAddGradeModal()" value="<fmt:message key="action.add_grade"/>"/>


        </div>
    </div>
</div>
<script src="<c:url value="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"/>"></script>
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.17.0/jquery.validate.min.js"/>"></script>
<script src="<c:url value="/js/gradesList.js"/>"></script>
<script src="<c:url value="/js/getLocale.js"/>"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>


</body>
</html>