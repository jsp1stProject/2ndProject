<%--
  Created by IntelliJ IDEA.
  User: sist-105
  Date: 25. 4. 14.
  Time: 오후 1:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://t1.kakaocdn.net/kakao_js_sdk/2.7.4/kakao.min.js"
            integrity="sha384-DKYJZ8NLiK8MN4/C5P2dtSmLQ4KwPaoqAfyA/DfmEc1VDxu4yyC7wy6K1Hs90nka" crossorigin="anonymous"></script>
    <script>
        Kakao.init('d24bc46e149baf82df42f1db148941b3'); // 사용하려는 앱의 JavaScript 키 입력
    </script>
</head>
<body>
<form action="login_ok.do" method="post" id="login">
<%--    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />--%>
    <input type="email" id="user_mail" name="username" placeholder="email">
    <input type="password" id="password" name="password" placeholder="password">
    <input type="submit" value="로그인">
    <input type="button" value="가입하기">
    <a id="kakao-login-btn" href="javascript:loginWithKakao()">
        <img src="https://k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg" width="222"
             alt="카카오 로그인 버튼" />
    </a>
    <p id="token-result"></p>
</form>
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
            let response=await axios({
                method:'post',
                url:'auth/login',
                headers:{
                    "Content-Type":"application/json"
                },
                data:formData
            });
            console.log(response.data);
            const token=response.data.token;
            if(token){
                // localStorage.setItem('jwtToken', token);
                document.cookie="login_token="+token;
                window.location.href='/main';
            }else{
                console.error("토큰이 없어요")
            }
        }catch (e) {
            console.error('로그인 실패:', error.response ? error.response.data : error);
        }
    }
</script>
<script>
    function loginWithKakao() {
        //카카오톡 -> 없으면 카카오 계정으로 로그인
        Kakao.Auth.authorize({
            redirectUri: 'http://localhost:8080/web/auth/join',
        });
    }
</script>
</body>
</html>
