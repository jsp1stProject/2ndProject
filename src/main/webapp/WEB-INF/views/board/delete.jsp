<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>게시글 삭제</title>
    <style>
        .container {
            width: 600px;
            margin: 50px auto;
            text-align: center;
            padding: 20px;
            border: 1px solid #ddd;
        }
        .btn-box {
            margin-top: 20px;
        }
        .btn-box a {
            margin: 0 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>🗑️ 게시글 삭제</h2>
        <p>정말 이 게시글을 삭제하시겠습니까?</p>
        <p><strong>${vo.title}</strong></p>

        <div class="btn-box">
            <a href="${pageContext.request.contextPath}/web/board/delete?post_id=${vo.post_id}">삭제</a>
            <a href="${pageContext.request.contextPath}/web/board/detail?post_id=${vo.post_id}">취소</a>
        </div>
    </div>
</body>
</html>
