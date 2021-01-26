<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
    <%@ include file="parts/header.jsp" %>
</head>
<body>
<div class="container h-100">
    <div class="row h-100 justify-content-center align-items-center">
        <div class="col-6">
            <c:if test="${noerror == false}">
                <div class="alert alert alert-danger alert-dismissible fade show" role="alert">
                    Registration failed. Check all the input requirements.
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${noerror == true}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    User Registration Successful. Now you can log in.
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <!-- Form -->
            <form method="POST" action="/register">
                <h1>Register your user</h1>
                <!-- Input fields -->
                <div class="form-group mt-2">
                    <label for="email">Email address</label>
                    <input type="email" class="form-control" id="email" placeholder="name@example.com" name="email"
                           required>
                    <small id="emailHelpBlock" class="form-text text-muted">
                        Your email must be one that is not registered
                    </small>
                </div>
                <div class="form-group mt-2">
                    <label for="username" class="col-sm-2 col-form-label">Username</label>
                    <input id="username" type="text" class="form-control rounded-right" name="username" required>
                    <small id="userHelpBlock" class="form-text text-muted">
                        Your username must have a minimum of 3 characters long, your username must be one that is not registered and must not contain
                        spaces, special characters, or emoji
                    </small>
                </div>
                <div class="form-group mt-2">
                    <label for="password" class="col-sm-2 col-form-label">Password</label>
                    <input type="password" id="password" class="form-control"
                           aria-describedby="passwordHelpBlock" name="password" required>
                    <small id="passwordHelpBlock" class="form-text text-muted">
                        Your password must have a minimum of 8 characters long, contain letters and numbers, and must not contain
                        spaces, special characters, or emoji.
                    </small>
                </div>
                <div class="form-group mt-2">
                    <label for="repeatPassword" class="col-sm-2 col-form-label">Repeat Password</label>
                    <input type="password" id="repeatPassword" class="form-control"
                           aria-describedby="passwordHelpBlock" name="repeatPassword" required>
                </div>
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <button id="registerButton" type="submit" class="btn btn-primary btn-customized">Register
                </button>
                <!-- End input fields -->
            </form>
            <!-- Form end -->
            <p><a href="/login">Go to login</a></p>
        </div>
    </div>
</div>
<%@ include file="parts/footer.jsp" %>
</body>
</html>
