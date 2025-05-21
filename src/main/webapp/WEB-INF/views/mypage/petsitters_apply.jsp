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
        <div class="login_inner" style="min-width:400px">
            <form @submit.prevent="apply" method="post" name="apply" id="apply" ref="uploadForm">
                <div class="alert alert-info" style="word-break: keep-all" role="alert">
                    반려동물 양육 경력과 자격증, 본인을 잘 나타낼 수 있는 소개글을 작성한 뒤 펫시터 자격을 신청해주세요.
                    신청서는 관리자 검토 후 최대 7일 내에 결과를 확인하실 수 있습니다.
                </div>
                <div class="input_group">
                    <div class="input_wrap">
                        <label>반려동물 양육 경력</label>
                        <textarea v-model="sitter.history" name="history" id="history" data-max="1000" placeholder="" required></textarea>
                        <p class="left-bytes fs-2"><span>0</span>/1000 bytes</p>
                    </div>
                    <div class="input_wrap overflow-visible">
                        <label>자기소개</label>
                        <textarea v-model="sitter.info" name="info" id="info" data-max="2000" placeholder="펫시터 자기소개를 적어주세요." required></textarea>
                        <p class="left-bytes fs-2"><span>0</span>/2000 bytes</p>
                    </div>
                </div>
                <div class="input_group">
                    <div class="input_wrap">
                        <label>반려동물종합관리사</label>
                        <div class="col-12 p-2">
                            <div class="form-check form-switch">
                                <input v-model="sitter.license" true-value="반려동물종합관리사" false-value="0" class="form-check-input" type="checkbox" id="license" name="license">
                                <label class="form-check-label" for="license">보유</label>
                            </div>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary" id="submit">신청하기</button>
            </form>
        </div>

    </div>
</div>
<script type="module">
    import { createApp, ref, nextTick } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

    const App = createApp({
        setup() {
            const sitter=ref([]);
            const formData=ref(null);
            const uploadForm = ref(null)

            //펫시터 정보 조회
            async function getPetsitter(){
                try{
                    let res=await axios({
                        method:'get',
                        url:'${pageContext.request.contextPath}/api${requestScope['javax.servlet.forward.servlet_path']}',
                        headers:{
                            "Content-Type":"application/json"
                        },
                        withCredentials: true
                    });
                    sitter.value=res.data.data;
                    console.log(res.data)
                    await nextTick()
                    formData.value=new FormData(uploadForm.value)
                }catch (e) {
                    console.log(e);
                    toast(e.response.data.message);
                }
            }

            //추가
            async function apply(){
                formData.value=new FormData(uploadForm.value)
                for (const x of formData.value.entries()) {
                    console.log(x);
                }
                try{
                    let res=await axios({
                        method:'post',
                        url:'${pageContext.request.contextPath}/api/mypage/petsitters',
                        headers:{
                            "Content-Type":"multipart/form-data"
                        },
                        data:formData.value,
                        withCredentials: true
                    });
                    <%--location.href='${pageContext.request.contextPath}/mypage/pets/list';--%>

                }catch (e) {
                    console.log(e);
                    if(e.status!=404){
                        toast(e.response.data.message);
                    }
                }
            }

            return {
                sitter,getPetsitter,formData,uploadForm,apply
            }
        },
        mounted(){
            this.getPetsitter();
            $("textarea").on("keyup",function(){
                const max=$(this).attr("data-max");
                let total=0;
                let slice=0;
                for (let i=0; i<$(this).val().length;i++){

                    let cur_byte = $(this).val().charAt(i);
                    if(encodeURI(cur_byte).length>4){
                        total+=2;
                    }else{
                        total++;
                    }
                    if(total<=max){
                        slice=i+1;
                    }
                }
                if(total > max){
                    let str=$(this).val().substring(0,slice)
                    $(this).val(str);
                }else{
                    $(this).siblings(".left-bytes").children("span").text(total);
                }
            });
        }
    });
    App.config.compilerOptions.delimiters = ['[[', ']]'];
    App.mount('#app');
</script>