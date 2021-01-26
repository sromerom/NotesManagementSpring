<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <%@ include file="parts/header.jsp" %>
    <link href="/resources/css/login.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" integrity="sha512-HK5fgLBL+xu6dm/Ii3z4xhlSUyZgTT9tuc/hSrtw6uzJOvgRr2a9jyxxT1ely+B+xFAmJKVSTbpM/CuL7qxO8w==" crossorigin="anonymous" />

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
                <h1>Management Notes Login</h1>
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
                <button type="submit" class="btn btn-primary btn-customized w-100">Login</button>
                <!-- End input fields -->
            </form>
            <form>
                <p style="display: block; text-align: center">Or login with</p>
                <button class="google__btn">
                    <i class="fab fa-google"></i>
                    Sign in with Google
                </button>
            </form>
            <!-- Form end -->
            <p><a href="${pageContext.request.contextPath}/register">Create your account</a></p>
        </div>
    </div>
</div>
<%@ include file="parts/footer.jsp" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/js/all.min.js" integrity="sha512-UwcC/iaz5ziHX7V6LjSKaXgCuRRqbTp1QHpbOJ4l1nw2/boCfZ2KlFIqBUA/uRVF0onbREnY9do8rM/uT/ilqw==" crossorigin="anonymous"></script>
</body>
</html>
