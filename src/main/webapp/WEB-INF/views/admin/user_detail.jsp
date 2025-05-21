<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<div class="row">
    <div class="col-12">
        <!-- The icons -->
        <div class="col-12">
            <div class="card card-outline">
                <div class="d-flex align-items-center justify-content-between p-3">
                    <div>
                        <img src="${pageContext.request.contextPath}/assets/libs/AdminLTE/dist/assets/img/user2-160x160.jpg"
                             class="rounded-circle shadow" alt="User Profile" width="48px" height="48px">
                        <span class="fs-6 ps-3">2025.04.24 12:13
                        <c:choose>
                            <c:when test="${result.status eq 0}">
                                <span class="badge text-bg-info align-text-top">대기</span>
                            </c:when>
                            <c:when test="${result.status eq 1}">
                                <span class="badge text-bg-success align-text-top">허가</span>
                            </c:when>
                            <c:when test="${result.status eq 2}">
                                <span class="badge text-bg-danger align-text-top">반려</span>
                            </c:when>
                        </c:choose>
                        </span>
                    </div>
                     <div>
                         <c:choose>
                             <c:when test="${result.status eq 0}">
                                 <button type="button" class="btn btn-sm btn-info">허가하기</button>
                                 <button type="button" class="btn btn-sm btn-danger">반려하기</button>
                             </c:when>
                         </c:choose>

                    </div>
                </div>
                <div class="card-body">
                    <h5>회원 정보</h5>
                    <div class="card border-light mb-4">
                        <div class="card-body p-0">
                            <table class="table">
                                <colgroup>
                                    <col style="width:120px">
                                    <col style="width:auto">
                                </colgroup>
                                <tbody>
                                <tr class="align-middle">
                                    <th>회원번호</th>
                                    <td>${result.user_no}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>카카오 연동</th>
                                    <td>${result.user_no}</td>
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
                                    <th>이메일</th>
                                    <td>${result.user_mail}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>생일</th>
                                    <td>${result.dbbirth}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>내 동네</th>
                                    <td>${result.addr}</td>
                                </tr>

                                </tbody>
                            </table>
                        </div>
                        <!-- /.card-body -->
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
                                    <th>그룹</th>
                                    <th>그룹 닉네임</th>
                                    <th>역할</th>
                                    <th>가입일</th>
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
                                    <th>프로필사진</th>
                                    <td>${result.user_no}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>펫시터 번호</th>
                                    <td>${result.user_no}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>돌봄 횟수</th>
                                    <td>${result.user_no}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>평점</th>
                                    <td>${result.user_name}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>태그</th>
                                    <td>${result.nickname}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>소개</th>
                                    <td>${result.user_mail}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>돌봄 지역</th>
                                    <td>${result.dbbirth}</td>
                                </tr>
                                <tr class="align-middle">
                                    <th>가격</th>
                                    <td>${result.addr}</td>
                                </tr>

                                </tbody>
                            </table>
                        </div>
                        <!-- /.card-body -->
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!-- /.col -->
</div>
