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
            <form @submit.prevent="updatePet" method="post" name="uploadForm" id="uploadForm" ref="uploadForm">
                <input type="file" id="pet_profilepic" class="d-none" name="pet_profilepic" accept="image/*" @change="checkProfile(this, $event)"/>
                <div id="profilewrap" @click="profileupload">
                    <div :style="pet.pet_profilepic ? { backgroundImage: 'url(https://pet4u.s3.ap-northeast-2.amazonaws.com/'+pet.pet_profilepic+')' } : {}"></div>
                </div>
                <input type="hidden" id="profileChange" name="profileChange" value="0">
                <input type="hidden" v-model="pet.pet_no" id="pet_no" name="pet_no">
                <div class="input_group">
                    <div class="input_wrap">
                        <label>반려동물 이름</label>
                        <input v-model="pet.pet_name" type="text" id="pet_name" name="pet_name" placeholder="반려동물의 이름을 적어주세요." required>
                    </div>
                    <div class="input_wrap overflow-visible">
                        <label>반려동물 종</label>
                        <div class="dropdown d-flex flex-grow-1 h-100" style="padding:10px;padding-top:4px">
                            <div class="search-wrap d-flex flex-grow-1 h-100 gap-1 align-items-center" id="typedrop1" data-bs-toggle="dropdown" aria-expanded="false">
                                <input v-model="pet.pet_type" type="text" name="pet_type" id="pet_type" class="search-input p-0 w-100" placeholder="어떤 동물인가요?" readonly required>
                            </div>
                            <div class="dropdown-menu select" aria-labelledby="typedrop1">
                                <button type="button" class="dropdown-item">강아지</button>
                                <button type="button" class="dropdown-item">고양이</button>
                                <button type="button" class="dropdown-item">새</button>
                                <button type="button" class="dropdown-item">기타</button>
                            </div>
                        </div>
                    </div>
                    <div class="input_wrap overflow-visible d-none" id="subtype-wrap">
                        <label>상세 분류</label>
                        <div class="dropdown d-flex flex-grow-1 h-100" style="padding:10px;padding-top:4px">
                            <div class="search-wrap d-flex flex-grow-1 h-100 gap-1 align-items-center" id="typedrop2" data-bs-toggle="dropdown" aria-expanded="false">
                                <input v-model="pet.pet_subtype" type="text" name="pet_subtype" id="pet_subtype" class="search-input p-0 w-100" readonly>
                            </div>
                            <div class="dropdown-menu select" aria-labelledby="typedrop2">
                                <button type="button" class="dropdown-item">대형견</button>
                                <button type="button" class="dropdown-item">중형견</button>
                                <button type="button" class="dropdown-item">소형견</button>
                            </div>
                        </div>
                    </div>
                    <div class="input_wrap">
                        <label>나이</label>
                        <input v-model="pet.pet_age" type="number" name="pet_age" id="pet_age" required>
                    </div>
                    <div class="input_wrap">
                        <label>몸무게</label>
                        <input v-model="pet.pet_weight" type="number" step="0.1" name="pet_weight" id="pet_weight" required>
                    </div>
                </div>
                <div class="input_group">
                    <div class="input_wrap">
                        <label>성격</label>
                        <div class="d-flex justify-content-between p-2">
                            <div class="form-check d-inline-block">
                                <input v-model="pet.pet_char1" :true-value="1" :false-value="0" type="checkbox" class="form-check-input" id="pet_char1" name="pet_char1">
                                <label for="pet_char1" class="form-check-label">낯가려요</label>
                            </div>
                            <div class="form-check d-inline-block">
                                <input v-model="pet.pet_char2" :true-value="1" :false-value="0" type="checkbox" class="form-check-input" id="pet_char2" name="pet_char2">
                                <label for="pet_char2" class="form-check-label">온순해요</label>
                            </div>
                            <div class="form-check d-inline-block">
                                <input v-model="pet.pet_char3" :true-value="1" :false-value="0" type="checkbox" class="form-check-input" id="pet_char3" name="pet_char3">
                                <label for="pet_char3" class="form-check-label">사나워요</label>
                            </div>
                        </div>
                    </div>
                    <div class="input_wrap">
                        <label>중성화 여부</label>
                        <div class="col-12 p-2">
                            <div class="form-check form-switch">
                                <input v-model="pet.pet_status" :true-value="1" :false-value="0" class="form-check-input" type="checkbox" id="pet_status" name="pet_status">
                                <label class="form-check-label" for="pet_status">중성화</label>
                            </div>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary" id="submit">수정하기</button>
                <button type="button" class="btn btn-outline-danger" id="delete">삭제하기</button>
            </form>
        </div>

    </div>
