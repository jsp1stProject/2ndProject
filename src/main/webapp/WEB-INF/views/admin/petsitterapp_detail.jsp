<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<div class="row">
    <div class="col-12">
        <!-- The icons -->
        <div class="col-12">
            <c:choose>
                <c:when test="${result.status eq 0}">
                <div class="card card-info card-outline">
                </c:when>
                <c:when test="${result.status eq 1}">
                    <div class="card card-success card-outline">
                </c:when>
                <c:when test="${result.status eq 2}">
                    <div class="card card-danger card-outline">
                </c:when>
            </c:choose>
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
                                    <col style="width:100px">
                                    <col style="width:auto">
                                </colgroup>
                                <tbody>
                                <tr class="align-middle">
                                    <th>회원번호</th>
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
                    <h5>반려동물 양육 경력</h5>
                    <div class="card border-light mb-4">
                        <div class="card-body">
                            ${result.history}
                        </div>
                    </div>
                    <h5>자기소개</h5>
                    <div class="card border-light mb-4">
                        <div class="card-body">
                            ${result.info}
                        </div>
                    </div>
                    <h5>반려동물종합관리사</h5>
                    <div class="card border-light mb-4">
                        <div class="card-body">
                            ${result.info ne null?'보유':'미보유'}
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!-- /.col -->
</div>
