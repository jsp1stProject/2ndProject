<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style>
	[v-cloak] { display: none; }
</style>
<header class="app-header" id="app" v-cloak="true">
	<nav class="navbar navbar-light">
		<ul class="navbar-nav">
			<a href="${pageContext.request.contextPath}/main">
				<img src="${pageContext.request.contextPath}/assets/images/logos/favicon.png" alt="" width="45" height="45" class="rounded-circle">
			</a>
		</ul>
		<div class="navbar justify-content-center">
			<ul class="navbar-nav align-items-center flex-row d-none d-md-flex">
				<li class="nav-item">
					<a class="nav-link">
						그룹
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link">
						그룹
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link">
						그룹
					</a>
				</li>
			</ul>
		</div>
		<div class="justify-content-end px-0" id="navbarNav">
			<ul class="navbar-nav flex-row ms-auto align-items-center justify-content-end">
				<li class="nav-item d-block d-md-none">
					<a class="nav-link sidebartoggler " id="headerCollapse" href="javascript:void(0)">
						<i class="ti ti-menu-2"></i>
					</a>
				</li>
				<li v-if="!isLogin" class="nav-item">
					<a class="nav-link" href="${pageContext.request.contextPath}/login">
						<iconify-icon icon="solar:login-2-linear" class="fs-6"></iconify-icon>
					</a>
				</li>
				<li v-if="isLogin" class="nav-item dropdown">
					<a class="nav-link " href="javascript:void(0)" id="drop1" data-bs-toggle="dropdown" aria-expanded="false">
						<iconify-icon icon="solar:bell-linear" class="fs-6"></iconify-icon>
						<div class="notification bg-primary rounded-circle"></div>
					</a>
					<div class="dropdown-menu dropdown-menu-end dropdown-menu-animate-up" aria-labelledby="drop1">
						<div class="message-body">
							<a href="javascript:void(0)" class="dropdown-item">
								댓글 알림
							</a>
							<a href="javascript:void(0)" class="dropdown-item">
								펫시터 알림
							</a>
						</div>
					</div>
				</li>
				<li v-if="isLogin" class="nav-item dropdown">
					<a class="nav-link " href="javascript:void(0)" id="drop2" data-bs-toggle="dropdown" aria-expanded="false">
						<img src="${pageContext.request.contextPath}/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
					</a>
					<div class="dropdown-menu dropdown-menu-end dropdown-menu-animate-up" aria-labelledby="drop2">
						<div class="message-body">
							<a href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<i class="ti ti-user fs-6"></i>
								<p class="mb-0 fs-3">내 프로필</p>
							</a>
							<a href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<i class="ti ti-mail fs-6"></i>
								<p class="mb-0 fs-3">내 반려동물</p>
							</a>
							<a href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<i class="ti ti-list-check fs-6"></i>
								<p class="mb-0 fs-3">펫시터 프로필</p>
							</a>
							<form action="${pageContext.request.contextPath }/logout" method="post" class="px-3 pt-2">
								<input type="submit" class="btn btn-outline-primary w-100" value="로그아웃">
							</form>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</nav>
</header>
<%--	<div class="scrollTopBtn">--%>
<%--		<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 384 512"><!--!Font Awesome Free 6.7.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M214.6 41.4c-12.5-12.5-32.8-12.5-45.3 0l-160 160c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L160 141.2 160 448c0 17.7 14.3 32 32 32s32-14.3 32-32l0-306.7L329.4 246.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3l-160-160z"></path></svg>--%>
<%--	</div>--%>
<script type="module">
	import { createApp, ref } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

	createApp({
		setup() {
			const message = ref(
					<c:out value="${not empty usermail}" default="false" />
			);
			const isLogin=ref(${not empty usermail});
			return {
				message,isLogin
			}
		}
	}).mount('#app')
</script>