<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
<style>
.badge {
  font-size: 0.9rem;
  padding: 0.4em 0.6em;
  border-radius: 999px;
}
</style>
<div class="container pt-header" id="group-detail-app">
 <div class="row pt-3">
    <!-- ⬅️ 좌측 일정 영역 -->
    <div class="col-lg-4">
      <div class="card w-100">
        <div class="card-body accordion">
          <div class="d-flex justify-content-between align-items-center">
            <button class="mb-0 card-title accordion-button w-auto p-0 inline-accordion" data-bs-toggle="collapse" data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
              <span class="me-2">그룹 일정</span>
            </button>
            <div class="dropdown">
			  <button id="dropdownMenuButton1"
			          data-bs-toggle="dropdown"
			          aria-expanded="false"
			          class="rounded-circle btn-transparent btn-sm px-1 btn shadow-none">
			    <i class="ti ti-dots-vertical fs-6"></i>
			  </button>
			
			  <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton1">
			    <!-- 일정 추가 모달 열기 -->
			    <li>
			      <button type="button" class="dropdown-item" data-bs-toggle="modal" data-bs-target="#newScheduleModal">
			        📅 일정 추가
			      </button>
			    </li>
			  </ul>
			</div>
          </div>
           
           <ul class="list-unstyled mb-0 accordion-collapse collapse show" id="collapseOne">
            <li v-for="(item, idx) in schedule_list" :key="idx" class="py-10 border-bottom">
              <h6 class="mb-1 fs-3">{{ item.sche_title }}</h6>
              <div class="fs-2 d-flex gap-2">
                <div class="d-flex align-items-center gap-1">
                  <iconify-icon icon="solar:clock-circle-broken" class="fs-4 text-primary"></iconify-icon>
                  <span>{{ item.sche_start_str }} - {{ item.sche_start_end_str }}</span>
                </div>
                <div class="d-flex align-items-center gap-1">
                  <iconify-icon icon="solar:users-group-rounded-broken" class="fs-4 text-primary"></iconify-icon>
                  <span v-for="(user, idx) in item.participants" :key="idx">
					  {{ user.nickname }}
					</span>
                </div>
              </div>
            </li>
            <a href="javascript:void(0)" class="fs-3 mt-3 text-center d-block">더보기</a>
          </ul>
        </div>
      </div>
    </div>
    <!-- ➡️ 우측: 피드 카드 리스트 -->
	<div class="col-lg-8">
	<!-- 그룹 정보 카드 (우측 피드 영역 상단) -->
	<div class="card mb-3 overflow-hidden">
	  <div class="row g-0">
	    <div class="col-sm-3 d-none d-sm-block">
		  <img 
		    :src="gvo.profile_img?.startsWith('http') ? gvo.profile_img : '${pageContext.request.contextPath}/s3/' + gvo.profile_img" 
		    class="card-img-top h-100" 
		    alt="그룹 이미지" 
		    style="object-fit: cover;">
		    
		</div>
	    <div class="card-body col-sm-9 p-3">
	      <div class="d-flex gap-2">
	        <div class="d-sm-none d-flex align-items-center">
	          <img :src="gvo.profile_img || '/assets/images/profile/default.png'" width="42" height="42" class="rounded-circle fs-1">
	        </div>
	        <div>
	          <span class="badge text-bg-light fs-2 py-1 px-2 lh-sm mb-2">그룹 정보</span>
	          <h4 class="fs-4 fw-semibold text-dark mb-2">{{ gvo.group_name }}</h4>
	          <p class="text-muted mb-2">{{ gvo.description }}</p>
	          <div class="d-flex align-items-center flex-wrap gap-2 fs-3">
	            <div class="d-flex align-items-center gap-1" >
	              <p>
				  <span v-for="(tag, idx) in gvo.tags" :key="idx" class="badge text-bg-light me-1">{{ tag }}</span>
				</p>
	            </div> 
	          </div>
	          <div class="d-flex align-items-center flex-wrap gap-2 fs-3">
	            <div class="d-flex align-items-center gap-1">
	              <span>방장</span><span class="text-dark">{{gvo.owner_name}}</span>
	            </div>
	            <div class="d-flex align-items-center gap-1">
	              <span>참여자</span><span class="text-dark">{{gvo.current_member_count}} / {{ gvo.capacity }}</span>
	            </div>
	          </div>
	          <div class="d-flex align-items-center flex-wrap gap-2 fs-3">
	            <div class="d-flex align-items-center gap-1">
	              <span>개설일</span><span class="text-dark">{{gvo.created_at}}</span>
	            </div>
	            <div class="d-flex align-items-center gap-1">
	              <span>공개 여부</span>
	              <span class="text-dark">{{ gvo.is_public === 'Y' ? '공개' : '비공개' }}</span>
	            </div>
	          </div>
	        </div>
	      </div>
	    </div>
	  </div>
	</div>
	  <div class="d-flex justify-content-end">
	    <button type="button" class="btn btn-primary mb-2" data-bs-toggle="modal" data-bs-target="#newPostModal">
	      <iconify-icon icon="solar:pen-2-broken" class="fs-6 align-middle"></iconify-icon>
	      새 글
	    </button>
	  </div>
	
	  <!-- 피드 목록 -->
	  <div v-for="(vo, index) in list" :key="vo.feed_no" class="card overflow-hidden mb-3">
	    <div class="row g-0">
	      <div class="card-body col-3 p-3">
	        <div class="d-flex align-items-start gap-2 mb-1">
			  <!-- 프로필 이미지 -->
			  <img :src="'${pageContext.request.contextPath}/s3/' + vo.profile"
			       alt="프로필 이미지"
			       class="rounded-circle"
			       style="width: 42px; height: 42px; object-fit: cover;">
			
			  <!-- 닉네임 + 작성일 -->
			  <div class="d-flex flex-column justify-content-center">
			    <span class="fs-3 text-dark">{{ vo.nickname }}</span>
			    <span class="fs-3 text-muted">{{ vo.dbday }}</span>
			  </div>
			</div>
			
			<!-- 제목 (작성자 정보 아래 한 줄 띄워서) -->
			<div class="mb-2">
			  <a :href="'../group/feed?feed_no=' + vo.feed_no"
			     class="fw-semibold fs-4 text-dark text-decoration-none link-primary">
			    {{ vo.title }}
			  </a>
			</div>

	        <!-- 이미지가 있을 경우에만 출력 -->
	        <div v-if="vo.images && vo.images.length">
	          <div :id="'carousel-' + index" class="carousel slide my-2" data-bs-ride="carousel">
	            <div class="carousel-inner">
	              <div class="carousel-item" v-for="(img, i) in vo.images" :class="{ active: i === 0 }">
	                <!-- <img :src="img" class="d-block w-100 rounded" style="max-height: 300px; object-fit: cover;"> -->
	                <img :src="img.startsWith('http') ? img : '${pageContext.request.contextPath}/s3/' + img" 
	                 class="d-block w-100 rounded" style="width: 70%; max-height: 400px; aspect-ratio: 4 / 3; object-fit: cover;">
	              </div>
	            </div>
	            <button class="carousel-control-prev" type="button" :data-bs-target="'#carousel-' + index" data-bs-slide="prev">
	              <span class="carousel-control-prev-icon"></span>
	            </button>
	            <button class="carousel-control-next" type="button" :data-bs-target="'#carousel-' + index" data-bs-slide="next">
	              <span class="carousel-control-next-icon"></span>
	            </button>
	          </div>
	        </div>
	
	        <!-- <div class="d-flex justify-content-end"> -->
			  <div class="d-flex align-items-center gap-3 mt-2 text-muted" style="padding-left: 12px;">
			    <!-- 좋아요 아이콘 버튼 -->
			    <button @click="selectLike(vo.feed_no)" class="btn btn-sm p-0 border-0 bg-transparent">
			    <i
				  :class="liked[vo.feed_no] ? 'bi bi-heart-fill text-danger' : 'bi bi-heart text-muted'"
				  style="font-size: 2em; position: relative; top: 2px;">
				</i>
			  </button>
			
			    <!-- 댓글 아이콘 버튼 (링크로 이동) -->
			    <a :href="'../group/feed?feed_no=' + vo.feed_no" class="text-muted">
			      <i class="bi bi-chat-dots" style="font-size: 2.0em;"></i>
			    </a>
			  </div>
			<!-- </div> -->
	      </div>
	    </div>
	  </div>
	</div> <!-- col-lg-8 -->
	<div class="modal fade" id="newPostModal" tabindex="-1" aria-labelledby="newPostModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <form @submit.prevent="addPost">
	        <div class="modal-header">
	          <h5 class="modal-title" id="newPostModalLabel">✏️ 새 글 작성</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
	        </div>
	        <div class="modal-body">
	          <div class="mb-3">
	            <label class="form-label">제목</label>
	            <input type="text" class="form-control" v-model="newPost.title" required>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">내용</label>
	            <textarea class="form-control" rows="3" v-model="newPost.content" required></textarea>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">이미지 첨부</label>
	            <input type="file" class="form-control" multiple @change="handleFileChange" accept="image/*">
	          </div>
	          <div class="row">
	            <div class="col-4" v-for="(preview, index) in imagePreviews" :key="index">
	              <img :src="preview" class="img-fluid rounded">
	            </div>
	          </div>
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-primary">게시하기</button>
	        </div>
	      </form>
	    </div>
	  </div>
	</div>
	
	<!-- 📅 새 일정 작성 모달 -->
	<div class="modal fade" id="newScheduleModal" tabindex="-1" aria-labelledby="newScheduleModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <form @submit.prevent="addSchedule">
	        <div class="modal-header">
	          <h5 class="modal-title" id="newScheduleModalLabel">📅 새 일정 추가</h5>
	          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
	        </div>
	        <div class="modal-body">
	          <div class="mb-3">
	            <label class="form-label">제목</label>
	            <input type="text" class="form-control" v-model="newSchedule.title" required>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">내용</label>
	            <textarea class="form-control" rows="3" v-model="newSchedule.content" required></textarea>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">시작일</label>
	            <input type="datetime-local" class="form-control" v-model="newSchedule.start" required>
	          </div>
	          <div class="mb-3">
	            <label class="form-label">종료일</label>
	            <input type="datetime-local" class="form-control" v-model="newSchedule.end" required>
	          </div>
	          <div class="form-check mb-3">
	            <input class="form-check-input" type="checkbox" v-model="newSchedule.is_important" id="importantCheck">
	            <label class="form-check-label" for="importantCheck">⭐️ 중요 일정</label>
	          </div>
	          <div class="form-check mb-3">
				  <input class="form-check-input" type="checkbox" v-model="newSchedule.alarm" id="alarmCheck">
				  <label class="form-check-label" for="alarmCheck">🔔 일정 전에 알림 받기</label>
				</div>
	          <div class="mb-3">
				  <label class="form-label">참여자 선택</label>
				  <div v-for="member in mvo" :key="member.user_no" class="form-check">
				    <input class="form-check-input"
				           type="checkbox"
				           :id="'member-' + member.user_no"
				           :value="member.user_no"
				           v-model="newSchedule.participants_no"
				           @change="convertParticipantToNumbers">
				    <label class="form-check-label" :for="'member-' + member.user_no">
				      {{ member.nickname }}
				    </label>
				  </div>
				</div>
	        </div>
	        <div class="modal-footer">
	          <button type="submit" class="btn btn-success">일정 등록</button>
	        </div>
	      </form>
	    </div>
	  </div>
	</div>
