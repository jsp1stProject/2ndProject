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
    <input type="email" name="user_mail" placeholder="mail" value="${response.kakao_account.email}">
    <input type="password" name="password" placeholder="password">
    <input type="text" name="user_name" placeholder="name">
    <input type="text" name="nickname" placeholder="nickname" value="${response.properties.nickname}">
    <input type="submit" value="가입">
</form>
<script>
    //카카오 가입
    const url=window.location.search;
    const code=new URLSearchParams(url).get('code');
    if(code!==''){

    }

    async function kakaoJoin(){
        try{
            const res=await axios({
                method:'post',
                url:'/web/api/auth/kakao/join',
                headers:{
                    "Content-Type":"application/json"
                },
                data:{
                    "code": code
                },
                withCredentials:true
            });
            if(res.status === 409){
                console.log("이미 가입된 이메일입니다.");
            }else{
                console.log("가입완료");
            }
            location.href='/web/main'
        }catch (e) {
            const status = e.response?.status;
            if (status === 409) {
                console.log("이미 가입된 이메일입니다.");
                alert("이미 가입된 이메일입니다.");
            } else {
                console.error("가입 실패:", e.response?.data || e);
                alert("예상치 못한 오류 발생");
            }
        }
    }
</script>
</body>
</html>
