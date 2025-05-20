<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${vo.fclty_nm} 상세보기</title>
</head>
<body>
    <h2 style="text-align:center;">${vo.fclty_nm} 상세정보</h2>

    <table border="1" cellpadding="10" cellspacing="0" align="center">
        <tr>
            <th>시설명</th>
            <td>${vo.fclty_nm}</td>
        </tr>
        <tr>
            <th>주소</th>
            <td>${vo.lnm_addr}</td>
        </tr>
        <tr>
            <th>전화번호</th>
            <td>${vo.tel_no}</td>
        </tr>
        <tr>
            <th>홈페이지</th>
            <td>
                <c:choose>
                    <c:when test="${not empty vo.hmpg_url}">
                        <a href="${vo.hmpg_url}" target="_blank">${vo.hmpg_url}</a>
                    </c:when>
                    <c:otherwise>없음</c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <th>카테고리</th>
            <td>${vo.CTGRY_TWO_NM}</td>
        </tr>
    </table>

    <div style="text-align:center; margin-top:20px;">
        <a href="/web/facility/list?category=${vo.CTGRY_TWO_NM}">← 목록으로 돌아가기</a>
    </div>
</body>
</html>