</div>
<script type="module">
    import { createApp, ref, nextTick } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

    const App = createApp({
        setup() {
            const pet=ref([]);
            const formData=ref(null);
            const uploadForm = ref(null)

            //반려동물 정보 조회
            async function getPet(){
                try{
                    let res=await axios({
                        method:'get',
                        url:'${pageContext.request.contextPath}/api${requestScope['javax.servlet.forward.servlet_path']}',
                        headers:{
                            "Content-Type":"application/json"
                        },
                        withCredentials: true
                    });
                    pet.value=res.data.data;
                    console.log(pet.value)
                    await nextTick()
                    formData.value=new FormData(uploadForm.value)
                    $("#profileChange").val(0);
                    if(res.data.data.pet_type == "강아지"){
                        $("#subtype-wrap").removeClass("d-none").addClass("d-block").attr("disabled",false);
                    }else{
                        $("#subtype-wrap").removeClass("d-block").addClass("d-none").attr("disabled",true);
                    }
                }catch (e) {
                    console.log(e);
                    toast(e.response.data.message);
                }
            }

            //수정
            async function updatePet(){
                formData.value=new FormData(uploadForm.value)
                for (const x of formData.value.entries()) {
                    console.log(x);
                }
                try{
                    let res=await axios({
                        method:'put',
                        url:'${pageContext.request.contextPath}/api${requestScope['javax.servlet.forward.servlet_path']}',
                        headers:{
                            "Content-Type":"multipart/form-data"
                        },
                        data:formData.value,
                        withCredentials: true
                    });
                    getPet();
                    toast('반려동물 정보가 수정되었습니다.');

                }catch (e) {
                    console.log(e);
                    toast(e.response.data.message);
                }
            }

            //프로필 사진 OnChange
            function checkProfile(obj, event){
                const fileNm = obj.value;
                const maxSize = 10485769; //10mb
                let fileSize = 0;
                if(fileNm !== ''){
                    fileSize=document.getElementById("pet_profilepic").files[0].size;
                    if(fileSize > maxSize){
                        toast('이미지는 10MB 이하만 등록할 수 있습니다.')
                        $("#pet_profilepic").val("")
                        return;
                    }
                    setThumbnail(event);
                }
            }
            //썸네일 설정
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
                $("#profileChange").val(1); //변경처리
            }
            //프로필 사진 input 실행
            function profileupload(){
                var ele=document.getElementById('pet_profilepic');
                ele.click();
                console.log('test');
            }

            return {
                pet,formData,uploadForm,getPet,updatePet,setThumbnail,checkProfile,profileupload
            }
        },
        mounted(){
            this.getPet();
            $(".dropdown-item").on("click",function(){
                $(this).closest(".dropdown").find("input").val($(this).text()).trigger('change');
            });
            $("#pet_type").on("change",function(){
                if($(this).val()=="강아지"){
                    $("#subtype-wrap").removeClass("d-none").addClass("d-block").attr("disabled",false);
                }else{
                    $("#subtype-wrap").removeClass("d-block").addClass("d-none").attr("disabled",true);
                }
            });
            $("#pet_age").on("change",function(){
                $(this).val(Number($(this).val()).toFixed(0));
            });
            $("#pet_weight").on("change",function(){
                toast($(this).val())
                $(this).val(parseFloat(Number($(this).val()).toFixed(1)));
            });
        }
    });
    App.config.compilerOptions.delimiters = ['[[', ']]'];
    App.mount('#app');
</script>