<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>커뮤니티 게시판</title>
    <style>
        .post-container {
            display: flex;
            justify-content: space-between;
            border: 1px solid #ddd;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 15px;
            background-color: #fffefc;
            box-shadow: 0 1px 4px rgba(0,0,0,0.1);
        }

        .post-content {
            flex: 1;
        }

        .post-image {
            width: 100px;
            height: 100px;
            margin-left: 15px;
            flex-shrink: 0;
        }

        .post-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            border-radius: 10px;
        }

        .category-badge {
            display: inline-block;
            background-color: #f5da42;
            color: black;
            font-size: 12px;
            padding: 2px 8px;
            border-radius: 12px;
            margin-bottom: 5px;
        }

        .post-title {
            font-weight: bold;
            font-size: 16px;
            margin: 5px 0;
            color: #333;
        }

        .post-body {
            color: #555;
            font-size: 14px;
            line-height: 1.4em;
            max-height: 2.8em;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
        }

        .post-meta {
            margin-top: 10px;
            font-size: 12px;
            color: #777;
        }

        .post-meta span {
            margin-right: 10px;
        }

        a { text-decoration: none; color: inherit; }
    </style>
</head>
<body>
    <h2>📋 커뮤니티 게시판</h2>
    <!-- 검색 + 필터 통합 -->
    <form method="get" action="list.do">
        <select name="type">
            <option value="title" ${type == 'title' ? 'selected' : ''}>제목</option>
            <option value="content" ${type == 'content' ? 'selected' : ''}>내용</option>
        </select>
        <input type="text" name="keyword" value="${keyword}" placeholder="검색어 입력">

        <select name="category">
            <option value="">전체</option>
            <option value="강아지" ${category == '강아지' ? 'selected' : ''}>강아지</option>
            <option value="고양이" ${category == '고양이' ? 'selected' : ''}>고양이</option>
            <option value="소동물" ${category == '소동물' ? 'selected' : ''}>소동물</option>
            <option value="기타" ${category == '기타' ? 'selected' : ''}>기타</option>
        </select>
        <input type="submit" value="검색">
    </form>

    <c:forEach var="vo" items="${list}">
    <c:if test="${empty list}">
  		<p>❗ 게시물이 없습니다 또는 리스트를 못 불러왔습니다.</p>
	</c:if>
        <a href="detail?post_id=${vo.post_id}">
            <div class="post-container">
                <div class="post-content">
                    <div class="category-badge">${vo.type}</div>
                    <div class="post-title">${vo.title}</div>
                    <div class="post-body">${vo.content}</div>
                    <div class="post-meta">
                        <span>${vo.nickname}</span>
                        <span>조회수 : ${vo.views }</span>
                        <!-- <span>댓글 : ${vo.reply_count}</span> -->
                        <span>좋아요 : ${vo.like_count}</span>
                        <span><fmt:formatDate value="${vo.created_at}" pattern="yyyy-MM-dd" /></span>
                    </div>
                </div>
                <c:if test="${not empty vo.image_url}">
                    <div class="post-image">
                        <img src="${vo.image_url}" alt="썸네일">
                    </div>
                </c:if>
            </div>
        </a>
    </c:forEach>
    
    <!-- 글쓰기 버튼 -->
    <div style="text-align: right; margin-top: 20px;">
        <a href="insert">✍ 글쓰기</a>
    </div>
</body>
</html>