</div>
</div>
<script type="module">
import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js';

createApp({
  data() {
    return {
      list: [],
      gvo: {},
      mvo: [],
      group_no: 1,
      newPost: {
        title: '',
        content: ''
      },
      selectedFiles: [],
      imagePreviews: [],
      schedule_list: [],
      newSchedule: {
        title: '',
        content: '',
        start: '',
        end: '',
        participants_no: [],
        type: 1,
		is_important: false,
		alarm:false
      },
	  curpage: 1,
	  liked: {}
	  
    }
  },
  mounted() {
    const params = new URLSearchParams(window.location.search);
    const groupNoParam = params.get('group_no');
    if (groupNoParam) {
      this.group_no = parseInt(groupNoParam);
    }
	console.log("dataRecv실행전")
    this.dataRecv();
    this.scheduleRecv();
  },
  methods: {
	selectLike(feed_no) {
    		axios.post('../api/feed/'+feed_no+'/like')
      		.then(res => {
        		this.liked[feed_no] = !this.liked[feed_no];
      		})
      		.catch(err => {
        		console.log(err);
      		});
  	},
    feed_detail(feed_no) {
      location.href = '../group/feed?feed_no=' + feed_no;
    },
    handleFileChange(event) {
      const files = Array.from(event.target.files);
      this.selectedFiles = files;
      this.imagePreviews = [];

      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.imagePreviews.push(e.target.result);
        };
        reader.readAsDataURL(file);
      });
    },
    addPost() {
      const formData = new FormData();
      formData.append('title', this.newPost.title);
      formData.append('content', this.newPost.content);
      this.selectedFiles.forEach(file => {
        formData.append('files', file);
      });

      axios.post('../api/groups/'+this.group_no+'/feeds', formData)
        .then(() => {
          bootstrap.Modal.getInstance(document.getElementById('newPostModal')).hide();
          this.newPost.title = '';
          this.newPost.content = '';
          this.selectedFiles = [];
          this.imagePreviews = [];
          this.dataRecv();
        })
        .catch(err => {
          console.error("게시 실패", err);
        });
    },
    addSchedule() {
      const formData = new FormData();
      formData.append('group_no', this.group_no);
      formData.append('sche_title', this.newSchedule.title);
      formData.append('sche_content', this.newSchedule.content);
      formData.append('sche_start_str', this.newSchedule.start);
      formData.append('sche_end_str', this.newSchedule.end);
      formData.append('type', this.newSchedule.type);
	  formData.append('is_important',this.newSchedule.is_important ? 1 : 0);
	  formData.append('alarm',this.newSchedule.alarm ? 1 : 0);
      this.newSchedule.participants_no.forEach(p => {
        formData.append('participants_no', p);
      });
	  axios.post('../api/schedules/group/'+this.group_no,formData)
		.then(res => {
			console.log("성공")
			const schedulemodal = bootstrap.Modal.getInstance(document.getElementById('newScheduleModal'));
  			schedulemodal.hide();
			this.title='';
        	this.content= '';
        	this.start= '';
        	this.end= '';
        	this.participants_no= [];
        	this.type= 1;
			this.is_important= false;
			this.alarm=false;
    		this.scheduleRecv();
		})
		.catch(err => {
     		 console.log("일정 등록 실패", err);
    	});
     
    },
	convertParticipantsToNumbers() {
    this.newSchedule.participants_no = this.newSchedule.participants_no.map(p => Number(p));
    },
    async dataRecv() {
	  console.log("dataRecv실행전")
      await axios.get('../api/groups/'+this.group_no+'/feeds', {
        params: { page : this.curpage }
      }).then(res => {
		console.log(res.data)
        this.list = res.data.list;
        this.gvo = res.data.gvo;
        this.mvo = res.data.mvo;
		this.liked = {};
    	this.list.forEach(feed => {
      		this.liked[feed.feed_no] = feed.is_liked === 1;
    	});
	  }).catch(error => {
		 console.err(error);
	  })
	  
    },
    async scheduleRecv() {
	  await axios.get('../api/schedules/group/'+this.group_no)
		.then(res => {
		console.log("스케쥴 그룹일정")
		console.log(res.data)
        this.schedule_list = res.data;

	  }).catch(error => {
		 console.err(error);
	  })
     
    }
  }
}).mount('#group-detail-app');
</script>