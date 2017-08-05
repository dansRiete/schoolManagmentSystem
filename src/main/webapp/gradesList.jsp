<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/> " rel="stylesheet">
    <title>Grades Web-Application</title>
</head>

<body>

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
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
            <h4 id="modalAddGrade-title-text" class="modal-title">Add a grade</h4>
        </div>

        <form method="post" id="addNewGradeForm" class="navbar-form" action="<c:url value="/create/grade"/>">

        <div class="modal-body">
            <p class="alert-danger" id="statusMessage"></p>
            <table class="table">
                <tbody>
                <tr>
                    <th>Subject</th>
                    <th>Date</th>
                    <th>Mark</th>
                </tr>
                <tr>
                    <th>
                        <select class="form-control" name="selectedSubject">
                            <c:forEach items="${requestScope.allSubjects}" var="subject">
                                <option ${requestScope.selectedSubjectId eq subject.id ? 'selected' : ''}
                                        value="${subject.id}">${subject}
                                </option>
                            </c:forEach>
                        </select>
                    </th>
                    <th>
                        <input name="date" type="date" value="${requestScope.date}" class="form-control"/>
                    </th>
                    <th>
                        <input name="mark" type="number" min="0" value="${requestScope.mark}" class="form-control"/>
                    </th>
                </tr>
                </tbody>
            </table>

        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-default" onclick="addGrade(this.form)">Add</button>
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

        <div class="col-lg-8">
            <table class="table">
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
                            <form action="deleteGrade" method="post">
                                <input type="hidden" name="deletedGradeId" value="${currentGrade.id}"/>
                                <input type="submit" class="btn btn-danger" value="Delete">
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

            <input type="button" id="addNewGradeButton" class="btn btn-success" value="Add a grade"/>



        </div>
    </div>
</div>
<script src="<c:url value="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"/>"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/js/index.js"/>"></script>
</body>
</html>