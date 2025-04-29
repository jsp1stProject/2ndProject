<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="https://t1.kakaocdn.net/kakao_js_sdk/2.7.4/kakao.min.js"
        integrity="sha384-DKYJZ8NLiK8MN4/C5P2dtSmLQ4KwPaoqAfyA/DfmEc1VDxu4yyC7wy6K1Hs90nka" crossorigin="anonymous"></script>
<script>
    Kakao.init('d24bc46e149baf82df42f1db148941b3'); // 사용하려는 앱의 JavaScript 키 입력
</script>
<div class="container pt-header login_wrap d-flex justify-content-center">
    <div class="login_inner">
        <form action="${pageContext.request.contextPath}/api/auth/join" method="post">
            <div class="input_group">
                <div class="input_wrap">
                    <label>이메일</label>
                    <input type="email" name="user_mail" placeholder="로그인 시 사용할 이메일을 적어주세요." value="${response.kakao_account.email}">
                </div>
            </div>
            <div class="input_group">
                <div class="input_wrap">
                    <label>비밀번호</label>
                    <input type="password" name="password">
                </div>
                <div class="input_wrap">
                    <label>비밀번호 확인</label>
                    <input type="password" name="password2">
                </div>
            </div>
            <div class="input_group">
                <div class="input_wrap">
                    <label>이름</label>
                    <input type="text" name="user_name" placeholder="실명을 적어주세요.">
                </div>
                <div class="input_wrap">
                    <label>닉네임</label>
                    <input type="text" name="nickname" placeholder="펫포유에서 공개될 닉네임을 적어주세요." value="${response.properties.nickname}">
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
                "code": code
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


</script>
