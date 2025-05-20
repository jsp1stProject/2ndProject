<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${category} 시설 목록</title>
    <style>
        body {
            font-family: "맑은 고딕", sans-serif;
            background-color: #f9f9f9;
            text-align: center;
            padding: 20px;
        }

        h2 {
            color: #333;
        }

        table {
            margin: auto;
            width: 90%;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:nth-child(even) {background-color: #f2f2f2;}
        tr:hover {background-color: #e0f7fa;}

        .pagination {
            margin-top: 20px;
        }

        .pagination a, .pagination strong {
            margin: 0 4px;
            padding: 6px 12px;
            text-decoration: none;
            border: 1px solid #ccc;
            color: #333;
            background-color: white;
            border-radius: 4px;
        }

        .pagination strong {
            background-color: #4CAF50;
            color: white;
            border-color: #4CAF50;
        }

        .pagination a:hover {
            background-color: #ddd;
        }
    </style>
</head>
<body>
<!-- 카테고리 선택 -->
<div style="text-align:center; margin-bottom: 20px;">
    <form method="get" action="${pageContext.request.contextPath}/facility/list">
	    <label for="category">카테고리 선택: </label>
	    <select name="category">
	        <c:forEach var="c" items="${categoryList}">
	            <option value="${c}" ${c == category ? 'selected' : ''}>${c}</option>
	        </c:forEach>
	    </select>
	
	    <label for="location">지역 입력: </label>
	    <input type="text" name="location" value="${location}" placeholder="예: 서울, 강남 등" />
	
	    <input type="submit" value="검색" />
	</form>
</div>
    <h2>${category} 시설 목록</h2>

    <table>
        <tr>
            <th>이름</th>
            <th>주소</th>
            <th>전화</th>
            <th>홈페이지</th>
        </tr>
        <c:forEach var="vo" items="${list}">
            <tr>
                <!-- <td><a href="/facility/detail?facilityId=${vo.facility_id}">${vo.fclty_nm}</a></td> 디테일용 --> 
                <td>${vo.fclty_nm}</td>
                <td>${vo.lnm_addr}</td>
                <td>${vo.tel_no}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty vo.hmpg_url}">
                            <a href="${vo.hmpg_url}" target="_blank">바로가기</a>
                        </c:when>
                        <c:otherwise>없음</c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>

    <!-- 페이징 영역 -->
	<div class="pagination">
	    <c:if test="${startPage > 1}">
	        <a href="${pageContext.request.contextPath}/facility/list?page=${startPage - 1}&category=${category}">이전</a>
	    </c:if>
	
	    <c:forEach var="i" begin="${startPage}" end="${endPage}">
	        <c:choose>
	            <c:when test="${i == page}">
	                <strong>${i}</strong>
	            </c:when>
	            <c:otherwise>
	                <a href="/web/facility/list?page=${i}&category=${category}&location=${location}">${i}</a>
	            </c:otherwise>
	        </c:choose>
	    </c:forEach>
	
	    <c:if test="${endPage < totalPage}">
	        <a href="${pageContext.request.contextPath}/facility/list?page=${endPage + 1}&category=${category}">다음</a>
	    </c:if>
	</div>


</body>
</html>
