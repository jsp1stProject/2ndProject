<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>게시글 상세보기</title>
    <style>
        .detail-container {
            max-width: 800px;
            margin: 30px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            background-color: #fffefc;
            box-shadow: 0 1px 6px rgba(0,0,0,0.1);
        }
        .post-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .category-badge {
            display: inline-block;
            background-color: #f5da42;
            color: black;
            font-size: 14px;
            padding: 2px 10px;
            border-radius: 12px;
            margin-bottom: 10px;
        }
        .post-meta {
            font-size: 13px;
            color: #888;
            margin-bottom: 20px;
        }
        .post-image img {
            max-width: 100%;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        .post-content {
            font-size: 16px;
            color: #333;
            line-height: 1.6em;
            white-space: pre-line;
        }
        .post-actions {
            margin-top: 30px;
            text-align: right;
        }
        .post-actions a {
            margin-left: 10px;
            text-decoration: none;
            color: #007bff;
        }
    </style>
</head>
<body>
    <div class="detail-container">
        <div class="category-badge">${vo.type}</div>
        <div class="post-title">${vo.title}</div>
        <div class="post-meta">
            작성자: ${vo.nickname} |
            작성일: <fmt:formatDate value="${vo.created_at}" pattern="yyyy-MM-dd" /> |
            조회수: ${vo.views} |
            좋아요: ${vo.like_count}
        </div>

        <c:if test="${not empty vo.image_url}">
            <div class="post-image">
                <img src="${vo.image_url}" alt="게시글 이미지">
            </div>
        </c:if>

        <div class="post-content">
            ${vo.content}
        </div>

        <div class="post-actions">
            <a href="update?post_id=${vo.post_id}">✏ 수정</a>
            <a href="delete?post_id=${vo.post_id}" onclick="return confirm('정말 삭제하시겠습니까?')">🗑 삭제</a>
            <a href="list">📋 목록</a>
        </div>
    </div>
</body>
</html>
