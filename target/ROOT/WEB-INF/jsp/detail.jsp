<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="util" class="com.liceu.sromerom.utils.ParseUtils"/>
<html>
<head>
    <title>Detail Note</title>
    <%@ include file="parts/header.jsp" %>
</head>
<body>

<main>
    <h1><c:out value="${titleNote}"/></h1>
    <section>
        <p>${util.renderToHTML(bodyNote)}</p>
    </section>
</main>
<%@ include file="parts/footer.jsp" %>
</body>
</html>
