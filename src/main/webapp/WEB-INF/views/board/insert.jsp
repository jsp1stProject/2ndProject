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
    <h2 style="text-align:center;">📝 게시글 작성</h2>

    <form method="post" action="${pageContext.request.contextPath}/web/board/insert" enctype="multipart/form-data">
        <input type="hidden" name="user_no" value="${sessionScope.user_no}" />

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
                <td><input type="text" name="post_title" required /></td>
            </tr>
            <tr>
                <td>내용</td>
                <td><textarea name="post_content" required></textarea></td>
            </tr>
            <!--  <tr>  이미지 파일 올리는거 나중에
            <th class="text-center" width="15%">첨부파일</th>
            <td width="85%">
                <input type="file" name="filename" id="filename" size="30" class="form-control-sm">
            </td>
        </tr>-->
        </table>

        <div class="btn-box">
            <input type="submit" value="등록">
            <a href="list">목록으로</a>
        </div>
    </form>
</body>
</html>
