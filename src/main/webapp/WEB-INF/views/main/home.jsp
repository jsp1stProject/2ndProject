<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>Home</title>
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
<h1>
	Hello world!  
</h1>
<a href="${pageContext.request.contextPath }/login">로그인</a>
<form action="${pageContext.request.contextPath }/api/auth/logout" method="post">
	<input type="submit" value="로그아웃">
</form>
<a href="${pageContext.request.contextPath }/users/test">user 권한 테스트 페이지</a>
<a href="${pageContext.request.contextPath }/admin">admin 권한 테스트 페이지</a>
<p>로그인 시 자동으로 setAttribute되는 목록</p>
<style>
	tr:not(:last-child) td{border-bottom:1px solid #dddddd}
	th{background-color: #eaeaea; padding: 0 5px;}
</style>
<table>
	<tr>
		<th>usermail</th>
		<td>유저 이메일</td>
	</tr>
	<tr>
		<th>userno</th>
		<td>유저 일련번호(pk) (해당 번호는 사용자에게 노출x)</td>
	</tr>
	<tr>
		<th>nickname</th>
		<td>유저 닉네임</td>
	</tr>
	<tr>
		<th>role</th>
		<td>유저 권한 [ROLE_ADMIN], [ROLE_SITTER], [ROLE_USER]</td>
	</tr>
	<tr>
		<th>관련 클래스</th>
		<td>security.JwtAuthenticationFilter</td>
	</tr>
</table>
<c:if test="${usermail ne null}">
	<p>${nickname} 님, 환영합니다.</p>
	<p>이메일: ${usermail}</p>
	<p>권한: ${role}</p>
</c:if>
<script>

</script>
</body>
</html>
