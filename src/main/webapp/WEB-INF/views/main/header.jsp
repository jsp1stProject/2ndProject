<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<header class="app-header">
	<nav class="navbar navbar-light">
		<ul class="navbar-nav">
			<a href="${pageContext.request.contextPath}/main">
				<img src="${pageContext.request.contextPath}/assets/images/logos/favicon.png" alt="" width="45" height="45" class="rounded-circle">
			</a>
		</ul>
		<div class="navbar justify-content-center">
			<ul class="navbar-nav align-items-center flex-row d-none d-md-flex">
				<li class="nav-item">
					<a class="nav-link fs-4" href="${pageContext.request.contextPath}/group/list">
						그룹
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link fs-4" href="${pageContext.request.contextPath}/sitter/list">
						펫시터
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link fs-4" href="${pageContext.request.contextPath}/schedule/home">
						일정
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link fs-4" href="${pageContext.request.contextPath}/board/list">
						게시판
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link fs-4" href="${pageContext.request.contextPath}/facility/list">
						시설 목록
					</a>
				</li>
				<li class="nav-item">
					<a class="nav-link fs-4" href="${pageContext.request.contextPath}/food/calorieCalculator">
						사료
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
				<c:choose>
					<c:when test="${empty usermail}">
						<li class="nav-item"> <%--로그인 버튼--%>
							<a class="nav-link" href="${pageContext.request.contextPath}/login">
								<iconify-icon icon="solar:user-circle-broken" class="fs-8"></iconify-icon>
							</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="nav-item dropdown"> <%--알림--%>
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
						<li class="nav-item dropdown"> <%--마이페이지 메뉴--%>
							<a id="profile-wrap" class="nav-link " href="javascript:void(0)" id="drop2" data-bs-toggle="dropdown" aria-expanded="false">
<%--								<img id="profile-pic" src="${pageContext.request.contextPath}/assets/images/profile/default_pf.png" alt="" width="35" height="35" class="rounded-circle">--%>
							</a>
							<div class="dropdown-menu dropdown-menu-end dropdown-menu-animate-up" aria-labelledby="drop2">
								<div class="message-body">
									<a href="${pageContext.request.contextPath}/mypage/profile" class="d-flex align-items-center gap-2 dropdown-item">
										<iconify-icon icon="solar:user-broken" class="fs-7"></iconify-icon>
										<p class="mb-0 fs-3">내 프로필</p>
									</a>
									<a href="${pageContext.request.contextPath}/mypage/pets/list" class="d-flex align-items-center gap-2 dropdown-item">
										<iconify-icon icon="solar:paw-linear" class="fs-7"></iconify-icon>
										<p class="mb-0 fs-3">내 반려동물</p>
									</a>
									<c:choose>
										<c:when test="${role[0] eq 'ROLE_USER'}">
											<a href="${pageContext.request.contextPath}/mypage/petsitterapp" class="d-flex align-items-center gap-2 dropdown-item">
												<iconify-icon icon="solar:user-id-broken" class="fs-7"></iconify-icon>
												<p class="mb-0 fs-3">펫시터 신청</p>
											</a>
										</c:when>
										<c:otherwise>
<%--											<a href="${pageContext.request.contextPath}/mypage/petsitterinfo" class="d-flex align-items-center gap-2 dropdown-item">--%>
<%--												<iconify-icon icon="solar:user-id-broken" class="fs-7"></iconify-icon>--%>
<%--												<p class="mb-0 fs-3">펫시터 프로필</p>--%>
<%--											</a>--%>
											<a href="${pageContext.request.contextPath}/sitter/insert" class="d-flex align-items-center gap-2 dropdown-item">
												<iconify-icon icon="solar:user-id-broken" class="fs-7"></iconify-icon>
												<p class="mb-0 fs-3">펫시터 프로필</p>
											</a>
										</c:otherwise>
									</c:choose>

									<c:if test="${role[0] eq 'ROLE_ADMIN'}">
										<a href="${pageContext.request.contextPath}/admin/users" class="d-flex align-items-center gap-2 dropdown-item">
											<iconify-icon icon="solar:shield-user-bold" class="fs-7"></iconify-icon>
											<p class="mb-0 fs-3">관리자 페이지</p>
										</a>
									</c:if>
									<form action="${pageContext.request.contextPath }/logout" method="post" class="px-3 pt-2">
										<input type="submit" class="btn btn-outline-primary w-100" value="로그아웃">
									</form>
								</div>
							</div>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</nav>
	<ul class="navbar-nav collapse d-md-none text-center" id="navCollapse">
		<li class="nav-item">
			<a class="nav-link" href="${pageContext.request.contextPath}/group/list">그룹</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="${pageContext.request.contextPath}/sitter/list">펫시터</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="${pageContext.request.contextPath}/schedule/home">일정</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="${pageContext.request.contextPath}/board/list">게시판</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="${pageContext.request.contextPath}/facility/list">
				시설 목록
			</a>
		</li>
		<li class="nav-item mb-2">
			<a class="nav-link" href="${pageContext.request.contextPath}/food/calorieCalculator">
				사료
			</a>
		</li>
	</ul>
</header>
<script>
	getHeader()
	async function getHeader(){
		try{
			const res = await axios({
				method:'get',
				url:'${pageContext.request.contextPath}/api/header',
				withCredentials:true
			});
			let profile="";
			console.log(res.data)
			if(res.data.data!=null){
				profile = '${pageContext.request.contextPath}/s3/' + res.data.data.profile;
			}else{
				profile = "${pageContext.request.contextPath}/assets/images/profile/default_pf.png"
			}

			$("#profile-wrap").html('<img id="profile-pic" src="'+profile+'" alt="" width="35" height="35" class="rounded-circle">');
			// $("#profile-pic").attr("src",profile);
		}catch (e) {
			const status = e.response?.status;
			if (status === 409) {
				console.error("실패:", e.response?.data || e);
				alert(e.response.data.message);
			} else {
				console.error("실패:", e.response?.data || e);
				alert(e.response.data.message);
			}
		}
	}

	function newmodal(content,name){
		let html;
		html = `
		<div class="modal" id="`+name+`" tabindex="-1">
		<div class="modal-dialog modal-sm modal-dialog-scrollable modal-dialog-centered">
		<div class="modal-content">
		<div class="modal-header">
		<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		</div>
		<div class="modal-body">
		<p>`+content+`</p>
		</div>
		<div class="modal-footer">
		<button type="button" class="btn btn-outline-dark">취소</button>
		<button type="button" class="btn btn-primary" data-bs-dismiss="modal" aria-label="Close">확인</button>
		</div>
		</div>
		</div>
		</div>`;
		$('body').append(html);
		return new bootstrap.Modal($('#'+name));
	}
</script>