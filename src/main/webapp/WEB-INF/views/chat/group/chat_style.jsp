<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>그룹 채팅</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">

    <link rel="shortcut icon" type="image/png" href="${pageContext.request.contextPath }/assets/images/logos/favicon.png" />
    <link rel="stylesheet" href="${pageContext.request.contextPath }/assets/css/styles.css" />

    <script src="${pageContext.request.contextPath }/assets/libs/jquery/dist/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath }/assets/libs/bootstrap/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath }/assets/js/sidebarmenu.js"></script>
    <script src="${pageContext.request.contextPath }/assets/js/app.min.js"></script>
    <script src="${pageContext.request.contextPath }/assets/libs/apexcharts/dist/apexcharts.min.js"></script>
    <script src="${pageContext.request.contextPath }/assets/libs/simplebar/dist/simplebar.js"></script>

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
    <div class="chat-wrapper d-flex">
        <div class="group-list d-md-flex d-none">
            <ul class="p-2">
                <li>
                    <a href="#">
                        <img src="/assets/images/profile/user-1.jpg" alt="" width="48" height="48" class="rounded-circle">
                    </a>
                </li>
                <li>
                    <a href="#">
                        <img src="/assets/images/profile/user-1.jpg" alt="" width="48" height="48" class="rounded-circle">
                    </a>
                </li>
                <li>
                    <a href="#">
                        <img src="/assets/images/profile/user-1.jpg" alt="" width="48" height="48" class="rounded-circle">
                    </a>
                </li>
            </ul>
        </div>
        <div class="group-room flex-grow-1">
            <div class="group-header d-flex">
                <div>
                    <div class="dropdown">
                        <button type="button" id="groupdrop" data-bs-toggle="dropdown" aria-expanded="false">
                            그룹 정보
                            <iconify-icon icon="solar:alt-arrow-down-bold" class="fs-5 align-text-bottom"></iconify-icon>
                        </button>
                        <div class="dropdown-menu" aria-labelledby="groupdrop">
                            <button type="button" class="dropdiwn-item">그룹 설정</button>
                            <button type="button" class="dropdiwn-item">그룹 프로필</button>
                        </div>
                    </div>
                </div>
                <div class="group-title flex-grow-1">
                    <p class="fs-5">그룹 이름</p>
                </div>
                <div>
                    <div class="dropdown">
                        <input type="text" id="searchdrop" data-bs-toggle="dropdown" aria-expanded="false" placeholder="검색하기">
                        <div class="dropdown-menu" aria-labelledby="searchdrop">
                            <button type="button" class="dropdiwn-item">메시지 내용:</button>
                            <button type="button" class="dropdiwn-item">날짜:</button>
                            <button type="button" class="dropdiwn-item">사용자:</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="group-main d-flex">
                <div class="group-chat flex-grow-1">
                    <div class="chat-body" data-simplebar="init">
                        <div class="msg d-flex">
                            <a href="#" class="user-profile">
                                <img src="/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
                            </a>
                            <div class="msg-body d-flex flex-column">
                                <div class="user-info"><span><b>이름</b></span><span>15:21</span></div>
                                <div class="msg-content">메시지내용메시지내용메시지내용</div>
                            </div>
                        </div>
                        <div class="msg d-flex">
                            <a href="#" class="user-profile">
                                <img src="/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
                            </a>
                            <div class="msg-body d-flex flex-column">
                                <div class="user-info"><span><b>닉네임123</b></span><span>15:21</span></div>
                                <div class="msg-content">메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용</div>
                            </div>
                        </div>
                        <div class="msg d-flex">
                            <a href="#" class="user-profile">
                                <img src="/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
                            </a>
                            <div class="msg-body d-flex flex-column">
                                <div class="user-info"><span>닉네임123</span><span>15:21</span></div>
                                <div class="msg-content">메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용</div>
                            </div>
                        </div>
                        <div class="msg d-flex">
                            <a href="#" class="user-profile">
                                <img src="/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
                            </a>
                            <div class="msg-body d-flex flex-column">
                                <div class="user-info"><span>닉네임123</span><span>15:21</span></div>
                                <div class="msg-content">메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용</div>
                            </div>
                        </div>
                        <div class="msg d-flex">
                            <a href="#" class="user-profile">
                                <img src="/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
                            </a>
                            <div class="msg-body d-flex flex-column">
                                <div class="user-info"><span>닉네임123</span><span>15:21</span></div>
                                <div class="msg-content">메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용</div>
                            </div>
                        </div>
                        <div class="msg d-flex">
                            <a href="#" class="user-profile">
                                <img src="/assets/images/profile/user-1.jpg" alt="" width="35" height="35" class="rounded-circle">
                            </a>
                            <div class="msg-body d-flex flex-column">
                                <div class="user-info"><span>닉네임123</span><span>15:21</span></div>
                                <div class="msg-content">메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용메시지내용</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="group-submenu">

                </div>
            </div>
        </div>
    </div>
</body>
</html>