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
</head>
<body>
<form action="login_ok.do" method="post" id="login">
<%--    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />--%>
    <input type="email" id="user_mail" name="user_mail" placeholder="mail">
    <input type="password" id="password" name="password" placeholder="password">
    <input type="submit" value="로그인">
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
                url:'login_ok.do',
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
                window.location.href='web/main.do';
            }else{
                console.error("토큰이 없어요")
            }
        }catch (e) {
            console.error('로그인 실패:', error.response ? error.response.data : error);
        }
    }
</script>
</body>
</html>
