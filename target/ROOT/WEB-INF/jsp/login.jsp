<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <%@ include file="parts/header.jsp" %>
    <link href="/resources/css/login.css" rel="stylesheet">

</head>
<body>
<div class="container h-100">
    <div class="row h-100 justify-content-center align-items-center">
        <div class="col-6">
            <c:if test="${noerror == false}">
                <div class="alert alert alert-danger alert-dismissible fade show" role="alert">
                    Invalid email address/password
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <!-- Form -->
            <form method="POST" action="${pageContext.request.contextPath}/login">
                <h1>Notes Management</h1>
                <!-- Input fields -->
                <div class="form-group mt-2">
                    <label for="username" class="col-sm-2 col-form-label">Username</label>
                    <input id="username" type="text" class="form-control rounded-right" name="username" required>
                </div>
                <div class="form-group mt-2">
                    <label for="password" class="col-sm-2 col-form-label">Password</label>
                    <input type="password" id="password" class="form-control"
                           aria-describedby="passwordHelpBlock" name="password" required>
                </div>
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <!-- sign in button -->
                <button class="signinbtn">
                   Log in
                </button>
                <!-- End input fields -->
            </form>
            <div class="separator">
                <p>OR</p>
            </div>
            <!--
            <button id="loginGoogle" class="google__btn">
                <i class="fab fa-google"></i>
                Sign in with Google
            </button>
            -->
            <!-- google button -->
            <button id="loginGoogle" class="google__btn">
                <i class="fab fa-google"></i>
                Sign in with Google
            </button>
            <!-- twitter button -->
            <button id="loginTwitter" class="twitter__btn">
                <i class="fab fa-twitter"></i>
                Sign in with Twitter
            </button>
            <!-- twitter button -->
            <button id="loginFacebook" class="facebook__btn">
                <i class="fab fa-facebook"></i>
                Sign in with Facebook
            </button>
            <!-- Form end -->
            <p><a href="${pageContext.request.contextPath}/register">Create your account</a></p>
        </div>
    </div>
</div>
<%@ include file="parts/footer.jsp" %>
<script>
    document.querySelector("#loginGoogle").addEventListener("click", () => {
        window.location.replace("/loginGoogle");
    })

    document.querySelector("#loginTwitter").addEventListener("click", () => {
        window.location.replace("/loginTwitter");
    })

    document.querySelector("#loginFacebook").addEventListener("click", () => {
        window.location.replace("/loginFacebook");
    })
</script>
</body>
</html>
