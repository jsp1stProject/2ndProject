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
					<a class="nav-link fs-4" href="#">
						그룹
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link fs-4" href="#">
						돌봄 신청
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link fs-4" href="#">
						게시판
					</a>
				</li>
			</ul>
		</div>
		<div class="justify-content-end px-0" id="navbarNav">
			<ul class="navbar-nav flex-row ms-auto align-items-center justify-content-end">
				<li class="nav-item d-block d-md-none">
					<a class="nav-link sidebartoggler " id="headerCollapse" href="#navCollapse" data-bs-toggle="collapse" role="button" aria-expanded="false" aria-controls="navCollapse">
						<i class="ti ti-menu-2"></i>
					</a>
				</li>
				<li v-if="!isLogin" class="nav-item"> <%--로그인 버튼--%>
					<a class="nav-link" href="${pageContext.request.contextPath}/login">
						<iconify-icon icon="solar:user-circle-broken" class="fs-8"></iconify-icon>
					</a>
				</li>
				<li v-if="isLogin" class="nav-item dropdown"> <%--알림--%>
					<a class="nav-link " href="javascript:void(0)" id="drop1" data-bs-toggle="dropdown" aria-expanded="false">
						<iconify-icon icon="solar:bell-linear" class="fs-7"></iconify-icon>
						<div class="notification bg-primary rounded-circle"></div>
					</a>
					<div class="dropdown-menu dropdown-menu-end dropdown-menu-animate-up" aria-labelledby="drop1">
						<div class="message-body">
							<a href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<iconify-icon icon="solar:mention-square-broken" class="fs-6"></iconify-icon>
								<p class="mb-0 fs-3">[댓글 알림]에 댓글이 달렸습니다.</p>
							</a>
							<a href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<iconify-icon icon="solar:cat-broken" class="fs-6"></iconify-icon>
								<p class="mb-0 fs-3">돌봄 예약 신청이 도착했습니다.</p>
							</a>
						</div>
					</div>
				</li>
				<li v-if="isLogin" class="nav-item dropdown"> <%--마이페이지 메뉴--%>
					<a class="nav-link " href="javascript:void(0)" id="drop2" data-bs-toggle="dropdown" aria-expanded="false">
						<img src="${pageContext.request.contextPath}/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
					</a>
					<div class="dropdown-menu dropdown-menu-end dropdown-menu-animate-up" aria-labelledby="drop2">
						<div class="message-body">
							<a href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<iconify-icon icon="solar:user-broken" class="fs-7"></iconify-icon>
								<p class="mb-0 fs-3">내 프로필</p>
							</a>
							<a href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<iconify-icon icon="solar:paw-linear" class="fs-7"></iconify-icon>
								<p class="mb-0 fs-3">내 반려동물</p>
							</a>
							<a href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<iconify-icon icon="solar:user-id-broken" class="fs-7"></iconify-icon>
								<p class="mb-0 fs-3">펫시터 프로필</p>
							</a>
							<a v-if="isAdmin" href="javascript:void(0)" class="d-flex align-items-center gap-2 dropdown-item">
								<iconify-icon icon="solar:shield-user-bold" class="fs-7"></iconify-icon>
								<p class="mb-0 fs-3">관리자 페이지</p>
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
	<ul class="navbar-nav collapse d-md-none text-center pb-2" id="navCollapse">
		<li class="nav-item">
			<a class="nav-link" href="#">그룹</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="#">돌봄 신청</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="#">게시판</a>
		</li>
	</ul>
</header>
<script type="module">
	import { createApp, ref, computed } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

	createApp({
		setup() {
			const isLogin=ref(${not empty usermail});
			const isAdmin=computed(()=>ref('${role}').value.includes('ROLE_ADMIN'))
			return {
				isLogin, isAdmin
			}
		}
	}).mount('#app')
</script>