<%--
  Created by IntelliJ IDEA.
  User: sist-105
  Date: 25. 4. 14.
  Time: 오후 1:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="https://t1.kakaocdn.net/kakao_js_sdk/2.7.4/kakao.min.js"
        integrity="sha384-DKYJZ8NLiK8MN4/C5P2dtSmLQ4KwPaoqAfyA/DfmEc1VDxu4yyC7wy6K1Hs90nka" crossorigin="anonymous"></script>
<script>
    Kakao.init('d24bc46e149baf82df42f1db148941b3'); // 사용하려는 앱의 JavaScript 키 입력
</script>
<div class="container position-relative login_wrap">
    <div class="login_inner position-absolute top-50 start-50 translate-middle">
        <form action="" method="post" name="loginform" id="login">
            <div class="input_group">
                <div class="input_wrap">
                    <input type="email" id="user_mail" name="username" placeholder="이메일">
                </div>
                <div class="input_wrap">
                    <input type="password" id="password" name="password" placeholder="비밀번호">
                </div>
            </div>
            <button class="btn btn-primary" type="submit" id="logBtn">로그인</button>
            <button class="btn" type="button" id="kakao-login-btn" onclick="loginWithKakao()">
                <img src="https://k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg"
                     alt="카카오 로그인 버튼" />
            </button>
            <div class="find_wrap">
                <a href="${pageContext.request.contextPath}/auth/join">가입하기</a>
                <a href="#">아이디, 비밀번호 찾기</a>
            </div>
        </form>
    </div>
</div>

<script>
    const form=document.getElementById("login");
    $(form).on("submit",function(e){
        e.preventDefault();
        if(!form.checkValidity()){
            form.reportValidity();
            return;
        }
        login();
    })
    async function login(){
        let formData=new FormData(form);
        try{
            await axios({
                method:'post',
                url:'${pageContext.request.contextPath}/api/auth/login',
                headers:{
                    "Content-Type":"application/json"
                },
                data:formData,
                withCredentials:true
            });
            location.href='${pageContext.request.contextPath }/main'
        }catch (e) {
            if(e.status==500){
                toast('아이디나 비밀번호가 일치하지 않습니다.');
                console.error('status:', e.status);
            }
        }
    }

    function loginWithKakao() {
        //카카오톡 -> 없으면 카카오 계정으로 로그인
        Kakao.Auth.authorize({
            redirectUri: '${fn:substringBefore(pageContext.request.requestURL,pageContext.request.requestURI)}/auth/join',
        });
    }
</script>
