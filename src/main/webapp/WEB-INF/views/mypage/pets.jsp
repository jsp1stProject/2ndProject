<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Home</title>
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
<div class="container pt-header">
    <p>user no: ${userno}</p>
    <c:if test="${usermail ne null}">
        <p>${nickname} 님, 환영합니다.<br>
            이메일: ${usermail}<br>
            권한:
            <c:forEach var="r" items="${role}">
                <span>${r} </span>
            </c:forEach>
        </p>
    </c:if>

</div>

<script>
    const m1=newmodal('<b>test</b> test','m1');
</script>
</body>
</html>
