<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <c:if test="${action == '/create'}">
        <title>Add Note</title>
    </c:if>
    <c:if test="${action == '/edit'}">
        <title>Edit Note</title>
    </c:if>
    <%@ include file="parts/header.jsp" %>
</head>
<body>
<div class="container h-100">
    <div class="row h-100 justify-content-center align-items-center">
        <div class="col-6">

            <c:if test="${noerror == false && action == '/create'}">
                <div class="alert alert alert-danger alert-dismissible fade show" role="alert">
                    The note could not be created successfully
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${noerror == false && action == '/edit'}">
                <div class="alert alert alert-danger alert-dismissible fade show" role="alert">
                    The note could not be edited successfully
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <form class="form-example" method="POST" action="${action}">
                <input type="hidden" name="_csrftoken" value="${csrfToken}">
                <input type="hidden" name="noteid" value="${noteid}">
                <c:if test="${action == '/create'}">
                    <h1>Create a new Note</h1>
                    <div class="form-group mt-2">
                        <label for="title" class="col-sm-2 col-form-label">Title</label>
                        <input id="title" type="text" class="form-control rounded-right" name="title" required>
                    </div>
                    <div class="form-group mt-2">
                        <label for="bodyContent">Body</label>
                        <textarea class="form-control" id="bodyContent" rows="3" name="bodyContent">${body}</textarea>
                    </div>
                    <button type="submit" class="btn btn-success">Add</button>
                </c:if>
                <c:if test="${action == '/edit'}">
                    <h1>Update a current note</h1>
                    <div class="form-group mt-2">
                        <label for="titleUpdate" class="col-sm-2 col-form-label">Title</label>
                        <input id="titleUpdate" type="text" class="form-control rounded-right" name="title"
                               value="${title}"
                               required>
                    </div>
                    <div class="form-group mt-2">
                        <label for="bodyContentUpdate">Body</label>
                        <textarea class="form-control" id="bodyContentUpdate" rows="3"
                                  name="bodyContent">${body}</textarea>
                    </div>
                    <button type="submit" class="btn btn-warning">Edit</button>
                </c:if>
            </form>
            <p><a href="${pageContext.request.contextPath}/home">Go to home</a></p>
        </div>
    </div>
</div>
<%@ include file="parts/footer.jsp" %>
</body>
</html>
