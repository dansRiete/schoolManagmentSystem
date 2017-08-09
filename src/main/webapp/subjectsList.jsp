<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<c:set var="language" value="${sessionScope.locale_language}"/>
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="text" />
<html lang="${language}">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/> " rel="stylesheet">
    <link href="<c:url value="/css/subjectList.css"/> " rel="stylesheet">
    <title>Grades Web-Application</title>
</head>

<body>

<%--Delete subject modal window--%>
<div class="modal fade" id="myModalDeleteSubject" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 id="modalDS-title-text" class="modal-title"><fmt:message key="result.there_is_a_grade"/></h4>
            </div>
            <div class="modal-body">
                <p id="modalDS-body-text"><fmt:message key="ask.sure_force_delete_subject"/></p>
            </div>
            <div class="modal-footer">
                <form method="post"  action="<c:url value="/deleteSubject"/>">
                    <input id="modalDeleteSubjId" type="hidden" name="deletedSubjectId"/>
                    <input type="submit" value="<fmt:message key="action.delete"/>">
                </form>
            </div>
        </div>
    </div>
</div>

<%--Add new subject modal window--%>
<div class="modal fade" id="myModalAddSubject" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 id="modalAS-title-text" class="modal-title"><fmt:message key="action.add_subject"/></h4>
            </div>
            <form id="new-subject-form" class="form-horizontal">
            <div class="modal-body">
                <p id="modalAS-body-text"></p>

                <div id="newSubjectTitleDiv" class="form-group row">
                    <div class="col-xs-8">
                        <label for="newSubjectTitle"><fmt:message key="entity.subject"/></label>
                        <input type="text" class="form-control" name="newSubjectTitle" id="newSubjectTitle" placeholder="<fmt:message key="entity.subject"/>">
                        <span id="subject-title-error" class="help-inline error-span"></span>
                    </div>

                </div>

            </div>
            <div class="modal-footer">
                <input type="button" class="btn btn-success" onclick="addSubject(this.form)" value="Create">
            </div>
            </form>
        </div>
    </div>
</div>



<div class="container">
    <div class="row">

        <div class="col-lg-12"><jsp:include page="navbar.jsp"/></div>

        <div class="col-lg-6 col-lg-offset-3">
            <table class="table">
                <tbody>
                <tr>
                    <th><fmt:message key="entity.subject"/></th>
                    <th><fmt:message key="action.action"/></th>
                </tr>
                <c:forEach items="${requestScope.subjects}" var="currentSubject">
                    <tr>
                        <td><c:out value="${currentSubject.title}"/></td>
                        <td>
                            <form id="${"deleteSubjectForm_".concat(currentSubject.id)}" style="margin-bottom: 0"
                                  action="<c:url value="/deleteSubject"/>" method="post">
                                <input type="hidden" name="deletedSubjectId" value="${currentSubject.id}"/>
                                <input type="button" class="btn btn-default" onclick="deleteSubject(${currentSubject.id})" value="<fmt:message key="action.delete"/>">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <hr>
            <input type="button" id="addNewSubjectButton" data-toggle="modal" onclick="showAddSubjectModal()"
                   class="btn btn-primary footer-buttons-class" value="<fmt:message key="action.create"/>"/>
        </div>

    </div>
</div>

<script src="<c:url value="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"/>"></script>
<script src="<c:url value="/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/js/subjectsList.js"/>"></script>

</body>
</html>