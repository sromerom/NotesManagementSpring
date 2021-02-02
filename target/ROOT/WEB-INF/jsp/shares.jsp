<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="util" class="com.liceu.sromerom.utils.ParseUtils"/>
<html>
<head>
    <c:if test="${action == '/share'}">
        <title>Share with users</title>
    </c:if>
    <c:if test="${action == '/deleteShare'}">
        <title>Delete shares</title>
    </c:if>
    <link href="/resources/css/users.css" rel="stylesheet">
    <%@ include file="parts/header.jsp" %>
</head>
<body>
<c:if test="${noerror == false && action == '/share'}">
    <div class="alert alert alert-danger alert-dismissible fade show" role="alert">
        The note could not be shared successfully
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>
<c:if test="${noerror == false && action == '/deleteShare'}">
    <div class="alert alert alert-danger alert-dismissible fade show" role="alert">
        Share could not be deleted
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>
<section id="container">
    <div>
        <h2>Users that you have shared for this note</h2>
        <table class="table table-striped table-dark">
            <thead>
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Permission</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="sharedNote" items="${usersShared}">
                <tr>
                    <td>${sharedNote.user.username}</td>
                    <td>
                        <form method="POST" action="${pageContext.request.contextPath}/updatePermission">
                            <input type="hidden" name="_csrftoken" value="${csrfToken}">
                            <input type="hidden" name="noteid" value="${noteid}">
                            <input type="hidden" name="shareduserid" value="${sharedNote.user.userid}">
                            <select name="permissionMode" id="permissionMode2">
                                <c:choose>
                                    <c:when test="${sharedNote.permissionMode == 'READMODE'}">
                                        <option selected="true" value="READMODE">Read Mode</option>
                                        <option value="WRITEMODE">Write Mode</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="READMODE">Read Mode</option>
                                        <option selected="true" value="WRITEMODE">Write Mode</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                            <button type="submit"> Edit</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${action == '/deleteShare'}">
            <button id="buttonToDelete" type="button" class="btn btn-danger" data-toggle="modal"
                    data-target="#modalDeleteAllShares">
                Delete all shares
            </button>
        </c:if>
    </div>

    <div>
        <c:choose>
        <c:when test="${action == '/deleteShare'}">
        <h2>Delete the users you have shared</h2>
        <form id="formActionDelete" method="POST" action="${action}">
            </c:when>
            <c:otherwise>
            <h2>Share this note with some users</h2>
            <form id="formActionShare" method="POST" action="${action}">
                </c:otherwise>
                </c:choose>

                <input id="noteidInput" type="hidden" name="noteid" value="${noteid}">
                <select class="js-example-basic-multiple" name="users[]" multiple="multiple">
                    <c:forEach var="user" items="${users}">
                        <option value="${user.username}">${user.username}</option>
                    </c:forEach>
                </select>

                <c:if test="${(action == '/share')}">
                    <select name="permissionMode" id="permissionMode">
                        <option value="READMODE">Read Mode</option>
                        <option value="WRITEMODE">Write Mode</option>
                    </select>
                </c:if>
                <small id="shareHelpBlock" class="form-text text-muted">
                    Remember that you cannot delete or create the share of a user that already exists.
                </small>
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <c:choose>
                    <c:when test="${action == '/deleteShare'}">
                        <button type="submit" class="btn btn-danger">Delete specific share</button>
                    </c:when>
                    <c:otherwise>
                        <button type="submit" class="btn btn-success">Add shares</button>
                    </c:otherwise>
                </c:choose>
            </form>
    </div>
</section>
<p><a href="${pageContext.request.contextPath}/home">Go to home</a></p>
<%@ include file="parts/footer.jsp" %>


<%-- ####################  Modal to Delete all shares #################### --%>
<div class="modal fade" id="modalDeleteAllShares" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="titleModalDeleteShares">Are you sure to delete all shares for this
                    note?</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Go Back</button>
                <form action="${pageContext.request.contextPath}/deleteAllShare" method="POST">
                    <input type="hidden" name="noteid" value="${noteid}">
                    <input type="hidden" name="_csrftoken" value="${csrfToken}">
                    <button type="submit" class="btn btn-danger">Delete all shares</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="titleModalDeleteShares2">Are you sure to delete yourself? You will not be
                    able to see this note anymore.</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                <button id="accept" type="button" class="btn btn-danger">Yes, I don't want this note anymore.</button>
            </div>
        </div>
    </div>
</div>
<input id="username" type="hidden" name="username" value="${username}">
<script>
    $(document).ready(function () {
        $('.js-example-basic-multiple').select2();
    });

    if (document.querySelector("#formActionDelete")) {
        document.querySelector("#formActionDelete").addEventListener("submit", function (e) {
            e.preventDefault();
            const username = document.querySelector("#username").value;
            const selected = $('.js-example-basic-multiple').select2("val");
            let check = false;
            selected.forEach(u => {
                if (u === username) {
                    check = true;
                    $('#exampleModal').modal('show')
                }
            })
            if (!check) {
                document.querySelector("#formActionDelete").submit();
            }
        })

        document.querySelector("#accept").addEventListener("click", function () {
            document.querySelector("#formActionDelete").submit();
        })
    }
</script>
</body>
</html>
