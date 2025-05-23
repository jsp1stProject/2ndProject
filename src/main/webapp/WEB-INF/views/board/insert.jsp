<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>게시글 작성</title>
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
    <h2 style="text-align:center;">📝 게시글 작성</h2>

    <form method="post" action="${pageContext.request.contextPath}/board/insert" enctype="multipart/form-data">
        <table>
            <tr>
                <td>게시판 종류</td>
                <td>
                    <select name="type" required>
                        <option value="">-- 선택하세요 --</option>
                        <option value="강아지">강아지</option>
                        <option value="고양이">고양이</option>
                        <option value="소동물">소동물</option>
                        <option value="기타">기타</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>제목</td>
                <td><input type="text" name="title" required /></td>
            </tr>
            <tr>
                <td>내용</td>
                <td><textarea name="content" required></textarea></td>
            </tr>
        </table>

        <div class="btn-box">
            <input type="submit" value="등록">
            <a href="list">목록으로</a>
        </div>
    </form>
</body>
</html>
