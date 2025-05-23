<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<div class="row">
    <div class="col-12">
        <div class="d-flex align-items-center justify-content-end pb-2">
            <div>
                <c:if test="${result.authority eq 'ROLE_SITTER'}">
                    <button type="button" class="btn btn-sm btn-dark">펫시터 권한 삭제</button>
                </c:if>
                <c:choose>
                    <c:when test="${result.authority eq 'ROLE_ADMIN'}">
                        <button type="button" class="btn btn-sm btn-danger">관리자 권한 삭제</button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-sm btn-info">관리자 권한 부여</button>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
        <div class="card-body">
            <div class="card border-light mb-4">
                <div class="card-body p-0">
                    <table class="table">
                        <colgroup>
                            <col style="width:120px">
                            <col style="width:auto">
                        </colgroup>
                        <tbody>
                        <tr class="align-middle">
                            <th>프로필 사진</th>
                            <td>
                                <c:choose>
                                    <c:when test="${result.profile ne null && result.profile ne '.'}">
                                        <img src="${pageContext.request.contextPath}/s3/${result.profile}"
                                             class="rounded-circle" alt="User Profile" width="48px" height="48px">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/assets/images/profile/default_pf.png"
                                             class="rounded-circle" alt="User Profile" width="48px" height="48px">
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr class="align-middle">
                            <th>회원번호</th>
                            <td>${result.user_no}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>이메일</th>
                            <td>${result.user_mail}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>이름</th>
                            <td>${result.user_name}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>닉네임</th>
                            <td>${result.nickname}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>등급</th>
                            <td>
                                <c:choose>
                                    <c:when test="${result.authority eq 'ROLE_USER'}">
                                        일반 회원
                                    </c:when>
                                    <c:when test="${result.authority eq 'ROLE_SITTER'}">
                                        펫시터
                                    </c:when>
                                    <c:otherwise>
                                        관리자
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr class="align-middle">
                            <th>가입일</th>
                            <td>${result.db_regdate}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>생일</th>
                            <td>${result.db_birthday}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>내 동네</th>
                            <td>${result.addr}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>카카오 연동</th>
                            <td>${result.social_id eq null?"미연동":"연동"}</td>
                        </tr>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <h5>반려동물 정보</h5>
        <div class="card border-light mb-4">
            <div class="card-body p-0">
                <table class="table">
                    <colgroup>
                        <col style="width:100px">
                        <col style="width:auto">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>이름</th>
                        <th>종</th>
                        <th>나이</th>
                        <th>몸무게</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${result2}" var="i">
                        <tr class="align-middle">
                            <td>${i.pet_name}</td>
                            <td>${i.pet_type}
                                <c:if test="${not empty i.pet_subtype}">
                                    (${i.pet_subtype})
                                </c:if>
                            </td>
                            <td>${i.pet_age}살</td>
                            <td>${i.pet_weight}kg</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <!-- /.card-body -->
        </div>
        <h5>그룹 정보</h5>
        <div class="card border-light mb-4">
            <div class="card-body p-0">
                <table class="table">
                    <colgroup>
                        <col style="width:100px">
                        <col style="width:auto">
                    </colgroup>
                    <thead>
                    <tr>
                        <th>그룹 번호</th>
                        <th>그룹 대표사진</th>
                        <th>그룹 이름</th>
                        <th>그룹 내 닉네임</th>
                        <th>역할</th>
                        <th>가입일</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${result3}" var="i">
                        <tr class="align-middle">
                            <td>${i.group_no}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${i.profile_img ne null && i.profile_img ne '.'}">
                                        <img src="${pageContext.request.contextPath}/s3/${i.profile_img}"
                                             class="rounded-circle" alt="User Profile" width="48px" height="48px">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/assets/images/profile/default_pf.png"
                                             class="rounded-circle" alt="User Profile" width="48px" height="48px">
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${i.group_name}</td>
                            <td>${i.nickname}</td>
                            <td>${i.role}</td>
                            <td>${i.joined_at}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <!-- /.card-body -->
        </div>
        <c:if test="${result.sitter_no ne null && result.sitter_no ne 0}">
            <h5>펫시터 프로필</h5>
            <div class="card border-light mb-4">
                <div class="card-body p-0">
                    <table class="table">
                        <colgroup>
                            <col style="width:120px">
                            <col style="width:auto">
                        </colgroup>
                        <tbody>
                        <tr class="align-middle">
                            <th>시터 프로필</th>
                            <td>
                                <c:choose>
                                    <c:when test="${result.sitter_pic ne null && result.sitter_pic ne '.'}">
                                        <img src="${pageContext.request.contextPath}/s3/${result.sitter_pic}"
                                             class="rounded-circle" alt="User Profile" width="48px" height="48px">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/assets/images/profile/default_pf.png"
                                             class="rounded-circle" alt="User Profile" width="48px" height="48px">
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr class="align-middle">
                            <th>펫시터 번호</th>
                            <td>${result.sitter_no}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>돌봄 횟수</th>
                            <td>${result.carecount}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>평점</th>
                            <td>${result.score}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>태그</th>
                            <td>${result.tag}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>소개</th>
                            <td>${result.content}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>돌봄 지역</th>
                            <td>${result.care_loc}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>돌봄 시작가</th>
                            <td>${result.pet_first_price}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>자격증</th>
                            <td>${result.license ne null?'반려동물종합관리사 보유':'미보유'}</td>
                        </tr>
                        <tr class="align-middle">
                            <th>돌봄 경력</th>
                            <td>${result.history}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <!-- /.card-body -->
            </div>
        </c:if>
    </div>
</div>
