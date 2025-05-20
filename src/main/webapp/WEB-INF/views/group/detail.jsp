<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
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
              <button id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false" class="rounded-circle btn-transparent btn-sm px-1 btn shadow-none">
                <i class="ti ti-dots-vertical fs-6"></i>
              </button>
              <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton1">
                <li><a class="dropdown-item" href="javascript:void(0)" data-bs-toggle="modal" data-bs-target="#newScheduleModal">일정 추가</a></li>
              </ul>
            </div>
          </div>
          <ul class="list-unstyled mb-0 accordion-collapse collapse show" id="collapseOne">
            <!-- <li v-for="(item, idx) in schedulelist" :key="idx" class="py-10 border-bottom"> -->
            <li class="py-10 border-bottom">
              <!-- <h6 class="mb-1 fs-3">{{ item.sche_title }}</h6> -->
              <h6 class="mb-1 fs-3">초코 산책 품앗이</h6>
              <div class="fs-2 d-flex gap-2">
                <div class="d-flex align-items-center gap-1">
                  <iconify-icon icon="solar:clock-circle-broken" class="fs-4 text-primary"></iconify-icon>
                  <span>2025.05.12</span>
                </div>
                <div class="d-flex align-items-center gap-1">
                  <iconify-icon icon="solar:users-group-rounded-broken" class="fs-4 text-primary"></iconify-icon>
                  <span>유저1, 유저2</span>
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
	      <img :src="gvo.profile_img || '/assets/images/profile/default.png'" class="card-img-top h-100" alt="그룹 이미지" style="object-fit: cover;">
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
	            <div class="d-flex align-items-center gap-1">
	              <span>최대 인원</span><span class="text-dark">{{ gvo.capacity }}명</span>
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
	  <!-- <div v-for="(vo, index) in list" :key="vo.feed_no" class="card overflow-hidden mb-3"> -->
	  <div class="card overflow-hidden mb-3">
	    <div class="row g-0">
	      <div class="card-body col-3 p-3">
	        <div class="d-flex gap-2">
	          <div class="d-flex align-items-center">
	            <!-- <img :src="vo.profile_img || '/assets/images/profile/default.png'" width="42" height="42" class="rounded-circle fs-1"> -->
	            <img src="../assets/images/profile/user-2.jpg" width="42" height="42" class="rounded-circle fs-1">
	          </div>
	          <div class="">
	            <a class="d-block fs-4 text-dark fw-semibold link-primary" href="javascript:void(0)" >  <!-- @click="feed_detail(vo.feed_no)" -->
	              <!-- {{ vo.title }} -->
	              다음주 금요일에 돌봄 가능하신 분!
	            </a>
	            <div class="d-flex align-items-center flex-wrap gap-2 fs-3">
	              <div class="d-flex align-items-center gap-1">
	                <span>작성자</span><span class="text-dark"><!-- {{ vo.writer }} -->test12</span>
	              </div>
	              <div class="d-flex align-items-center gap-1">
	                <span>작성일</span><span class="text-dark"><!-- {{ vo.dbday }} -->25.05.20</span>
	              </div>
	            </div>
	          </div>
	        </div>
	
	        <div class="feed-content my-3">
	          <p><!-- {{ vo.content }} -->강아지 좋아하는 사람들 다 모이세요!! 서로 정보도 공유하고 품앗이도 가능한 방 입니다</p>
	        </div>
	
	        <!-- 이미지가 있을 경우에만 출력 -->
	        <!-- <div v-if="vo.images && vo.images.length"> -->
	        <div>
	          <div :id="'carousel-' + index" class="carousel slide my-2" data-bs-ride="carousel">
	            <div class="carousel-inner">
	              <!-- <div class="carousel-item" v-for="(img, i) in vo.images" :class="{ active: i === 0 }"> -->
	              <div class="carousel-item">
	                <img :src="'/web/images/' + img" class="d-block w-100 rounded" style="max-height: 300px; object-fit: cover;">
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
	
	        <div class="d-flex justify-content-end fs-3">
	          <button type="button" class="btn btn-link">더보기</button>
	        </div>
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
	          <div class="mb-3">
	            <label class="form-label">참여자 선택</label>
	            <div v-for="member in mvo" :key="member.user_no" class="form-check">
	              <input class="form-check-input" type="checkbox" :id="'member-' + member.user_no" :value="member.user_no" v-model="newSchedule.participants">
	              <label class="form-check-label" :for="'member-' + member.user_no">
	                {{ member.user_no }} ({{ member.role }})
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
      schedulelist: [],
      newSchedule: {
        title: '',
        content: '',
        start: '',
        end: '',
        participants: [],
        type: 1
      },
	  curpage: 1,
	  
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
      formData.append('group_no', this.group_no);
      this.selectedFiles.forEach(file => {
        formData.append('files', file);
      });

      axios.post('../group/feeds', formData)
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
      this.newSchedule.participants.forEach(p => {
        formData.append('participants', p);
      });

      axios.post('../api/schedules', formData)
        .then(() => {
          bootstrap.Modal.getInstance(document.getElementById('newScheduleModal')).hide();
          this.scheduleRecv();
        })
        .catch(err => {
          console.error("일정 등록 실패", err);
        });
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
	  }).catch(error => {
		 console.err(error);
	  })
	  
    },
    async scheduleRecv() {
      const res = await axios.get('../api/schedules', {
         
      });
      this.schedulelist = res.data.list;
    }
  }
}).mount('#group-detail-app');
</script>