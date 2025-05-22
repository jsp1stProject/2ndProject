<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Pet4U 관리자 페이지</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fontsource/source-sans-3@5.0.12/index.css" integrity="sha256-tXJfXfp6Ewt1ilPzLDtQnJV4hclT9XuaZUKyUvmyr+Q=" crossorigin="anonymous" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.10.1/styles/overlayscrollbars.min.css" integrity="sha256-tZHrRjVqNSRyWg2wbppGnT833E/Ys0DHWGwT04GiqQg=" crossorigin="anonymous" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" integrity="sha256-9kPW/n5nn53j4WMRYAxe9c1rCY96Oogo/MKSVdKzPmI=" crossorigin="anonymous" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/libs/AdminLTE/dist/css/adminlte.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin_reboot.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/apexcharts@3.37.1/dist/apexcharts.css" integrity="sha256-4MX+61mt9NVvvuPjUWdUdyfZfxSB1/Rf9WtqRHgG5S0=" crossorigin="anonymous"/>
    <script src="${pageContext.request.contextPath}/assets/libs/AdminLTE/dist/js/adminlte.js"></script>
    <script src="${pageContext.request.contextPath}/assets/libs/jquery/dist/jquery.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
    <nav class="app-header navbar navbar-expand bg-body">
        <div class="container-fluid">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" data-lte-toggle="sidebar" href="#" role="button">
                        <i class="bi bi-list"></i>
                    </a>
                </li>
                <li class="nav-item d-none d-md-block"><a href="#" class="nav-link">Pet4U</a></li>
            </ul>
            <ul class="navbar-nav ms-auto">
                <li class="nav-item dropdown user-menu">
                    <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
                        <span class="d-none d-md-inline">관리자 <b class="text-dark">${requestScope.nickname}</b> 님, 환영합니다.</span>
                    </a>
<%--                    <ul class="dropdown-menu dropdown-menu-lg dropdown-menu-end">--%>
<%--                        <li class="user-header text-bg-primary">--%>
<%--                            <img src="${pageContext.request.contextPath}/assets/libs/AdminLTE/dist/assets/img/user2-160x160.jpg" class="user-image rounded-circle shadow" alt="User Image"/>--%>
<%--                            <p>--%>
<%--                                Alexander Pierce - Web Developer--%>
<%--                                <small>Member since Nov. 2023</small>--%>
<%--                            </p>--%>
<%--                        </li>--%>
<%--                        <li class="user-body">--%>
<%--                            <div class="row">--%>
<%--                                <div class="col-4 text-center"><a href="#">Followers</a></div>--%>
<%--                                <div class="col-4 text-center"><a href="#">Sales</a></div>--%>
<%--                                <div class="col-4 text-center"><a href="#">Friends</a></div>--%>
<%--                            </div>--%>
<%--                        </li>--%>
<%--                        <li class="user-footer">--%>
<%--                            <a href="#" class="btn btn-default btn-flat">Profile</a>--%>
<%--                            <a href="#" class="btn btn-default btn-flat float-end">Sign out</a>--%>
<%--                        </li>--%>
<%--                    </ul>--%>
                </li>
            </ul>
        </div>
    </nav>
    <aside class="app-sidebar bg-success-subtle shadow" data-bs-theme="">
        <!--begin::Sidebar Brand-->
        <div class="sidebar-brand">
            <!--begin::Brand Link-->
            <a href="${pageContext.request.contextPath}/admin/" class="brand-link">
                <img src="${pageContext.request.contextPath}/assets/images/logos/favicon.png" alt="Pet4U Logo" class="brand-image opacity-75 shadow rounded-circle bg-light" />
                <span class="brand-text fw-light">관리자 페이지</span>
            </a>
        </div>
        <div class="sidebar-wrapper">
            <nav class="mt-2">
                <ul
                        class="nav sidebar-menu flex-column"
                        data-lte-toggle="treeview"
                        role="menu"
                        data-accordion="false"
                >
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/admin/users" class="nav-link active" data-menu="1">
                            <i class="nav-icon bi bi-speedometer"></i>
                            <p>
                                회원
                            </p>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="#" class="nav-link" data-menu="2">
                            <i class="nav-icon bi bi-speedometer"></i>
                            <p>
                                펫시터
                                <i class="nav-arrow bi bi-chevron-right"></i>
                            </p>
                        </a>
                        <ul class="nav nav-treeview">
                            <li class="nav-item ps-2">
                                <a href="${pageContext.request.contextPath}/admin/petsitters" class="nav-link" data-menu="2-1">
                                    <i class="nav-icon bi bi-circle"></i>
                                    <p>펫시터 목록</p>
                                </a>
                            </li>
                            <li class="nav-item ps-2">
                                <a href="${pageContext.request.contextPath}/admin/petsitters/applications" class="nav-link" data-menu="2-2">
                                    <i class="nav-icon bi bi-circle"></i>
                                    <p>펫시터 신청</p>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li class="nav-header">링크</li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/main" target="_blank" class="nav-link">
                            <i class="nav-icon bi bi-box-arrow-in-right"></i>
                            <p>
                                펫포유 사용자 페이지
                                <i class="nav-arrow bi bi-chevron-right"></i>
                            </p>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </aside>
    <main class="app-main">
        <div class="app-content-header">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6"><h3 class="mb-0">${title}</h3></div>
                </div>
            </div>
        </div>
        <div class="app-content">
            <div class="container-fluid">
                <jsp:include page="${main_jsp }"></jsp:include>
            </div>
        </div>
    </main>
</div>
<script src="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.10.1/browser/overlayscrollbars.browser.es6.min.js" integrity="sha256-dghWARbRe2eLlIJ56wNB+b760ywulqK3DzZYEpsg2fQ=" crossorigin="anonymous" ></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous" ></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>

<script>
    $(function(){
        $("a.nav-link").removeClass("active");
        const active=$("a.nav-link[data-menu='${menu}']");
        const active_menu=active.closest("ul.nav-treeview").closest("li");
        active.addClass("active");
        active_menu.addClass("menu-open");
        active_menu.children("a.nav-link").addClass("active");
        console.log('${requestScope.menu}');
        console.log('${requestScope.title}');
    })
</script>
</body>
</html>
