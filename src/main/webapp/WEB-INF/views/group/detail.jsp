<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>그룹상세페이지 피드출력</title>
<!-- <link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"> -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- 
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
 -->
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

			<!-- 왼쪽 일정 영역 -->
			<div class="col-md-3 mb-4">
				<div class="card">
					<div class="card-header">📅 오늘의 일정</div>
					<div class="card-body">
						<ul class="list-unstyled">
							<li><strong>10:00</strong> 회의</li>
							<li><strong>13:30</strong> 디자인 피드백</li>
							<li><strong>16:00</strong> 개발 회의</li>
						</ul>
					</div>
				</div>
			</div>

			<!-- 중앙 피드 영역 -->
			<div class="col-sm-9">
				<div class="col-md-9">
					<div class="card mb-3">
						<div class="card-body d-flex align-items-center">
							<img :src="gvo.profile_img" class="rounded-circle me-3"
								style="width: 100px; height: 100px; object-fit: cover;">
							<div>
								<h4>{{ gvo.group_name }}</h4>
								<p class="text-muted">{{ gvo.description }}</p>
								<span class="badge bg-secondary">비공개 그룹</span>
								<p class="mt-2">최대 인원: {{ gvo.capacity }}명</p>
							</div>
						</div>
					</div>
					<!-- 상단 새 피드 쓰기 버튼 -->
					<div class="mb-3">
						<button class="btn btn-primary" data-bs-toggle="modal"
							data-bs-target="#newPostModal">✏️ 새 글 쓰기</button>
					</div>
					<!--  상단 새 일정 쓰기 버튼 -->
					<!-- <div class="text-right" style="margin-bottom: 15px;">
        <button class="btn btn-primary" data-toggle="modal" data-target="#newPostModal">
          <span class="glyphicon glyphicon-pencil"></span> 일정 추가
        </button>
      </div> -->

					<div class="modal fade" id="newPostModal" tabindex="-1"
						aria-labelledby="newPostModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title">새 글 작성</h5>
									<button type="button" class="btn-close" data-bs-dismiss="modal"
										aria-label="Close"></button>
								</div>
								<div class="modal-body">
									<form @submit.prevent="addPost">
										<div class="mb-3">
											<label class="form-label">제목</label> <input type="text"
												class="form-control" v-model="newPost.title" required>
										</div>
										<div class="mb-3">
											<label class="form-label">내용</label>
											<textarea class="form-control" rows="4"
												v-model="newPost.content" required></textarea>
										</div>
										<div class="mb-3">
											<label class="form-label">이미지 첨부</label> <input type="file"
												class="form-control" multiple @change="handleFileChange"
												accept="image/*">
										</div>
										<div class="row">
											<div class="col-3" v-for="(preview, index) in imagePreviews"
												:key="index">
												<img :src="preview" class="preview-img">
											</div>
										</div>
										<div class="mt-3 text-end">
											<button type="submit" class="btn btn-primary">게시하기</button>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>

					<!-- 게시글 반복 렌더링 -->
					<div v-for="(vo, index) in list" :key="vo.feed_no"
						class="card mb-4">
						<div class="card-body" >
							<!-- 제목 -->
							<h5 class="card-title" @click="feed_detail(vo.feed_no)">{{ vo.title }}</h5>
							<!-- 작성일 -->
							<h6 class="card-subtitle mb-2 text-muted">{{ vo.dbday }}</h6>

							<!-- 이미지 슬라이드 -->
							<div v-if="vo.images && vo.images.length"
								:id="'carousel-' + index" class="carousel slide my-3"
								data-bs-ride="carousel">
								<div class="carousel-inner">
									<div class="carousel-item" v-for="(img, imgIndex) in vo.images"
										:class="{ active: imgIndex === 0 }" :key="imgIndex">
										<img :src="'/web/images/' + img" class="d-block w-100 rounded"
											style="max-height: 100%; max-width: 100%; object-fit: contain;">
									</div>
								</div>
								<button class="carousel-control-prev" type="button"
									:data-bs-target="'#carousel-' + index" data-bs-slide="prev">
									<span class="carousel-control-prev-icon" aria-hidden="true"></span>
									<span class="visually-hidden">Previous</span>
								</button>
								<button class="carousel-control-next" type="button"
									:data-bs-target="'#carousel-' + index" data-bs-slide="next">
									<span class="carousel-control-next-icon" aria-hidden="true"></span>
									<span class="visually-hidden">Next</span>
								</button>
							</div>

							<!-- 내용 -->
							<p class="card-text mt-3">{{ vo.content }}</p>

							<!-- 버튼들 -->
							<div class="d-flex justify-content-start gap-2">
								<button type="button" class="btn btn-outline-primary btn-sm">
									<i class="bi bi-chat-dots"></i> 댓글
								</button>
								<button type="button" class="btn btn-outline-success btn-sm">
									<i class="bi bi-share"></i> 공유
								</button>
								<button type="button" class="btn btn-outline-warning btn-sm">
									<i class="bi bi-star"></i> 저장
								</button>
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
		 gvo:{},
		 group_no:1,
		 
		 newPost: {
         			title: '',
         			content: '',
      			   },
		 selectedFiles: [],
    	 imagePreviews: []
		 
      }
    },
	mounted(){
		const params = new URLSearchParams(window.location.search);
  		const groupNoParam = params.get('group_no');

  if (groupNoParam) {
    this.group_no = parseInt(groupNoParam);
  }
		this.dataRecv()
	},
    methods:{
		feed_detail(feed_no)
		{
			location.href='../group/feed?feed_no='+feed_no
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

		resetForm() {
	    this.newPost.title = '';
  		this.newPost.content = '';
   	 	this.selectedFiles = [];
   	 	this.imagePreviews = [];
  		},
		addPost() {
			console.log("데이터 등록 시작")
			const formData = new FormData();
 			formData.append('title', this.newPost.title);
  			formData.append('content', this.newPost.content);
			formData.append('group_no',this.group_no);
  			this.selectedFiles.forEach(file => {
    			formData.append('files', file);
  			});
			console.log("데이터 등록 시작2")
  			axios.post('../group/feeds', formData, {
    			headers: {
      			'Content-Type': 'multipart/form-data'
    			}
  			}).then(response => {
    			alert("게시글이 등록되었습니다!");
			    const modal = bootstrap.Modal.getInstance(document.getElementById('newPostModal'));
  				modal.hide();
    			this.resetForm();
    			this.dataRecv();
  			}).catch(error => { 
  				console.error(error);
  				alert("1에서 오류가 발생했습니다!");
			});
			console.log("데이터 등록 시작3")
			/*
			console.log("등록하는 파일들"+this.newPost.files)
			const res = axios.post('../group/feeds',{
					title : this.newPost.title,
					content : this.newPost.content,
					files : this.newPost.files,
					group_no : this.group_no
					
			}).then(response=> {
				console.log("데이터 등록 성공")
				
			}).catch(error => { 
  				console.error(error);
  				alert("2에서 오류가 발생했습니다!");
			});
			*/
			
		},
		async dataRecv(){
			console.log("dataRecv 실행")
			console.log(this.group_no)
			const res = await axios.get('../group/feeds',{
					params:{
							group_no:this.group_no
					}
			})
            this.list=res.data.list
			this.gvo=res.data.gvo
			console.log(res.data)
		}

	}
  }).mount('.container-fluid')
</script>

</body>
</html>