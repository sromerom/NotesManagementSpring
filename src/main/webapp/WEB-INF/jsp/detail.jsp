<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="util" class="com.liceu.sromerom.utils.ParseUtils"/>
<html>
<head>
    <title>Detail Note</title>
    <link href="/resources/css/detail.css" rel="stylesheet">
    <%@ include file="parts/header.jsp" %>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
</head>
<body>

<main>
    <section id="containerView">
        <div id="renderNote">
            <h1><c:out value="${view.title}"/></h1>
            <p>${util.renderToHTML(view.body)}</p>
        </div>
        <div id="infoVersion">
            <div class="accordion" id="accordionExample">
                <div class="accordion-item">
                    <h2 class="accordion-header" id="headingOne">
                        <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                            See historical from this note
                        </button>
                    </h2>

                    <c:choose>
                    <c:when test="${empty versionUrl}">
                    <div id="collapseOne" class="accordion-collapse collapse" aria-labelledby="headingOne"
                         data-bs-parent="#accordionExample">
                        </c:when>
                        <c:otherwise>
                        <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne"
                             data-bs-parent="#accordionExample">
                            </c:otherwise>
                            </c:choose>

                            <div class="accordion-body">
                                <div class="list-group">
                                    <c:choose>
                                    <c:when test="${empty versionUrl}">
                                    <a href="/detail?id=${actualNote.noteid}" class="list-group-item list-group-item-action active"
                                       aria-current="true">
                                        </c:when>
                                        <c:otherwise>
                                        <a href="/detail?id=${actualNote.noteid}" class="list-group-item list-group-item-action"
                                           aria-current="true">
                                            </c:otherwise>
                                            </c:choose>
                                            <div class="d-flex w-100 justify-content-between">
                                                <h5 class="mb-1">${util.parseDefaultDateTime(actualNote.lastModification)}</h5>

                                                <small>${util.getDifferenceDays(actualNote.creationDate)} days ago</small>
                                            </div>
                                            <small>Current version</small>
                                            <p class="mb-1">${view.user.username}</p>
                                        </a>
                                        <c:forEach var="version" items="${versions}">

                                        <c:choose>
                                        <c:when test="${versionUrl == version.versionid}">
                                        <a href="/detail?id=${actualNote.noteid}&version=${version.versionid}"
                                           class="list-group-item list-group-item-action active" aria-current="true">
                                            </c:when>
                                            <c:otherwise>
                                            <a href="/detail?id=${actualNote.noteid}&version=${version.versionid}"
                                               class="list-group-item list-group-item-action" aria-current="true">
                                                </c:otherwise>
                                                </c:choose>
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1">${util.parseDefaultDateTime(version.creationDate)}</h5>
                                                    <small>${util.getDifferenceDays(version.creationDate)} days ago</small>
                                                </div>
                                                <p class="mb-1">${version.user.username}</p>
                                            </a>
                                            </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </section>
</main>
<%@ include file="parts/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW"
        crossorigin="anonymous"></script>
</body>
</html>
