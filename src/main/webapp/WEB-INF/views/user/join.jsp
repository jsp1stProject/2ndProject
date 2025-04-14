<%--
  Created by IntelliJ IDEA.
  User: sist-105
  Date: 25. 4. 14.
  Time: 오후 1:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="join_ok.do" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="email" name="user_mail" placeholder="mail">
    <input type="password" name="password" placeholder="password">
    <input type="text" name="user_name" placeholder="name">
    <input type="text" name="nickname" placeholder="nickname">
    <input type="submit" value="가입">
</form>
</body>
</html>
