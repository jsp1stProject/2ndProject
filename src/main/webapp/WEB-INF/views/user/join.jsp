<%--
  Created by IntelliJ IDEA.
  User: sist-105
  Date: 25. 4. 14.
  Time: 오후 1:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
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
<form action="join_ok.do" method="post">
    <p id="token-result"></p>
<%--    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />--%>
    <input type="email" name="user_mail" placeholder="mail">
    <input type="password" name="password" placeholder="password">
    <input type="text" name="user_name" placeholder="name">
    <input type="text" name="nickname" placeholder="nickname">
    <input type="submit" value="가입">
</form>
<script>
    //토큰 가져오기
    login();
    async function login(){
        let params = new URLSearchParams(document.location.search);
        let code = params.get("code");
        console.log("code: "+code);
        try{
            let response=await axios({
                method:'post',
                url:'https://kauth.kakao.com/oauth/token',
                headers:{
                    "Content-Type":"application/x-www-form-urlencoded;charset=utf-8"
                },
                data:{
                    grant_type:'authorization_code',
                    client_id:'${client_id}', //server로 숨기기
                    redirect_uri:'http://localhost:8080/web/join/join.do',
                    code:code
                }
            });
            console.log(response.data);
            const token=response.data.access_token;
            if(token){
                // localStorage.setItem('jwtToken', token);
                document.cookie="kakao-access-token="+token;
                displayToken();
            }else{
                console.error("토큰이 없어요")
            }
        }catch (error) {
            console.error('로그인 실패:', error.response ? error.response.data : error);
        }
    }


    function displayToken() {
        var token = getCookie('kakao-access-token');

        if(token) {
            Kakao.Auth.setAccessToken(token);
            Kakao.Auth.getStatusInfo()
                .then(function(res) {
                    if (res.status === 'connected') {
                        document.getElementById('token-result').innerText
                            = 'login success, token: ' + Kakao.Auth.getAccessToken();
                    }
                })
                .catch(function(err) {
                    Kakao.Auth.setAccessToken(null);
                });
        }
    }

    function getCookie(name) {
        var parts = document.cookie.split(name + '=');
        if (parts.length === 2) { return parts[1].split(';')[0]; }
    }
</script>
</body>
</html>
