<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/> " rel="stylesheet">
    <title>Grades Web-Application</title>
</head>

<body>
<div class="modal fade" id="myModalDeleteSubject" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 id="modalDS-title-text" class="modal-title"></h4>
            </div>
            <div class="modal-body">
                <p id="modalDS-body-text"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
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
                    <th>Subject's title</th>
                    <th>Action</th>
                </tr>
                <c:forEach items="${requestScope.subjects}" var="currentSubject">
                    <tr>
                        <td><c:out value="${currentSubject.id}"/></td>
                        <td><c:out value="${currentSubject.title}"/></td>
                        <td>
                            <form action="<c:url value="/deleteSubject"/>" method="post">
                                <input type="hidden" name="deletedSubjectId" value="${currentSubject.id}"/>
                                <input type="submit" value="Delete">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="js/bootstrap.min.js"></script>
</body>
</html>