<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
   <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <div class="container pt-header">
        <div class="row pt-3">
            <div class="col-lg-4">
                <div class="filter-container">
                    <form action="" name="filterform" method="post">
                    <div class="d-flex justify-content-between pb-2">
                        <button type="button" class="cpsbtn">필터</button>
                        <button type="reset" class="btn btn-light resetbtn">초기화</button>
                    </div>
                        <div class="filter-inner mb-3">
                            <div class="filter-wrap p-3" id="filter">
                                <div class="filter-item"> <!--checkbox 타입-->
                                    <h6>태그</h6>
                                    <div class="checkbtn-wrap">
                                        <input type="checkbox" name="type" id="1">
                                        <label for="1">태그1</label>
                                        <input type="checkbox" name="type" id="2">
                                        <label for="2">태그2</label>
                                    </div>
                                </div>
                                <div class="filter-item"> <!--radio 타입-->
                                    <h6>조건</h6>
                                    <div class="radio-wrap row">
                                        <div class="col-3 col-lg-6">
                                            <input type="radio" name="enddate" value="false" id="enddate1" checked>
                                            <label for="enddate1">조건 1</label>
                                        </div>
                                        <div class="col-3 col-lg-6">
                                            <input type="radio" name="enddate" value="true" id="enddate2">
                                            <label for="enddate2">조건 2</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="filter-item">
                                    <h6>조건</h6>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault">
                                        <label class="form-check-label" for="flexSwitchCheckDefault">입장 가능</label>
                                    </div>
                                </div>
                                <div class="filter-item">
                                    <button type="button" class="btn btn-light w-100" onclick="filtersubmit();">결과 보기</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="d-flex justify-content-end">
                    <button type="button" class="btn btn-primary mb-2 "  data-bs-toggle="modal" data-bs-target="#newPostModal">
                        <iconify-icon icon="solar:users-group-rounded-broken" class="fs-6 align-middle"></iconify-icon>
                        새 그룹
                    </button>
                </div>
                <!--  그룹 리스트 출력부분  -->
                <div v-for="vo in list" :key="vo.group_no" class="card overflow-hidden mb-3">
                    <div class="row g-0">
                        <div class="position-relative col-sm-4 d-none d-sm-block">
                                <img :src="vo.profile_img" class="card-img-top h-100" alt="그룹프로필이미지" style="object-fit: cover"> 
                        </div>
                        <div class="card-body col-3 p-3">
                            <div class="d-flex gap-2">
                                <div class="d-flex align-items-center d-sm-none">
                                    <img :src="vo.profile_img" width="42" height="42" class="rounded-circle fs-1">
                                </div>
                                <div class="">
                                    <span v-for="(tag, idx) in vo.tag?.split(',')" :key="idx" class="badge text-bg-light fs-2 py-1 px-2 lh-sm">태그1</span>
                                    <a class="d-block mt-1 mb-2 fs-5 text-dark fw-semibold link-primary" href="">{{vo.group_name}}</a>
                                </div>
                            </div>
                            <div class="d-flex align-items-center flex-wrap gap-2 mb-2 fs-2">
                                <div class="d-flex align-items-center gap-1">
                                    <span>방장</span><span class="text-dark">{{vo.owner_name}}</span>
                                </div>
                                <div class="d-flex align-items-center gap-1">
                                    <span>참여자</span><span class="text-dark">{{vo.current_member_count}} / {{ vo.capacity }}</span>
                                </div>
                                <div class="d-flex align-items-center gap-1">
                                    <span>개설일</span><span class="text-dark">{{vo.created_at}}</span>
                                </div>
                            </div>
                            <p class="mb-3">{{vo.description}}</p>
                            <div class="d-flex justify-content-end fs-3">
                                <button v-if="stateMap[vo.group_no]?.is_member === 'Y'"  type="button" class="btn btn-outline-dark" @click="detail(vo.group_no)">입장하기</button>
                                <button v-else-if="stateMap[vo.group_no]?.join_status === 'WAITING'" type="button" class="btn btn-outline-warning" disabled>가입 대기 중</button>
                                <button v-else="vo.state === 'WAITING'" type="button" class="btn btn-outline-warning" @click="join(vo.group_no)">신청하기</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 새 그룹에 대한 모달창 -->
	<div class="modal fade" id="newPostModal" tabindex="-1">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">새 그룹</h5>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      <div class="modal-body">
	        <form @submit.prevent="addPost">
	          <div class="mb-3">
	            <label class="form-label">그룹명</label>
	            <input type="text" class="form-control" v-model="newPost.group_name" required>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">그룹 설명</label>
	            <textarea class="form-control" v-model="newPost.description" rows="3" required></textarea>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">최대 인원</label>
	            <input type="number" class="form-control" v-model="newPost.capacity" required>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">공개 여부</label>
	            <select class="form-select" v-model="newPost.is_public">
	              <option value="Y">공개</option>
	              <option value="N">비공개</option>
	            </select>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">프로필 사진 URL</label>
	            <input type="file" class="form-control" accept="image/*" @change="handleFileChange">
	          </div>
	          <div class="text-end">
	            <button type="submit" class="btn btn-success">생성하기</button>
	          </div>
	        </form>
	      </div>
	    </div>
	  </div>
	</div>

<script>
    $(document).on("click",".cpsbtn",function(){
        var con=$(this).closest('.filter-container')
        if(con.hasClass('active')){
            con.removeClass('active');
        }else{
            con.addClass('active');
        }
    });
    
</script>    

<script type="module">    
    import { createApp, ref } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

    createApp({
      data() {
        return {
          list: [],
		  states_list:[],
		  user_no : '',
          selectedFiles: [],
          newPost: {
            group_name: '',
            profile_img: '',
            description: '',
            capacity: '',
            is_public: 'Y'
          }
        }
      },
      mounted() {
        this.dataRecv();
      },
      methods: {
        handleFileChange(event) {
          this.selectedFiles = Array.from(event.target.files);
        },
	    join(group_no) {
		  console.log(group_no)
		  if(!this.user_no)
		  {
			 alert("로그인페이지로이동");
			 location.href = '';
		  }
		  axios.post('../api/groups/'+group_no+'/join',{


		  }).then(res => {
			 alert("가입 신청 완료!!")
			 this.dataRecv();
		  }).catch(err => {
		     alert("가입 신청 실패.");
			 console.error(err);
		  });
		},
        detail(group_no) {
          location.href = '../group/detail?group_no=' + group_no;
        },
        addPost() {
          const formData = new FormData();
          formData.append('group_name', this.newPost.group_name);
          formData.append('description', this.newPost.description);
          formData.append('capacity', this.newPost.capacity);
          formData.append('is_public', this.newPost.is_public);

          this.selectedFiles.forEach(file => {
            formData.append('files', file);
          });

          axios.post('../group/groups', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          }).then(response => {
            alert('새 그룹 생성 완료');
            const modal = bootstrap.Modal.getInstance(document.getElementById('newPostModal'));
            modal.hide();
            this.dataRecv();
          }).catch(error => {
            console.error(error);
            alert('등록 중 오류 발생');
          });
        },
        async dataRecv() {
          const res = await axios.get('../api/groups');
          console.log(res.data)
		  this.list = res.data.data.group_list;
		  this.state_list = res.data.data.states_list
		  this.user_no = res.data.data.user_no;
		  this.stateMap = {};
		  this.state_list.forEach(state => {
        	  this.stateMap[state.GROUP_NO] = state;
			  
      	  });
          console.log(this.stateMap)
        }
      }
    }).mount('.container');


</script>

