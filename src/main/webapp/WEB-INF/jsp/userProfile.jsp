<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Profile</title>
    <%@ include file="parts/header.jsp" %>
</head>
<body>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
<c:if test="${noerror == false}">
    <c:choose>
        <c:when test="${action == 'edit'}">
            <div class="alert alert alert-danger alert-dismissible fade show" role="alert">
                The profile could not be edited successfully
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert alert-danger alert-dismissible fade show" role="alert">
                The profile could not be deleted successfully
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:otherwise>
    </c:choose>
</c:if>
<div class="container">
    <div class="row flex-lg-nowrap">
        <div class="col">
            <div class="row">
                <div class="col mb-3">
                    <div class="card">
                        <div class="card-body">
                            <div class="e-profile">
                                <div class="row">
                                    <div class="col-12 col-sm-auto mb-3">
                                        <div class="mx-auto" style="width: 140px;">
                                            <div class="d-flex justify-content-center align-items-center rounded"
                                                 style="height: 140px; background-color: rgb(233, 236, 239);">
                                                <span style="color: rgb(166, 168, 170); font: bold 8pt Arial;">140x140</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col d-flex flex-column flex-sm-row justify-content-between mb-3">
                                        <div class="text-center text-sm-left mb-2 mb-sm-0">
                                            <h4 class="pt-sm-2 pb-1 mb-0 text-nowrap">${user.username}</h4>
                                            <p class="mb-0">@${user.username}</p>
                                            <p>Registred with ${user.typeUser} method.</p>
                                            <div class="mt-2">
                                                <button class="btn btn-primary" type="button">
                                                    <i class="fa fa-fw fa-camera"></i>
                                                    <span>Change Photo</span>
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-content pt-3">
                                    <div class="tab-pane active">
                                        <div class="row">
                                            <div class="col">
                                                <form class="form" novalidate="" method="POST"
                                                      action="${pageContext.request.contextPath}/editProfile">
                                                    <input type="hidden" name="_csrftoken" value="${csrfToken}">
                                                    <div class="row">
                                                        <div class="col">
                                                            <div class="form-group">
                                                                <label>Username</label>
                                                                <input class="form-control" type="text"
                                                                       placeholder="${user.username}" name="newUser"
                                                                       value="${user.username}">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col">
                                                            <div class="form-group">
                                                                <label>Email</label>
                                                                <input class="form-control" type="text"
                                                                       placeholder="email" name="newEmail"
                                                                       value="${user.email}">
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col d-flex justify-content-end">
                                                            <button class="btn btn-primary" type="submit">Save Changes
                                                            </button>
                                                        </div>
                                                    </div>
                                                </form>
                                                <c:if test="${user.typeUser == 'NATIVE'}">
                                                    <form class="form" novalidate="" method="POST"
                                                          action="${pageContext.request.contextPath}/editProfile">
                                                        <input type="hidden" name="_csrftoken" value="${csrfToken}">
                                                        <div class="mb-2"><b>Change Password</b></div>
                                                        <div class="row">
                                                            <div class="col">
                                                                <div class="form-group">
                                                                    <label>Current Password</label>
                                                                    <input class="form-control" type="password"
                                                                           name="currentPassword" placeholder="••••••">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col">
                                                                <div class="form-group">
                                                                    <label>New Password</label>
                                                                    <input class="form-control" type="password"
                                                                           name="newPass"
                                                                           placeholder="••••••">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col">
                                                                <div class="form-group">
                                                                    <label>Confirm <span
                                                                            class="d-none d-xl-inline">Password</span></label>
                                                                    <input class="form-control" type="password"
                                                                           name="newPassConfirm" placeholder="••••••">
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col d-flex justify-content-end">
                                                                <button class="btn btn-primary" type="submit">Save
                                                                    Changes
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-12 col-md-3 mb-3">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="px-xl-3">
                                <form method="POST" action="${pageContext.request.contextPath}/unlogin" class="inline">
                                    <input type="hidden" name="_csrftoken" value="${csrfToken}">
                                    <button type="submit" class="btn btn-block btn-secondary">
                                        <i class="fas fa-sign-out-alt"></i>
                                        <span>Logout</span>
                                    </button>
                                </form>
                            </div>
                            <div class="px-xl-3">
                                <form id="deleteUserForm" method="POST"
                                      action="${pageContext.request.contextPath}/deleteUser" class="inline">
                                    <input type="hidden" name="_csrftoken" value="${csrfToken}">
                                    <button type="submit" class="btn btn-danger">
                                        <i class="fas fa-user-slash"></i>
                                        Delete your user
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="confirmDeleteUser" tabindex="-1" role="dialog" aria-labelledby="deleteUser"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteUser">Are you sure delete this account? You will lose all the
                    notes.</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                <button id="accept" type="button" class="btn btn-danger">I'm completely sure. I want delete my account
                </button>
            </div>
        </div>
    </div>
</div>
<%@ include file="parts/footer.jsp" %>
<script>
    document.querySelector("#deleteUserForm").addEventListener("submit", function (e) {
        e.preventDefault();
        console.log("Submit!!!");
        $('#confirmDeleteUser').modal('show')
    })

    document.querySelector("#accept").addEventListener("click", function () {
        document.querySelector("#deleteUserForm").submit();
    })
</script>
</body>
</html>
