<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="row">
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title"></h3>

                <div class="card-tools">
                    <form class="input-group input-group-sm" method="get" action="${pageContext.request.contextPath}/admin/petsitters/list" name="search">
                        <input type="text" name="mail" class="form-control float-right" placeholder="이메일 검색">
                        <div class="input-group-append">
                            <button type="submit" class="btn btn-info">
                                <i class="bi bi-search"></i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            <!-- /.card-header -->
            <div class="card-body table-responsive p-0">
                <table class="table table-hover table-linked text-nowrap">
                    <thead>
                    <tr>
                        <th>신청번호</th>
                        <th>회원번호</th>
                        <th>이메일</th>
                        <th>닉네임</th>
                        <th>신청일</th>
                        <th>자격증</th>
                        <th>상태</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${result.list}" var="i" varStatus="c">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/admin/petsitters/${i.user_no}/applications" class="stretched-link"></a>${i.app_no}</td>
                            <td>${i.user_no}</td>
                            <td>${i.user_mail}</td>
                            <td>${i.nickname}</td>
                            <td>${i.dbday}</td>
                            <td>${i.license eq null?'':'보유'}</td>
                            <td class="py-1">
                                <c:if test="${i.status eq 0}">
                                    <button type="button" class="btn btn-sm btn-info">대기</button>
                                </c:if>
                                <c:if test="${i.status eq 1}">
                                    <button type="button" class="btn btn-sm btn-success" disabled>허가</button>
                                </c:if>
                                <c:if test="${i.status eq 2}">
                                    <button type="button" class="btn btn-sm btn-dark" disabled>반려</button>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <!-- /.card-body -->
            <div class="card-footer clearfix">
                <ul class="pagination pagination-sm m-0 float-end">
                    <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/admin/petsitters/applications?page=1">«</a></li>
                    <c:forEach begin="${result.startPage}" end="${result.endPage}" var="i">
                        <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/admin/petsitters/applications?page=${i}">${i}</a></li>
                    </c:forEach>
                    <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/admin/petsitters/applications?page=${result.endPage}">»</a></li>
                </ul>
            </div>
        </div>
        <!-- /.card -->
    </div>
</div>
