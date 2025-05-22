<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
   <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
   <!-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script> -->
   <style>
	.description-line {
	  max-width: 100%;         /* 부모 컨테이너에 맞게 제한 */
	  overflow: hidden;
	  text-overflow: ellipsis;
	  white-space: nowrap;     /* 줄바꿈 방지 */
	}
	</style>
    <div class="container pt-header" id="groupListApp">
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
                    <div class="filter-item mt-4">
					  <h6 class="mb-3">✅ 내가 가입한 그룹</h6>
					  <div v-if="joinedgroup_list.length > 0">					  
					    <div v-for="vo in joinedgroup_list" :key="vo.group_no" class="card mb-2">
					      <div class="card-body p-2">
					        <div class="d-flex justify-content-between align-items-center">
					          <div>
					            <strong>{{ vo.group_name }}</strong>
					            </div>
					          <button class="btn btn-sm btn-outline-primary" @click="detail(vo.group_no)">입장</button>
					        </div>
					      </div>
					    </div>
					  </div>
					  <div v-else class="text-muted">가입한 그룹이 없습니다.</div>
					</div>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="d-flex justify-content-end">
                    <button type="button" class="btn btn-primary mb-2 "  data-bs-toggle="modal" data-bs-target="#createGroupModal">
                        <iconify-icon icon="solar:users-group-rounded-broken" class="fs-6 align-middle"></iconify-icon>
                        새 그룹
                    </button>
                    <button type="button" class="btn btn-primary mb-2 "  @click="group_member_management(user_no)" >
                        <iconify-icon icon="solar:users-group-rounded-broken" class="fs-6 align-middle"></iconify-icon>
                        내 그룹관리
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
                                <button v-if="stateMap[vo.group_no]?.IS_MEMBER === 'Y'"  type="button" class="btn btn-outline-dark" @click="detail(vo.group_no)">입장하기</button>
                                <button v-else-if="stateMap[vo.group_no]?.JOIN_STATUS === 'WAITING'" type="button" class="btn btn-outline-warning" disabled>가입 대기 중</button>
                                <button v-else type="button" class="btn btn-outline-warning" @click="join(vo.group_no)">신청하기</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        
    
    <!-- 새 그룹에 대한 모달창 -->
	<div class="modal fade" id="createGroupModal" tabindex="-1">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title">새 그룹</h5>
	        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
	      </div>
	      <div class="modal-body">
	          <div class="mb-3">
            <label class="form-label">그룹명</label>
            <input type="text" class="form-control" v-model="groupDetail.group_name">
            </div>
            <div class="mb-3">
            <label class="form-label">설명</label>
            <textarea class="form-control" v-model="groupDetail.description"></textarea>
            </div>
            <div class="mb-3">
            <label class="form-label">정원</label>
            <input type="number" class="form-control" v-model="groupDetail.capacity" min="1">
            </div>
            <div class="mb-3">
            <label class="form-label">공개 여부</label>
            <select class="form-select" v-model="groupDetail.is_public">
                <option value="Y">공개</option>
                <option value="N">비공개</option>
            </select>
            </div>
            <div class="mb-3">
			  <label class="form-label">태그 선택</label>
			  <div class="d-flex flex-wrap gap-2">
			    <div v-for="(tag, idx) in allTags" :key="idx" class="form-check form-check-inline">
			      <input class="form-check-input" type="checkbox" :id="'tag-'+idx" :value="tag" v-model="selectedTags">
			      <label class="form-check-label" :for="'tag-'+idx">{{ tag }}</label>
			    </div>
			  </div>
			</div>
            <div class="mb-3">
            <label class="form-label">프로필 이미지</label>
            <input type="file" class="form-control" @change="handleProfileImgChange" ref="profileImgInput">
            </div>
            <div class="mb-3" v-if="groupDetail.profile_img">
            <label class="form-label">미리보기</label><br>
            <img :src="groupDetail.profile_img" alt="미리보기" style="height: 100px">
            </div>
            
	      </div>
	      <div class="modal-footer">
            <button class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
            <button class="btn btn-primary" @click="submitCreateGroup">생성</button>
        </div>
	    </div>
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
		  joinedgroup_list:[],
		  user_no : '',
          selectedFiles: [],
          newPost: {
            group_name: '',
            profile_img: '',
            description: '',
            capacity: '',
            is_public: 'Y'
          },
		  groupDetail: {
    		group_no: '',
    		group_name: '',
    		description: '',
    		profile_img: null,
    		capacity: 0,
    		is_public: '',
    		owner: ''
  		 },
  		 allTags: [
    		'산책', '사료공유', '훈련정보', '미용', 
    		'병원정보', '입양', '사진공유', '소형견', 
    		'대형견', '고양이', '중성화', '혼종사랑'],
  		 selectedTags: [],
         }
      },
      mounted() {
        this.dataRecv();
		this.fetchLoginUser();
      },
      methods: {
		async fetchLoginUser() {
			
			const contextPath = "${pageContext.request.contextPath}";
			console.log('contextPath:', contextPath);
			try{
				const res=await axios.get(contextPath+'/api/token');
				this.user_no = res.data.userNo;
				console.log('로그인 유저 번호:', this.user_no);
			} catch(err) {
				console.error("로그인 사용자 정보 가져오기 실패",err)
			}
		},
        handleFileChange(event) {
		  const file = event.target.files[0];
		  if (!file) return;

		  this.groupDetail.profile_img = URL.createObjectURL(file);
        },
		group_member_management(user_no){
		  console.log("그룹멤버관리")
		  location.href = '../group/member_management?user_no='+user_no;
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
        async updateGroupDetail() {
    		if (!this.groupDetail.group_name?.trim()) {
      		alert('그룹명을 입력해주세요.');
      		return;
    	}
    		if (!this.groupDetail.description?.trim()) {
      		alert('그룹 설명을 입력해주세요.');
      		return;
    	}
		
  		this.groupDetail.owner =Number(this.user_no);
  		console.log('전송 전 owner:', this.groupDetail.owner);

    	const dto = {
      		...this.groupDetail,
      		tags: this.selectedTags,
    	};
    
    	const formData = new FormData();
    	formData.append(
      		'groupDetail',
      		new Blob([JSON.stringify(dto)], { type: 'application/json' })
    	);

    	const fileInput = this.$refs.profileImgInput;
    	if (fileInput && fileInput.files && fileInput.files[0]) {
      		formData.append('profileImg', fileInput.files[0]);
    	}

    	const res = await axios.put(
      	'../api/groups/'+this.groupDetail.group_no, formData);

    	 return res.data;
  		},

		async submitCreateGroup() {
    		if (!this.groupDetail.group_name?.trim()) {
      		alert('그룹명을 입력해주세요.');
     		 return;
    	}
    	if (!this.groupDetail.description?.trim()) {
    	  alert('그룹 설명을 입력해주세요.');
    	  return;
   		 }
		this.groupDetail.owner =Number(this.user_no);
    	const dto = { ...this.groupDetail, tags: this.selectedTags };
		console.log(dto)
    	const formData = new FormData();
   	    formData.append('groupDetail', new Blob([JSON.stringify(dto)], { type: 'application/json' }));

    	const fileInput = this.$refs.profileImgInput;
		console.log('파일:', fileInput?.files?.[0]);
    	if (fileInput?.files?.[0]) {
      		formData.append('profileImg', fileInput.files[0]);
			console.log('✅ 파일을 FormData에 추가했습니다.');
    	} else {
 			 console.warn('❌ 파일이 없어서 FormData에 추가하지 못했습니다.');
		}
		for (let pair of formData.entries()) {
 			 console.log('FormData key:', pair[0], 'value:', pair[1]);
		}
    	try {
      	const res = await axios.post('../api/groups', formData);
      	alert('그룹이 생성되었습니다.');
      	bootstrap.Modal.getInstance(document.getElementById('createGroupModal')).hide();
      	this.dataRecv();
    	} catch (error) {
      	console.error('그룹 생성 실패', error);
      	alert('그룹 생성에 실패했습니다.');
    	}
  		},
        async dataRecv() {
          const res = await axios.get('../api/groups');
          console.log(res.data)
		  this.list = res.data.data.group_list;
		  this.state_list = res.data.data.states_list
		  this.user_no = res.data.data.user_no;
		  this.joinedgroup_list=res.data.data.joinedgroup_list;
		  this.stateMap = {};
		  this.state_list.forEach(state => {
        	  this.stateMap[state.GROUP_NO] = state;
			  
      	  });
          console.log(this.stateMap)
        }
      }
    }).mount('#groupListApp');


</script>

