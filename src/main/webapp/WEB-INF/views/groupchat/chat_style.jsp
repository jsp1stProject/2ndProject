
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>그룹 채팅</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">
	
	<!-- CSS -->
    <link rel="shortcut icon" type="image/png" href="${pageContext.request.contextPath }/assets/images/logos/favicon.png" />
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/styles.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/groupchat.css" />

	<!-- JS -->
    <script src="${pageContext.request.contextPath }/assets/libs/jquery/dist/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath }/assets/libs/bootstrap/dist/js/bootstrap.bundle.js"></script>
    <script src="${pageContext.request.contextPath }/assets/js/chat.js"></script>
    <script src="${pageContext.request.contextPath }/assets/libs/simplebar/dist/simplebar.js"></script>
    
	<!-- STOMP, VUE, Axios -->    
    <script src="https://unpkg.com/vue@3"></script>
	<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	

    <script src="https://cdn.jsdelivr.net/npm/iconify-icon@1.0.8/dist/iconify-icon.min.js"></script>

    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <script>
        function toast(msg) {
            Toastify({
                text: msg,
                duration: 3000,
                newWindow: true,
                close: true,
                gravity: "bottom", // `top` or `bottom`
                position: "right", // `left`, `center` or `right`
                stopOnFocus: true, // Prevents dismissing of toast on hover
                style: {
                    background: "linear-gradient(to right, #00b09b, #96c93d)",
                },
                onClick: function () {
                } // Callback after click
            }).showToast();
        }
    </script>
