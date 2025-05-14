<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr@latest/dist/l10n/ko.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<div class="container pt-header" id="app">
    <div class="login_wrap d-flex justify-content-center">
        <div class="login_inner">
            <input type="file" id="profile" name="profile" accept="image/*" @change="setThumbnail"/>
            <div id="profilewrap" @click="profileupload">
                <div></div>
            </div>
            <form @submit.prevent="updateUser" method="post" name="uploadForm" id="uploadForm" ref="uploadForm">
                <button type="button" class="btn btn-sm btn-outline-danger d-block mx-auto mb-3" @click="deleteProfile">기본 이미지 적용</button>
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
                        <input type="password" name="orig_pwd" id="orig_pwd" required>
                    </div>
                    <div class="input_wrap">
                        <label>새 비밀번호</label>
                        <input type="password" name="new_pwd" id="new_pwd">
                    </div>
                    <div class="input_wrap">
                        <label>새 비밀번호 확인</label>
                        <input type="password" name="new_pwd2" id="new_pwd2">
                    </div>
                </div>
                <div class="input_group">
                    <div class="input_wrap">
                        <label>생일</label>
                        <input v-model="user.db_birthday" type="date" id="birthday" name="birthday" placeholder="" value="${vo.birthday}">
                    </div>
                    <div class="input_wrap">
                        <label>휴대폰</label>
                        <input v-model="user.phone" type="text" id="phone" name="phone" placeholder="010-0000-0000" value="${vo.addr1}">
                    </div>
                    <div class="input_wrap">
                        <label>내 동네</label>
                        <input type="text" id="addr" name="addr" placeholder="서울 마포구" value="${vo.addr1}" @click="postApi" readonly>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary" id="submit">수정하기</button>
                <button type="submit" class="btn btn-link mt-5 p-0 text-end" onclick="location.href='withdrawBefore.do'">회원 탈퇴하기</button>
            </form>
        </div>

    </div>
</div>
<script type="module">
    import { createApp, ref, computed, nextTick } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

    createApp({
        setup() {
            const user=ref([]);
            const formData=ref(null);
            const uploadForm = ref(null)
            //회원 정보 조회
            async function getUser(){
                try{
                    let res=await axios({
                        method:'post',
                        url:'${pageContext.request.contextPath}/api/mypage/profile',
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
                        $("input[type='password']").val('');
                    }
                    await nextTick()
                    formData.value=new FormData(uploadForm.value)
                    if(res.data.data.social_id!=null ){
                        $("#orig_pwd").prop('required',false);
                    }

                }catch (e) {
                    console.log(e)
                }

            }

            //수정
            async function updateUser(){
                formData.value=new FormData(uploadForm.value)
                for (const x of formData.value.entries()) {
                    console.log(x);
                }
                try{
                    let res=await axios({
                        method:'put',
                        url:'${pageContext.request.contextPath}/api/mypage/profile',
                        headers:{
                            "Content-Type":"multipart/form-data"
                        },
                        data:formData.value,
                        withCredentials: true
                });
                    getUser();
                    toast('내 정보가 수정되었습니다.');

                }catch (e) {
                    console.log(e);
                    toast(e.response.data.message);
                }
            }

            //프로필 사진 변경
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
                console.log('onchange')
            }
            function profileupload(){
                var ele=document.getElementById('profile');
                ele.click();
                console.log('test');
            }
            const deleteProfile=()=>{
                $(document.querySelector("#profilewrap>div")).attr("style","");
            }

            //주소 api
            function postApi(){
                console.log('click')
                new daum.Postcode({
                    oncomplete: function(data) {
                        let sido=data.sido;
                        let sigungu=data.sigungu;
                        $('#addr').val(sido+" "+sigungu);
                    }
                }).open();
            }

            return {
                user,formData,uploadForm,getUser,updateUser,setThumbnail,profileupload,deleteProfile,postApi
            }
        },
        mounted(){
            this.getUser();

            //생일 flatpickr
            const picker = flatpickr("#birthday",{
                locale: 'ko'
            });

            //비밀번호 변경
            //새 비밀번호 입력
            $("#new_pwd").blur(function(){
                // 새 비밀번호를 작성했을 경우 반드시 확인하도록 required 추가
                if($("#new_pwd").val()!=null && $("#new_pwd").val().length>0){
                    $("#new_pwd2").prop('required',true);
                }else{
                    $("#new_pwd2").prop('required',false);
                }
                if($(this).val().length<5){
                    toast("비밀번호는 6글자 이상이어야 합니다.");
                    $(this).val("");
                }
            });
            //비밀번호 확인
            $("#new_pwd2").blur(function(){
                if($("#new_pwd").val()!=$(this).val()){
                    toast("새 비밀번호 확인이 일치하지 않습니다.");
                    $(this).val("");
                }
            });
        }
    }).mount('#app')
</script>