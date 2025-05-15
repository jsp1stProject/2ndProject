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
	<a class="btn btn-primary" href="${pageContext.request.contextPath }/login">로그인</a>
	<form action="${pageContext.request.contextPath }/api/auth/logout" method="post">
		<input type="submit" class="btn btn-danger" value="로그아웃">
	</form>
	<a href="${pageContext.request.contextPath }/users/test" class="btn btn-outline-primary">user 권한 테스트 페이지</a>
	<a href="${pageContext.request.contextPath }/admin" class="btn btn-outline-success">admin 권한 테스트 페이지</a>
	<h5 class="mt-2">로그인 시 자동으로 setAttribute되는 목록</h5>
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
		<p>${nickname} 님, 환영합니다.<br>
			이메일: ${usermail}<br>
			권한:
			<c:forEach var="r" items="${role}">
				<span>${r} </span>
			</c:forEach>
		</p>
	</c:if>
	<p>
		단순 확인용 모달 :<br>
		const m1=newmodal('내용','ID');<br>
		m1.show();<br>
		<button type="button" class="btn  btn-dark" onclick="m1.show()">모달 보기</button>
	</p>
	<p>
		toast 메시지 :<br>
		toast('내용');<br>
		<button type="button" class="btn btn-warning" onclick="toast('!!!')">토스트 확인</button>
	</p>
</div>

<script>
	const m1=newmodal('<b>test</b> test','m1');
</script>
</body>
</html>