</head>
<body>
	<div id="app">
    <div class="chat-wrapper d-flex">
        <div class="group-list d-flex flex-column justify-content-between" id="glist">
            <!-- 그룹 목록 -->
            <ul class="p-2 m-0" data-simplebar="init">
                <li v-for="group in availableGroups" :key="group.group_no">
                	<a href="#" @click.prevent="joinGroup(group.group_no)">
                        <div class="group-initial">{{ group.group_name.substring(0, 2) }}</div>
                    </a>
				</li>
				<li>
					<a href="">
						<img src="${pageContext.request.contextPath }/assets/images/blog/blog-img1.jpg" alt="" class="group-thumbnail" />
					</a>
				</li>
            </ul>
            <div>
                <!-- 그룹 설정 -->
                <div class="dropdown">
                    <button type="button" id="groupdrop" data-bs-toggle="dropdown" aria-expanded="false">
                        <span class="visually-hidden">그룹 정보 보기</span>
                        <iconify-icon icon="solar:menu-dots-circle-line-duotone" class="fs-14"></iconify-icon>
                    </button>
                    <div class="dropdown-menu" aria-labelledby="groupdrop">
                        <button type="button" class="dropdown-item">그룹 설정</button>
                        <button type="button" class="dropdown-item">그룹 프로필</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="group-room flex-grow-1">
            <div class="group-header d-flex">
                <button type="button" class="chattoggler d-block d-md-none" data-target="glist">
                    <iconify-icon icon="solar:widget-2-linear" class="d-flex align-items-center fs-10 h-100 px-2"></iconify-icon>
                </button>
                <div class="group-title flex-grow-1">
                    <p class="fs-5">그룹 이름</p>
                </div>
                <button type="button"  class="chattoggler d-block d-md-none" data-target="gside">
                    <iconify-icon icon="solar:users-group-rounded-broken" class="d-flex align-items-center fs-10 h-100 px-2"></iconify-icon>
                </button>
            </div>
            <!-- 채팅 목록 -->
            <div class="group-main">
                <div class="group-chat">
                    <div class="chat-body" data-simplebar ref="scrollContainer">
                    
                        <div class="msg d-flex" v-for="msg in messages" :key="msg.message_no">
                            <a href="#" class="user-profile">
                                <img src="${pageContext.request.contextPath }/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
                            </a>
                            <div class="msg-body d-flex flex-column">
                                <div class="user-info"><span><b>{{ msg.sender_nickname }}</b></span><span>{{ msg.created_at }}</span></div>
                                <div class="msg-content">{{ msg.content }}</div>
                            </div>
                        </div>
                        
                    </div>
                </div>
                <div class="chat-input-wrapper">
                    <div class="chat-input-box d-flex">
                        <input type="text" class="flex-grow-1" v-model="message" @keyup.enter="sendMessage">
                        <!-- 채팅 입력창 input -->
                        <div class="dropdown">
                            <button type="button" id="chatdrop" class="h-100" data-bs-toggle="dropdown" aria-expanded="false">
                                <span class="visually-hidden">메시지 설정</span>
                                <iconify-icon icon="solar:add-circle-line-duotone" class="fs-10 mx-1 align-middle"></iconify-icon>
                            </button>
                            <div class="dropdown-menu" aria-labelledby="chatdrop">
                                <button type="button" class="dropdown-item">추가 기능1</button>
                                <button type="button" class="dropdown-item">추가 기능2</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="group-side" id="gside">
            <div class="group-header d-flex">
                <div class="dropdown d-flex flex-grow-1 h-100 p-2">
                    <div class="search-wrap d-flex flex-grow-1 h-100 gap-1 align-items-center" id="searchdrop" data-bs-toggle="dropdown" aria-expanded="false">
                        <iconify-icon icon="solar:magnifer-broken" class="fs-6 align-middle"></iconify-icon>
                        <input type="text"  class="search-input w-100" placeholder="검색하기">
                    </div>
                    <!-- 채팅 내역 검색 -->
                    <div class="dropdown-menu" aria-labelledby="searchdrop">
                        <button type="button" class="dropdown-item">메시지 내용</button>
                        <button type="button" class="dropdown-item">날짜</button>
                        <button type="button" class="dropdown-item">사용자</button>
                    </div>
                </div>
                <button type="button" class="px-2 chatclose d-block d-md-none" data-target="gside">
                    <iconify-icon icon="solar:close-circle-line-duotone" class="fs-10 d-flex align-items-center"></iconify-icon>
                </button>
            </div>
            <div class="group-submenu p-2" data-simplebar="init">
                <div class="accordion accordion-flush open">
                    <!-- 온라인 유저 -->
                    <div class="accordion-item">
                        <button type="button" id="onlinedrop" class="accordion-button" data-bs-toggle="collapse" data-bs-target="#onlineUl" aria-expanded="true" aria-controls="onlineUl">온라인</button>
                        <ul id="onlineUl" class="accordion-collapse collapse show" aria-labelledby="onlinedrop">
                            <li v-for="m in members.filter(m => m.isOnline)" :key="m.user_no">
                                <a href="#" class="user-profile">
                                    <img src="${pageContext.request.contextPath }/assets/images/profile/user-3.jpg" alt="" width="35" height="35" class="rounded-circle">
                                    <p class="user-info">{{ m.nickname }}</p>
                                </a>
                            </li>
                        </ul>
                    </div>
                    <!-- 오프라인 유저 -->
                    <div class="accordion-item">
                        <button type="button" id="offlinedrop" class="accordion-button" data-bs-toggle="collapse" data-bs-target="#offlineUl" aria-expanded="true" aria-controls="offlineUl">오프라인</button>
                        <ul id="offlineUl" class="accordion-collapse show" aria-labelledby="offlinedrop">
                            <li v-for="m in members.filter(m => !m.isOnline)" :key="m.user_no">
                                <a href="#" class="user-profile">
                                    <img src="${pageContext.request.contextPath }/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
                                    <p class="user-info">{{ m.nickname }}</p>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </div>
    <script type="module">
		import { initGroupChat } from '${pageContext.request.contextPath}/assets/js/groupchat.js';
  		import * as Vue from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';
	    document.addEventListener('DOMContentLoaded', () => {
	   		initGroupChat(`${pageContext.request.contextPath}`, Vue.createApp);
	   	});
    </script>
</body>
</html>
