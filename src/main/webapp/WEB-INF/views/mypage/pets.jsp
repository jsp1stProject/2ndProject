<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<div class="container pt-header" id="app">
    <div class="row pt-3">
        <div class="col-12">
            <div class="d-flex justify-content-end">
                <a href="${pageContext.request.contextPath}/mypage/pets/new" class="btn btn-primary mb-2 ">
                    <iconify-icon icon="solar:paw-linear" class="fs-6 align-middle"></iconify-icon>
                    반려동물 추가
                </a>
            </div>
            <div v-for="pet in pets" class="card overflow-hidden">
                <a :href="'${pageContext.request.contextPath}/mypage/pets/'+pet.pet_no" class="stretched-link">
                    <div class="row g-0">
                        <div class="position-relative col-5 col-sm-3 col-lg-2 d-sm-block">
                            <a href="javascript:void(0)">
                                <img :src="'https://pet4u.s3.ap-northeast-2.amazonaws.com/'+pet.pet_profilepic" class="card-img-top h-100" :alt="pet.pet_name" style="object-fit: cover">
                            </a>
                        </div>
                        <div class="card-body col-3 p-3">
                            <div class="d-flex gap-2">
                                <div class="">
                                    <a class="d-block mt-1 mb-2 fs-5 text-dark fw-semibold link-primary" href="">[[pet.pet_name]]</a>
                                </div>
                            </div>
                            <div class="d-flex align-items-center flex-wrap gap-1 mb-2 fs-2">
                                <div class="d-flex align-items-center">
                                    <span class="text-dark">[[pet.pet_type]]</span><span v-if="pet.pet_subtype != null">([[pet.pet_subtype]])</span>
                                </div>
                                <span>·</span>
                                <div class="d-flex align-items-center gap-1">
                                    <span class="text-dark">[[pet.pet_age]]살</span>
                                </div>
                                <span>·</span>
                                <div class="d-flex align-items-center gap-1">
                                    <span v-if="pet.pet_weight != 0" class="text-dark">[[pet.pet_weight]]kg</span>
                                </div>
                            </div>
                            <div class="d-flex gap-1">
                                <span v-if="pet.pet_char1 == 1" class="badge text-bg-light fs-2 py-1 px-2 lh-sm">#낯가려요</span>
                                <span v-if="pet.pet_char2 == 1" class="badge text-bg-light fs-2 py-1 px-2 lh-sm">#온순해요</span>
                                <span v-if="pet.pet_char3 == 1" class="badge text-bg-light fs-2 py-1 px-2 lh-sm">#사나워요</span>
                                <span v-if="pet.pet_status == 1" class="badge text-bg-light fs-2 py-1 px-2 lh-sm">#중성화</span>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </div>
    </div>
</div>
<script type="module">
    import { createApp, ref } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

    const App = createApp({
        setup() {
            const pets=ref([]);

            //반려동물 목록 조회
            async function getPets(){
                try{
                    let res=await axios({
                        method:'get',
                        url:'${pageContext.request.contextPath}/api/mypage/pets',
                        headers:{
                            "Content-Type":"application/json"
                        },
                        withCredentials: true
                    });

                    pets.value=res.data.data;
                    console.log(res.data)
                    if(pets.value == null){
                        toast("no pets")
                    }
                }catch (e) {
                    console.log(e);
                    toast(e.response.data.message);
                }
            }

            return {
                pets,getPets
            }
        },
        mounted(){
            this.getPets();
        }
    });
    App.config.compilerOptions.delimiters = ['[[', ']]'];
    App.mount('#app');
</script>