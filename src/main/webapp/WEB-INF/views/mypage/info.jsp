<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr@latest/dist/l10n/ko.js"></script>
<div class="container pt-header" id="app">
    <div class="login_wrap d-flex justify-content-center">
        <div class="login_inner">
            <form action="" method="post" name="infoUpload">
                <input type="file" id="profile" name="profile" accept="image/*" onchange="setThumbnail(event);"/>
                <div id="profilewrap" onclick="profileupload();">
                    <div></div>
                </div>
                <div class="input_group">
                    <div class="input_wrap">
                        <label>이메일</label>
                        <input v-model="user.user_mail" type="email" id="user_mail" name="user_mail" placeholder="로그인 시 사용할 이메일을 적어주세요." required>
                    </div>
                    <div class="input_wrap">
                        <label>이름</label>
                        <input v-model="user.user_name" type="text" id="user_name" name="user_name" placeholder="실명을 적어주세요." required>
                    </div>
                    <div class="input_wrap">
                        <label>닉네임</label>
                        <input v-model="user.nickname" type="text" name="nickname" id="nickname" placeholder="펫포유에서 공개될 닉네임을 적어주세요." required>
                    </div>
                </div>
                <div class="input_group">
                    <div class="input_wrap">
                        <label>기존 비밀번호</label>
                        <input type="password" name="password" id="pwd" required>
                    </div>
                    <div class="input_wrap">
                        <label>새 비밀번호</label>
                        <input type="password" name="password" id="newpwd" required>
                    </div>
                    <div class="input_wrap">
                        <label>새 비밀번호 확인</label>
                        <input type="password" name="password2" id="newpwd2" required>
                    </div>
                </div>
                <div class="input_group">
                    <div class="input_wrap">
                        <label>생일</label>
                        <input v-model="user.birth" type="text" id="birth" name="birth" placeholder="" value="${vo.birthday}" required>
                    </div>
                    <div class="input_wrap">
                        <label>휴대폰</label>
                        <input v-model="user.phone" type="text" id="phone" name="phone" placeholder="010-0000-0000" value="${vo.addr1}" required>
                    </div>
                    <div class="input_wrap">
                        <label>내 동네</label>
                        <input type="text" id="addr" name="addr1" placeholder="서울 마포구 독막로3길 13" value="${vo.addr1}" required>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary" id="submit">수정하기</button>
                <button type="submit" class="btn btn-link mt-5 p-0 text-end" onclick="location.href='withdrawBefore.do'">회원 탈퇴하기</button>
            </form>
        </div>

    </div>
</div>
<script type="module">
    import { createApp, ref, computed } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

    createApp({
        setup() {
            let user=ref([]);
            async function getUser(){
                try{
                    let res=await axios({
                        method:'post',
                        url:'${pageContext.request.contextPath}/api/mypage',
                        headers:{
                            "Content-Type":"application/json"
                        },
                        withCredentials: true
                    });
                    if(res.status === 404){
                        console.log(res.data.message);
                    }else{
                        console.log(res.data.data);
                        user.value=res.data.data;
                    }
                }catch (e) {
                    console.log(e)
                }
            }
            //프로필사진
            function setThumbnail(event) {
                var reader = new FileReader();
                reader.onload = function(event) {
                    document.getElementById("profilewrap").innerHTML ="";
                    var img = document.createElement("div");
                    img.setAttribute("style", "background-image:url("+event.target.result+")");
                    img.setAttribute("ID", "profileimg");
                    document.querySelector("div#profilewrap").appendChild(img);
                };
                reader.readAsDataURL(event.target.files[0]);
            }
            function profileupload(){
                var ele=document.getElementById('profile');
                ele.click();
                console.log('test');
            }
            return {
                getUser, user,setThumbnail,profileupload
            }
        },
        mounted(){
            this.getUser();
            const picker = flatpickr("#birth",{
                locale: 'ko'
            });

            //비밀번호 변경
            //새 비밀번호 입력
            $("#newpwd").blur(function(){
                if($(this).val().length<5){
                    toast("비밀번호는 6글자 이상이어야 합니다.");
                    $(this).val("");
                }
            });
            //비밀번호 확인
            $("#newpwd2").blur(function(){
                if($("#pwd").val()!=$(this).val()){
                    toast("새 비밀번호 확인이 일치하지 않습니다.");
                    $(this).val("");
                }
            });


        }
    }).mount('#app')
</script>
<script>
    const m1=newmodal('<b>test</b> test','m1');
</script>