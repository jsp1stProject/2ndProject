<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="https://t1.kakaocdn.net/kakao_js_sdk/2.7.4/kakao.min.js"
        integrity="sha384-DKYJZ8NLiK8MN4/C5P2dtSmLQ4KwPaoqAfyA/DfmEc1VDxu4yyC7wy6K1Hs90nka" crossorigin="anonymous"></script>
<script>
    Kakao.init('d24bc46e149baf82df42f1db148941b3'); // 사용하려는 앱의 JavaScript 키 입력
</script>
<div class="container pt-header login_wrap d-flex justify-content-center">
    <div class="login_inner">
        <form action="" method="post" id="joinForm">
            <div class="input_group">
                <div class="input_wrap">
                    <label>이메일</label>
                    <input type="email" name="user_mail" placeholder="로그인 시 사용할 이메일을 적어주세요." value="${response.kakao_account.email}" required>
                </div>
            </div>
            <div class="input_group">
                <div class="input_wrap">
                    <label>비밀번호</label>
                    <input type="password" name="password" id="pwd" required>
                </div>
                <div class="input_wrap">
                    <label>비밀번호 확인</label>
                    <input type="password" name="password2" id="pwd2" required>
                </div>
            </div>
            <div class="input_group">
                <div class="input_wrap">
                    <label>이름</label>
                    <input type="text" name="user_name" placeholder="실명을 적어주세요." required>
                </div>
                <div class="input_wrap">
                    <label>닉네임</label>
                    <input type="text" name="nickname" placeholder="펫포유에서 공개될 닉네임을 적어주세요." value="${response.properties.nickname}" required>
                </div>
            </div>

            <button class="btn btn-primary" type="submit" id="logBtn">가입하기</button>
        </form>
    </div>

</div>

<script>
    const kakaoJoin=async()=>{
        try{
            const res=await axios({
                method:'post',
                url:'${pageContext.request.contextPath}/api/auth/kakao/join',
                headers:{
                    "Content-Type":"application/json"
                },
                data:{
                    "code": code,
                    "url": "${fn:substringBefore(pageContext.request.requestURL,pageContext.request.requestURI)}${pageContext.request.contextPath }"
                },
                withCredentials:true
            });
            if(res.status === 409){
                console.log("이미 가입된 이메일입니다.");
            }else{
                //가입 혹은 로그인
                location.href='${pageContext.request.contextPath}/main'
            }
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
    //카카오 가입
    const url=window.location.search;
    const code=new URLSearchParams(url).get('code');
    if(code){
        kakaoJoin();
    }

    //일반 가입
    const form=document.getElementById("joinForm");
    $(form).on("submit",function(e){
        e.preventDefault();
        if(!form.checkValidity()){
            form.reportValidity();
            return;
        }
        join();
    })
    async function join(){
        let formData=new FormData(form);
        console.log(formData)
        try{
            await axios({
                method:'post',
                url:'${pageContext.request.contextPath}/api/auth/join',
                headers:{
                    "Content-Type":"application/x-www-form-urlencoded"
                },
                data:formData,
                withCredentials:true
            });
            location.href='${pageContext.request.contextPath }/main'
        }catch (e) {
            const status = e.response?.status;
            if (status === 409) {
                console.error("가입 실패:", e.response?.data || e);
                alert(e.response.data.message);
            } else {
                console.error("가입 실패:", e.response?.data || e);
                alert(e.response.data.message);
            }
        }
    }

    //비밀번호 변경
    //새 비밀번호 입력
    $("#pwd").blur(function(){
        if($(this).val().length<5){
            toast("비밀번호는 6글자 이상이어야 합니다.");
            $(this).val("");
        }
    });
    //비밀번호 확인
    $("#pwd2").blur(function(){
        if($("#pwd").val()!=$(this).val()){
            toast("비밀번호 확인이 일치하지 않습니다.");
            $(this).val("");
        }
    });

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
