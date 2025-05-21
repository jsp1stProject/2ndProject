<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 style="text-align:center; margin-bottom: 30px; color: #333;">${vo.fclty_nm} 상세정보</h2>

<style>
    table {
        width: 800px;
        margin: 0 auto;
        border-collapse: collapse;
        background-color: white;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    }

    th, td {
        padding: 12px 15px;
        border: 1px solid #ddd;
        text-align: left;
    }

    th {
        background-color: #f0f0f0;
        color: #555;
        width: 30%;
    }

    tr:nth-child(even) td {
        background-color: #fafafa;
    }

    .section-header {
        background-color: #e0e7ff;
        color: #333;
        text-align: center;
        font-weight: bold;
    }

    .back-link {
        display: block;
        text-align: center;
        margin-top: 30px;
        font-size: 16px;
        text-decoration: none;
        color: #3366cc;
    }

    .back-link:hover {
        text-decoration: underline;
    }

    a {
        color: #0077cc;
    }
</style>
<h2 style="text-align:center; margin-bottom: 30px; color: #333;">${vo.fclty_nm} 상세정보</h2>
<table>
    <!-- 기본 정보 -->
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

    <!-- 운영 정보 -->
    <tr>
        <td colspan="2" class="section-header">운영 정보</td>
    </tr>
    <tr>
        <th>운영시간</th>
        <td>${vo.OPER_TIME}</td>
    </tr>
    <tr>
        <th>휴무일 안내</th>
        <td>${vo.RSTDE_GUID_CN}</td>
    </tr>
    <tr>
        <th>주차 가능 여부</th>
        <td>
            <c:choose>
                <c:when test="${vo.PARKNG_POSBL_AT eq 'Y'}">가능</c:when>
                <c:otherwise>불가능</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>이용 가격</th>
        <td>${vo.UTILIIZA_PRC_CN}</td>
    </tr>

    <!-- 반려동물 정보 -->
    <tr>
        <td colspan="2" class="section-header">반려동물 정보</td>
    </tr>
    <tr>
        <th>반려동물 동반 가능 여부</th>
        <td>
            <c:choose>
                <c:when test="${vo.PET_POSBL_AT eq 'Y'}">가능</c:when>
                <c:otherwise>불가능</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>반려동물 정보</th>
        <td>${vo.PET_INFO_CN}</td>
    </tr>
    <tr>
        <th>입장 가능 반려동물 크기</th>
        <td>${vo.ENTRN_POSBL_PET_SIZE_VALUE}</td>
    </tr>
    <tr>
        <th>반려동물 제한 사항</th>
        <td>${vo.PET_LMTT_MTR_CN}</td>
    </tr>
    <tr>
        <th>내부 동반 가능</th>
        <td>
            <c:choose>
                <c:when test="${vo.IN_PLACE_ACP_POSBL_AT eq 'Y'}">가능</c:when>
                <c:otherwise>불가능</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>외부 동반 가능</th>
        <td>
            <c:choose>
                <c:when test="${vo.OUT_PLACE_ACP_POSBL_AT eq 'Y'}">가능</c:when>
                <c:otherwise>불가능</c:otherwise>
            </c:choose>
        </td>
    </tr>
    <tr>
        <th>반려동물 추가 요금</th>
        <td>${vo.PET_ACP_ADIT_CHRGE_VALUE}</td>
    </tr>
</table>

<!-- 지도 출력 영역 -->
<div style="display: flex; justify-content: center;">
  <div id="map" style="width:800px;height:350px;"></div>
</div>

<!-- Kakao 지도 API 스크립트 -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=b72d19d574bc9065c96bcc5bdc881fc0&libraries=services"></script>
<script>
    // JSP 변수에서 위도/경도/시설명 받기 (예: requestScope 또는 model에 담겨 있음)
    var latitude = ${vo.LC_LA};   // 위도
    var longitude = ${vo.LC_LO};  // 경도

    var mapContainer = document.getElementById('map');
    var mapOption = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 3
    };

    // 지도 생성
    var map = new kakao.maps.Map(mapContainer, mapOption);

    // 마커 생성
    var marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(latitude, longitude),
        map: map
    });

    // 인포윈도우 생성
    var infowindow = new kakao.maps.InfoWindow({
        content: '<div style="padding:5px;">${vo.fclty_nm}</div>'
    });
    infowindow.open(map, marker);
</script>


<a class="back-link" href="/web/facility/list?category=${vo.CTGRY_TWO_NM}">← 목록으로 돌아가기</a>
