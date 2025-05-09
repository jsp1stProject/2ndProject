<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>그룹리스트</title>
  <link	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
  <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<style>
body {
	background-color: #f6f7f8;
	font-family: 'Segoe UI', sans-serif;
	padding: 20px;
}

.custom-container {
	width: 70%;
	margin: 0 auto;
}

.post-card {
	margin-bottom: 20px;
}

.post-body {
	margin-top: 10px;
}

.preview-img {
	max-width: 100px;
	max-height: 100px;
	margin: 5px;
	border-radius: 6px;
	object-fit: cover;
}

.group-img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

@media ( max-width : 768px) {
	.custom-container {
		width: 95%;
	}
}
</style>
</head>
<body>
<div class="container-fluid custom-container">
  <div class="row">
    
    <!-- 왼쪽 필드 영역 -->
    <div class="col-md-3 mb-4">
      <div class="card">
        <div class="card-header">📅 오늘의 일정</div>
        <div class="card-body">
          <ul class="list-unstyled mb-0">
            <li><strong>그룹에 대한 필터 예정</strong></li>
          </ul>
        </div>
      </div>
    </div>

    <!-- 중앙 피드 영역 -->
    <div class="col-md-9">
      
      
      <!-- 상단 새 피드 쓰기 버튼 -->
      <div class="mb-3">
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#newPostModal">
          ✏️ 새 그룹
        </button>
      </div>
      <!--  상단 새 일정 쓰기 버튼 -->
      <!-- <div class="text-right" style="margin-bottom: 15px;">
        <button class="btn btn-primary" data-toggle="modal" data-target="#newPostModal">
          <span class="glyphicon glyphicon-pencil"></span> 일정 추가
        </button>
      </div> -->

	  <!-- 새 그룹 모달 -->
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
                  <input type="text" class="form-control" v-model="newPost.profile_img">
                </div>
                <div class="text-end">
                  <button type="submit" class="btn btn-success">생성하기</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <!-- 게시글 반복 렌더링 -->
      <div v-for="(vo, index) in list" :key="vo.group_no" class="card mb-4">
        <div class="card-body">
          <div class="d-flex align-items-center">
            <img :src="vo.profile_img" class="group-img me-3">
            <div>
              <h5 class="mb-1">{{ vo.group_name }}</h5>
              <p class="mb-1 text-muted">{{ vo.description }}</p>
              <p class="mb-1">태그: {{ vo.tag }}</p>
              <p class="mb-1">인원: {{ vo.currentMemberCount }} / {{ vo.capacity }}명</p>
              <p class="mb-1">방장: {{ vo.owner }}</p>
              <p class="mb-1">개설일: {{ vo.created_at }}</p>
              <button class="btn btn-primary btn-sm mt-2" @click="detail(vo.group_no)">입장하기</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<script type="module">
  import { createApp, ref } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

  createApp({
	data(){
      return {
         list:[],
		 newPost: {
         			group_name: '',
         			profile_img: '',
         			description: '',
					capacity:'',
					is_public: 'Y'
      			   },
		 
      }
    },
	mounted(){
		this.dataRecv()
	},
    methods:{
		detail(group_no) {
			location.href='../group/detail?group_no='+group_no
		},
		addPost() {
			console.log("데이터 등록 시작")
			const formData = new FormData();
			formData.append('title', this.newPost.title)
			formData.append('content', this.newPost.title)
			this.selectedFiles.forEach(file => {
    			formData.append('files', file);
  			});
			axios.post('../group/groups',formData,{
				headers:{
				'Content-Type':'multipart/form-data'
				}
			}).then(response=>{
				alert("새그룹 생성 완료")
				const modal=boottstrap.Modal.getInstance(document.getElementById('newPostModal'));
				modal.hide();
				this.dataRecv();
			}).catch(error=>{
				console.error(error);
				alert("데이터등록 요류발생")
			})
/*
			const res = axios.post('../group/groups',{
					title : this.newPost.title
					content : this.newPost.content
					files : this.newPost.files
			}).then(response=> {
				console.log("데이터 등록 성공")
			}).catch(error=> {
				console.error(error);
				alert("데이터등록 중 오류발생")
			});
*/

			
			
		},
		async dataRecv(){
			console.log("dataRecv 실행")
			const res = await axios.get('../group/groups',{
			})
            this.list=res.data.list 
			console.log(res)
		}

	}
  }).mount('.container-fluid')
</script>
</body>
</html>