<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>게시글 수정</title>
    <style>
        form { width: 600px; margin: 0 auto; }
        table { width: 100%; border-collapse: collapse; }
        td { padding: 10px; vertical-align: top; }
        input[type="text"], textarea, select {
            width: 100%; padding: 8px; box-sizing: border-box;
        }
        textarea { height: 200px; resize: vertical; }
        .btn-box { text-align: center; margin-top: 20px; }
    </style>
</head>
<body>
	<h1>🐾</h1>
    <h1 style="text-align:center;">🐶🐱</h1>
    <h2 style="text-align:center;">✏️ 게시글 수정</h2>

    <form method="post" action="${pageContext.request.contextPath}/web/board/update">
        <input type="hidden" name="post_id" value="${vo.post_id}" />
        <input type="hidden" name="user_no" value="${vo.user_no}" />

        <table>
            <tr>
                <td>게시판 종류</td>
                <td>
                    <select name="type" required>
                        <option value="">-- 선택하세요 --</option>
                        <option value="강아지" ${vo.type == '강아지' ? 'selected' : ''}>강아지</option>
                        <option value="고양이" ${vo.type == '고양이' ? 'selected' : ''}>고양이</option>
                        <option value="소동물" ${vo.type == '소동물' ? 'selected' : ''}>소동물</option>
                        <option value="기타" ${vo.type == '기타' ? 'selected' : ''}>기타</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>제목</td>
                <td><input type="text" name="title" value="${vo.title}" required /></td>
            </tr>
            <tr>
                <td>내용</td>
                <td><textarea name="content" required>${vo.content}</textarea></td>
            </tr>
        </table>

        <div class="btn-box">
            <input type="submit" value="수정완료">
            <a href="/board/detail?post_id=${vo.post_id}">취소</a>
        </div>
    </form>
</body>
</html>
